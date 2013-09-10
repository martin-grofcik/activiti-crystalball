package org.activiti.crystalball.processengine.wrapper.activiti.queries;

import org.activiti.crystalball.processengine.wrapper.queries.GroupWrapper;
import org.activiti.engine.identity.Group;

import java.util.ArrayList;
import java.util.List;

public class GroupWrapperImpl implements GroupWrapper {
    protected final Group group;

    @Override
    public String getId() {
        return group.getId();
    }

    @Override
    public void setId(String id) {
        group.setId(id);
    }

    @Override
    public String getName() {
        return group.getName();
    }

    @Override
    public void setName(String name) {
        group.setName(name);
    }

    @Override
    public String getType() {
        return group.getType();
    }

    @Override
    public void setType(String string) {
        group.setType(string);
    }

    public GroupWrapperImpl(Group group) {
        this.group = group;
    }

    public static List<GroupWrapper> convert(List<Group> list) {
        if (list != null) {
            List<GroupWrapper> destinationList = new ArrayList<GroupWrapper>(list.size());
            for (Group item : list) {
                destinationList.add( new GroupWrapperImpl( item ));
            }
            return destinationList;
        }
        return null;
    }

    public Group getGroup() {
        return group;
    }
}
