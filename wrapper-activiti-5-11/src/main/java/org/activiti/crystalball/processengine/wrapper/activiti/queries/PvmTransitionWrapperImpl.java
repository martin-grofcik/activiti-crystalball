package org.activiti.crystalball.processengine.wrapper.activiti.queries;

import org.activiti.crystalball.processengine.wrapper.queries.PvmActivityWrapper;
import org.activiti.crystalball.processengine.wrapper.queries.PvmTransitionWrapper;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmProcessDefinition;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.TransitionImpl;

import java.util.ArrayList;
import java.util.List;

public class PvmTransitionWrapperImpl implements PvmTransitionWrapper {
    protected final TransitionImpl pvmTransition;

    @Override
    public PvmActivityWrapper getSource() {
        return new PvmActivityWrapperImpl( pvmTransition.getSource());
    }

    @Override
    public PvmActivityWrapper getDestination() {
        return new PvmActivityWrapperImpl( pvmTransition.getDestination());
    }

    @Override
    public String getId() {
        return pvmTransition.getId();
    }

    public PvmProcessDefinition getProcessDefinition() {
        return pvmTransition.getProcessDefinition();
    }

    @Override
    public Object getProperty(String name) {
        return pvmTransition.getProperty(name);
    }

    @Override
    public List<Integer> getWaypoints() {
        return pvmTransition.getWaypoints();
    }

    public PvmTransitionWrapperImpl( TransitionImpl pvmTransition) {
        this.pvmTransition = pvmTransition;
    }

    public static List<PvmTransitionWrapper> convert(List<PvmTransition> list) {
        if (list != null) {
            List<PvmTransitionWrapper> destinationList = new ArrayList<PvmTransitionWrapper>(list.size());
            for (PvmTransition item : list) {
                destinationList.add( new PvmTransitionWrapperImpl( (TransitionImpl) item ));
            }
            return destinationList;
        }
        return null;
    }
}
