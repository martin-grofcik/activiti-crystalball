package org.activiti.crystalball.simulator.result;

import org.activiti.crystalball.simulator.impl.persistence.entity.ResultEntity;
import org.activiti.engine.query.Query;

public interface ResultQuery extends Query<ResultQuery, ResultEntity>{
	  /**
	   * Only select result with the given result id (in practice, there will be
	   * maximum one of this kind)
	   */
	  ResultQuery resultId(String resultId);

	  /** Only select results with the given runId */
	  ResultQuery resultRunId(String type);

	  /** Only select results with the given type */
	  ResultQuery resultType(String type);
	  
	  /** Only select results with the given processDefinitionKey*/
	  ResultQuery resultProcessDefinitionKey(String processDefinitinoKey);

	  /** Only select results with the given taskDefinitionKey*/
	  ResultQuery resultTaskDefinitionKey(String taskDefinitinoKey);

	  /** Only select results for given simulationInstanceId*/
	  ResultQuery simulationInstanceId(String simulationInstanceId);

	  /** Only select results with given variable and value*/
	  ResultQuery resultVariableValueEquals(String variableName, Object variableValue);

}
