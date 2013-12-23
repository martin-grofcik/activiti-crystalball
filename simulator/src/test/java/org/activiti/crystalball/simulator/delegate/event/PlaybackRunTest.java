package org.activiti.crystalball.simulator.delegate.event;

import org.activiti.crystalball.simulator.PlaybackEventCalendarFactory;
import org.activiti.crystalball.simulator.SimpleSimulationRun;
import org.activiti.crystalball.simulator.SimulationEventComparator;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PlaybackRunTest {
  public static final String SIMPLEST_PROCESS = "theSimplestProcess";
  public static final String BUSINESS_KEY = "testBusinessKey";
  public static final String TEST_VALUE = "TestValue";
  public static final String TEST_VARIABLE = "testVariable";

  protected RecordActivitiEventTestListener listener = new RecordActivitiEventTestListener(ExecutionEntity.class);

  private static final String THE_SIMPLEST_PROCESS = "org/activiti/crystalball/simulator/delegate/event/PlaybackProcessStartTest.testDemo.bpmn20.xml";

  @Test
  public void testProcessInstanceStartEvents() throws Exception {
    recordEvents();

    final SimpleSimulationRun.Builder builder = new SimpleSimulationRun.Builder();
    // init simulation run
    DefaultSimulationProcessEngineFactory simulationProcessEngineFactory = new DefaultSimulationProcessEngineFactory(THE_SIMPLEST_PROCESS);
    builder.processEngineFactory(simulationProcessEngineFactory)
      .eventCalendarFactory(new PlaybackEventCalendarFactory(new SimulationEventComparator(), listener.getSimulationEvents()))
      .customEventHandlerMap(EventRecorderTestUtils.getHandlers());
    SimpleSimulationRun simRun = builder.build();

    simRun.execute();

    checkStatus(simulationProcessEngineFactory.getObject());
    simRun.getProcessEngine().close();
    ProcessEngines.destroy();
  }

  private void recordEvents() {
    ProcessEngine processEngine = (new RecordableProcessEngineFactory(THE_SIMPLEST_PROCESS, listener))
                                  .getObject();
    Map<String,Object> variables = new HashMap<String, Object>();
    variables.put(TEST_VARIABLE, TEST_VALUE);
    processEngine.getRuntimeService().startProcessInstanceByKey(SIMPLEST_PROCESS, BUSINESS_KEY,variables);
    checkStatus(processEngine);
    EventRecorderTestUtils.closeProcessEngine(processEngine, listener);
    ProcessEngines.destroy();
  }

  private void checkStatus(ProcessEngine processEngine) {
    HistoryService historyService = processEngine.getHistoryService();
    final HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().
                                                            finished().
                                                            includeProcessVariables().
                                                            singleResult();
    assertNotNull(historicProcessInstance);
    RepositoryService repositoryService = processEngine.getRepositoryService();
    final ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().
                                                processDefinitionId(historicProcessInstance.getProcessDefinitionId()).
                                                singleResult();
    assertEquals(SIMPLEST_PROCESS, processDefinition.getKey());

    assertEquals(1, historicProcessInstance.getProcessVariables().size());
    assertEquals(TEST_VALUE, historicProcessInstance.getProcessVariables().get(TEST_VARIABLE));
    assertEquals(BUSINESS_KEY, historicProcessInstance.getBusinessKey());
  }

}