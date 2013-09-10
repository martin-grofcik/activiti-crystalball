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


import org.activiti.crystalball.simulator.impl.db.DbSimulatorSqlSession;
import org.activiti.crystalball.simulator.impl.interceptor.Command;
import org.activiti.crystalball.simulator.impl.interceptor.CommandContext;




/**
 * @author Tom Baeyens
 */
final class SchemaOperationSimulationEngineClose implements Command<Object> {

  public Object execute(CommandContext commandContext) {
    commandContext
      .getSession(DbSimulatorSqlSession.class)
      .performSchemaOperationsSimulationEngineClose();
    return null;
  }
}