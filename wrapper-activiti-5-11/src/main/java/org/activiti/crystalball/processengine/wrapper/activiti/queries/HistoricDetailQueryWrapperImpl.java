package org.activiti.crystalball.processengine.wrapper.activiti.queries;

import org.activiti.crystalball.processengine.wrapper.queries.HistoricDetailQueryWrapper;
import org.activiti.crystalball.processengine.wrapper.queries.HistoricDetailWrapper;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricDetailQuery;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: martin.grofcik
 * Date: 9.9.2013
 * Time: 13:51
 * To change this template use File | Settings | File Templates.
 */
public class HistoricDetailQueryWrapperImpl implements HistoricDetailQueryWrapper {
    private final HistoricDetailQuery historicDetailQuery;

    @Override
    public HistoricDetailQueryWrapper processInstanceId(String processInstanceId) {
        historicDetailQuery.processInstanceId(processInstanceId); return this;
    }

    @Override
    public HistoricDetailQueryWrapper executionId(String executionId) {
        historicDetailQuery.executionId(executionId); return this;
    }

    @Override
    @Deprecated
    public HistoricDetailQueryWrapper activityId(String activityId) {
        historicDetailQuery.activityId(activityId); return this;
    }

    @Override
    public HistoricDetailQueryWrapper activityInstanceId(String activityInstanceId) {
        historicDetailQuery.activityInstanceId(activityInstanceId); return this;
    }

    @Override
    public HistoricDetailQueryWrapper taskId(String taskId) {
        historicDetailQuery.taskId(taskId); return this;
    }

    @Override
    public HistoricDetailQueryWrapper formProperties() {
        historicDetailQuery.formProperties(); return this;
    }

    @Override
    public HistoricDetailQueryWrapper variableUpdates() {
        historicDetailQuery.variableUpdates(); return this;
    }

    @Override
    public HistoricDetailQueryWrapper excludeTaskDetails() {
        historicDetailQuery.excludeTaskDetails(); return this;
    }

    @Override
    public HistoricDetailQueryWrapper orderByProcessInstanceId() {
        historicDetailQuery.orderByProcessInstanceId(); return this;
    }

    @Override
    public HistoricDetailQueryWrapper orderByVariableName() {
        historicDetailQuery.orderByVariableName(); return this;
    }

    @Override
    public HistoricDetailQueryWrapper orderByFormPropertyId() {
        historicDetailQuery.orderByFormPropertyId(); return this;
    }

    @Override
    public HistoricDetailQueryWrapper orderByVariableType() {
        historicDetailQuery.orderByVariableType(); return this;
    }

    @Override
    public HistoricDetailQueryWrapper orderByVariableRevision() {
        historicDetailQuery.orderByVariableRevision(); return this;
    }

    @Override
    public HistoricDetailQueryWrapper orderByTime() {
        historicDetailQuery.orderByTime(); return this;
    }

    @Override
    public HistoricDetailQueryWrapper asc() {
        historicDetailQuery.asc(); return this;
    }

    @Override
    public HistoricDetailQueryWrapper desc() {
        historicDetailQuery.desc(); return this;
    }

    @Override
    public long count() {
        return historicDetailQuery.count();
    }

    @Override
    public HistoricDetailWrapper singleResult() {
        return new HistoricDetailWrapperImpl( historicDetailQuery.singleResult() );
    }

    @Override
    public List<HistoricDetailWrapper> list() {
        return HistoricDetailWrapperImpl.convert(historicDetailQuery.list());
    }

    @Override
    public List<HistoricDetailWrapper> listPage(int firstResult, int maxResults) {
        return HistoricDetailWrapperImpl.convert(historicDetailQuery.listPage(firstResult, maxResults));
    }

    public HistoricDetailQueryWrapperImpl(HistoricDetailQuery historicDetailQuery) {
        this.historicDetailQuery = historicDetailQuery;
    }
}
