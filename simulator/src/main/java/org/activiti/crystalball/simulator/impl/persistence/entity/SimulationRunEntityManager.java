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

import org.activiti.crystalball.simulator.impl.context.SimulationContext;
import org.activiti.crystalball.simulator.impl.persistence.AbstractManager;
import org.activiti.engine.impl.cfg.IdGenerator;

import java.util.HashMap;
import java.util.Map;



public class SimulationRunEntityManager extends AbstractManager {

	public SimulationRunEntity createSimulationRun(SimulationInstanceEntity simInstance, int replication) {

		IdGenerator idGenerator = SimulationContext.getSimulationEngineConfiguration()
				.getIdGenerator();

		SimulationRunEntity simulationRun = new SimulationRunEntity();
		simulationRun.setId(idGenerator.getNextId());
		simulationRun.setSimulationEntityId(simInstance.getId());
		simulationRun.setReplication(replication);

		getDbSqlSession().insert(simulationRun);
		
		return simulationRun;
	}

	public SimulationRunEntity findSimulationRunById(String simulationRunId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("simulationRunId", simulationRunId);
		return (SimulationRunEntity) getDbSqlSession().selectOne(
				"selectSimulationRunById", parameters);
	}

	public SimulationRunEntity findSimulationRunWithReferencesById(String simulationRunId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("simulationRunId", simulationRunId);
		return (SimulationRunEntity) getDbSqlSession().selectOne(
				"selectSimulationRunWithReferencesById", parameters);
	}

}
