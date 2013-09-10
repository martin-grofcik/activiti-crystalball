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

package org.activiti.crystalball.simulator.impl.persistence;

import org.activiti.crystalball.simulator.impl.context.SimulationContext;
import org.activiti.crystalball.simulator.impl.db.DbSimulatorSqlSession;
import org.activiti.crystalball.simulator.impl.db.PersistentObject;
import org.activiti.crystalball.simulator.impl.interceptor.Session;
import org.activiti.crystalball.simulator.impl.persistence.entity.ByteArrayManager;

/**
 * @author Tom Baeyens
 * @author Joram Barrez
 */
public abstract class AbstractManager implements Session {
  
  public void insert(PersistentObject persistentObject) {
    getDbSqlSession().insert(persistentObject);
  }

  public void delete(PersistentObject persistentObject) {
    getDbSqlSession().delete(persistentObject);
  }

  protected DbSimulatorSqlSession getDbSqlSession() {
    return getSession(DbSimulatorSqlSession.class);
  }

  protected <T> T getSession(Class<T> sessionClass) {
    return SimulationContext.getCommandContext().getSession(sessionClass);
  }

//  protected DeploymentManager getDeploymentManager() {
//    return getSession(DeploymentManager.class);
//  }

//  protected ResourceManager getResourceManager() {
//    return getSession(ResourceManager.class);
//  }
  
  protected ByteArrayManager getByteArrayManager() {
    return getSession(ByteArrayManager.class);
  }
  
//  protected ProcessDefinitionManager getProcessDefinitionManager() {
//    return getSession(ProcessDefinitionManager.class);
//  }
  
//  protected ModelManager getModelManager() {
//    return getSession(ModelManager.class);
//  }

  
//  protected HistoryManager getHistoryManager() {
//    return getSession(HistoryManager.class);
//  }
  
  public void close() {
  }

  public void flush() {
  }
}
