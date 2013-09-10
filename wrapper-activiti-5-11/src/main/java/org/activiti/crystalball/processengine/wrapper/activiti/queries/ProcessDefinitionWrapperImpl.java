package org.activiti.crystalball.processengine.wrapper.activiti.queries;


import org.activiti.crystalball.processengine.wrapper.queries.ActivityWrapper;
import org.activiti.crystalball.processengine.wrapper.queries.LaneSetWrapper;
import org.activiti.crystalball.processengine.wrapper.queries.ParticipantProcessWrapper;
import org.activiti.crystalball.processengine.wrapper.queries.ProcessDefinitionWrapper;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.ProcessDefinition;

import java.util.ArrayList;
import java.util.List;

public class ProcessDefinitionWrapperImpl implements ProcessDefinitionWrapper{
    private final ProcessDefinitionEntity processDefinitionEntity;

    public ProcessDefinitionWrapperImpl(ProcessDefinitionEntity processDefinitionEntity ) {
        this.processDefinitionEntity = processDefinitionEntity;
    }

    @Override
    public String getId() {
        return processDefinitionEntity.getId();
    }

    @Override
    public String getCategory() {
        return processDefinitionEntity.getCategory();
    }

    @Override
    public String getName() {
        return processDefinitionEntity.getName();
    }

    @Override
    public String getKey() {
        return processDefinitionEntity.getKey();
    }

    @Override
    public String getDescription() {
        return processDefinitionEntity.getDescription();
    }

    @Override
    public int getVersion() {
        return processDefinitionEntity.getVersion();
    }

    @Override
    public String getResourceName() {
        return processDefinitionEntity.getResourceName();
    }

    @Override
    public String getDeploymentId() {
        return processDefinitionEntity.getDeploymentId();
    }

    @Override
    public String getDiagramResourceName() {
        return processDefinitionEntity.getDiagramResourceName();
    }

    @Override
    public boolean hasStartFormKey() {
        return processDefinitionEntity.hasStartFormKey();
    }

    @Override
    public boolean isSuspended() {
        return processDefinitionEntity.isSuspended();
    }

    @Override
    public List<ActivityWrapper> getActivities() {
        return ActivityWrapperImpl.convert(processDefinitionEntity.getActivities());
    }

    @Override
    public ParticipantProcessWrapper getParticipantProcess() {
        return new ParticipantProcessWrapperImpl(processDefinitionEntity.getParticipantProcess());
    }

    @Override
    public List<LaneSetWrapper> getLaneSets() {
        return LaneSetWrapperImpl.convert(processDefinitionEntity.getLaneSets());
    }

    public ProcessDefinitionEntity getProcessDefinitionEntity() {
        return processDefinitionEntity;
    }

    public static List<ProcessDefinitionWrapper> convert(List<ProcessDefinition> list) {
        if (list != null) {
            List<ProcessDefinitionWrapper> destinationList = new ArrayList<ProcessDefinitionWrapper>(list.size());
            for (ProcessDefinition item : list) {
                destinationList.add( new ProcessDefinitionWrapperImpl( (ProcessDefinitionEntity) item ));
            }
            return destinationList;
        }
        return null;
    }
}
