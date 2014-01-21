package org.activiti.crystalball.simulator;

import org.activiti.crystalball.simulator.delegate.event.*;
import org.activiti.crystalball.simulator.delegate.event.EventRecorderTestUtils;
import org.activiti.crystalball.simulator.delegate.event.impl.RecordActivitiEventListener;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.util.ClockUtil;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class SimpleSimulationRunTest {
  public static final String TEST_VALUE = "TestValue";
  public static final String TEST_VARIABLE = "testVariable";

  private static final String USERTASK_PROCESS = "org/activiti/crystalball/simulator/delegate/event/PlaybackProcessStartTest.testUserTask.bpmn20.xml";

  protected RecordActivitiEventListener listener;

  @Before
  public void initListener() {
    listener = new RecordActivitiEventListener(ExecutionEntity.class, EventRecorderTestUtils.getTransformers());
  }

  @After
  public void cleanupListener() {
    listener = null;
  }

  @Test
  public void testStep() throws Exception {

    recordEvents();

    SimulationDebugger simDebugger = createDebugger();

    simDebugger.init();

    RuntimeService runtimeService = SimulationRunContext.getRuntimeService();
    TaskService taskService = SimulationRunContext.getTaskService();
    HistoryService historyService = SimulationRunContext.getHistoryService();

    // debuger step - start process and stay on the userTask
    simDebugger.step();
    step1Check(runtimeService, taskService);

    // debugger step - complete userTask and finish process
    simDebugger.step();
    step2Check(runtimeService, taskService);

    checkStatus(historyService);

    simDebugger.close();
    ProcessEngines.destroy();
  }

  private void step2Check(RuntimeService runtimeService, TaskService taskService) {ProcessInstance procInstance = runtimeService.createProcessInstanceQuery().active().processInstanceBusinessKey("oneTaskProcessBusinessKey").singleResult();
    assertNull(procInstance);
    Task t = taskService.createTaskQuery().active().taskDefinitionKey("userTask").singleResult();
    assertNull(t);
  }

  @Test
  public void testRunToTime() throws Exception {

    recordEvents();

    SimulationDebugger simDebugger = createDebugger();

    simDebugger.init();

    RuntimeService runtimeService = SimulationRunContext.getRuntimeService();
    TaskService taskService = SimulationRunContext.getTaskService();
    HistoryService historyService = SimulationRunContext.getHistoryService();

    simDebugger.runTo(0);
    ProcessInstance procInstance = runtimeService.createProcessInstanceQuery().active().processInstanceBusinessKey("oneTaskProcessBusinessKey").singleResult();
    assertNull(procInstance);

    // debuger step - start process and stay on the userTask
    simDebugger.runTo(1);
    step1Check(runtimeService, taskService);

    // process engine should be in the same state as before
    simDebugger.runTo(1000);
    step1Check(runtimeService, taskService);

    // debugger step - complete userTask and finish process
    simDebugger.runTo(1500);
    step2Check(runtimeService, taskService);

    checkStatus(historyService);

    simDebugger.close();
    ProcessEngines.destroy();
  }

  @Test(expected = RuntimeException.class )
  public void testRunToTimeInThePast() throws Exception {

    recordEvents();
    SimulationDebugger simDebugger = createDebugger();
    simDebugger.init();
    try {
      simDebugger.runTo(-1);
      fail("RuntimeException expected - unable to execute event from the past");
    } finally {
      simDebugger.close();
      ProcessEngines.destroy();
    }
  }

  @Test
  public void testRunToEvent() throws Exception {

    recordEvents();
    SimulationDebugger simDebugger = createDebugger();
    simDebugger.init();
    try {
      simDebugger.runTo(EventRecorderTestUtils.USER_TASK_COMPLETED_EVENT_TYPE);
      step1Check(SimulationRunContext.getRuntimeService(), SimulationRunContext.getTaskService());
      simDebugger.runContinue();
    } finally {
      simDebugger.close();
      ProcessEngines.destroy();
    }
  }

  @Test(expected = RuntimeException.class)
  public void testRunToNonExistingEvent() throws Exception {

    recordEvents();
    SimulationDebugger simDebugger = createDebugger();
    simDebugger.init();
    try {
      simDebugger.runTo("");
      checkStatus(SimulationRunContext.getHistoryService());
    } finally {
      simDebugger.close();
      ProcessEngines.destroy();
    }
  }

  private void step1Check(RuntimeService runtimeService, TaskService taskService) {ProcessInstance procInstance;
    procInstance = runtimeService.createProcessInstanceQuery().active().processInstanceBusinessKey("oneTaskProcessBusinessKey").singleResult();
    assertNotNull(procInstance);
    Task t = taskService.createTaskQuery().active().taskDefinitionKey("userTask").singleResult();
    assertNotNull(t);
  }


  @Test
  public void testRunContinue() throws Exception {
    recordEvents();

    SimulationDebugger simDebugger = createDebugger();

    simDebugger.init();
    simDebugger.runContinue();
    checkStatus(SimulationRunContext.getHistoryService());

    simDebugger.close();
    ProcessEngines.destroy();
  }

  private SimulationDebugger createDebugger() {
    final SimpleSimulationRun.Builder builder = new SimpleSimulationRun.Builder();
    // init simulation run
    DefaultSimulationProcessEngineFactory simulationProcessEngineFactory = new DefaultSimulationProcessEngineFactory(USERTASK_PROCESS);
    builder.processEngineFactory(simulationProcessEngineFactory)
      .eventCalendarFactory(new PlaybackEventCalendarFactory(new SimulationEventComparator(), listener.getSimulationEvents()))
      .customEventHandlerMap(EventRecorderTestUtils.getHandlers());
    return builder.build();
  }

  private void checkStatus(HistoryService historyService) {
    final HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().
      finished().
      singleResult();
    assertNotNull(historicProcessInstance);
    assertEquals("oneTaskProcessBusinessKey", historicProcessInstance.getBusinessKey());
    HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskDefinitionKey("userTask").singleResult();
    assertEquals("user1", historicTaskInstance.getAssignee());
  }

  private void recordEvents() {
    ClockUtil.setCurrentTime(new Date(0));
    ProcessEngine processEngine = (new RecordableProcessEngineFactory(USERTASK_PROCESS, listener))
      .getObject();
    TaskService taskService = processEngine.getTaskService();

    Map<String, Object> variables = new HashMap<String, Object>();
    variables.put(TEST_VARIABLE, TEST_VALUE);
    processEngine.getRuntimeService().startProcessInstanceByKey("oneTaskProcess", "oneTaskProcessBusinessKey", variables);
    EventRecorderTestUtils.increaseTime();
    Task task = taskService.createTaskQuery().taskDefinitionKey("userTask").singleResult();
    taskService.complete(task.getId());
    checkStatus(processEngine.getHistoryService());
    EventRecorderTestUtils.closeProcessEngine(processEngine, listener);
    ProcessEngines.destroy();
  }
}
