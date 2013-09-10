package org.activiti.crystalball.processengine.wrapper.activiti.queries;

import org.activiti.crystalball.processengine.wrapper.queries.IdentityLinkWrapper;
import org.activiti.engine.task.IdentityLink;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class IdentityLinkWrapperImpl implements IdentityLinkWrapper {
    IdentityLink link;

    @Override
    public String getType() {
        return link.getType();
    }

    @Override
    public String getUserId() {
        return link.getUserId();
    }

    @Override
    public String getGroupId() {
        return link.getGroupId();
    }

    @Override
    public String getTaskId() {
        return link.getTaskId();
    }

    public IdentityLinkWrapperImpl(IdentityLink link) {
        this.link = link;
    }

    public static List<IdentityLinkWrapper> convert(Set<IdentityLink> list) {
        if (list != null) {
            List<IdentityLinkWrapper> destinationList = new ArrayList<IdentityLinkWrapper>(list.size());
            for (IdentityLink item : list) {
                destinationList.add( new IdentityLinkWrapperImpl( item ));
            }
            return destinationList;
        }
        return null;
    }

    public static List<IdentityLinkWrapper> convert(List<IdentityLink> list) {
        if (list != null) {
            List<IdentityLinkWrapper> destinationList = new ArrayList<IdentityLinkWrapper>(list.size());
            for (IdentityLink item : list) {
                destinationList.add( new IdentityLinkWrapperImpl( item ));
            }
            return destinationList;
        }
        return null;
    }

}
