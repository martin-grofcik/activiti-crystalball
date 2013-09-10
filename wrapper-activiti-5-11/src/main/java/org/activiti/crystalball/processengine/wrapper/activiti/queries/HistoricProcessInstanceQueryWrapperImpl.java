package org.activiti.crystalball.processengine.wrapper.activiti.queries;

import org.activiti.crystalball.processengine.wrapper.queries.HistoricProcessInstanceQueryWrapper;
import org.activiti.crystalball.processengine.wrapper.queries.HistoricProcessInstanceWrapper;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;

import java.util.Date;
import java.util.List;
import java.util.Set;


public class HistoricProcessInstanceQueryWrapperImpl implements HistoricProcessInstanceQueryWrapper {
    private final HistoricProcessInstanceQuery historicProcessInstanceQuery;

    @Override
    public HistoricProcessInstanceQueryWrapper processInstanceId(String processInstanceId) {
        historicProcessInstanceQuery.processInstanceId(processInstanceId);
        return this;
    }

    @Override
    public HistoricProcessInstanceQueryWrapper processInstanceIds(Set<String> processInstanceIds) {
        historicProcessInstanceQuery.processInstanceIds(processInstanceIds);         return this;
    }

    @Override
    public HistoricProcessInstanceQueryWrapper processDefinitionId(String processDefinitionId) {
        historicProcessInstanceQuery.processDefinitionId(processDefinitionId);         return this;
    }

    @Override
    public HistoricProcessInstanceQueryWrapper processDefinitionKey(String processDefinitionKey) {
        historicProcessInstanceQuery.processDefinitionKey(processDefinitionKey);         return this;
    }

    @Override
    public HistoricProcessInstanceQueryWrapper processDefinitionKeyNotIn(List<String> processDefinitionKeys) {
        historicProcessInstanceQuery.processDefinitionKeyNotIn(processDefinitionKeys);         return this;
    }

    @Override
    public HistoricProcessInstanceQueryWrapper processInstanceBusinessKey(String processInstanceBusinessKey) {
        historicProcessInstanceQuery.processInstanceBusinessKey(processInstanceBusinessKey);         return this;
    }

    @Override
    public HistoricProcessInstanceQueryWrapper finished() {
        historicProcessInstanceQuery.finished();         return this;
    }

    @Override
    public HistoricProcessInstanceQueryWrapper unfinished() {
        historicProcessInstanceQuery.unfinished();         return this;
    }

    @Override
    public HistoricProcessInstanceQueryWrapper variableValueEquals(String name, Object value) {
        historicProcessInstanceQuery.variableValueEquals(name, value);         return this;
    }

    @Override
    public HistoricProcessInstanceQueryWrapper variableValueEquals(Object value) {
        historicProcessInstanceQuery.variableValueEquals(value);         return this;
    }

    @Override
    public HistoricProcessInstanceQueryWrapper variableValueEqualsIgnoreCase(String name, String value) {
        historicProcessInstanceQuery.variableValueEqualsIgnoreCase(name, value);         return this;
    }

    @Override
    public HistoricProcessInstanceQueryWrapper variableValueNotEquals(String name, Object value) {
        historicProcessInstanceQuery.variableValueNotEquals(name, value);         return this;
    }

    @Override
    public HistoricProcessInstanceQueryWrapper variableValueGreaterThan(String name, Object value) {
        historicProcessInstanceQuery.variableValueGreaterThan(name, value);         return this;
    }

    @Override
    public HistoricProcessInstanceQueryWrapper variableValueGreaterThanOrEqual(String name, Object value) {
        historicProcessInstanceQuery.variableValueGreaterThanOrEqual(name, value);         return this;
    }

    @Override
    public HistoricProcessInstanceQueryWrapper variableValueLessThan(String name, Object value) {
        historicProcessInstanceQuery.variableValueLessThan(name, value);         return this;
    }

    @Override
    public HistoricProcessInstanceQueryWrapper variableValueLessThanOrEqual(String name, Object value) {
        historicProcessInstanceQuery.variableValueLessThanOrEqual(name, value);         return this;
    }

    @Override
    public HistoricProcessInstanceQueryWrapper variableValueLike(String name, String value) {
        historicProcessInstanceQuery.variableValueLike(name, value);         return this;
    }

    @Override
    public HistoricProcessInstanceQueryWrapper startedBefore(Date date) {
        historicProcessInstanceQuery.startedBefore(date);         return this;
    }

    @Override
    public HistoricProcessInstanceQueryWrapper startedAfter(Date date) {
        historicProcessInstanceQuery.startedAfter(date);         return this;
    }

    @Override
    public HistoricProcessInstanceQueryWrapper finishedBefore(Date date) {
        historicProcessInstanceQuery.finishedBefore(date);         return this;
    }

    @Override
    public HistoricProcessInstanceQueryWrapper finishedAfter(Date date) {
        historicProcessInstanceQuery.finishedAfter(date);         return this;
    }

    @Override
    public HistoricProcessInstanceQueryWrapper startedBy(String userId) {
        historicProcessInstanceQuery.startedBy(userId);         return this;
    }

    @Override
    public HistoricProcessInstanceQueryWrapper orderByProcessInstanceId() {
        historicProcessInstanceQuery.orderByProcessInstanceId();         return this;
    }

    @Override
    public HistoricProcessInstanceQueryWrapper orderByProcessDefinitionId() {
        historicProcessInstanceQuery.orderByProcessDefinitionId();         return this;
    }

    @Override
    public HistoricProcessInstanceQueryWrapper orderByProcessInstanceBusinessKey() {
        historicProcessInstanceQuery.orderByProcessInstanceBusinessKey();         return this;
    }

    @Override
    public HistoricProcessInstanceQueryWrapper orderByProcessInstanceStartTime() {
        historicProcessInstanceQuery.orderByProcessInstanceStartTime();         return this;
    }

    @Override
    public HistoricProcessInstanceQueryWrapper orderByProcessInstanceEndTime() {
        historicProcessInstanceQuery.orderByProcessInstanceEndTime();         return this;
    }

    @Override
    public HistoricProcessInstanceQueryWrapper orderByProcessInstanceDuration() {
        historicProcessInstanceQuery.orderByProcessInstanceDuration();         return this;
    }

    @Override
    public HistoricProcessInstanceQueryWrapper superProcessInstanceId(String superProcessInstanceId) {
        historicProcessInstanceQuery.superProcessInstanceId(superProcessInstanceId);         return this;
    }

    @Override
    public HistoricProcessInstanceQueryWrapper startDateBy(Date date) {
        historicProcessInstanceQuery.startDateBy(date);         return this;
    }

    @Override
    public HistoricProcessInstanceQueryWrapper startDateOn(Date date) {
        historicProcessInstanceQuery.startDateOn(date);         return this;
    }

    @Override
    public HistoricProcessInstanceQueryWrapper finishDateBy(Date date) {
        historicProcessInstanceQuery.finishDateBy(date);         return this;
    }

    @Override
    public HistoricProcessInstanceQueryWrapper finishDateOn(Date date) {
        historicProcessInstanceQuery.finishDateOn(date);         return this;
    }

    @Override
    public HistoricProcessInstanceQueryWrapper asc() {
        historicProcessInstanceQuery.asc();         return this;
    }

    @Override
    public HistoricProcessInstanceQueryWrapper desc() {
        historicProcessInstanceQuery.desc();         return this;
    }

    @Override
    public long count() {
        return historicProcessInstanceQuery.count();
    }

    @Override
    public HistoricProcessInstanceWrapper singleResult() {
        return new HistoricProcessInstanceWrapperImpl( (HistoricProcessInstanceEntity) historicProcessInstanceQuery.singleResult());
    }

    @Override
    public List<HistoricProcessInstanceWrapper> list() {
        return HistoricProcessInstanceWrapperImpl.convert(historicProcessInstanceQuery.list());
    }

    @Override
    public List<HistoricProcessInstanceWrapper> listPage(int firstResult, int maxResults) {
        return HistoricProcessInstanceWrapperImpl.convert(historicProcessInstanceQuery.listPage(firstResult, maxResults));
    }

    public HistoricProcessInstanceQueryWrapperImpl(HistoricProcessInstanceQuery historicProcessInstanceQuery) {
        this.historicProcessInstanceQuery = historicProcessInstanceQuery;
    }
}
