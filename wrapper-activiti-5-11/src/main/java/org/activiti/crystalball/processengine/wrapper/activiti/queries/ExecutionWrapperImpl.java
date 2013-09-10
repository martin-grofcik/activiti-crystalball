package org.activiti.crystalball.processengine.wrapper.activiti.queries;

import org.activiti.crystalball.processengine.wrapper.queries.ExecutionWrapper;
import org.activiti.engine.runtime.Execution;

import java.util.ArrayList;
import java.util.List;

public class ExecutionWrapperImpl implements ExecutionWrapper {
    protected final Execution execution;

    @Override
    public String getId() {
        return execution.getId();
    }

    @Override
    public boolean isSuspended() {
        return execution.isSuspended();
    }

    @Override
    public boolean isEnded() {
        return execution.isEnded();
    }

    @Override
    public String getProcessInstanceId() {
        return execution.getProcessInstanceId();
    }

    public ExecutionWrapperImpl(Execution execution) {
        this.execution = execution;
    }

    public static List<ExecutionWrapper> convert(List<Execution> list) {
        if (list != null) {
            List<ExecutionWrapper> destinationList = new ArrayList<ExecutionWrapper>(list.size());
            for (Execution item : list) {
                destinationList.add( new ExecutionWrapperImpl( item ));
            }
            return destinationList;
        }
        return null;
    }
}
