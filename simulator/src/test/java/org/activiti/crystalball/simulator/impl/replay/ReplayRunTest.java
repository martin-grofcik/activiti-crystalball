package org.activiti.crystalball.simulator.impl.replay;

import org.activiti.crystalball.simulator.*;
import org.activiti.crystalball.simulator.delegate.event.ActivitiEventToSimulationEventTransformer;
import org.activiti.crystalball.simulator.delegate.event.impl.ProcessInstanceCreateTransformer;
import org.activiti.crystalball.simulator.delegate.event.impl.RecordActivitiEventListener;
import org.activiti.crystalball.simulator.delegate.event.impl.UserTaskCompleteTransformer;
import org.activiti.crystalball.simulator.impl.*;
import org.activiti.crystalball.simulator.impl.playback.PlaybackUserTaskCompleteEventHandler;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ReplayRunTest {

  // Process instance start event
  private static final String PROCESS_INSTANCE_START_EVENT_TYPE = "PROCESS_INSTANCE_START";
  private static final String PROCESS_DEFINITION_ID_KEY = "processDefinitionId";
  private static final String VARIABLES_KEY = "variables";
  // User task completed event
  private static final String USER_TASK_COMPLETED_EVENT_TYPE = "USER_TASK_COMPLETED";

  private static final String USERTASK_PROCESS = "oneTaskProcess";
  private static final String BUSINESS_KEY = "testBusinessKey";
  private static final String TEST_VALUE = "TestValue";
  private static final String TEST_VARIABLE = "testVariable";

  protected RecordActivitiEventListener listener = new RecordActivitiEventListener(ExecutionEntity.class, getTransformers());

  private static final String THE_USERTASK_PROCESS = "org/activiti/crystalball/simulator/impl/playback/PlaybackProcessStartTest.testUserTask.bpmn20.xml";

  @Test @Ignore("replay test implementation is in progress")
  public void testProcessInstanceStartEvents() throws Exception {
    FactoryBean<ProcessEngine> singletonProcessEngineFactory = new SingletonFactoryBean<ProcessEngine>(new RecordableProcessEngineFactory(THE_USERTASK_PROCESS, listener));
    ProcessEngine processEngine = singletonProcessEngineFactory.getObject();

    TaskService taskService = processEngine.getTaskService();
    RuntimeService runtimeService = processEngine.getRuntimeService();

    Map<String, Object> variables = new HashMap<String, Object>();
    variables.put(TEST_VARIABLE, TEST_VALUE);
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(USERTASK_PROCESS, BUSINESS_KEY, variables);

    Task task = taskService.createTaskQuery().taskDefinitionKey("userTask").singleResult();
    EventRecorderTestUtils.increaseTime();
    taskService.complete(task.getId());


    final SimpleSimulationRun.Builder builder = new SimpleSimulationRun.Builder();
    // init simulation run
    builder.processEngineFactory(singletonProcessEngineFactory)
      .eventCalendarFactory(new PlaybackEventCalendarFactory(new SimulationEventComparator(), listener.getSimulationEvents()))
      .customEventHandlerMap(getHandlers());
    SimpleSimulationRun simRun = builder.build();

    simRun.execute();

    simRun.getProcessEngine().close();
    ProcessEngines.destroy();
  }

  private void recordEvents() {
  }

  private List<ActivitiEventToSimulationEventTransformer> getTransformers() {
    List<ActivitiEventToSimulationEventTransformer> transformers = new ArrayList<ActivitiEventToSimulationEventTransformer>();
    transformers.add(new ProcessInstanceCreateTransformer(PROCESS_INSTANCE_START_EVENT_TYPE, PROCESS_DEFINITION_ID_KEY, BUSINESS_KEY, VARIABLES_KEY));
    transformers.add(new UserTaskCompleteTransformer(USER_TASK_COMPLETED_EVENT_TYPE));
    return transformers;
  }

  public static Map<String, SimulationEventHandler> getHandlers() {
    Map<String, SimulationEventHandler> handlers = new HashMap<String, SimulationEventHandler>();
    handlers.put(PROCESS_INSTANCE_START_EVENT_TYPE, new StartProcessEventHandler(PROCESS_DEFINITION_ID_KEY, BUSINESS_KEY, VARIABLES_KEY));
    handlers.put(USER_TASK_COMPLETED_EVENT_TYPE, new PlaybackUserTaskCompleteEventHandler());
    return handlers;
  }
}