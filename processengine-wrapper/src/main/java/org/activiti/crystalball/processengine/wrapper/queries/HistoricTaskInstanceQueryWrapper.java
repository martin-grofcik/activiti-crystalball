/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.crystalball.processengine.wrapper.queries;

import java.util.Date;


/**
 * Allows programmatic querying for {@#link HistoricTaskInstance}s.
 * 
 * @author Tom Baeyens
 */
public interface HistoricTaskInstanceQueryWrapper extends Query<HistoricTaskInstanceQueryWrapper, HistoricTaskInstanceWrapper> {

  /** Only select historic task instances for the given task id. */
  HistoricTaskInstanceQueryWrapper taskId(String taskId);
  
  /** Only select historic task instances for the given process instance. */
  HistoricTaskInstanceQueryWrapper processInstanceId(String processInstanceId);
  
  /** Only select historic task instances for the given execution. */
  HistoricTaskInstanceQueryWrapper executionId(String executionId);
  
  /** Only select historic task instances for the given process definition. */
  HistoricTaskInstanceQueryWrapper processDefinitionId(String processDefinitionId);
  
  /**
   * Only select historic task instances which are part of a (historic) process instance 
   * which has the given process definition key.
   */
  HistoricTaskInstanceQueryWrapper processDefinitionKey(String processDefinitionKey);
  
  /**
   * Only select historic task instances which are part of a (historic) process instance 
   * which has the given definition name.
   */
  HistoricTaskInstanceQueryWrapper processDefinitionName(String processDefinitionName);
  
  /** 
   * Only select historic task instances with the given task name.
   * This is the last name given to the task. 
   */
  HistoricTaskInstanceQueryWrapper taskName(String taskName);
  
  /** 
   * Only select historic task instances with a task name like the given value.
   * This is the last name given to the task.
   * The syntax that should be used is the same as in SQL, eg. %activiti%.
   */
  HistoricTaskInstanceQueryWrapper taskNameLike(String taskNameLike);
  
  /** 
   * Only select historic task instances with the given task description.
   * This is the last description given to the task.  
   */
  HistoricTaskInstanceQueryWrapper taskDescription(String taskDescription);
  
  /** 
   * Only select historic task instances with a task description like the given value.
   * This is the last description given to the task.
   * The syntax that should be used is the same as in SQL, eg. %activiti%.
   */
  HistoricTaskInstanceQueryWrapper taskDescriptionLike(String taskDescriptionLike);
  
  /**
   * Only select historic task instances with the given task definition key.
   * @#see Task#getTaskDefinitionKey()
   */
  HistoricTaskInstanceQueryWrapper taskDefinitionKey(String taskDefinitionKey);
  
  /** Only select historic task instances with the given task delete reason. */
  HistoricTaskInstanceQueryWrapper taskDeleteReason(String taskDeleteReason);
  
  /** 
   * Only select historic task instances with a task description like the given value.
   * The syntax that should be used is the same as in SQL, eg. %activiti%.
   */
  HistoricTaskInstanceQueryWrapper taskDeleteReasonLike(String taskDeleteReasonLike);
  
  /** 
   * Only select historic task instances which were last assigned to the given assignee.
   */
  HistoricTaskInstanceQueryWrapper taskAssignee(String taskAssignee);
  
  /** 
   * Only select historic task instances which were last assigned to an assignee like
   * the given value.
   * The syntax that should be used is the same as in SQL, eg. %activiti%.
   */
  HistoricTaskInstanceQueryWrapper taskAssigneeLike(String taskAssigneeLike);
  
  /** 
   * Only select historic task instances which have the given owner.
   */
  HistoricTaskInstanceQueryWrapper taskOwner(String taskOwner);
  
  /** 
   * Only select historic task instances which have an owner like the one specified.
   * The syntax that should be used is the same as in SQL, eg. %activiti%.
   */
  HistoricTaskInstanceQueryWrapper taskOwnerLike(String taskOwnerLike);
  
  /** 
   * Only select historic task instances with the given priority.
   */
  HistoricTaskInstanceQueryWrapper taskPriority(Integer taskPriority);
  
  /** 
   * Only select historic task instances which are finished.
   */
  HistoricTaskInstanceQueryWrapper finished();
  
  /** 
   * Only select historic task instances which aren't finished yet.
   */
  HistoricTaskInstanceQueryWrapper unfinished();
  
  /**
   * Only select historic task instances which are part of a process
   * instance which is already finished. 
   */
  HistoricTaskInstanceQueryWrapper processFinished();
  
  /**
   * Only select historic task instances which are part of a process
   * instance which is not finished yet. 
   */
  HistoricTaskInstanceQueryWrapper processUnfinished();
  
  /**
   * Only select historic task instances which have a local task variable with the
   * given name set to the given value. The last variable value in the variable updates 
   * ({@#link HistoricDetail}) will be used, so make sure history-level is configured
   * to full when this feature is used.
   */
  HistoricTaskInstanceQueryWrapper taskVariableValueEquals(String variableName, Object variableValue);
  
  /** Only select subtasks of the given parent task */
  HistoricTaskInstanceQueryWrapper taskParentTaskId(String parentTaskId);

  /**
   * Only select historic task instances which are part of a process instance which have a variable 
   * with the given name set to the given value. The last variable value in the variable updates 
   * ({@#link HistoricDetail}) will be used, so make sure history-level is configured
   * to full when this feature is used.
   */
  HistoricTaskInstanceQueryWrapper processVariableValueEquals(String variableName, Object variableValue);
  
  /**
   * Only select select historic task instances with the given due date.
   */
  HistoricTaskInstanceQueryWrapper taskDueDate(Date dueDate);
  
  /**
   * Only select select historic task instances which have a due date before the given date.
   */
  HistoricTaskInstanceQueryWrapper taskDueBefore(Date dueDate);

  /**
   * Only select select historic task instances which have a due date after the given date.
   */
  HistoricTaskInstanceQueryWrapper taskDueAfter(Date dueDate);
  
  /** Order by task id (needs to be followed by {@link #asc()} or {@link #desc()}). */
  HistoricTaskInstanceQueryWrapper orderByTaskId();
  
  /** 
   * Order by the historic activity instance id this task was used in
   * (needs to be followed by {@link #asc()} or {@link #desc()}). 
   */
  HistoricTaskInstanceQueryWrapper orderByHistoricActivityInstanceId();
  
  /** Order by process definition id (needs to be followed by {@link #asc()} or {@link #desc()}). */
  HistoricTaskInstanceQueryWrapper orderByProcessDefinitionId();
  
  /** Order by process instance id (needs to be followed by {@link #asc()} or {@link #desc()}). */
  HistoricTaskInstanceQueryWrapper orderByProcessInstanceId();

  /** Order by execution id (needs to be followed by {@link #asc()} or {@link #desc()}). */
  HistoricTaskInstanceQueryWrapper orderByExecutionId();
  
  /** Order by duration (needs to be followed by {@link #asc()} or {@link #desc()}). */
  HistoricTaskInstanceQueryWrapper orderByHistoricTaskInstanceDuration();
  
  /** Order by end time (needs to be followed by {@link #asc()} or {@link #desc()}). */
  HistoricTaskInstanceQueryWrapper orderByHistoricTaskInstanceEndTime();
  
  /** Order by start time (needs to be followed by {@link #asc()} or {@link #desc()}). */
  HistoricTaskInstanceQueryWrapper orderByHistoricActivityInstanceStartTime();
  
  /** Order by task name (needs to be followed by {@link #asc()} or {@link #desc()}). */
  HistoricTaskInstanceQueryWrapper orderByTaskName();
  
  /** Order by task description (needs to be followed by {@link #asc()} or {@link #desc()}). */
  HistoricTaskInstanceQueryWrapper orderByTaskDescription();
  
  /** Order by task assignee (needs to be followed by {@link #asc()} or {@link #desc()}). */
  HistoricTaskInstanceQueryWrapper orderByTaskAssignee();
  
  /** Order by task owner (needs to be followed by {@link #asc()} or {@link #desc()}). */
  HistoricTaskInstanceQueryWrapper orderByTaskOwner();
  
  /** Order by task due date (needs to be followed by {@link #asc()} or {@link #desc()}). */
  HistoricTaskInstanceQueryWrapper orderByTaskDueDate();
  
  /** Order by task delete reason (needs to be followed by {@link #asc()} or {@link #desc()}). */
  HistoricTaskInstanceQueryWrapper orderByDeleteReason();

  /** Order by task definition key (needs to be followed by {@link #asc()} or {@link #desc()}). */
  HistoricTaskInstanceQueryWrapper orderByTaskDefinitionKey();
  
  /** Order by task priority key (needs to be followed by {@link #asc()} or {@link #desc()}). */
  HistoricTaskInstanceQueryWrapper orderByTaskPriority();
}
