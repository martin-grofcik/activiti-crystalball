package org.activiti.crystalball.simulator.impl;

import java.util.List;

import org.activiti.crystalball.simulator.SimulatorException;
import org.activiti.crystalball.simulator.impl.interceptor.CommandContext;
import org.activiti.crystalball.simulator.impl.interceptor.CommandExecutor;
import org.activiti.crystalball.simulator.impl.persistence.entity.ResultEntity;
import org.activiti.crystalball.simulator.result.ResultQuery;
import org.activiti.engine.impl.Page;


public class ResultQueryImpl extends AbstractVariableQueryImpl<ResultQuery, ResultEntity> implements ResultQuery {
	  
	  private static final long serialVersionUID = 1L;
	  protected String resultId;
	  protected String runId;
	  protected String simulationInstanceId;
	  protected String type;
	  protected String processDefinitionKey;
	  protected String taskDefinitionKey;


	  public ResultQueryImpl() {
	  }
	  
	  public ResultQueryImpl(CommandContext commandContext) {
	    super(commandContext);
	  }
	  
	  public ResultQueryImpl(CommandExecutor commandExecutor) {
	    super(commandExecutor);
	  }
	  
	  @Override
	public ResultQuery resultId(String resultId) {
		    if (resultId == null) {
		        throw new SimulatorException("Result id is null");
		      }
		      this.resultId = resultId;
		      return this;	
	}
	  
	@Override
	public ResultQuery resultRunId(String type) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ResultQuery resultType(String type) {
		this.type = type;
		return this;
	}
	@Override
	public ResultQuery resultProcessDefinitionKey(String processDefinitinoKey) {
		this.processDefinitionKey = processDefinitinoKey;
		return this;
	}
	@Override
	public ResultQuery resultTaskDefinitionKey(String taskDefinitionKey) {
		this.taskDefinitionKey = taskDefinitionKey;
		return this;
	}
	@Override
	public long executeCount(CommandContext commandContext) {
	    checkQueryOk();
	    return commandContext.getResultEntityManager().findResultCountByQueryCriteria( this);
	}
	
	@Override
	public List<ResultEntity> executeList(CommandContext commandContext, Page page) {
	    checkQueryOk();
	    ensureVariablesInitialized();
	    return commandContext
	      .getResultEntityManager()
	      .findResultsByQueryCriteria(this, page);
	}

	public String getResultId() {
		return resultId;
	}

	public String getRunId() {
		return runId;
	}

	public String getType() {
		return type;
	}

	public String getProcessDefinitionKey() {
		return processDefinitionKey;
	}

	public String getTaskDefinitionKey() {
		return taskDefinitionKey;
	}

	@Override
	public ResultQuery simulationInstanceId(String simulationInstanceId) {
		this.simulationInstanceId = simulationInstanceId;
		return this;
	}
}
