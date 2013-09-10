package org.activiti.crystalball.processengine.wrapper.activiti.queries;

import org.activiti.crystalball.processengine.wrapper.queries.HistoricDetailWrapper;
import org.activiti.crystalball.processengine.wrapper.queries.HistoricTaskInstanceWrapper;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricTaskInstance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoricDetailWrapperImpl implements HistoricDetailWrapper {
    private final HistoricDetail historicDetail;

    @Override
    public String getId() {
        return historicDetail.getId();
    }

    @Override
    public String getProcessInstanceId() {
        return historicDetail.getProcessInstanceId();
    }

    @Override
    public String getActivityInstanceId() {
        return historicDetail.getActivityInstanceId();
    }

    @Override
    public String getExecutionId() {
        return historicDetail.getExecutionId();
    }

    @Override
    public String getTaskId() {
        return historicDetail.getTaskId();
    }

    @Override
    public Date getTime() {
        return historicDetail.getTime();
    }

    public HistoricDetailWrapperImpl(HistoricDetail historicDetail) {
        this.historicDetail = historicDetail;
    }

    public static List<HistoricDetailWrapper> convert(List<HistoricDetail> list) {
        if (list != null) {
            List<HistoricDetailWrapper> destinationList = new ArrayList<HistoricDetailWrapper>(list.size());
            for (HistoricDetail item : list) {
                destinationList.add( new HistoricDetailWrapperImpl( item ));
            }
            return destinationList;
        }
        return null;

    }
}
