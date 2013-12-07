package org.activiti.crystalball.simulator.delegate.event;

import org.activiti.crystalball.simulator.PlaybackEventCalendarFactory;
import org.activiti.crystalball.simulator.SimpleSimulationRun;
import org.activiti.crystalball.simulator.SimulationEventComparator;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.util.ClockUtil;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SignalProcessStartEventRecorderTest {
  protected RecordActivitiEventTestListener listener = new RecordActivitiEventTestListener(ExecutionEntity.class);

  private static final String SIGNAL_PROCESSES = "org/activiti/crystalball/simulator/delegate/event/SignalEventTests.catchAlertSignal.bpmn20.xml";

  @Test
  public void testProcessInstanceEvents() throws Exception {
    recordEvents();

    final SimpleSimulationRun.Builder builder = new SimpleSimulationRun.Builder();
    // init simulation run
    DefaultSimulationProcessEngineFactory simulationProcessEngineFactory = new DefaultSimulationProcessEngineFactory(SIGNAL_PROCESSES);
    builder.processEngineFactory(simulationProcessEngineFactory)
        .eventCalendarFactory(new PlaybackEventCalendarFactory(new SimulationEventComparator(), listener.getSimulationEvents()))
        .customEventHandlerMap(EventRecorderTestUtils.getHandlers());
    SimpleSimulationRun simRun = builder.build();

    simRun.execute(EventRecorderTestUtils.DO_NOT_CLOSE_PROCESS_ENGINE);

    checkStatus(simulationProcessEngineFactory.getObject());
    simulationProcessEngineFactory.getObject().close();
    ProcessEngines.destroy();
  }

  private void recordEvents() {
    ProcessEngine processEngine = (new RecordableProcessEngineFactory(SIGNAL_PROCESSES, listener))
                                  .getObject();

    processEngine.getRuntimeService().startProcessInstanceByKey("catchSignal");

    assertEquals(1, processEngine.getRuntimeService().createProcessInstanceQuery().count());
    Calendar c = Calendar.getInstance();
    c.add(Calendar.SECOND,1);
    Date d = c.getTime();
    ClockUtil.setCurrentTime( d );
    processEngine.getRuntimeService().startProcessInstanceByKey("throwSignal");

    assertEquals(0, processEngine.getRuntimeService().createProcessInstanceQuery().count());

    checkStatus(processEngine);
    EventRecorderTestUtils.closeProcessEngine(processEngine, listener);
    ProcessEngines.destroy();
  }

  private void checkStatus(ProcessEngine processEngine) {
    HistoryService historyService = processEngine.getHistoryService();
    final List<HistoricProcessInstance> historicProcessInstances = historyService.createHistoricProcessInstanceQuery().
                                                            finished().
                                                            list();
    assertNotNull(historicProcessInstances);
    assertEquals(2, historicProcessInstances.size());
  }

}