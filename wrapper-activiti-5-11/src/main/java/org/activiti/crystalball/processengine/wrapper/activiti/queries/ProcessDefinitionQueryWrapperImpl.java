package org.activiti.crystalball.processengine.wrapper.activiti.queries;

import org.activiti.crystalball.processengine.wrapper.queries.ProcessDefinitionQueryWrapper;
import org.activiti.crystalball.processengine.wrapper.queries.ProcessDefinitionWrapper;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: martin.grofcik
 * Date: 10.9.2013
 * Time: 9:31
 * To change this template use File | Settings | File Templates.
 */
public class ProcessDefinitionQueryWrapperImpl implements ProcessDefinitionQueryWrapper {
    protected final ProcessDefinitionQuery processDefinitionQuery;

    @Override
    public ProcessDefinitionQueryWrapper processDefinitionId(String processDefinitionId) {
        processDefinitionQuery.processDefinitionId(processDefinitionId);
        return this;
    }

    @Override
    public ProcessDefinitionQueryWrapper processDefinitionCategory(String processDefinitionCategory) {
        processDefinitionQuery.processDefinitionCategory(processDefinitionCategory);
        return this;
    }

    @Override
    public ProcessDefinitionQueryWrapper processDefinitionCategoryLike(String processDefinitionCategoryLike) {
        processDefinitionQuery.processDefinitionCategoryLike(processDefinitionCategoryLike);
        return this;
    }

    @Override
    public ProcessDefinitionQueryWrapper processDefinitionCategoryNotEquals(String categoryNotEquals) {
        processDefinitionQuery.processDefinitionCategoryNotEquals(categoryNotEquals);
        return this;
    }

    @Override
    public ProcessDefinitionQueryWrapper processDefinitionName(String processDefinitionName) {
        processDefinitionQuery.processDefinitionName(processDefinitionName);
        return this;
    }

    @Override
    public ProcessDefinitionQueryWrapper processDefinitionNameLike(String processDefinitionNameLike) {
        processDefinitionQuery.processDefinitionNameLike(processDefinitionNameLike);
        return this;
    }

    @Override
    public ProcessDefinitionQueryWrapper deploymentId(String deploymentId) {
        processDefinitionQuery.deploymentId(deploymentId);
        return this;
    }

    @Override
    public ProcessDefinitionQueryWrapper processDefinitionKey(String processDefinitionKey) {
        processDefinitionQuery.processDefinitionKey(processDefinitionKey);
        return this;
    }

    @Override
    public ProcessDefinitionQueryWrapper processDefinitionKeyLike(String processDefinitionKeyLike) {
        processDefinitionQuery.processDefinitionKeyLike(processDefinitionKeyLike);
        return this;
    }

    @Override
    public ProcessDefinitionQueryWrapper processDefinitionVersion(Integer processDefinitionVersion) {
        processDefinitionQuery.processDefinitionVersion(processDefinitionVersion);
        return this;
    }

    @Override
    public ProcessDefinitionQueryWrapper latestVersion() {
        processDefinitionQuery.latestVersion();
        return this;
    }

    @Override
    public ProcessDefinitionQueryWrapper processDefinitionResourceName(String resourceName) {
        processDefinitionQuery.processDefinitionResourceName(resourceName);
        return this;
    }

    @Override
    public ProcessDefinitionQueryWrapper processDefinitionResourceNameLike(String resourceNameLike) {
        processDefinitionQuery.processDefinitionResourceNameLike(resourceNameLike);
        return this;
    }

    @Override
    public ProcessDefinitionQueryWrapper startableByUser(String userId) {
        processDefinitionQuery.startableByUser(userId);
        return this;
    }

    @Override
    public ProcessDefinitionQueryWrapper suspended() {
        processDefinitionQuery.suspended();
        return this;
    }

    @Override
    public ProcessDefinitionQueryWrapper active() {
        processDefinitionQuery.active();
        return this;
    }

    @Override
    @Deprecated
    public ProcessDefinitionQueryWrapper messageEventSubscription(String messageName) {
        processDefinitionQuery.messageEventSubscription(messageName);
        return this;
    }

    @Override
    public ProcessDefinitionQueryWrapper messageEventSubscriptionName(String messageName) {
        processDefinitionQuery.messageEventSubscriptionName(messageName);
        return this;
    }

    @Override
    public ProcessDefinitionQueryWrapper orderByProcessDefinitionCategory() {
        processDefinitionQuery.orderByProcessDefinitionCategory();
        return this;
    }

    @Override
    public ProcessDefinitionQueryWrapper orderByProcessDefinitionKey() {
        processDefinitionQuery.orderByProcessDefinitionKey();
        return this;
    }

    @Override
    public ProcessDefinitionQueryWrapper orderByProcessDefinitionId() {
        processDefinitionQuery.orderByProcessDefinitionId();
        return this;
    }

    @Override
    public ProcessDefinitionQueryWrapper orderByProcessDefinitionVersion() {
        processDefinitionQuery.orderByProcessDefinitionVersion();
        return this;
    }

    @Override
    public ProcessDefinitionQueryWrapper orderByProcessDefinitionName() {
        processDefinitionQuery.orderByProcessDefinitionName();
        return this;
    }

    @Override
    public ProcessDefinitionQueryWrapper orderByDeploymentId() {
        processDefinitionQuery.orderByDeploymentId();
        return this;
    }

    @Override
    public ProcessDefinitionQueryWrapper asc() {
        processDefinitionQuery.asc();
        return this;
    }

    @Override
    public ProcessDefinitionQueryWrapper desc() {
        processDefinitionQuery.desc();
        return this;
    }

    @Override
    public long count() {
        return processDefinitionQuery.count();
    }

    @Override
    public ProcessDefinitionWrapper singleResult() {
        return new ProcessDefinitionWrapperImpl( (ProcessDefinitionEntity) processDefinitionQuery.singleResult() );
    }

    @Override
    public List<ProcessDefinitionWrapper> list() {
        return ProcessDefinitionWrapperImpl.convert( processDefinitionQuery.list());
    }

    @Override
    public List<ProcessDefinitionWrapper> listPage(int firstResult, int maxResults) {
        return ProcessDefinitionWrapperImpl.convert( processDefinitionQuery.listPage(firstResult, maxResults));
    }

    public ProcessDefinitionQueryWrapperImpl(ProcessDefinitionQuery processDefinitionQuery) {
        this.processDefinitionQuery = processDefinitionQuery;
    }


}
