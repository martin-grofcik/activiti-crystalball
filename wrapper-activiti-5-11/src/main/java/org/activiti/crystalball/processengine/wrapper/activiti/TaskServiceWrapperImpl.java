package org.activiti.crystalball.processengine.wrapper.activiti;

import org.activiti.crystalball.processengine.wrapper.TaskServiceWrapper;
import org.activiti.crystalball.processengine.wrapper.activiti.queries.*;
import org.activiti.crystalball.processengine.wrapper.queries.*;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.*;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class TaskServiceWrapperImpl implements TaskServiceWrapper {
    protected final TaskService taskService;

    @Override
    public TaskWrapper newTask() {
        return new TaskWrapperImpl( taskService.newTask());
    }

    @Override
    public TaskWrapper newTask(String taskId) {
        return new TaskWrapperImpl( taskService.newTask(taskId));
    }

    public void saveTask(TaskWrapper task) {
        taskService.saveTask(((TaskWrapperImpl) task).getTask());
    }

    @Override
    public void deleteTask(String taskId) {
        taskService.deleteTask(taskId);
    }

    @Override
    public void deleteTasks(Collection<String> taskIds) {
        taskService.deleteTasks(taskIds);
    }

    @Override
    public void deleteTask(String taskId, boolean cascade) {
        taskService.deleteTask(taskId, cascade);
    }

    @Override
    public void deleteTasks(Collection<String> taskIds, boolean cascade) {
        taskService.deleteTasks(taskIds, cascade);
    }

    @Override
    public void deleteTask(String taskId, String deleteReason) {
        taskService.deleteTask(taskId, deleteReason);
    }

    @Override
    public void deleteTasks(Collection<String> taskIds, String deleteReason) {
        taskService.deleteTasks(taskIds, deleteReason);
    }

    @Override
    public void claim(String taskId, String userId) {
        taskService.claim(taskId, userId);
    }

    @Override
    public void complete(String taskId) {
        taskService.complete(taskId);
    }

    @Override
    public void delegateTask(String taskId, String userId) {
        taskService.delegateTask(taskId, userId);
    }

    @Override
    public void resolveTask(String taskId) {
        taskService.resolveTask(taskId);
    }

    @Override
    public void complete(String taskId, Map<String, Object> variables) {
        taskService.complete(taskId, variables);
    }

    @Override
    public void setAssignee(String taskId, String userId) {
        taskService.setAssignee(taskId, userId);
    }

    @Override
    public void setOwner(String taskId, String userId) {
        taskService.setOwner(taskId, userId);
    }

    @Override
    public List<IdentityLinkWrapper> getIdentityLinksForTask(String taskId) {
        return IdentityLinkWrapperImpl.convert(taskService.getIdentityLinksForTask(taskId));
    }

    @Override
    public void addCandidateUser(String taskId, String userId) {
        taskService.addCandidateUser(taskId, userId);
    }

    @Override
    public void addCandidateGroup(String taskId, String groupId) {
        taskService.addCandidateGroup(taskId, groupId);
    }

    @Override
    public void addUserIdentityLink(String taskId, String userId, String identityLinkType) {
        taskService.addUserIdentityLink(taskId, userId, identityLinkType);
    }

    @Override
    public void addGroupIdentityLink(String taskId, String groupId, String identityLinkType) {
        taskService.addGroupIdentityLink(taskId, groupId, identityLinkType);
    }

    @Override
    public void deleteCandidateUser(String taskId, String userId) {
        taskService.deleteCandidateUser(taskId, userId);
    }

    @Override
    public void deleteCandidateGroup(String taskId, String groupId) {
        taskService.deleteCandidateGroup(taskId, groupId);
    }

    @Override
    public void deleteUserIdentityLink(String taskId, String userId, String identityLinkType) {
        taskService.deleteUserIdentityLink(taskId, userId, identityLinkType);
    }

    @Override
    public void deleteGroupIdentityLink(String taskId, String groupId, String identityLinkType) {
        taskService.deleteGroupIdentityLink(taskId, groupId, identityLinkType);
    }

    @Override
    public void setPriority(String taskId, int priority) {
        taskService.setPriority(taskId, priority);
    }

    @Override
    public TaskQueryWrapper createTaskQuery() {
        return new TaskQueryWrapperImpl( taskService.createTaskQuery() );
    }

    @Override
    public NativeTaskQueryWrapper createNativeTaskQuery() {
        return new NativeTaskQueryWrapperImpl( taskService.createNativeTaskQuery());
    }

    @Override
    public void setVariable(String taskId, String variableName, Object value) {
        taskService.setVariable(taskId, variableName, value);
    }

    @Override
    public void setVariables(String taskId, Map<String, ? extends Object> variables) {
        taskService.setVariables(taskId, variables);
    }

    @Override
    public void setVariableLocal(String taskId, String variableName, Object value) {
        taskService.setVariableLocal(taskId, variableName, value);
    }

    @Override
    public void setVariablesLocal(String taskId, Map<String, ? extends Object> variables) {
        taskService.setVariablesLocal(taskId, variables);
    }

    @Override
    public Object getVariable(String taskId, String variableName) {
        return taskService.getVariable(taskId, variableName);
    }

    @Override
    public Object getVariableLocal(String taskId, String variableName) {
        return taskService.getVariableLocal(taskId, variableName);
    }

    @Override
    public Map<String, Object> getVariables(String taskId) {
        return taskService.getVariables(taskId);
    }

    @Override
    public Map<String, Object> getVariablesLocal(String taskId) {
        return taskService.getVariablesLocal(taskId);
    }

    @Override
    public Map<String, Object> getVariables(String taskId, Collection<String> variableNames) {
        return taskService.getVariables(taskId, variableNames);
    }

    @Override
    public Map<String, Object> getVariablesLocal(String taskId, Collection<String> variableNames) {
        return taskService.getVariablesLocal(taskId, variableNames);
    }

    @Override
    public void removeVariable(String taskId, String variableName) {
        taskService.removeVariable(taskId, variableName);
    }

    @Override
    public void removeVariableLocal(String taskId, String variableName) {
        taskService.removeVariableLocal(taskId, variableName);
    }

    @Override
    public void removeVariables(String taskId, Collection<String> variableNames) {
        taskService.removeVariables(taskId, variableNames);
    }

    @Override
    public void removeVariablesLocal(String taskId, Collection<String> variableNames) {
        taskService.removeVariablesLocal(taskId, variableNames);
    }

    @Override
    public void addComment(String taskId, String processInstanceId, String message) {
        taskService.addComment(taskId, processInstanceId, message);
    }

    @Override
    public List<CommentWrapper> getTaskComments(String taskId) {
        return CommentWrapperImpl.convert( taskService.getTaskComments(taskId) );
    }

    @Override
    public List<EventWrapper> getTaskEvents(String taskId) {
        return EventWrapperImpl.convert(taskService.getTaskEvents(taskId));
    }

    @Override
    public List<CommentWrapper> getProcessInstanceComments(String processInstanceId) {
        return CommentWrapperImpl.convert(taskService.getProcessInstanceComments(processInstanceId));
    }

    @Override
    public AttachmentWrapper createAttachment(String attachmentType, String taskId, String processInstanceId, String attachmentName, String attachmentDescription, InputStream content) {
        return new AttachmentWrapperImpl( taskService.createAttachment(attachmentType, taskId, processInstanceId, attachmentName, attachmentDescription, content));
    }

    @Override
    public AttachmentWrapper createAttachment(String attachmentType, String taskId, String processInstanceId, String attachmentName, String attachmentDescription, String url) {
        return new AttachmentWrapperImpl( taskService.createAttachment(attachmentType, taskId, processInstanceId, attachmentName, attachmentDescription, url));
    }

    public void saveAttachment(AttachmentWrapper attachment) {
        taskService.saveAttachment(((AttachmentWrapperImpl) attachment).getAttachment());
    }

    @Override
    public AttachmentWrapper getAttachment(String attachmentId) {
        return new AttachmentWrapperImpl( taskService.getAttachment(attachmentId));
    }

    @Override
    public InputStream getAttachmentContent(String attachmentId) {
        return taskService.getAttachmentContent(attachmentId);
    }

    @Override
    public List<AttachmentWrapper> getTaskAttachments(String taskId) {
        return AttachmentWrapperImpl.convert( taskService.getTaskAttachments(taskId));
    }

    @Override
    public List<AttachmentWrapper> getProcessInstanceAttachments(String processInstanceId) {
        return AttachmentWrapperImpl.convert(taskService.getProcessInstanceAttachments(processInstanceId));
    }

    @Override
    public void deleteAttachment(String attachmentId) {
        taskService.deleteAttachment(attachmentId);
    }

    @Override
    public List<TaskWrapper> getSubTasks(String parentTaskId) {
        return TaskWrapperImpl.convert(taskService.getSubTasks(parentTaskId));
    }

    public TaskServiceWrapperImpl(TaskService taskService) {
        this.taskService = taskService;
    }
}
