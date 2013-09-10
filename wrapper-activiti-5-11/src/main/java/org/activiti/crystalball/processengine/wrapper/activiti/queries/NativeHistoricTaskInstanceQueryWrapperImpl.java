package org.activiti.crystalball.processengine.wrapper.activiti.queries;

import org.activiti.crystalball.processengine.wrapper.queries.HistoricTaskInstanceWrapper;
import org.activiti.crystalball.processengine.wrapper.queries.NativeHistoricTaskInstanceQueryWrapper;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.NativeHistoricTaskInstanceQuery;

import java.util.List;

public class NativeHistoricTaskInstanceQueryWrapperImpl implements NativeHistoricTaskInstanceQueryWrapper {
    protected final NativeHistoricTaskInstanceQuery nativeHistoricTaskInstanceQuery;

    @Override
    public NativeHistoricTaskInstanceQueryWrapper sql(String selectClause) {
        nativeHistoricTaskInstanceQuery.sql(selectClause); return this;
    }

    @Override
    public NativeHistoricTaskInstanceQueryWrapper parameter(String name, Object value) {
        nativeHistoricTaskInstanceQuery.parameter(name, value); return this;
    }

    @Override
    public long count() {
        return nativeHistoricTaskInstanceQuery.count();
    }

    @Override
    public HistoricTaskInstanceWrapper singleResult() {
        return new HistoricTaskInstanceWrapperImpl( nativeHistoricTaskInstanceQuery.singleResult());
    }

    @Override
    public List<HistoricTaskInstanceWrapper> list() {
        return HistoricTaskInstanceWrapperImpl.convert(nativeHistoricTaskInstanceQuery.list());
    }

    public NativeHistoricTaskInstanceQueryWrapperImpl(NativeHistoricTaskInstanceQuery nativeHistoricTaskInstanceQuery) {
        this.nativeHistoricTaskInstanceQuery = nativeHistoricTaskInstanceQuery;
    }
}
