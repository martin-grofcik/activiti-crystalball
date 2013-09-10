package org.activiti.crystalball.processengine.wrapper.activiti.queries;

import org.activiti.crystalball.processengine.wrapper.queries.HistoricTaskInstanceQueryWrapper;
import org.activiti.crystalball.processengine.wrapper.queries.HistoricTaskInstanceWrapper;
import org.activiti.engine.history.HistoricTaskInstanceQuery;

import java.util.Date;
import java.util.List;

public class HistoricTaskInstanceQueryWrapperImpl implements HistoricTaskInstanceQueryWrapper {
    HistoricTaskInstanceQuery historicTaskInstanceQuery;

    public HistoricTaskInstanceQueryWrapperImpl(HistoricTaskInstanceQuery historicTaskInstanceQuery) {
        this.historicTaskInstanceQuery = historicTaskInstanceQuery;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper taskId(String taskId) {
        historicTaskInstanceQuery.taskId(taskId);
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper processInstanceId(String processInstanceId) {
        historicTaskInstanceQuery.processInstanceId(processInstanceId);
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper executionId(String executionId) {
        historicTaskInstanceQuery.executionId(executionId);
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper processDefinitionId(String processDefinitionId) {
        historicTaskInstanceQuery.processDefinitionId(processDefinitionId);
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper processDefinitionKey(String processDefinitionKey) {
        historicTaskInstanceQuery.processDefinitionKey(processDefinitionKey);
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper processDefinitionName(String processDefinitionName) {
        historicTaskInstanceQuery.processDefinitionName(processDefinitionName);
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper taskName(String taskName) {
        historicTaskInstanceQuery.taskName(taskName);
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper taskNameLike(String taskNameLike) {
        historicTaskInstanceQuery.taskNameLike(taskNameLike);
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper taskDescription(String taskDescription) {
        historicTaskInstanceQuery.taskDescription(taskDescription);
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper taskDescriptionLike(String taskDescriptionLike) {
        historicTaskInstanceQuery.taskDescriptionLike(taskDescriptionLike);
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper taskDefinitionKey(String taskDefinitionKey) {
        historicTaskInstanceQuery.taskDefinitionKey(taskDefinitionKey);
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper taskDeleteReason(String taskDeleteReason) {
        historicTaskInstanceQuery.taskDeleteReason(taskDeleteReason);
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper taskDeleteReasonLike(String taskDeleteReasonLike) {
        historicTaskInstanceQuery.taskDeleteReasonLike(taskDeleteReasonLike);
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper taskAssignee(String taskAssignee) {
        historicTaskInstanceQuery.taskAssignee(taskAssignee);
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper taskAssigneeLike(String taskAssigneeLike) {
        historicTaskInstanceQuery.taskAssigneeLike(taskAssigneeLike);
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper taskOwner(String taskOwner) {
        historicTaskInstanceQuery.taskOwner(taskOwner);
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper taskOwnerLike(String taskOwnerLike) {
        historicTaskInstanceQuery.taskOwnerLike(taskOwnerLike);
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper taskPriority(Integer taskPriority) {
        historicTaskInstanceQuery.taskPriority(taskPriority);
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper finished() {
        historicTaskInstanceQuery.finished();
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper unfinished() {
        historicTaskInstanceQuery.unfinished();
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper processFinished() {
        historicTaskInstanceQuery.processFinished();
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper processUnfinished() {
        historicTaskInstanceQuery.processUnfinished();
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper taskVariableValueEquals(String variableName, Object variableValue) {
        historicTaskInstanceQuery.taskVariableValueEquals(variableName, variableValue);
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper taskParentTaskId(String parentTaskId) {
        historicTaskInstanceQuery.taskParentTaskId(parentTaskId);
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper processVariableValueEquals(String variableName, Object variableValue) {
        historicTaskInstanceQuery.processVariableValueEquals(variableName, variableValue);
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper taskDueDate(Date dueDate) {
        historicTaskInstanceQuery.taskDueDate(dueDate);
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper taskDueBefore(Date dueDate) {
        historicTaskInstanceQuery.taskDueBefore(dueDate);
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper taskDueAfter(Date dueDate) {
        historicTaskInstanceQuery.taskDueAfter(dueDate);
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper orderByTaskId() {
        historicTaskInstanceQuery.orderByTaskId();
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper orderByHistoricActivityInstanceId() {
        historicTaskInstanceQuery.orderByHistoricActivityInstanceId();
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper orderByProcessDefinitionId() {
        historicTaskInstanceQuery.orderByProcessDefinitionId();
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper orderByProcessInstanceId() {
        historicTaskInstanceQuery.orderByProcessInstanceId();
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper orderByExecutionId() {
        historicTaskInstanceQuery.orderByExecutionId();
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper orderByHistoricTaskInstanceDuration() {
        historicTaskInstanceQuery.orderByHistoricTaskInstanceDuration();
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper orderByHistoricTaskInstanceEndTime() {
        historicTaskInstanceQuery.orderByHistoricTaskInstanceEndTime();
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper orderByHistoricActivityInstanceStartTime() {
        historicTaskInstanceQuery.orderByHistoricActivityInstanceStartTime();
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper orderByTaskName() {
        historicTaskInstanceQuery.orderByTaskName();
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper orderByTaskDescription() {
        historicTaskInstanceQuery.orderByTaskDescription();
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper orderByTaskAssignee() {
        historicTaskInstanceQuery.orderByTaskAssignee();
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper orderByTaskOwner() {
        historicTaskInstanceQuery.orderByTaskOwner();
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper orderByTaskDueDate() {
        historicTaskInstanceQuery.orderByTaskDueDate();
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper orderByDeleteReason() {
        historicTaskInstanceQuery.orderByDeleteReason();
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper orderByTaskDefinitionKey() {
        historicTaskInstanceQuery.orderByTaskDefinitionKey();
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper orderByTaskPriority() {
        historicTaskInstanceQuery.orderByTaskPriority();
        return this;
    }

    @Override
    public HistoricTaskInstanceQueryWrapper asc() {
        historicTaskInstanceQuery.asc();
        return this.asc();
    }

    @Override
    public HistoricTaskInstanceQueryWrapper desc() {
        historicTaskInstanceQuery.desc();
        return this.asc();
    }

    @Override
    public long count() {
        return historicTaskInstanceQuery.count();
    }

    @Override
    public HistoricTaskInstanceWrapper singleResult() {
        return new HistoricTaskInstanceWrapperImpl(historicTaskInstanceQuery.singleResult());
    }

    @Override
    public List<HistoricTaskInstanceWrapper> list() {
        return HistoricTaskInstanceWrapperImpl.convert(historicTaskInstanceQuery.list());
    }

    @Override
    public List<HistoricTaskInstanceWrapper> listPage(int firstResult, int maxResults) {
        return HistoricTaskInstanceWrapperImpl.convert( historicTaskInstanceQuery.listPage(firstResult, maxResults));
    }
}
