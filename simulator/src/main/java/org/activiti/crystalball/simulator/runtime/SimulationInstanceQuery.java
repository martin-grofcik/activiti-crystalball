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
package org.activiti.crystalball.simulator.runtime;

import org.activiti.crystalball.simulator.impl.persistence.entity.SimulationInstanceEntity;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.query.Query;
import org.activiti.engine.runtime.ProcessInstance;

import java.io.Serializable;
import java.util.Set;

/**
 * Allows programmatic querying of {@link ProcessInstance}s.
 * 
 * @author Joram Barrez
 * @author Frederik Heremans
 * @author Falko Menge
 */
public interface SimulationInstanceQuery extends Query<SimulationInstanceQuery, SimulationInstanceEntity> {

  /** Select the process instance with the given id */
  SimulationInstanceQuery simulationInstanceId(String processInstanceId);
  
  /** Select process instances whose id is in the given set of ids */
  SimulationInstanceQuery simulationInstanceIds(Set<String> processInstanceIds);
  
  /** Select process instances with the given business key */
  SimulationInstanceQuery simulationInstanceName(String processInstanceBusinessKey);
  
  /** Select process instance with the given business key, unique for the given process definition */
  SimulationInstanceQuery simulationInstanceName(String simulationNameInstanceBusinessKey, String simulationDefinitionKey);


  /**
   * Selects the process instances which are defined by a process definition
   * with the given id.
   */
  SimulationInstanceQuery simulationConfigurationId(String simulationConfigurationId);

  /** 
   * Only select process instances which have a global variable with the given value. The type
   * of variable is determined based on the value, using types configured in 
   * {@link ProcessEngineConfiguration#getVariableTypes()}. 
   * Byte-arrays and {@link Serializable} objects (which are not primitive type wrappers)
   * are not supported.
   * @param name name of the variable, cannot be null.
   */
  SimulationInstanceQuery variableValueEquals(String name, Object value);
  
  /** 
   * Only select process instances which have at least one global variable with the given value. The type
   * of variable is determined based on the value, using types configured in 
   * {@link ProcessEngineConfiguration#getVariableTypes()}. 
   * Byte-arrays and {@link Serializable} objects (which are not primitive type wrappers)
   * are not supported.
   */
  SimulationInstanceQuery variableValueEquals(Object value);
  
  /** 
   * Only select process instances which have a local string variable with the given value, 
   * case insensitive.
   * <p>
   * This method only works if your database has encoding/collation that supports case-sensitive
   * queries. For example, use "collate UTF-8" on MySQL and for MSSQL, select one of the case-sensitive Collations 
   * available (<a href="http://msdn.microsoft.com/en-us/library/ms144250(v=sql.105).aspx">MSDN Server Collation Reference</a>).
   * </p>
   * @param name name of the variable, cannot be null.
   * @param value value of the variable, cannot be null.
   */
  SimulationInstanceQuery variableValueEqualsIgnoreCase(String name, String value);
  
  /** 
   * Only select process instances which have a global variable with the given name, but
   * with a different value than the passed value.
   * Byte-arrays and {@link Serializable} objects (which are not primitive type wrappers)
   * are not supported.
   * @param name name of the variable, cannot be null.
   */
  SimulationInstanceQuery variableValueNotEquals(String name, Object value);
  
  /** 
   * Only select process instances which have a local string variable which is not the given value, 
   * case insensitive.
   * <p>
   * This method only works if your database has encoding/collation that supports case-sensitive
   * queries. For example, use "collate UTF-8" on MySQL and for MSSQL, select one of the case-sensitive Collations 
   * available (<a href="http://msdn.microsoft.com/en-us/library/ms144250(v=sql.105).aspx">MSDN Server Collation Reference</a>).
   * </p>
   * @param name name of the variable, cannot be null.
   * @param value value of the variable, cannot be null.
   */
  SimulationInstanceQuery variableValueNotEqualsIgnoreCase(String name, String value);

  /** 
   * Only select process instances which have a variable value greater than the passed value.
   * Booleans, Byte-arrays and {@link Serializable} objects (which are not primitive type wrappers)
   * are not supported.
   * @param name variable name, cannot be null.
   * @param value variable value, cannot be null.
   */
  SimulationInstanceQuery variableValueGreaterThan(String name, Object value);
  
  /** 
   * Only select process instances which have a global variable value greater than or equal to
   * the passed value. Booleans, Byte-arrays and {@link Serializable} objects (which 
   * are not primitive type wrappers) are not supported.
   * @param name variable name, cannot be null.
   * @param value variable value, cannot be null.
   */
  SimulationInstanceQuery variableValueGreaterThanOrEqual(String name, Object value);
  
  /** 
   * Only select process instances which have a global variable value less than the passed value.
   * Booleans, Byte-arrays and {@link Serializable} objects (which are not primitive type wrappers)
   * are not supported.
   * @param name variable name, cannot be null.
   * @param value variable value, cannot be null.
   */
  SimulationInstanceQuery variableValueLessThan(String name, Object value);
  
  /** 
   * Only select process instances which have a global variable value less than or equal to the passed value.
   * Booleans, Byte-arrays and {@link Serializable} objects (which are not primitive type wrappers)
   * are not supported.
   * @param name variable name, cannot be null.
   * @param value variable value, cannot be null.
   */
  SimulationInstanceQuery variableValueLessThanOrEqual(String name, Object value);
  
  /** 
   * Only select process instances which have a global variable value like the given value.
   * This be used on string variables only.
   * @param name variable name, cannot be null.
   * @param value variable value, cannot be null. The string can include the
   * wildcard character '%' to express like-strategy: 
   * starts with (string%), ends with (%string) or contains (%string%).
   */
  SimulationInstanceQuery variableValueLike(String name, String value);
  
  /**
   * Only selects process instances which are suspended, either because the 
   * process instance itself is suspended or because the corresponding process 
   * definition is suspended
   */
  SimulationInstanceQuery suspended();
  
  /**
   * Only selects process instances which are active, which means that 
   * neither the process instance nor the corresponding process definition 
   * are suspended.
   */
  SimulationInstanceQuery active();

  SimulationInstanceQuery ended();
  
  //ordering /////////////////////////////////////////////////////////////////
  
  /** Order by id (needs to be followed by {@link #asc()} or {@link #desc()}). */
  SimulationInstanceQuery orderBySimulationInstanceId();
    
  /** Order by process definition id (needs to be followed by {@link #asc()} or {@link #desc()}). */
  SimulationInstanceQuery orderBySimulationConfigurationId();
  
}
