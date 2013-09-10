package org.activiti.crystalball.processengine.wrapper.activiti.queries;

import org.activiti.crystalball.processengine.wrapper.queries.ExecutionQueryWrapper;
import org.activiti.crystalball.processengine.wrapper.queries.ExecutionWrapper;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ExecutionQuery;

import java.util.List;

public class ExecutionQueryWrapperImpl implements ExecutionQueryWrapper {
    protected final ExecutionQuery executionQuery;

    @Override
    public ExecutionQueryWrapper processDefinitionKey(String processDefinitionKey) {
        executionQuery.processDefinitionKey(processDefinitionKey); return this;
    }

    @Override
    public ExecutionQueryWrapper processDefinitionId(String processDefinitionId) {
        executionQuery.processDefinitionId(processDefinitionId); return this;
    }

    @Override
    public ExecutionQueryWrapper processInstanceId(String processInstanceId) {
        executionQuery.processInstanceId(processInstanceId); return this;
    }

    @Override
    public ExecutionQueryWrapper processInstanceBusinessKey(String processInstanceBusinessKey) {
        executionQuery.processInstanceBusinessKey(processInstanceBusinessKey); return this;
    }

    @Override
    public ExecutionQueryWrapper executionId(String executionId) {
        executionQuery.executionId(executionId); return this;
    }

    @Override
    public ExecutionQueryWrapper activityId(String activityId) {
        executionQuery.activityId(activityId); return this;
    }

    @Override
    public ExecutionQueryWrapper variableValueEquals(String name, Object value) {
        executionQuery.variableValueEquals(name, value); return this;
    }

    @Override
    public ExecutionQueryWrapper variableValueEqualsIgnoreCase(String name, String value) {
        executionQuery.variableValueEqualsIgnoreCase(name, value); return this;
    }

    @Override
    public ExecutionQueryWrapper variableValueEquals(Object value) {
        executionQuery.variableValueEquals(value); return this;
    }

    @Override
    public ExecutionQueryWrapper variableValueNotEquals(String name, Object value) {
        executionQuery.variableValueNotEquals(name, value); return this;
    }

    @Override
    public ExecutionQueryWrapper variableValueNotEqualsIgnoreCase(String name, String value) {
        executionQuery.variableValueNotEqualsIgnoreCase(name, value); return this;
    }

    @Override
    public ExecutionQueryWrapper variableValueGreaterThan(String name, Object value) {
        executionQuery.variableValueGreaterThan(name, value); return this;
    }

    @Override
    public ExecutionQueryWrapper variableValueGreaterThanOrEqual(String name, Object value) {
        executionQuery.variableValueGreaterThanOrEqual(name, value); return this;
    }

    @Override
    public ExecutionQueryWrapper variableValueLessThan(String name, Object value) {
        executionQuery.variableValueLessThan(name, value); return this;
    }

    @Override
    public ExecutionQueryWrapper variableValueLessThanOrEqual(String name, Object value) {
        executionQuery.variableValueLessThanOrEqual(name, value); return this;
    }

    @Override
    public ExecutionQueryWrapper variableValueLike(String name, String value) {
        executionQuery.variableValueLike(name, value); return this;
    }

    @Override
    public ExecutionQueryWrapper processVariableValueEquals(String variableName, Object variableValue) {
        executionQuery.processVariableValueEquals(variableName, variableValue); return this;
    }

    @Override
    public ExecutionQueryWrapper processVariableValueEquals(Object variableValue) {
        executionQuery.processVariableValueEquals(variableValue); return this;
    }

    @Override
    public ExecutionQueryWrapper processVariableValueNotEquals(String variableName, Object variableValue) {
        executionQuery.processVariableValueNotEquals(variableName, variableValue); return this;
    }

    @Override
    public ExecutionQueryWrapper processVariableValueEqualsIgnoreCase(String name, String value) {
        executionQuery.processVariableValueEqualsIgnoreCase(name, value); return this;
    }

    @Override
    public ExecutionQueryWrapper processVariableValueNotEqualsIgnoreCase(String name, String value) {
        executionQuery.processVariableValueNotEqualsIgnoreCase(name, value); return this;
    }

    @Override
    public ExecutionQueryWrapper signalEventSubscriptionName(String signalName) {
        executionQuery.signalEventSubscriptionName(signalName); return this;
    }

    @Override
    public ExecutionQueryWrapper messageEventSubscriptionName(String messageName) {
        executionQuery.messageEventSubscriptionName(messageName); return this;
    }

    @Override
    public ExecutionQueryWrapper orderByProcessInstanceId() {
        executionQuery.orderByProcessInstanceId(); return this;
    }

    @Override
    public ExecutionQueryWrapper orderByProcessDefinitionKey() {
        executionQuery.orderByProcessDefinitionKey(); return this;
    }

    @Override
    public ExecutionQueryWrapper orderByProcessDefinitionId() {
        executionQuery.orderByProcessDefinitionId(); return this;
    }

    public ExecutionQueryWrapper asc() {
        executionQuery.asc(); return this;
    }

    public ExecutionQueryWrapper desc() {
        executionQuery.desc(); return this;
    }

    public long count() {
        return executionQuery.count();
    }

    public ExecutionWrapper singleResult() {
        return new ExecutionWrapperImpl( executionQuery.singleResult());
    }

    public List<ExecutionWrapper> list() {
        return ExecutionWrapperImpl.convert( executionQuery.list());
    }

    public List<ExecutionWrapper> listPage(int firstResult, int maxResults) {
        return ExecutionWrapperImpl.convert( executionQuery.listPage(firstResult, maxResults));
    }

    public ExecutionQueryWrapperImpl(ExecutionQuery executionQuery) {
        this.executionQuery = executionQuery;
    }
}
