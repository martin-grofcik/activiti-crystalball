package org.activiti.crystalball.processengine.wrapper.activiti.queries;

import org.activiti.crystalball.processengine.wrapper.queries.LaneSetWrapper;
import org.activiti.crystalball.processengine.wrapper.queries.LaneWrapper;
import org.activiti.engine.impl.pvm.process.Lane;
import org.activiti.engine.impl.pvm.process.LaneSet;

import java.util.ArrayList;
import java.util.List;

public class LaneSetWrapperImpl implements LaneSetWrapper {

    protected final LaneSet laneSet;

    @Override
    public LaneWrapper getLaneForId(String id) {
        return new LaneWrapperImpl( laneSet.getLaneForId(id));
    }

    @Override
    public void setId(String id) {
        laneSet.setId(id);
    }

    @Override
    public String getId() {
        return laneSet.getId();
    }

    @Override
    public String getName() {
        return laneSet.getName();
    }

    @Override
    public void setName(String name) {
        laneSet.setName(name);
    }

    @Override
    public List<LaneWrapper> getLanes() {
        return LaneWrapperImpl.convert(laneSet.getLanes());
    }

    public LaneSetWrapperImpl(LaneSet item) {
        this.laneSet = item;
    }

    public static List<LaneSetWrapper> convert(List<LaneSet> list) {
        if (list != null) {
            List<LaneSetWrapper> destinationList = new ArrayList<LaneSetWrapper>(list.size());
            for (LaneSet item : list) {
                destinationList.add( new LaneSetWrapperImpl(  item ));
            }
            return destinationList;
        }
        return null;
    }
}
