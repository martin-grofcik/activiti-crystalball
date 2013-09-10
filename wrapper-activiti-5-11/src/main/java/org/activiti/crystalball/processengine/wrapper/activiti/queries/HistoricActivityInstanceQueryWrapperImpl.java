package org.activiti.crystalball.processengine.wrapper.activiti.queries;

import org.activiti.crystalball.processengine.wrapper.queries.HistoricActivityInstanceQueryWrapper;
import org.activiti.crystalball.processengine.wrapper.queries.HistoricActivityInstanceWrapper;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;

import java.util.List;


public class HistoricActivityInstanceQueryWrapperImpl implements HistoricActivityInstanceQueryWrapper {
    private final HistoricActivityInstanceQuery historicActivityInstanceQuery;

    public HistoricActivityInstanceQueryWrapperImpl(HistoricActivityInstanceQuery historicActivityInstanceQuery) {
        this.historicActivityInstanceQuery = historicActivityInstanceQuery;
    }

    @Override
    public HistoricActivityInstanceQueryWrapper activityInstanceId(String processInstanceId) {
        historicActivityInstanceQuery.activityInstanceId(processInstanceId);
        return this;
    }

    @Override
    public HistoricActivityInstanceQueryWrapper processInstanceId(String processInstanceId) {
        historicActivityInstanceQuery.processInstanceId(processInstanceId);
        return this;
    }

    @Override
    public HistoricActivityInstanceQueryWrapper processDefinitionId(String processDefinitionId) {
        historicActivityInstanceQuery.processDefinitionId(processDefinitionId);
        return this;
    }

    @Override
    public HistoricActivityInstanceQueryWrapper executionId(String executionId) {
        historicActivityInstanceQuery.executionId(executionId);
        return this;
    }

    @Override
    public HistoricActivityInstanceQueryWrapper activityId(String activityId) {
        historicActivityInstanceQuery.activityId(activityId);
        return this;
    }

    @Override
    public HistoricActivityInstanceQueryWrapper  activityName(String activityName) {
        historicActivityInstanceQuery.activityName(activityName);
        return this;
    }

    @Override
    public HistoricActivityInstanceQueryWrapper  activityType(String activityType) {
        historicActivityInstanceQuery.activityType(activityType);
        return this;
    }

    @Override
    public HistoricActivityInstanceQueryWrapper  taskAssignee(String userId) {
        historicActivityInstanceQuery.taskAssignee(userId);
        return this;
    }

    @Override
    public HistoricActivityInstanceQueryWrapper  finished() {
        historicActivityInstanceQuery.finished();
        return this;
    }

    @Override
    public HistoricActivityInstanceQueryWrapper  unfinished() {
        historicActivityInstanceQuery.unfinished();
        return this;
    }

    @Override
    public HistoricActivityInstanceQueryWrapper orderByHistoricActivityInstanceId() {
        historicActivityInstanceQuery.orderByHistoricActivityInstanceId();
        return this;
    }

    @Override
    public HistoricActivityInstanceQueryWrapper orderByProcessInstanceId() {
        historicActivityInstanceQuery.orderByProcessInstanceId();
        return this;
    }

    @Override
    public HistoricActivityInstanceQueryWrapper orderByExecutionId() {
        historicActivityInstanceQuery.orderByExecutionId();
        return this;
    }

    @Override
    public HistoricActivityInstanceQueryWrapper orderByActivityId() {
        historicActivityInstanceQuery.orderByActivityId();
        return this;
    }

    @Override
    public HistoricActivityInstanceQueryWrapper orderByActivityName() {
        historicActivityInstanceQuery.orderByActivityName();
        return this;
    }

    @Override
    public HistoricActivityInstanceQueryWrapper orderByActivityType() {
        historicActivityInstanceQuery.orderByActivityType();
        return this;
    }

    @Override
    public HistoricActivityInstanceQueryWrapper orderByHistoricActivityInstanceStartTime() {
        historicActivityInstanceQuery.orderByHistoricActivityInstanceStartTime();
        return this;
    }

    @Override
    public HistoricActivityInstanceQueryWrapper orderByHistoricActivityInstanceEndTime() {
        historicActivityInstanceQuery.orderByHistoricActivityInstanceEndTime();
        return this;
    }

    @Override
    public HistoricActivityInstanceQueryWrapper orderByHistoricActivityInstanceDuration() {
        historicActivityInstanceQuery.orderByHistoricActivityInstanceDuration();
        return this;
    }

    @Override
    public HistoricActivityInstanceQueryWrapper orderByProcessDefinitionId() {
        historicActivityInstanceQuery.orderByProcessDefinitionId();
        return this;
    }

    @Override
    public HistoricActivityInstanceQueryWrapper asc() {
        historicActivityInstanceQuery.asc();
        return this;
    }

    @Override
    public HistoricActivityInstanceQueryWrapper desc() {
        historicActivityInstanceQuery.desc();
        return this;
    }

    @Override
    public long count() {
        return historicActivityInstanceQuery.count();
    }

    @Override
    public HistoricActivityInstanceWrapper singleResult() {
        return new HistoricActivityInstanceWrapperImpl( historicActivityInstanceQuery.singleResult());
    }

    @Override
    public List<HistoricActivityInstanceWrapper> list() {
        return HistoricActivityInstanceWrapperImpl.convert( historicActivityInstanceQuery.list());
    }

    @Override
    public List<HistoricActivityInstanceWrapper> listPage(int firstResult, int maxResults) {
        return HistoricActivityInstanceWrapperImpl.convert(historicActivityInstanceQuery.listPage(firstResult, maxResults));
    }

}
