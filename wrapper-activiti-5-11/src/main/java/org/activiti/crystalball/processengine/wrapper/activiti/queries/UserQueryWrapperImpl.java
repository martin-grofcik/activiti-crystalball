package org.activiti.crystalball.processengine.wrapper.activiti.queries;

import org.activiti.crystalball.processengine.wrapper.queries.UserQueryWrapper;
import org.activiti.crystalball.processengine.wrapper.queries.UserWrapper;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;

import java.util.List;

public class UserQueryWrapperImpl implements UserQueryWrapper {
    protected final UserQuery userQuery;

    public List<UserWrapper> listPage(int firstResult, int maxResults) {
        return UserWrapperImpl.convert(userQuery.listPage(firstResult, maxResults));
    }

    @Override
    public UserQueryWrapper userId(String id) {
        userQuery.userId(id); return this;
    }

    @Override
    public UserQueryWrapper userFirstName(String firstName) {
        userQuery.userFirstName(firstName); return this;
    }

    @Override
    public UserQueryWrapper userFirstNameLike(String firstNameLike) {
        userQuery.userFirstNameLike(firstNameLike); return this;
    }

    @Override
    public UserQueryWrapper userLastName(String lastName) {
        userQuery.userLastName(lastName); return this;
    }

    @Override
    public UserQueryWrapper userLastNameLike(String lastNameLike) {
        userQuery.userLastNameLike(lastNameLike); return this;
    }

    @Override
    public UserQueryWrapper userEmail(String email) {
        userQuery.userEmail(email); return this;
    }

    @Override
    public UserQueryWrapper userEmailLike(String emailLike) {
        userQuery.userEmailLike(emailLike); return this;
    }

    @Override
    public UserQueryWrapper memberOfGroup(String groupId) {
        userQuery.memberOfGroup(groupId); return this;
    }

    @Override
    public UserQueryWrapper potentialStarter(String procDefId) {
        userQuery.potentialStarter(procDefId); return this;
    }

    @Override
    public UserQueryWrapper orderByUserId() {
        userQuery.orderByUserId(); return this;
    }

    @Override
    public UserQueryWrapper orderByUserFirstName() {
        userQuery.orderByUserFirstName(); return this;
    }

    @Override
    public UserQueryWrapper orderByUserLastName() {
        userQuery.orderByUserLastName(); return this;
    }

    @Override
    public UserQueryWrapper orderByUserEmail() {
        userQuery.orderByUserEmail(); return this;
    }

    public UserQueryWrapper asc() {
        userQuery.asc(); return this;
    }

    public UserQueryWrapper desc() {
        userQuery.desc(); return this;
    }

    public long count() {
        return userQuery.count();
    }

    public UserWrapper singleResult() {
        return new UserWrapperImpl(userQuery.singleResult());
    }

    public List<UserWrapper> list() {
        return UserWrapperImpl.convert(userQuery.list());
    }

    public UserQueryWrapperImpl(UserQuery userQuery) {
        this.userQuery = userQuery;
    }
}
