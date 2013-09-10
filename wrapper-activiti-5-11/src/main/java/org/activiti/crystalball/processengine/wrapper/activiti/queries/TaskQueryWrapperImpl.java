package org.activiti.crystalball.processengine.wrapper.activiti.queries;

import org.activiti.crystalball.processengine.wrapper.queries.TaskQueryWrapper;
import org.activiti.crystalball.processengine.wrapper.queries.TaskWrapper;
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;

import java.util.Date;
import java.util.List;

public class TaskQueryWrapperImpl implements TaskQueryWrapper {
    protected final TaskQuery taskQuery;

    @Override
    public TaskQueryWrapper taskId(String taskId) {
        taskQuery.taskId(taskId); return this;
    }

    @Override
    public TaskQueryWrapper taskName(String name) {
        taskQuery.taskName(name); return this;
    }

    @Override
    public TaskQueryWrapper taskNameLike(String nameLike) {
        taskQuery.taskNameLike(nameLike); return this;
    }

    @Override
    public TaskQueryWrapper taskDescription(String description) {
        taskQuery.taskDescription(description); return this;
    }

    @Override
    public TaskQueryWrapper taskDescriptionLike(String descriptionLike) {
        taskQuery.taskDescriptionLike(descriptionLike); return this;
    }

    @Override
    public TaskQueryWrapper taskPriority(Integer priority) {
        taskQuery.taskPriority(priority); return this;
    }

    @Override
    public TaskQueryWrapper taskMinPriority(Integer minPriority) {
        taskQuery.taskMinPriority(minPriority); return this;
    }

    @Override
    public TaskQueryWrapper taskMaxPriority(Integer maxPriority) {
        taskQuery.taskMaxPriority(maxPriority); return this;
    }

    @Override
    public TaskQueryWrapper taskAssignee(String assignee) {
        taskQuery.taskAssignee(assignee); return this;
    }

    @Override
    public TaskQueryWrapper taskOwner(String owner) {
        taskQuery.taskOwner(owner); return this;
    }

    @Override
    public TaskQueryWrapper taskUnassigned() {
        taskQuery.taskUnassigned(); return this;
    }

    @Override
    @Deprecated
    public TaskQueryWrapper taskUnnassigned() {
        taskQuery.taskUnnassigned(); return this;
    }

    public TaskQueryWrapper taskDelegationState(org.activiti.crystalball.processengine.wrapper.queries.DelegationState delegationState) {
        taskQuery.taskDelegationState(DelegationState.valueOf(delegationState.name())); return this;
    }

    @Override
    public TaskQueryWrapper taskCandidateUser(String candidateUser) {
        taskQuery.taskCandidateUser(candidateUser); return this;
    }

    @Override
    public TaskQueryWrapper taskInvolvedUser(String involvedUser) {
        taskQuery.taskInvolvedUser(involvedUser); return this;
    }

    @Override
    public TaskQueryWrapper taskCandidateGroup(String candidateGroup) {
        taskQuery.taskCandidateGroup(candidateGroup); return this;
    }

    @Override
    public TaskQueryWrapper taskCandidateGroupIn(List<String> candidateGroups) {
        taskQuery.taskCandidateGroupIn(candidateGroups); return this;
    }

    @Override
    public TaskQueryWrapper processInstanceId(String processInstanceId) {
        taskQuery.processInstanceId(processInstanceId); return this;
    }

    @Override
    public TaskQueryWrapper processInstanceBusinessKey(String processInstanceBusinessKey) {
        taskQuery.processInstanceBusinessKey(processInstanceBusinessKey); return this;
    }

    @Override
    public TaskQueryWrapper executionId(String executionId) {
        taskQuery.executionId(executionId); return this;
    }

    @Override
    public TaskQueryWrapper taskCreatedOn(Date createTime) {
        taskQuery.taskCreatedOn(createTime); return this;
    }

    @Override
    public TaskQueryWrapper taskCreatedBefore(Date before) {
        taskQuery.taskCreatedBefore(before); return this;
    }

    @Override
    public TaskQueryWrapper taskCreatedAfter(Date after) {
        taskQuery.taskCreatedAfter(after); return this;
    }

    @Override
    public TaskQueryWrapper excludeSubtasks() {
        taskQuery.excludeSubtasks(); return this;
    }

    @Override
    public TaskQueryWrapper taskDefinitionKey(String key) {
        taskQuery.taskDefinitionKey(key); return this;
    }

    @Override
    public TaskQueryWrapper taskDefinitionKeyLike(String keyLike) {
        taskQuery.taskDefinitionKeyLike(keyLike); return this;
    }

    @Override
    public TaskQueryWrapper taskVariableValueEquals(String variableName, Object variableValue) {
        taskQuery.taskVariableValueEquals(variableName, variableValue); return this;
    }

    @Override
    public TaskQueryWrapper taskVariableValueEquals(Object variableValue) {
        taskQuery.taskVariableValueEquals(variableValue); return this;
    }

    @Override
    public TaskQueryWrapper taskVariableValueEqualsIgnoreCase(String name, String value) {
        taskQuery.taskVariableValueEqualsIgnoreCase(name, value); return this;
    }

    @Override
    public TaskQueryWrapper taskVariableValueNotEquals(String variableName, Object variableValue) {
        taskQuery.taskVariableValueNotEquals(variableName, variableValue); return this;
    }

    @Override
    public TaskQueryWrapper taskVariableValueNotEqualsIgnoreCase(String name, String value) {
        taskQuery.taskVariableValueNotEqualsIgnoreCase(name, value); return this;
    }

    @Override
    public TaskQueryWrapper processVariableValueEquals(String variableName, Object variableValue) {
        taskQuery.processVariableValueEquals(variableName, variableValue); return this;
    }

    @Override
    public TaskQueryWrapper processVariableValueEquals(Object variableValue) {
        taskQuery.processVariableValueEquals(variableValue); return this;
    }

    @Override
    public TaskQueryWrapper processVariableValueEqualsIgnoreCase(String name, String value) {
        taskQuery.processVariableValueEqualsIgnoreCase(name, value); return this;
    }

    @Override
    public TaskQueryWrapper processVariableValueNotEquals(String variableName, Object variableValue) {
        taskQuery.processVariableValueNotEquals(variableName, variableValue); return this;
    }

    @Override
    public TaskQueryWrapper processVariableValueNotEqualsIgnoreCase(String name, String value) {
        taskQuery.processVariableValueNotEqualsIgnoreCase(name, value); return this;
    }

    @Override
    public TaskQueryWrapper processDefinitionKey(String processDefinitionKey) {
        taskQuery.processDefinitionKey(processDefinitionKey); return this;
    }

    @Override
    public TaskQueryWrapper processDefinitionId(String processDefinitionId) {
        taskQuery.processDefinitionId(processDefinitionId); return this;
    }

    @Override
    public TaskQueryWrapper processDefinitionName(String processDefinitionName) {
        taskQuery.processDefinitionName(processDefinitionName); return this;
    }

    @Override
    public TaskQueryWrapper dueDate(Date dueDate) {
        taskQuery.dueDate(dueDate); return this;
    }

    @Override
    public TaskQueryWrapper dueBefore(Date dueDate) {
        taskQuery.dueBefore(dueDate); return this;
    }

    @Override
    public TaskQueryWrapper dueAfter(Date dueDate) {
        taskQuery.dueAfter(dueDate); return this;
    }

    @Override
    public TaskQueryWrapper suspended() {
        taskQuery.suspended(); return this;
    }

    @Override
    public TaskQueryWrapper active() {
        taskQuery.active(); return this;
    }

    @Override
    public TaskQueryWrapper orderByTaskId() {
        taskQuery.orderByTaskId(); return this;
    }

    @Override
    public TaskQueryWrapper orderByTaskName() {
        taskQuery.orderByTaskName(); return this;
    }

    @Override
    public TaskQueryWrapper orderByTaskDescription() {
        taskQuery.orderByTaskDescription(); return this;
    }

    @Override
    public TaskQueryWrapper orderByTaskPriority() {
        taskQuery.orderByTaskPriority(); return this;
    }

    @Override
    public TaskQueryWrapper orderByTaskAssignee() {
        taskQuery.orderByTaskAssignee(); return this;
    }

    @Override
    public TaskQueryWrapper orderByTaskCreateTime() {
        taskQuery.orderByTaskCreateTime(); return this;
    }

    @Override
    public TaskQueryWrapper orderByProcessInstanceId() {
        taskQuery.orderByProcessInstanceId(); return this;
    }

    @Override
    public TaskQueryWrapper orderByExecutionId() {
        taskQuery.orderByExecutionId(); return this;
    }

    @Override
    public TaskQueryWrapper orderByDueDate() {
        taskQuery.orderByDueDate(); return this;
    }

    @Override
    public TaskQueryWrapper asc() {
        taskQuery.asc(); return this;
    }

    @Override
    public TaskQueryWrapper desc() {
        taskQuery.desc(); return this;
    }

    @Override
    public long count() {
        return taskQuery.count();
    }

    @Override
    public TaskWrapper singleResult() {
        return new TaskWrapperImpl( taskQuery.singleResult() );
    }

    @Override
    public List<TaskWrapper> list() {
        return TaskWrapperImpl.convert( taskQuery.list());
    }

    @Override
    public List<TaskWrapper> listPage(int firstResult, int maxResults) {
        return TaskWrapperImpl.convert( taskQuery.listPage(firstResult, maxResults));
    }

    public TaskQueryWrapperImpl(TaskQuery taskQuery) {
        this.taskQuery = taskQuery;
    }

}
