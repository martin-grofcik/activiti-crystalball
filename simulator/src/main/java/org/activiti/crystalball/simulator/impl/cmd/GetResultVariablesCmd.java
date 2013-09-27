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

package org.activiti.crystalball.simulator.impl.cmd;

import org.activiti.crystalball.simulator.impl.context.SimulationContext;
import org.activiti.crystalball.simulator.impl.interceptor.Command;
import org.activiti.crystalball.simulator.impl.interceptor.CommandContext;
import org.activiti.crystalball.simulator.impl.persistence.entity.ResultEntity;
import org.activiti.engine.ActivitiException;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Tom Baeyens
 */
public class GetResultVariablesCmd implements Command<Map<String,Object>>, Serializable {

  private static final long serialVersionUID = 1L;
  protected String resultId;
  protected Collection<String> variableNames;
  protected boolean isLocal;

  public GetResultVariablesCmd(String resultId, Collection<String> variableNames, boolean isLocal) {
    this.resultId = resultId;
    this.variableNames = variableNames;
    this.isLocal = isLocal;
  }

  public Map<String, Object> execute(CommandContext commandContext) {
    if(resultId == null) {
      throw new ActivitiException("resultId is null");
    }
    
    ResultEntity result = SimulationContext.getCommandContext()
      .getResultEntityManager().findResultById(resultId);
    
    if (result==null) {
      throw new ActivitiException("task "+resultId+" doesn't exist");
    }
    
    Map<String, Object> taskVariables;
    if (isLocal) {
      taskVariables = result.getVariablesLocal();
    } else {
      taskVariables = result.getVariables();
    }
    
    if (variableNames==null) {
      variableNames = taskVariables.keySet();
    }
    
    // this copy is made to avoid lazy initialization outside a command context
    Map<String, Object> variables = new HashMap<String, Object>();
    for (String variableName: variableNames) {
      variables.put(variableName, result.getVariable(variableName));
    }
    
    return variables;
    
  }
}
