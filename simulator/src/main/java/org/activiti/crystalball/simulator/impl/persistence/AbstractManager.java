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
import org.activiti.engine.impl.db.PersistentObject;
import org.activiti.engine.impl.history.HistoryManager;
import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.persistence.deploy.DeploymentManager;
import org.activiti.engine.impl.persistence.entity.*;


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

  protected DeploymentEntityManager getDeploymentManager() {
    return getSession(DeploymentEntityManager.class);
  }

  protected ResourceEntityManager getResourceManager() {
    return getSession(ResourceEntityManager.class);
  }
  
  protected ByteArrayEntityManager getByteArrayManager() {
    return getSession(ByteArrayEntityManager.class);
  }
  
  protected ProcessDefinitionEntityManager getProcessDefinitionManager() {
    return getSession(ProcessDefinitionEntityManager.class);
  }
  
  protected ModelEntityManager getModelManager() {
    return getSession(ModelEntityManager.class);
  }

  
  protected HistoryManager getHistoryManager() {
    return getSession(HistoryManager.class);
  }
  
  public void close() {
  }

  public void flush() {
  }
}
