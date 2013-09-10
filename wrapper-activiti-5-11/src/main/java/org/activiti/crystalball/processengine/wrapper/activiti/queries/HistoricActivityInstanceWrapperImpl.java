package org.activiti.crystalball.processengine.wrapper.activiti.queries;

import org.activiti.crystalball.processengine.wrapper.queries.HistoricActivityInstanceWrapper;
import org.activiti.engine.history.HistoricActivityInstance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 */
public class HistoricActivityInstanceWrapperImpl implements HistoricActivityInstanceWrapper {
    private final HistoricActivityInstance historicActivityInstance;

    public HistoricActivityInstanceWrapperImpl(HistoricActivityInstance historicActivityInstance) {
        this.historicActivityInstance = historicActivityInstance;
    }

    @Override
    public String getId() {
        return historicActivityInstance.getId();
    }

    @Override
    public String getActivityId() {
        return historicActivityInstance.getActivityId();
    }

    @Override
    public String getActivityName() {
        return historicActivityInstance.getActivityName();
    }

    @Override
    public String getActivityType() {
        return historicActivityInstance.getActivityType();
    }

    @Override
    public String getProcessDefinitionId() {
        return historicActivityInstance.getProcessDefinitionId();
    }

    @Override
    public String getProcessInstanceId() {
        return historicActivityInstance.getProcessInstanceId();
    }

    @Override
    public String getExecutionId() {
        return historicActivityInstance.getExecutionId();
    }

    @Override
    public String getTaskId() {
        return historicActivityInstance.getTaskId();
    }

    @Override
    public String getCalledProcessInstanceId() {
        return historicActivityInstance.getCalledProcessInstanceId();
    }

    @Override
    public String getAssignee() {
        return historicActivityInstance.getAssignee();
    }

    @Override
    public Date getStartTime() {
        return historicActivityInstance.getStartTime();
    }

    @Override
    public Date getEndTime() {
        return historicActivityInstance.getEndTime();
    }

    @Override
    public Long getDurationInMillis() {
        return historicActivityInstance.getDurationInMillis();
    }

    public static List<HistoricActivityInstanceWrapper> convert(List<HistoricActivityInstance> list) {
        if (list != null) {
            List<HistoricActivityInstanceWrapper> destinationList = new ArrayList(list.size());
            for (HistoricActivityInstance item : list) {
                destinationList.add( new HistoricActivityInstanceWrapperImpl( item ));
            }
            return destinationList;
        }
        return null;
    }
}
