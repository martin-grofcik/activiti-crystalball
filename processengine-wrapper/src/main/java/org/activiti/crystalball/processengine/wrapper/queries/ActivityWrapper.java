package org.activiti.crystalball.processengine.wrapper.queries;

import java.util.List;

/**

 */
public interface ActivityWrapper extends HasDIBounds{
    List<? extends ActivityWrapper> getActivities();

    Object getProperty(String type);

    String getId();

    public List<PvmTransitionWrapper> getOutgoingTransitions();
}
