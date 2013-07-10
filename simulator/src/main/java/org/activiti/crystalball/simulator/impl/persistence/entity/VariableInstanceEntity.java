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
package org.activiti.crystalball.simulator.impl.persistence.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.activiti.crystalball.simulator.impl.context.SimulationContext;
import org.activiti.engine.impl.db.HasRevision;
import org.activiti.engine.impl.db.PersistentObject;
import org.activiti.engine.impl.persistence.entity.ByteArrayEntity;
import org.activiti.engine.impl.variable.ValueFields;
import org.activiti.engine.impl.variable.VariableType;

/**
 * @author Tom Baeyens
 */
public class VariableInstanceEntity implements ValueFields, PersistentObject, HasRevision, Serializable {

  private static final long serialVersionUID = 1L;

  protected String id;
  protected int revision;

  protected String name;

  protected String simulationInstanceId;
  protected String runId;
  protected String resultId;

  protected Long longValue;
  protected Double doubleValue; 
  protected String textValue;
  protected String textValue2;

  protected ByteArrayEntity byteArrayValue;
  protected String byteArrayValueId;

  protected Object cachedValue;

  protected VariableType type;
  
  // Default constructor for SQL mapping
  protected VariableInstanceEntity() {
  }

  public static VariableInstanceEntity createAndInsert(String name, VariableType type, Object value) {
    VariableInstanceEntity variableInstance = create(name, type, value);

    SimulationContext
      .getCommandContext()
      .getDbSqlSession()
      .insert(variableInstance);
  
    return variableInstance;
  }
  
  public static VariableInstanceEntity create(String name, VariableType type, Object value) {
    VariableInstanceEntity variableInstance = new VariableInstanceEntity();
    variableInstance.name = name;
    variableInstance.type = type;
    variableInstance.setValue(value);
    
    return variableInstance;
  }

  public void setRun(SimulationRunEntity simulationRun) {
    this.runId = simulationRun.getId();
    this.simulationInstanceId = simulationRun.getSimulationId();
  }

  public void delete() {
    // delete variable
    SimulationContext
      .getCommandContext()
      .getDbSqlSession()
      .delete(this);
    
    deleteByteArrayValue();
  }

  public Object getPersistentState() {
    Map<String, Object> persistentState = new HashMap<String, Object>();
    if (longValue != null) {
      persistentState.put("longValue", longValue);
    }
    if (doubleValue != null) {
      persistentState.put("doubleValue", doubleValue);
    }
    if (textValue != null) {
      persistentState.put("textValue", textValue);
    }
    if (byteArrayValueId != null) {
      persistentState.put("byteArrayValueId", byteArrayValueId);
    }
    return persistentState;
  }
  
  public int getRevisionNext() {
    return revision+1;
  }

  // lazy initialized relations ///////////////////////////////////////////////

  public void setProcessInstanceId(String processInstanceId) {
    this.simulationInstanceId = processInstanceId;
  }

  public void setRunId(String runId) {
    this.runId = runId;
  }
  
  // byte array value /////////////////////////////////////////////////////////
  
  // i couldn't find a easy readable way to extract the common byte array value logic
  // into a common class.  therefor it's duplicated in VariableInstanceEntity, 
  // HistoricVariableInstance and HistoricDetailVariableInstanceUpdateEntity 
  
  public String getByteArrayValueId() {
    return byteArrayValueId;
  }

  public void setByteArrayValueId(String byteArrayValueId) {
    this.byteArrayValueId = byteArrayValueId;
    this.byteArrayValue = null;
  }

  public ByteArrayEntity getByteArrayValue() {
    if ((byteArrayValue == null) && (byteArrayValueId != null)) {
      byteArrayValue = SimulationContext
        .getCommandContext()
        .getDbSqlSession()
        .selectById(ByteArrayEntity.class, byteArrayValueId);
    }
    return byteArrayValue;
  }
  
  public void setByteArrayValue(byte[] bytes) {
    ByteArrayEntity byteArrayValue = null;
    if (this.byteArrayValueId!=null) {
      getByteArrayValue();
      SimulationContext
        .getCommandContext()
        .getByteArrayManager()
        .deleteByteArrayById(byteArrayValueId);
    }
    if (bytes!=null) {
      byteArrayValue = new ByteArrayEntity(bytes);
      SimulationContext
        .getCommandContext()
        .getDbSqlSession()
        .insert(byteArrayValue);
    }
    this.byteArrayValue = byteArrayValue;
    if (byteArrayValue != null) {
      this.byteArrayValueId = byteArrayValue.getId();
    } else {
      this.byteArrayValueId = null;
    }
  }

  protected void deleteByteArrayValue() {
    if (byteArrayValueId != null) {
      // the next apparently useless line is probably to ensure consistency in the DbSqlSession 
      // cache, but should be checked and docced here (or removed if it turns out to be unnecessary)
      getByteArrayValue();
      SimulationContext
        .getCommandContext()
        .getByteArrayManager()
        .deleteByteArrayById(byteArrayValueId);
    }
  }

  // type /////////////////////////////////////////////////////////////////////

  public Object getValue() {
    if (!type.isCachable() || cachedValue==null) {
      cachedValue = type.getValue(this);
    }
    return cachedValue;
  }

  public void setValue(Object value) {
    type.setValue(value, this);
    cachedValue = value;
  }

  // getters and setters //////////////////////////////////////////////////////

  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getTextValue() {
    return textValue;
  }
  public String getProcessInstanceId() {
    return simulationInstanceId;
  }
  public String getRunId() {
    return runId;
  }
  public Long getLongValue() {
    return longValue;
  }
  public void setLongValue(Long longValue) {
    this.longValue = longValue;
  }
  public Double getDoubleValue() {
    return doubleValue;
  }
  public void setDoubleValue(Double doubleValue) {
    this.doubleValue = doubleValue;
  }
  public void setName(String name) {
    this.name = name;
  }
  public void setTextValue(String textValue) {
    this.textValue = textValue;
  }
  public String getName() {
    return name;
  }
  public int getRevision() {
    return revision;
  }
  public void setRevision(int revision) {
    this.revision = revision;
  }
  public void setType(VariableType type) {
    this.type = type;
  }
  public VariableType getType() {
    return type;
  }
  public Object getCachedValue() {
    return cachedValue;
  }
  public void setCachedValue(Object cachedValue) {
    this.cachedValue = cachedValue;
  }
  public String getTextValue2() {
    return textValue2;
  }
  public void setTextValue2(String textValue2) {
    this.textValue2 = textValue2;
  }
  public String getResultId() {
    return resultId;
  }
  public void setResultId(String resultId) {
    this.resultId = resultId;
  }
}
