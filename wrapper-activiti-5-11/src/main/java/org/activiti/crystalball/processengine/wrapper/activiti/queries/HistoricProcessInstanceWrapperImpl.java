package org.activiti.crystalball.processengine.wrapper.activiti.queries;

import org.activiti.crystalball.processengine.wrapper.queries.HistoricProcessInstanceWrapper;
import org.activiti.crystalball.processengine.wrapper.queries.HistoricTaskInstanceWrapper;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoricProcessInstanceWrapperImpl implements HistoricProcessInstanceWrapper {
    private final HistoricProcessInstanceEntity historicProcessInstance;

    public Object getPersistentState() {
        return historicProcessInstance.getPersistentState();
    }

    @Override
    public String getEndActivityId() {
        return historicProcessInstance.getEndActivityId();
    }

    @Override
    public String getBusinessKey() {
        return historicProcessInstance.getBusinessKey();
    }

    public void setBusinessKey(String businessKey) {
        historicProcessInstance.setBusinessKey(businessKey);
    }

    public void setEndActivityId(String endActivityId) {
        historicProcessInstance.setEndActivityId(endActivityId);
    }

    @Override
    public String getStartUserId() {
        return historicProcessInstance.getStartUserId();
    }

    public void setStartUserId(String startUserId) {
        historicProcessInstance.setStartUserId(startUserId);
    }

    @Override
    public String getStartActivityId() {
        return historicProcessInstance.getStartActivityId();
    }

    public void setStartActivityId(String startUserId) {
        historicProcessInstance.setStartActivityId(startUserId);
    }

    @Override
    public String getSuperProcessInstanceId() {
        return historicProcessInstance.getSuperProcessInstanceId();
    }

    public void setSuperProcessInstanceId(String superProcessInstanceId) {
        historicProcessInstance.setSuperProcessInstanceId(superProcessInstanceId);
    }

    public void markEnded(String deleteReason) {
        historicProcessInstance.markEnded(deleteReason);
    }

    public String getProcessInstanceId() {
        return historicProcessInstance.getProcessInstanceId();
    }

    @Override
    public String getProcessDefinitionId() {
        return historicProcessInstance.getProcessDefinitionId();
    }

    @Override
    public Date getStartTime() {
        return historicProcessInstance.getStartTime();
    }

    @Override
    public Date getEndTime() {
        return historicProcessInstance.getEndTime();
    }

    @Override
    public Long getDurationInMillis() {
        return historicProcessInstance.getDurationInMillis();
    }

    @Override
    public String getId() {
        return historicProcessInstance.getId();
    }

    public void setId(String id) {
        historicProcessInstance.setId(id);
    }

    public void setProcessInstanceId(String processInstanceId) {
        historicProcessInstance.setProcessInstanceId(processInstanceId);
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        historicProcessInstance.setProcessDefinitionId(processDefinitionId);
    }

    public void setStartTime(Date startTime) {
        historicProcessInstance.setStartTime(startTime);
    }

    public void setEndTime(Date endTime) {
        historicProcessInstance.setEndTime(endTime);
    }

    public void setDurationInMillis(Long durationInMillis) {
        historicProcessInstance.setDurationInMillis(durationInMillis);
    }

    @Override
    public String getDeleteReason() {
        return historicProcessInstance.getDeleteReason();
    }

    public void setDeleteReason(String deleteReason) {
        historicProcessInstance.setDeleteReason(deleteReason);
    }

    public HistoricProcessInstanceWrapperImpl(HistoricProcessInstanceEntity historicProcessInstance) {
        this.historicProcessInstance = historicProcessInstance;
    }


    public static List<HistoricProcessInstanceWrapper> convert(List<HistoricProcessInstance> list) {
        if (list != null) {
            List<HistoricProcessInstanceWrapper> destinationList = new ArrayList<HistoricProcessInstanceWrapper>(list.size());
            for (HistoricProcessInstance item : list) {
                destinationList.add( new HistoricProcessInstanceWrapperImpl( (HistoricProcessInstanceEntity) item ));
            }
            return destinationList;
        }
        return null;
    }
}
