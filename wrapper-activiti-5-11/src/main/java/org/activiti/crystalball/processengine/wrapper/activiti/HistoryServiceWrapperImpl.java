package org.activiti.crystalball.processengine.wrapper.activiti;

import org.activiti.crystalball.processengine.wrapper.HistoryServiceWrapper;
import org.activiti.crystalball.processengine.wrapper.activiti.queries.*;
import org.activiti.crystalball.processengine.wrapper.queries.*;
import org.activiti.engine.HistoryService;

/**
 */
public class HistoryServiceWrapperImpl implements HistoryServiceWrapper {
    private final HistoryService historyService;

    public HistoryServiceWrapperImpl(HistoryService historyService) {
        this.historyService = historyService;
    }

    @Override
    public HistoricActivityInstanceQueryWrapper createHistoricActivityInstanceQuery() {
        return new HistoricActivityInstanceQueryWrapperImpl(historyService.createHistoricActivityInstanceQuery());
    }

    @Override
    public HistoricTaskInstanceQueryWrapper createHistoricTaskInstanceQuery() {
        return new HistoricTaskInstanceQueryWrapperImpl(historyService.createHistoricTaskInstanceQuery());
    }

    @Override
    public HistoricProcessInstanceQueryWrapper createHistoricProcessInstanceQuery() {
        return new HistoricProcessInstanceQueryWrapperImpl(historyService.createHistoricProcessInstanceQuery());  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public HistoricDetailQueryWrapper createHistoricDetailQuery() {
        return new HistoricDetailQueryWrapperImpl(historyService.createHistoricDetailQuery());
    }

    @Override
    public NativeHistoricTaskInstanceQueryWrapper createNativeHistoricTaskInstanceQuery() {
        return new NativeHistoricTaskInstanceQueryWrapperImpl(historyService.createNativeHistoricTaskInstanceQuery());
    }
}
