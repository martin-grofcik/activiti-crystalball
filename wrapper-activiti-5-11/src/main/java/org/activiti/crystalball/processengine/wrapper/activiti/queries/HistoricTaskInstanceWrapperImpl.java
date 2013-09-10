package org.activiti.crystalball.processengine.wrapper.activiti.queries;

import org.activiti.crystalball.processengine.wrapper.queries.HistoricActivityInstanceWrapper;
import org.activiti.crystalball.processengine.wrapper.queries.HistoricTaskInstanceWrapper;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 */
public class HistoricTaskInstanceWrapperImpl implements HistoricTaskInstanceWrapper {
    HistoricTaskInstance historicTaskInstance;

    public String getId() {
        return historicTaskInstance.getId();
    }

    public Date getDueDate() {
        return historicTaskInstance.getDueDate();
    }

    public String getTaskDefinitionKey() {
        return historicTaskInstance.getTaskDefinitionKey();
    }

    public String getProcessInstanceId() {
        return historicTaskInstance.getProcessInstanceId();
    }

    public String getOwner() {
        return historicTaskInstance.getOwner();
    }

    public String getName() {
        return historicTaskInstance.getName();
    }

    public int getPriority() {
        return historicTaskInstance.getPriority();
    }

    public String getProcessDefinitionId() {
        return historicTaskInstance.getProcessDefinitionId();
    }

    public Date getStartTime() {
        return historicTaskInstance.getStartTime();
    }

    public String getAssignee() {
        return historicTaskInstance.getAssignee();
    }

    public String getDeleteReason() {
        return historicTaskInstance.getDeleteReason();
    }

    public Date getEndTime() {
        return historicTaskInstance.getEndTime();
    }

    public String getParentTaskId() {
        return historicTaskInstance.getParentTaskId();
    }

    public String getExecutionId() {
        return historicTaskInstance.getExecutionId();
    }

    public String getDescription() {
        return historicTaskInstance.getDescription();
    }

    public Long getDurationInMillis() {
        return historicTaskInstance.getDurationInMillis();
    }

    public HistoricTaskInstanceWrapperImpl(HistoricTaskInstance historicTaskInstance) {
        this.historicTaskInstance = historicTaskInstance;
    }

    public static List<HistoricTaskInstanceWrapper> convert(List<HistoricTaskInstance> list) {
        if (list != null) {
            List<HistoricTaskInstanceWrapper> destinationList = new ArrayList<HistoricTaskInstanceWrapper>(list.size());
            for (HistoricTaskInstance item : list) {
                destinationList.add( new HistoricTaskInstanceWrapperImpl( item ));
            }
            return destinationList;
        }
        return null;
    }
}
