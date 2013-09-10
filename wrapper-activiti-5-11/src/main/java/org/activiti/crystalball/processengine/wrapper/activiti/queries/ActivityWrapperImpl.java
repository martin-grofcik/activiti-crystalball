package org.activiti.crystalball.processengine.wrapper.activiti.queries;


import org.activiti.crystalball.processengine.wrapper.queries.ActivityWrapper;
import org.activiti.crystalball.processengine.wrapper.queries.PvmTransitionWrapper;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.bpmn.data.IOSpecification;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.ScopeImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ActivityWrapperImpl implements ActivityWrapper {
    protected final ActivityImpl item;

    public TransitionImpl createOutgoingTransition() {
        return item.createOutgoingTransition();
    }

    public TransitionImpl createOutgoingTransition(String transitionId) {
        return item.createOutgoingTransition(transitionId);
    }

    public TransitionImpl findOutgoingTransition(String transitionId) {
        return item.findOutgoingTransition(transitionId);
    }

    @Override
    public String toString() {
        return item.toString();
    }

    public ActivityImpl getParentActivity() {
        return item.getParentActivity();
    }

    @Override
    public List<PvmTransitionWrapper> getOutgoingTransitions() {
        return PvmTransitionWrapperImpl.convert( item.getOutgoingTransitions());
    }

    public ActivityBehavior getActivityBehavior() {
        return item.getActivityBehavior();
    }

    public void setActivityBehavior(ActivityBehavior activityBehavior) {
        item.setActivityBehavior(activityBehavior);
    }

    public ScopeImpl getParent() {
        return item.getParent();
    }

    public List<PvmTransition> getIncomingTransitions() {
        return item.getIncomingTransitions();
    }

    public boolean isScope() {
        return item.isScope();
    }

    public void setScope(boolean isScope) {
        item.setScope(isScope);
    }

    @Override
    public int getX() {
        return item.getX();
    }

    @Override
    public void setX(int x) {
        item.setX(x);
    }

    @Override
    public int getY() {
        return item.getY();
    }

    @Override
    public void setY(int y) {
        item.setY(y);
    }

    @Override
    public int getWidth() {
        return item.getWidth();
    }

    @Override
    public void setWidth(int width) {
        item.setWidth(width);
    }

    @Override
    public int getHeight() {
        return item.getHeight();
    }

    @Override
    public void setHeight(int height) {
        item.setHeight(height);
    }

    public boolean isAsync() {
        return item.isAsync();
    }

    public void setAsync(boolean isAsync) {
        item.setAsync(isAsync);
    }

    public boolean isExclusive() {
        return item.isExclusive();
    }

    public void setExclusive(boolean isExclusive) {
        item.setExclusive(isExclusive);
    }

    public ActivityImpl findActivity(String activityId) {
        return item.findActivity(activityId);
    }

    public ActivityImpl createActivity() {
        return item.createActivity();
    }

    public ActivityImpl createActivity(String activityId) {
        return item.createActivity(activityId);
    }

    public boolean contains(ActivityImpl activity) {
        return item.contains(activity);
    }

    public List<ExecutionListener> getExecutionListeners(String eventName) {
        return item.getExecutionListeners(eventName);
    }

    public void addExecutionListener(String eventName, ExecutionListener executionListener) {
        item.addExecutionListener(eventName, executionListener);
    }

    public void addExecutionListener(String eventName, ExecutionListener executionListener, int index) {
        item.addExecutionListener(eventName, executionListener, index);
    }

    public Map<String, List<ExecutionListener>> getExecutionListeners() {
        return item.getExecutionListeners();
    }

    @Override
    public List<ActivityWrapper> getActivities() {
        return ActivityWrapperImpl.convert( item.getActivities());
    }

    public IOSpecification getIoSpecification() {
        return item.getIoSpecification();
    }

    public void setIoSpecification(IOSpecification ioSpecification) {
        item.setIoSpecification(ioSpecification);
    }

    public void setProperty(String name, Object value) {
        item.setProperty(name, value);
    }

    @Override
    public Object getProperty(String name) {
        return item.getProperty(name);
    }

    public Map<String, Object> getProperties() {
        return item.getProperties();
    }

    @Override
    public String getId() {
        return item.getId();
    }

    public void setProperties(Map<String, Object> properties) {
        item.setProperties(properties);
    }

    public ProcessDefinitionImpl getProcessDefinition() {
        return item.getProcessDefinition();
    }

    public ActivityWrapperImpl(ActivityImpl item) {
        this.item = item;
    }

    public static List<ActivityWrapper> convert(List<ActivityImpl> list) {
        if (list != null) {
            List<ActivityWrapper> destinationList = new ArrayList<ActivityWrapper>(list.size());
            for (ActivityImpl item : list) {
                destinationList.add( new ActivityWrapperImpl( item ));
            }
            return destinationList;
        }
        return null;

    }
}
