package org.activiti.crystalball.processengine.wrapper.activiti.queries;

import org.activiti.crystalball.processengine.wrapper.queries.IdentityLinkWrapper;
import org.activiti.crystalball.processengine.wrapper.queries.TaskWrapper;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.Task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskWrapperImpl implements TaskWrapper {
    protected final Task task;

    @Override
    public String getId() {
        return task.getId();
    }

    @Override
    public String getName() {
        return task.getName();
    }

    @Override
    public void setName(String name) {
        task.setName(name);
    }

    @Override
    public String getDescription() {
        return task.getDescription();
    }

    @Override
    public void setDescription(String description) {
        task.setDescription(description);
    }

    @Override
    public int getPriority() {
        return task.getPriority();
    }

    @Override
    public void setPriority(int priority) {
        task.setPriority(priority);
    }

    @Override
    public String getOwner() {
        return task.getOwner();
    }

    @Override
    public void setOwner(String owner) {
        task.setOwner(owner);
    }

    @Override
    public String getAssignee() {
        return task.getAssignee();
    }

    @Override
    public void setAssignee(String assignee) {
        task.setAssignee(assignee);
    }

    @Override
    public org.activiti.crystalball.processengine.wrapper.queries.DelegationState getDelegationState() {
        return org.activiti.crystalball.processengine.wrapper.queries.DelegationState.valueOf(task.getDelegationState().name());
    }

    @Override
    public void setDelegationState(org.activiti.crystalball.processengine.wrapper.queries.DelegationState delegationState) {
        task.setDelegationState(DelegationState.valueOf(delegationState.name()));
    }

    @Override
    public String getProcessInstanceId() {
        return task.getProcessInstanceId();
    }

    @Override
    public String getExecutionId() {
        return task.getExecutionId();
    }

    @Override
    public String getProcessDefinitionId() {
        return task.getProcessDefinitionId();
    }

    @Override
    public Date getCreateTime() {
        return task.getCreateTime();
    }

    @Override
    public String getTaskDefinitionKey() {
        return task.getTaskDefinitionKey();
    }

    @Override
    public Date getDueDate() {
        return task.getDueDate();
    }

    @Override
    public void setDueDate(Date dueDate) {
        task.setDueDate(dueDate);
    }

    @Override
    public void delegate(String userId) {
        task.delegate(userId);
    }

    @Override
    public void setParentTaskId(String parentTaskId) {
        task.setParentTaskId(parentTaskId);
    }

    @Override
    public String getParentTaskId() {
        return task.getParentTaskId();
    }

    @Override
    public boolean isSuspended() {
        return task.isSuspended();
    }

    @Override
    public List<IdentityLinkWrapper> getCandidates() {
        return IdentityLinkWrapperImpl.convert(((TaskEntity) task).getCandidates());
    }

    public TaskWrapperImpl(Task task) {
        this.task = task;
    }

    public static List<TaskWrapper> convert(List<Task> list) {
        if (list != null) {
            List<TaskWrapper> destinationList = new ArrayList<TaskWrapper>(list.size());
            for (Task item : list) {
                destinationList.add( new TaskWrapperImpl( item ));
            }
            return destinationList;
        }
        return null;
    }

    public Task getTask() {
        return task;
    }
}
