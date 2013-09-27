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

import org.activiti.crystalball.simulator.RuntimeService;
import org.activiti.crystalball.simulator.SimulationEngine;
import org.activiti.crystalball.simulator.impl.cfg.SimulationEngineConfigurationImpl;
import org.activiti.crystalball.simulator.impl.cfg.TransactionContextFactory;
import org.activiti.crystalball.simulator.impl.db.DbSimulatorSqlSession;
import org.activiti.crystalball.simulator.impl.db.DbSimulatorSqlSessionFactory;
import org.activiti.crystalball.simulator.impl.interceptor.CommandExecutor;
import org.activiti.crystalball.simulator.impl.simulationexecutor.JobExecutor;
import org.activiti.engine.impl.el.ExpressionManager;
import org.activiti.engine.impl.interceptor.SessionFactory;

import java.util.Map;
import java.util.logging.Logger;



public class SimulationEngineImpl implements SimulationEngine {

  private static Logger log = Logger.getLogger(SimulationEngineImpl.class.getName());

  protected String name;
  protected String databaseSchemaUpdate;
  protected RuntimeService runtimeService;
  protected CommandExecutor commandExecutor;
  protected Map<Class<?>, SessionFactory> sessionFactories;
  protected ExpressionManager expressionManager;
  protected JobExecutor jobExecutor;
  protected TransactionContextFactory transactionContextFactory;
  protected SimulationEngineConfigurationImpl simulationEngineConfiguration;

  public SimulationEngineImpl(SimulationEngineConfigurationImpl simulationEngineConfiguration) {
    this.simulationEngineConfiguration = simulationEngineConfiguration;
    this.name = simulationEngineConfiguration.getProcessEngineName();
    this.runtimeService = simulationEngineConfiguration.getRuntimeService();

    this.databaseSchemaUpdate = simulationEngineConfiguration.getDatabaseSchemaUpdate();
    this.jobExecutor = simulationEngineConfiguration.getJobExecutor();

    this.commandExecutor = simulationEngineConfiguration.getCommandExecutorTxRequired();
    this.sessionFactories = simulationEngineConfiguration.getSessionFactories();
    this.transactionContextFactory = simulationEngineConfiguration.getTransactionContextFactory();
    
    commandExecutor.execute(new SchemaOperationsSimulationEngineBuild());

    if (name == null) {
      log.info("default activiti SimulationEngine created");
    } else {
      log.info("SimulationEngine " + name + " created");
    }
    
    if ((jobExecutor != null) && (jobExecutor.isAutoActivate())) {
        jobExecutor.start();
    } 
  }
  
  public void close() {
		if ((jobExecutor != null) && (jobExecutor.isActive())) {
			jobExecutor.shutdown();
		}

		commandExecutor.execute(new SchemaOperationSimulationEngineClose());
  }

  public DbSimulatorSqlSessionFactory getDbSqlSessionFactory() {
    return (DbSimulatorSqlSessionFactory) sessionFactories.get(DbSimulatorSqlSession.class);
  }
  
  // getters and setters //////////////////////////////////////////////////////

  public String getName() {
    return name;
  }

	@Override
	public RuntimeService getRuntimeService() {
		return runtimeService;
	}
}
