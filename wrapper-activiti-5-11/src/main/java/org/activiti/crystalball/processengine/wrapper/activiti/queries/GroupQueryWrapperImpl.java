package org.activiti.crystalball.processengine.wrapper.activiti.queries;

import org.activiti.crystalball.processengine.wrapper.queries.GroupQueryWrapper;
import org.activiti.crystalball.processengine.wrapper.queries.GroupWrapper;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.GroupQuery;

import java.util.List;

public class GroupQueryWrapperImpl implements GroupQueryWrapper {
    protected final GroupQuery groupQuery;

    @Override
    public GroupQueryWrapper groupId(String groupId) {
        groupQuery.groupId(groupId); return this;
    }

    @Override
    public GroupQueryWrapper groupName(String groupName) {
        groupQuery.groupName(groupName); return this;
    }

    @Override
    public GroupQueryWrapper groupNameLike(String groupNameLike) {
        groupQuery.groupNameLike(groupNameLike); return this;
    }

    @Override
    public GroupQueryWrapper groupType(String groupType) {
        groupQuery.groupType(groupType); return this;
    }

    @Override
    public GroupQueryWrapper groupMember(String groupMemberUserId) {
        groupQuery.groupMember(groupMemberUserId); return this;
    }

    @Override
    public GroupQueryWrapper potentialStarter(String procDefId) {
        groupQuery.potentialStarter(procDefId); return this;
    }

    @Override
    public GroupQueryWrapper orderByGroupId() {
        groupQuery.orderByGroupId(); return this;
    }

    @Override
    public GroupQueryWrapper orderByGroupName() {
        groupQuery.orderByGroupName(); return this;
    }

    @Override
    public GroupQueryWrapper orderByGroupType() {
        groupQuery.orderByGroupType(); return this;
    }

    @Override
    public GroupQueryWrapper asc() {
        groupQuery.asc(); return this;
    }

    @Override
    public GroupQueryWrapper desc() {
        groupQuery.desc(); return this;
    }

    @Override
    public long count() {
        return groupQuery.count();
    }

    @Override
    public GroupWrapper singleResult() {
        return new GroupWrapperImpl(groupQuery.singleResult());
    }

    @Override
    public List<GroupWrapper> list() {
        return GroupWrapperImpl.convert(groupQuery.list());
    }

    @Override
    public List<GroupWrapper> listPage(int firstResult, int maxResults) {
        return GroupWrapperImpl.convert(groupQuery.listPage(firstResult, maxResults));
    }

    public GroupQueryWrapperImpl(GroupQuery groupQuery) {
        this.groupQuery = groupQuery;
    }
}
