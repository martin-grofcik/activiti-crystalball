package org.activiti.crystalball.processengine.wrapper.activiti;

import org.activiti.crystalball.processengine.wrapper.RuntimeServiceWrapper;
import org.activiti.crystalball.processengine.wrapper.activiti.queries.ExecutionQueryWrapperImpl;
import org.activiti.crystalball.processengine.wrapper.activiti.queries.ProcessInstanceWrapperImpl;
import org.activiti.crystalball.processengine.wrapper.queries.ExecutionQueryWrapper;
import org.activiti.crystalball.processengine.wrapper.queries.ProcessInstanceWrapper;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;


public class RuntimeServiceWrapperImpl implements RuntimeServiceWrapper {
    protected final RuntimeService runtimeService;

    @Override
    public ProcessInstanceWrapper startProcessInstanceByKey(String processDefinitionKey) {
        return new ProcessInstanceWrapperImpl( runtimeService.startProcessInstanceByKey(processDefinitionKey));
    }

    @Override
    public ExecutionQueryWrapper createExecutionQuery() {
        return new ExecutionQueryWrapperImpl(runtimeService.createExecutionQuery());
    }

    @Override
    public ProcessInstanceWrapper startProcessInstanceByKey(String processDefinitionKey, Map<String, Object> variables) {
        return new ProcessInstanceWrapperImpl( runtimeService.startProcessInstanceByKey(processDefinitionKey, variables));
    }

    public RuntimeServiceWrapperImpl(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }
}
