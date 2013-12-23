package org.activiti.crystalball.simulator.delegate.event;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PlaybackProcessStartTest extends AbstractPlaybackTest {
  public static final String SIMPLEST_PROCESS = "theSimplestProcess";
  public static final String BUSINESS_KEY = "testBusinessKey";
  public static final String TEST_VALUE = "TestValue";
  public static final String TEST_VARIABLE = "testVariable";

  @CheckStatus(methodName = "demoCheckStatus")
  @Deployment
  public void testDemo() {
    Map<String, Object> variables = new HashMap<String, Object>();
    variables.put(TEST_VARIABLE, TEST_VALUE);
    processEngine.getRuntimeService().startProcessInstanceByKey(SIMPLEST_PROCESS, BUSINESS_KEY, variables);
  }

  public void demoCheckStatus() {
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

  @CheckStatus(methodName = "messageProcessStartCheckStatus")
  @Deployment
  public void testMessageProcessStart() {

    runtimeService.startProcessInstanceByMessage("startProcessMessage");
  }

  public void messageProcessStartCheckStatus() {
    final HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().
      finished().
      singleResult();
    assertNotNull(historicProcessInstance);
    assertTrue(startsWith(historicProcessInstance.getProcessDefinitionId(), "messageStartEventProcess"));
  }

  @Deployment
  @CheckStatus(methodName = "checkStatus")
  public void testSignals() throws Exception {
    runtimeService.startProcessInstanceByKey("catchSignal");
    EventRecorderTestUtils.increaseTime();
    runtimeService.startProcessInstanceByKey("throwSignal");
  }

  public void checkStatus() {
    final List<HistoricProcessInstance> historicProcessInstances = historyService.createHistoricProcessInstanceQuery().
                                                            finished().
                                                            list();
    assertNotNull(historicProcessInstances);
    assertEquals(2, historicProcessInstances.size());
  }

  @Deployment
  @CheckStatus(methodName = "userTaskCheckStatus")
  public void testUserTask() throws Exception {
    runtimeService.startProcessInstanceByKey("oneTaskProcess", "oneTaskProcessBusinessKey");
    Task task = taskService.createTaskQuery().taskDefinitionKey("userTask").singleResult();
    EventRecorderTestUtils.increaseTime();
    taskService.complete(task.getId());
  }

  public void userTaskCheckStatus() {
    final HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().
      finished().
      singleResult();
    assertNotNull(historicProcessInstance);
    assertEquals("oneTaskProcessBusinessKey", historicProcessInstance.getBusinessKey());
    HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskDefinitionKey("userTask").singleResult();
    assertEquals("user1", historicTaskInstance.getAssignee());
  }
}