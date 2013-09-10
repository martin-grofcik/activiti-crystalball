package org.activiti.crystalball.processengine.wrapper.activiti.queries;

import org.activiti.crystalball.processengine.wrapper.queries.PvmActivityWrapper;
import org.activiti.crystalball.processengine.wrapper.queries.PvmScopeWrapper;
import org.activiti.crystalball.processengine.wrapper.queries.PvmTransitionWrapper;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmProcessDefinition;
import org.activiti.engine.impl.pvm.PvmScope;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.TransitionImpl;

import java.util.ArrayList;
import java.util.List;

public class PvmActivityWrapperImpl implements PvmActivityWrapper {
    protected final PvmActivity activity;

    public Object getProperty(String name) {
        return activity.getProperty(name);
    }

    @Override
    public boolean isAsync() {
        return activity.isAsync();
    }

    @Override
    public boolean isExclusive() {
        return activity.isExclusive();
    }

    @Override
    public PvmScopeWrapper getParent() {
        return new PvmScopeWrapperImpl( activity.getParent());
    }

    @Override
    public List<PvmTransitionWrapper> getIncomingTransitions() {
        return PvmTransitionWrapperImpl.convert(activity.getIncomingTransitions());
    }

    @Override
    public List<PvmTransitionWrapper> getOutgoingTransitions() {
        return PvmTransitionWrapperImpl.convert(activity.getOutgoingTransitions());
    }

    @Override
    public PvmTransitionWrapper findOutgoingTransition(String transitionId) {
        return new PvmTransitionWrapperImpl( (TransitionImpl) activity.findOutgoingTransition(transitionId)) ;
    }

    @Override
    public List<? extends PvmActivityWrapper> getActivities() {
        return PvmActivityWrapperImpl.convert(activity.getActivities());
    }

    @Override
    public PvmActivityWrapper findActivity(String activityId) {
        return new PvmActivityWrapperImpl( activity.findActivity(activityId));
    }

    public String getId() {
        return activity.getId();
    }

    public PvmProcessDefinition getProcessDefinition() {
        return activity.getProcessDefinition();
    }

    public PvmActivityWrapperImpl(PvmActivity source) {
        this.activity = source;
    }

    public static List<? extends PvmActivityWrapper> convert(List<? extends PvmActivity> list) {
        if (list != null) {
            List<PvmActivityWrapper> destinationList = new ArrayList<PvmActivityWrapper>(list.size());
            for (PvmActivity item : list) {
                destinationList.add( new PvmActivityWrapperImpl(  item ));
            }
            return destinationList;
        }
        return null;
    }
}
