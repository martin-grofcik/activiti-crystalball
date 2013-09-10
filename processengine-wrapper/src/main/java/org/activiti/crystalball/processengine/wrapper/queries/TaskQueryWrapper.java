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
import java.util.List;

/**
 * Allows programmatic querying of {@link Task}s;
 * 
 * @author Joram Barrez
 * @author Falko Menge
 */
public interface TaskQueryWrapper extends Query<TaskQueryWrapper, TaskWrapper>{

  /**
   * Only select tasks with the given task id (in practice, there will be
   * maximum one of this kind)
   */
  TaskQueryWrapper taskId(String taskId);

  /** Only select tasks with the given name */
  TaskQueryWrapper taskName(String name);
  
  /** Only select tasks with a name matching the parameter.
   *  The syntax is that of SQL: for example usage: nameLike(%activiti%)*/
  TaskQueryWrapper taskNameLike(String nameLike);
  
  /** Only select tasks with the given description. */
  TaskQueryWrapper taskDescription(String description);
  
  /** Only select tasks with a description matching the parameter .
   *  The syntax is that of SQL: for example usage: descriptionLike(%activiti%)*/
  TaskQueryWrapper taskDescriptionLike(String descriptionLike);
  
  /** Only select tasks with the given priority. */
  TaskQueryWrapper taskPriority(Integer priority);

  /** Only select tasks with the given priority or higher. */
  TaskQueryWrapper taskMinPriority(Integer minPriority);

  /** Only select tasks with the given priority or lower. */
  TaskQueryWrapper taskMaxPriority(Integer maxPriority);

  /** Only select tasks which are assigned to the given user. */
  TaskQueryWrapper taskAssignee(String assignee);
  
  /** Only select tasks for which the given user is the owner. */
  TaskQueryWrapper taskOwner(String owner);
  
  /** Only select tasks which don't have an assignee. */
  TaskQueryWrapper taskUnassigned();
  
  /** @see {@link #taskUnassigned} */
  @Deprecated
  TaskQueryWrapper taskUnnassigned();

  /** Only select tasks with the given {@link DelegationState}. */
  TaskQueryWrapper taskDelegationState(DelegationState delegationState);

  /** Only select tasks for which the given user is a candidate. */
  TaskQueryWrapper taskCandidateUser(String candidateUser);
  
  /** Only select tasks for which there exist an {@#link IdentityLink} with the given user */
  TaskQueryWrapper taskInvolvedUser(String involvedUser);

  /** Only select tasks for which users in the given group are candidates. */
  TaskQueryWrapper taskCandidateGroup(String candidateGroup);
  
  /** 
   * Only select tasks for which the 'candidateGroup' is one of the given groups.
   * 
   * @#throws ActivitiException
   *   When query is executed and {@link #taskCandidateGroup(String)} or 
   *     {@link #taskCandidateUser(String)} has been executed on the query instance. 
   *   When passed group list is empty or <code>null</code>. 
   */
  TaskQueryWrapper taskCandidateGroupIn(List<String> candidateGroups);

  /** Only select tasks for the given process instance id. */
  TaskQueryWrapper processInstanceId(String processInstanceId);
  
  /** Only select tasks foe the given business key */
  TaskQueryWrapper processInstanceBusinessKey(String processInstanceBusinessKey);

  /** Only select tasks for the given execution. */
  TaskQueryWrapper executionId(String executionId);
  
  /** Only select tasks that are created on the given date. **/
  TaskQueryWrapper taskCreatedOn(Date createTime);
  
  /** Only select tasks that are created before the given date. **/
  TaskQueryWrapper taskCreatedBefore(Date before);

  /** Only select tasks that are created after the given date. **/
  TaskQueryWrapper taskCreatedAfter(Date after);
  
  /** Only select tasks that have no parent (i.e. do not select subtasks). **/
  TaskQueryWrapper excludeSubtasks();

  /** 
   * Only select tasks with the given taskDefinitionKey.
   * The task definition key is the id of the userTask:
   * &lt;userTask id="xxx" .../&gt;
   **/
  TaskQueryWrapper taskDefinitionKey(String key);
  
  /** 
   * Only select tasks with a taskDefinitionKey that match the given parameter.
   *  The syntax is that of SQL: for example usage: taskDefinitionKeyLike("%activiti%").
   * The task definition key is the id of the userTask:
   * &lt;userTask id="xxx" .../&gt;
   **/
  TaskQueryWrapper taskDefinitionKeyLike(String keyLike);
  
  /**
   * Only select tasks which have a local task variable with the given name
   * set to the given value.
   */
  TaskQueryWrapper taskVariableValueEquals(String variableName, Object variableValue);
  
  /**
   * Only select tasks which have at least one local task variable with the given value.
   */
  TaskQueryWrapper taskVariableValueEquals(Object variableValue);
  
  /**
   * Only select tasks which have a local string variable with the given value, 
   * case insensitive.
   */
  TaskQueryWrapper taskVariableValueEqualsIgnoreCase(String name, String value);
  
  /** 
   * Only select tasks which have a local task variable with the given name, but
   * with a different value than the passed value.
   * Byte-arrays and {@link java.io.Serializable} objects (which are not primitive type wrappers)
   * are not supported.
   */
  TaskQueryWrapper taskVariableValueNotEquals(String variableName, Object variableValue);

  /**
   * Only select tasks which have a local string variable with is not the given value,
   * case insensitive.
   */
  TaskQueryWrapper taskVariableValueNotEqualsIgnoreCase(String name, String value);

  /**
   * Only select tasks which are part of a process that has a variable
   * with the given name set to the given value.
   */
  TaskQueryWrapper processVariableValueEquals(String variableName, Object variableValue);

  /**
   * Only select tasks which are part of a process that has at least one variable
   * with the given value.
   */
  TaskQueryWrapper processVariableValueEquals(Object variableValue);

  /**
   * Only select tasks which are part of a process that has a local string variable which
   * is not the given value, case insensitive.
   */
  TaskQueryWrapper processVariableValueEqualsIgnoreCase(String name, String value);

  /**
   * Only select tasks which have a variable with the given name, but
   * with a different value than the passed value.
   * Byte-arrays and {@link java.io.Serializable} objects (which are not primitive type wrappers)
   * are not supported.
   */
  TaskQueryWrapper processVariableValueNotEquals(String variableName, Object variableValue);
  
  /**
   * Only select tasks which are part of a process that has a string variable with 
   * the given value, case insensitive.
   */
  TaskQueryWrapper processVariableValueNotEqualsIgnoreCase(String name, String value);
  
  /**
   * Only select tasks which are part of a process instance which has the given
   * process definition key.
   */
  TaskQueryWrapper processDefinitionKey(String processDefinitionKey);
  
  /**
   * Only select tasks which are part of a process instance which has the given
   * process definition id.
   */
  TaskQueryWrapper processDefinitionId(String processDefinitionId);
  
  /**
   * Only select tasks which are part of a process instance which has the given
   * process definition name.
   */
  TaskQueryWrapper processDefinitionName(String processDefinitionName);
  
  /**
   * Only select tasks with the given due date.
   */
  TaskQueryWrapper dueDate(Date dueDate);
  
  /**
   * Only select tasks which have a due date before the given date.
   */
  TaskQueryWrapper dueBefore(Date dueDate);

  /**
   * Only select tasks which have a due date after the given date.
   */
  TaskQueryWrapper dueAfter(Date dueDate);
  
  /**
   * Only selects tasks which are suspended, because its process instance was suspended.
   */
  TaskQueryWrapper suspended();
  
  /**
   * Only selects tasks which are active (ie. not suspended)
   */
  TaskQueryWrapper active();
  
  // ordering ////////////////////////////////////////////////////////////
  
  /** Order by task id (needs to be followed by {@link #asc()} or {@link #desc()}). */
  TaskQueryWrapper orderByTaskId();
  
  /** Order by task name (needs to be followed by {@link #asc()} or {@link #desc()}). */
  TaskQueryWrapper orderByTaskName();
  
  /** Order by description (needs to be followed by {@link #asc()} or {@link #desc()}). */
  TaskQueryWrapper orderByTaskDescription();
  
  /** Order by priority (needs to be followed by {@link #asc()} or {@link #desc()}). */
  TaskQueryWrapper orderByTaskPriority();
  
  /** Order by assignee (needs to be followed by {@link #asc()} or {@link #desc()}). */
  TaskQueryWrapper orderByTaskAssignee();
  
  /** Order by the time on which the tasks were created (needs to be followed by {@link #asc()} or {@link #desc()}). */
  TaskQueryWrapper orderByTaskCreateTime();
  
  /** Order by process instance id (needs to be followed by {@link #asc()} or {@link #desc()}). */
  TaskQueryWrapper orderByProcessInstanceId();
  
  /** Order by execution id (needs to be followed by {@link #asc()} or {@link #desc()}). */
  TaskQueryWrapper orderByExecutionId();
  
  /** Order by due date (needs to be followed by {@link #asc()} or {@link #desc()}). */
  TaskQueryWrapper orderByDueDate();
}
