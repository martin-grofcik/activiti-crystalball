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
package org.activiti.crystalball.simulator.initialization;

import java.io.IOException;
import java.util.Date;

import org.activiti.crystalball.simulator.RuntimeService;
import org.activiti.crystalball.simulator.SimulationEngine;
import org.activiti.crystalball.simulator.SimulationEngineConfiguration;
import org.activiti.crystalball.simulator.runtime.SimulationInstance;
import org.activiti.engine.impl.test.PvmTestCase;

/**
 * @author Tom Baeyens
 */
public class SimulationEngineTest extends PvmTestCase {
  
  protected void setUp() throws Exception {
    super.setUp();
  }
  
  protected void tearDown() throws Exception {
    super.tearDown();
  }

  public void testSimulationEngineInitialization() throws IOException {
	  	  
      SimulationEngineConfiguration simulationEngineConfiguration = SimulationEngineConfiguration.createStandaloneInMemSimulationEngineConfiguration();
      SimulationEngine simulationEngine = simulationEngineConfiguration.buildSimulationEngine();
      assertNotNull(simulationEngine);
      simulationEngine.close();      
  }

  public void testSimulationStart() throws IOException, InterruptedException {
	  SimulationEngineConfiguration simulationEngineConfiguration = SimulationEngineConfiguration.createSimulationEngineConfigurationFromInputStream(ClassLoader.getSystemResource("crystalball.cfg.xml").openStream());
	  SimulationEngine simulationEngine = simulationEngineConfiguration.buildSimulationEngine();
      assertNotNull(simulationEngine);
      
      RuntimeService runtimeService = simulationEngine.getRuntimeService();
	  SimulationInstance simInstance = runtimeService.startSimulationInstanceByKey("test", "test-desc", "test-author", new Date(), new Date(), 1, 20L, "test-url");
      assertNotNull( simInstance.getId());
      
      Thread.sleep(10000);
      
      simulationEngine.close();      	  
  }
}
