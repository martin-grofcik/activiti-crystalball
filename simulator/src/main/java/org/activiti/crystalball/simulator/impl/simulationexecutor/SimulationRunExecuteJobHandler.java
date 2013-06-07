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
package org.activiti.crystalball.simulator.impl.simulationexecutor;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.activiti.crystalball.simulator.SimulationRun;
import org.activiti.crystalball.simulator.SimulationRunHelper;
import org.activiti.crystalball.simulator.impl.context.SimulationContext;
import org.activiti.crystalball.simulator.impl.interceptor.CommandContext;
import org.activiti.crystalball.simulator.impl.persistence.entity.JobEntity;
import org.activiti.crystalball.simulator.impl.persistence.entity.SimulationInstanceEntity;
import org.activiti.crystalball.simulator.impl.persistence.entity.SimulationRunEntity;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * @author Tom Baeyens
 * @author Joram Barrez
 */
public class SimulationRunExecuteJobHandler implements JobHandler {
  
  private static Logger log = Logger.getLogger(SimulationRunExecuteJobHandler.class.getName());
  
  public static final String TYPE = "simulation-run";

  public String getType() {
    return TYPE;
  }
  
  public void execute(JobEntity job, String configuration, SimulationInstanceEntity simulationInstance, CommandContext commandContext) {
	  log.log(Level.INFO,"Starting simulation experiment [" + simulationInstance + "] configuration ["+ configuration+"]");

	  SimulationRunEntity simulationRun = commandContext.getSimulationRunManager().findSimulationRunWithReferencesById(configuration);
	  SimulationContext.setSimulationRun(simulationRun);
	  
	  //initializeSimulationRun
	  ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(simulationRun.getSimulation().getSimulationConfigUrl());
	  PropertyPlaceholderConfigurer propConfig = new PropertyPlaceholderConfigurer();	  
	  Properties properties = new Properties();
	  properties.put("simulationRunId", configuration);
	  propConfig.setProperties(properties);
	  appContext.addBeanFactoryPostProcessor(propConfig);
	  appContext.refresh();
	  
	  SimulationRunHelper runHelper = new NoopSimulationRunHelper();
	  if (appContext.containsBean("simulationRunHelper"))
		  runHelper = (SimulationRunHelper) appContext.getBean("simulationRunHelper");

	  try {
		  runHelper.before( configuration);
		  
		  SimulationRun simRun = (SimulationRun) appContext.getBean("simulationRun");
		  simRun.execute(simulationRun);
		} catch (Exception e) {
			log.log(Level.SEVERE, "SimulationRun handling error" + simulationRun, e);
		} 	finally {
		
		runHelper.after( configuration);
		appContext.close();
	  	}
	  job.delete();

	  SimulationContext.removeSimulationRun();

	  // check whether all jobs for given simulationInstance were already executed.
	  simulationInstance.checkActivity();
	  
	  log.log(Level.INFO,"finished simulation experiment [" + simulationInstance + "] configuration ["+ configuration+"]");	  
  }

}
