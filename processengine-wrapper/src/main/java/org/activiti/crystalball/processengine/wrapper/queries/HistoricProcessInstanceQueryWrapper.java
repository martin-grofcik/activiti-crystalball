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
import java.util.Set;

/**
 * Allows programmatic querying of {@#link HistoricProcessInstance}s.
 * 
 * @author Tom Baeyens
 * @author Joram Barrez
 * @author Falko Menge
 */
public interface HistoricProcessInstanceQueryWrapper extends Query<HistoricProcessInstanceQueryWrapper, HistoricProcessInstanceWrapper> {

  /** Only select historic process instances with the given process instance.
   * {@#link ProcessInstance) ids and {@#link HistoricProcessInstance} ids match. */
  HistoricProcessInstanceQueryWrapper processInstanceId(String processInstanceId);

  /** Only select historic process instances whose id is in the given set of ids.
   * {@#link ProcessInstance) ids and {@#link HistoricProcessInstance} ids match. */
  HistoricProcessInstanceQueryWrapper processInstanceIds(Set<String> processInstanceIds);

  /** Only select historic process instances for the given process definition */
  HistoricProcessInstanceQueryWrapper processDefinitionId(String processDefinitionId);

  /** Only select historic process instances that are defined by a process
   * definition with the given key.  */
  HistoricProcessInstanceQueryWrapper processDefinitionKey(String processDefinitionKey);

  /** Only select historic process instances that don't have a process-definition of which the key is present in the given list */
  HistoricProcessInstanceQueryWrapper processDefinitionKeyNotIn(List<String> processDefinitionKeys);

  /** Only select historic process instances with the given business key */
  HistoricProcessInstanceQueryWrapper processInstanceBusinessKey(String processInstanceBusinessKey);

  /** Only select historic process instances that are completely finished. */
  HistoricProcessInstanceQueryWrapper finished();

  /** Only select historic process instance that are not yet finished. */
  HistoricProcessInstanceQueryWrapper unfinished();

  /** Only select process instances which had a global variable with the given value
   * when they ended. The type only applies to already ended
   * process instances, otherwise use a {@#link ProcessInstanceQuery} instead! of
   * variable is determined based on the value, using types configured in
   * {@#link ProcessEngineConfiguration#getVariableTypes()}. Byte-arrays and
   * {@link java.io.Serializable} objects (which are not primitive type wrappers) are
   * not supported.
   * @param name of the variable, cannot be null. */
  HistoricProcessInstanceQueryWrapper variableValueEquals(String name, Object value);

  /** Only select process instances which had at least one global variable with the given value
   * when they ended. The type only applies to already ended
   * process instances, otherwise use a {@#link ProcessInstanceQuery} instead! of
   * variable is determined based on the value, using types configured in
   * {@#link ProcessEngineConfiguration#getVariableTypes()}. Byte-arrays and
   * {@link java.io.Serializable} objects (which are not primitive type wrappers) are
   * not supported.
   */
  HistoricProcessInstanceQueryWrapper variableValueEquals(Object value);

  /**
   * Only select historic process instances which have a local string variable with the
   * given value, case insensitive.
   * @param name name of the variable, cannot be null.
   * @param value value of the variable, cannot be null.
   */
  HistoricProcessInstanceQueryWrapper variableValueEqualsIgnoreCase(String name, String value);

  /** Only select process instances which had a global variable with the given name, but
   * with a different value than the passed value when they ended. Only select
   * process instances which have a variable value greater than the passed
   * value. Byte-arrays and {@link java.io.Serializable} objects (which are not
   * primitive type wrappers) are not supported.
   * @param name of the variable, cannot be null. */
  HistoricProcessInstanceQueryWrapper variableValueNotEquals(String name, Object value);

  /** Only select process instances which had a global variable value greater than the
   * passed value when they ended. Booleans, Byte-arrays and
   * {@link java.io.Serializable} objects (which are not primitive type wrappers) are
   * not supported. Only select process instances which have a variable value
   * greater than the passed value.
   * @param name cannot be null.
   * @param value cannot be null. */
  HistoricProcessInstanceQueryWrapper variableValueGreaterThan(String name, Object value);

  /** Only select process instances which had a global variable value greater than or
   * equal to the passed value when they ended. Booleans, Byte-arrays and
   * {@link java.io.Serializable} objects (which are not primitive type wrappers) are
   * not supported. Only applies to already ended process instances, otherwise
   * use a {@#link ProcessInstanceQuery} instead!
   * @param name cannot be null.
   * @param value cannot be null. */
  HistoricProcessInstanceQueryWrapper variableValueGreaterThanOrEqual(String name, Object value);

  /** Only select process instances which had a global variable value less than the
   * passed value when the ended. Only applies to already ended process
   * instances, otherwise use a {@#link ProcessInstanceQuery} instead! Booleans,
   * Byte-arrays and {@link java.io.Serializable} objects (which are not primitive type
   * wrappers) are not supported.
   * @param name cannot be null.
   * @param value cannot be null. */
  HistoricProcessInstanceQueryWrapper variableValueLessThan(String name, Object value);

  /** Only select process instances which has a global variable value less than or equal
   * to the passed value when they ended. Only applies to already ended process
   * instances, otherwise use a {@#link ProcessInstanceQuery} instead! Booleans,
   * Byte-arrays and {@link java.io.Serializable} objects (which are not primitive type
   * wrappers) are not supported.
   * @param name cannot be null.
   * @param value cannot be null. */
  HistoricProcessInstanceQueryWrapper variableValueLessThanOrEqual(String name, Object value);

  /** Only select process instances which had global variable value like the given value
   * when they ended. Only applies to already ended process instances, otherwise
   * use a {@#link ProcessInstanceQuery} instead! This can be used on string
   * variables only.
   * @param name cannot be null.
   * @param value cannot be null. The string can include the
   *          wildcard character '%' to express like-strategy: starts with
   *          (string%), ends with (%string) or contains (%string%). */
  HistoricProcessInstanceQueryWrapper variableValueLike(String name, String value);

  /** Only select historic process instances that were started before the given date. */
  HistoricProcessInstanceQueryWrapper startedBefore(Date date);

  /** Only select historic process instances that were started after the given date. */
  HistoricProcessInstanceQueryWrapper startedAfter(Date date);

  /** Only select historic process instances that were started before the given date. */
  HistoricProcessInstanceQueryWrapper finishedBefore(Date date);

  /** Only select historic process instances that were started after the given date. */
  HistoricProcessInstanceQueryWrapper finishedAfter(Date date);

  /** Only select historic process instance that are started by the given user. */
  HistoricProcessInstanceQueryWrapper startedBy(String userId);

  /** Order by the process instance id (needs to be followed by {@link #asc()} or {@link #desc()}). */
  HistoricProcessInstanceQueryWrapper orderByProcessInstanceId();

  /** Order by the process definition id (needs to be followed by {@link #asc()} or {@link #desc()}). */
  HistoricProcessInstanceQueryWrapper orderByProcessDefinitionId();

  /** Order by the business key (needs to be followed by {@link #asc()} or {@link #desc()}). */
  HistoricProcessInstanceQueryWrapper orderByProcessInstanceBusinessKey();

  /** Order by the start time (needs to be followed by {@link #asc()} or {@link #desc()}). */
  HistoricProcessInstanceQueryWrapper orderByProcessInstanceStartTime();

  /** Order by the end time (needs to be followed by {@link #asc()} or {@link #desc()}). */
  HistoricProcessInstanceQueryWrapper orderByProcessInstanceEndTime();

  /** Order by the duration of the process instance (needs to be followed by {@link #asc()} or {@link #desc()}). */
  HistoricProcessInstanceQueryWrapper orderByProcessInstanceDuration();

  /** Only select historic process instances started by the given process
   * instance. {@link ProcessInstanceWrapper) ids and {@#link HistoricProcessInstance}
   * ids match. */
  HistoricProcessInstanceQueryWrapper superProcessInstanceId(String superProcessInstanceId);

  // below is deprecated and should be removed in 5.12

  /** Only select historic process instances that were started as of the provided
   * date. (Date will be adjusted to reflect midnight)
   * @deprecated will be removed in 5.12, use {@link #startedAfter(java.util.Date)} and {@link #startedBefore(java.util.Date)} instead */
  HistoricProcessInstanceQueryWrapper startDateBy(Date date);

  /** Only select historic process instances that were started on the provided date.
   * @deprecated will be removed in 5.12, use {@link #startedAfter(java.util.Date)} and {@link #startedBefore(java.util.Date)} instead */
  HistoricProcessInstanceQueryWrapper startDateOn(Date date);

  /** Only select historic process instances that were finished as of the
   * provided date. (Date will be adjusted to reflect one second before midnight)
   * @deprecated will be removed in 5.12, use {@link #startedAfter(java.util.Date)} and {@link #startedBefore(java.util.Date)} instead */
  HistoricProcessInstanceQueryWrapper finishDateBy(Date date);

  /** Only select historic process instances that were finished on provided date.
   * @deprecated will be removed in 5.12, use {@link #startedAfter(java.util.Date)} and {@link #startedBefore(java.util.Date)} instead */
  HistoricProcessInstanceQueryWrapper finishDateOn(Date date);
}
