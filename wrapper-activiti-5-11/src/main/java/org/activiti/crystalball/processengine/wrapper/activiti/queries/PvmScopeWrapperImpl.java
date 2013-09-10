package org.activiti.crystalball.processengine.wrapper.activiti.queries;

import org.activiti.crystalball.processengine.wrapper.queries.PvmActivityWrapper;
import org.activiti.crystalball.processengine.wrapper.queries.PvmScopeWrapper;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmProcessDefinition;
import org.activiti.engine.impl.pvm.PvmScope;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: martin.grofcik
 * Date: 10.9.2013
 * Time: 8:40
 * To change this template use File | Settings | File Templates.
 */
public class PvmScopeWrapperImpl implements PvmScopeWrapper {
    protected final PvmScope scope;

    public Object getProperty(String name) {
        return scope.getProperty(name);
    }

    @Override
    public List<? extends PvmActivityWrapper> getActivities() {
        return PvmActivityWrapperImpl.convert(scope.getActivities());
    }

    @Override
    public PvmActivityWrapper findActivity(String activityId) {
        return new PvmActivityWrapperImpl( scope.findActivity(activityId));
    }

    public String getId() {
        return scope.getId();
    }

    public PvmProcessDefinition getProcessDefinition() {
        return scope.getProcessDefinition();
    }

    public PvmScopeWrapperImpl(PvmScope parent) {
        this.scope = parent;
    }
}
