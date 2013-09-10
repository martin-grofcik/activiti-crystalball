package org.activiti.crystalball.processengine.wrapper.activiti.queries;

import org.activiti.crystalball.processengine.wrapper.queries.ProcessInstanceWrapper;
import org.activiti.engine.runtime.ProcessInstance;

public class ProcessInstanceWrapperImpl implements ProcessInstanceWrapper {
    protected final ProcessInstance processInstance;

    @Override
    public String getProcessDefinitionId() {
        return processInstance.getProcessDefinitionId();
    }

    @Override
    public String getBusinessKey() {
        return processInstance.getBusinessKey();
    }

    @Override
    public boolean isSuspended() {
        return processInstance.isSuspended();
    }

    @Override
    public String getId() {
        return processInstance.getId();
    }

    @Override
    public boolean isEnded() {
        return processInstance.isEnded();
    }

    @Override
    public String getProcessInstanceId() {
        return processInstance.getProcessInstanceId();
    }

    public ProcessInstanceWrapperImpl(ProcessInstance processInstance) {
        this.processInstance = processInstance;
    }
}
