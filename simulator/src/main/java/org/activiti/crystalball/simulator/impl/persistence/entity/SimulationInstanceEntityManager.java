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

import org.activiti.crystalball.simulator.impl.SimulationInstanceQueryImpl;
import org.activiti.crystalball.simulator.impl.context.SimulationContext;
import org.activiti.crystalball.simulator.impl.persistence.AbstractManager;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.cfg.IdGenerator;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class SimulationInstanceEntityManager extends AbstractManager {

	public SimulationInstanceEntity createSimulationInstance(String name, String description, String author, Date start, Date end, int replication, Long seed, String simulationConfigUrl) {

		IdGenerator idGenerator = SimulationContext.getSimulationEngineConfiguration()
				.getIdGenerator();

		SimulationInstanceEntity simulationInstance = new SimulationInstanceEntity();
		simulationInstance.setId(idGenerator.getNextId());
		simulationInstance.setName(name);
		simulationInstance.setDescription(description);
		simulationInstance.setAuthor(author);
		simulationInstance.setStart(start);
		simulationInstance.setEnd(end);
		simulationInstance.setReplication(replication);
		simulationInstance.setSeed(seed);
		simulationInstance.setSimulationConfigUrl(simulationConfigUrl);

		getDbSqlSession().insert(simulationInstance);
		
		return simulationInstance;
	}

	public SimulationInstanceEntity findSimulationInstanceById(String simulationInstanceId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("simulationInstanceId", simulationInstanceId);
		return (SimulationInstanceEntity) getDbSqlSession().selectOne(
				"selectSimulationInstanceById", parameters);
	}
	
	public long findSimulationInstanceCountByQueryCriteria( SimulationInstanceQueryImpl executionQuery) {
		return (Long) getDbSqlSession().selectOne(
				"selectSimulationInstanceCountByQueryCriteria", executionQuery);
	}

	@SuppressWarnings("unchecked")
	public List<SimulationInstanceEntity> findSimulationInstancesByQueryCriteria(SimulationInstanceQueryImpl executionQuery, Page page) {
		return getDbSqlSession().selectList("selectSimulationInstancesByQueryCriteria",
				executionQuery, page);
	}

	public void update(SimulationInstanceEntity simulationInstanceEntity) {
		getDbSqlSession().update( simulationInstanceEntity);
	}

}
