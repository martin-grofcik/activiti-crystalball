package org.activiti.crystalball.processengine.wrapper.activiti.queries;

import org.activiti.crystalball.processengine.wrapper.queries.LaneWrapper;
import org.activiti.engine.impl.pvm.process.Lane;

import java.util.ArrayList;
import java.util.List;

public class LaneWrapperImpl implements LaneWrapper {
    protected final Lane lane;

    public void setId(String id) {
        lane.setId(id);
    }

    @Override
    public String getId() {
        return lane.getId();
    }

    @Override
    public String getName() {
        return lane.getName();
    }

    public void setName(String name) {
        lane.setName(name);
    }

    @Override
    public int getX() {
        return lane.getX();
    }

    @Override
    public void setX(int x) {
        lane.setX(x);
    }

    @Override
    public int getY() {
        return lane.getY();
    }

    @Override
    public void setY(int y) {
        lane.setY(y);
    }

    @Override
    public int getWidth() {
        return lane.getWidth();
    }

    @Override
    public void setWidth(int width) {
        lane.setWidth(width);
    }

    @Override
    public int getHeight() {
        return lane.getHeight();
    }

    @Override
    public void setHeight(int height) {
        lane.setHeight(height);
    }

    @Override
    public List<String> getFlowNodeIds() {
        return lane.getFlowNodeIds();
    }

    public LaneWrapperImpl(Lane laneForId) {
        lane = laneForId;
    }

    public static List<LaneWrapper> convert(List<Lane> list) {
        if (list != null) {
            List<LaneWrapper> destinationList = new ArrayList<LaneWrapper>(list.size());
            for (Lane item : list) {
                destinationList.add( new LaneWrapperImpl(  item ));
            }
            return destinationList;
        }
        return null;
    }
}
