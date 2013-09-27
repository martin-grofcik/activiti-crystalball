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

package org.activiti.crystalball.simulator.impl;

import org.activiti.crystalball.simulator.impl.interceptor.CommandContext;
import org.activiti.crystalball.simulator.impl.interceptor.CommandExecutor;
import org.activiti.crystalball.simulator.impl.persistence.entity.SimulationInstanceEntity;
import org.activiti.crystalball.simulator.runtime.SimulationInstanceQuery;
import org.activiti.crystalball.simulator.runtime.SuspensionState;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.EventSubscriptionQueryValue;
import org.activiti.engine.impl.Page;

import java.io.Serializable;
import java.util.List;
import java.util.Set;


/**
 * @author Tom Baeyens
 * @author Joram Barrez
 * @author Frederik Heremans
 * @author Falko Menge
 * @author Daniel Meyer
 */
public class SimulationInstanceQueryImpl extends AbstractVariableQueryImpl<SimulationInstanceQuery, SimulationInstanceEntity> implements SimulationInstanceQuery, Serializable {

  private static final long serialVersionUID = 1L;
  protected String simulationInstanceId;
  protected String simulationDefinitionId;
  protected String name;
  protected Set<String> simulationInstanceIds; 
  protected SuspensionState suspensionState;
  
  // Unused, see dynamic query
  protected String activityId;
  protected List<EventSubscriptionQueryValue> eventSubscriptions;
  
  public SimulationInstanceQueryImpl() {
  }
  
  public SimulationInstanceQueryImpl(CommandContext commandContext) {
    super(commandContext);
  }
  
  public SimulationInstanceQueryImpl(CommandExecutor commandExecutor) {
    super(commandExecutor);
  }

  public SimulationInstanceQueryImpl simulationInstanceId(String processInstanceId) {
    if (processInstanceId == null) {
      throw new ActivitiException("Process instance id is null");
    }
    this.simulationInstanceId = processInstanceId;
    return this;
  }
  
  public SimulationInstanceQuery simulationInstanceIds(Set<String> simulationInstanceIds) {
    if (simulationInstanceIds == null) {
      throw new ActivitiException("Set of process instance ids is null");
    }
    if (simulationInstanceIds.isEmpty()) {
      throw new ActivitiException("Set of process instance ids is empty");
    }
    this.simulationInstanceIds = simulationInstanceIds;
    return this;
  }

  public SimulationInstanceQuery simulationInstanceName(String name) {
    if (name == null) {
      throw new ActivitiException("Business key is null");
    }
    this.name = name;
    return this;
  }
  
  public SimulationInstanceQuery simulationInstanceBusinessKey(String name, String simulationDefinitionId) {
    if (name == null) {
      throw new ActivitiException("Business key is null");
    }
    this.name = name;
    this.simulationDefinitionId = simulationDefinitionId;
    return this;
  }
  
  public SimulationInstanceQueryImpl processDefinitionId(String simulationDefinitionId) {
    if (simulationDefinitionId == null) {
      throw new ActivitiException("Process definition id is null");
    }
    this.simulationDefinitionId = simulationDefinitionId;
    return this;
  }

  public SimulationInstanceQuery orderBySimulationInstanceId() {
    this.orderProperty = SimulationInstanceQueryProperty.SIMULATION_INSTANCE_ID;
    return this;
  }
  
  public SimulationInstanceQuery orderByProcessDefinitionId() {
    this.orderProperty = SimulationInstanceQueryProperty.SIMULATION_DEFINITION_ID;
    return this;
  }
  
  
  public SimulationInstanceQuery active() {
    this.suspensionState = SuspensionState.ACTIVE;
    return this;
  }
  
  public SimulationInstanceQuery suspended() {
    this.suspensionState = SuspensionState.SUSPENDED;
    return this;
  }

  public SimulationInstanceQuery ended() {
	    this.suspensionState = SuspensionState.FINISHED;
	    return this;
  }

  //results /////////////////////////////////////////////////////////////////
  
  public long executeCount(CommandContext commandContext) {
    checkQueryOk();
    ensureVariablesInitialized();
    return commandContext
      .getSimulationInstanceManager()
      .findSimulationInstanceCountByQueryCriteria(this);
  }

  public List<SimulationInstanceEntity> executeList(CommandContext commandContext, Page page) {
    checkQueryOk();
    ensureVariablesInitialized();
    return commandContext
      .getSimulationInstanceManager()
      .findSimulationInstancesByQueryCriteria(this, page);
  }
  
  //getters /////////////////////////////////////////////////////////////////
  
  public boolean getOnlyProcessInstances() {
    return true; // See dynamic query in runtime.mapping.xml
  }
  public String getProcessInstanceId() {
    return simulationInstanceId;
  }
  public Set<String> getProcessInstanceIds() {
    return simulationInstanceIds;
  }
  public String getProcessDefinitionId() {
    return simulationDefinitionId;
  }
  public String getActivityId() {
    return null; // Unused, see dynamic query
  }
  public SuspensionState getSuspensionState() {
    return suspensionState;
  }  
  public void setSuspensionState(SuspensionState suspensionState) {
    this.suspensionState = suspensionState;
  }  

  public List<EventSubscriptionQueryValue> getEventSubscriptions() {
    return eventSubscriptions;
  }

  public void setEventSubscriptions(List<EventSubscriptionQueryValue> eventSubscriptions) {
    this.eventSubscriptions = eventSubscriptions;
  }

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}


@Override
public SimulationInstanceQuery simulationInstanceName(
		String simulationNameInstanceBusinessKey, String simulationDefinitionKey) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public SimulationInstanceQuery simulationConfigurationId(
		String simulationConfigurationId) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public SimulationInstanceQuery orderBySimulationConfigurationId() {
	// TODO Auto-generated method stub
	return null;
}
}
