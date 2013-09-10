package org.activiti.crystalball.processengine.wrapper.activiti;

import org.activiti.crystalball.processengine.wrapper.IdentityServiceWrapper;
import org.activiti.crystalball.processengine.wrapper.activiti.queries.GroupQueryWrapperImpl;
import org.activiti.crystalball.processengine.wrapper.activiti.queries.GroupWrapperImpl;
import org.activiti.crystalball.processengine.wrapper.activiti.queries.UserQueryWrapperImpl;
import org.activiti.crystalball.processengine.wrapper.activiti.queries.UserWrapperImpl;
import org.activiti.crystalball.processengine.wrapper.queries.GroupQueryWrapper;
import org.activiti.crystalball.processengine.wrapper.queries.GroupWrapper;
import org.activiti.crystalball.processengine.wrapper.queries.UserQueryWrapper;
import org.activiti.crystalball.processengine.wrapper.queries.UserWrapper;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.*;

import java.util.List;

public class IdentityServiceWrapperImpl implements IdentityServiceWrapper {
    protected final IdentityService identityService;

    @Override
    public UserWrapper newUser(String userId) {
        return new UserWrapperImpl( identityService.newUser(userId));
    }

    public void saveUser(UserWrapper user) {
        identityService.saveUser( ((UserWrapperImpl) user).getUser());
    }

    @Override
    public UserQueryWrapper createUserQuery() {
        return new UserQueryWrapperImpl( identityService.createUserQuery()) ;
    }

    @Override
    public void deleteUser(String userId) {
        identityService.deleteUser(userId);
    }

    @Override
    public GroupWrapper newGroup(String groupId) {
        return new GroupWrapperImpl( identityService.newGroup(groupId));
    }

    @Override
    public GroupQueryWrapper createGroupQuery() {
        return new GroupQueryWrapperImpl( identityService.createGroupQuery() );
    }

    public void saveGroup(GroupWrapper group) {
        identityService.saveGroup(((GroupWrapperImpl) group).getGroup());
    }

    @Override
    public void deleteGroup(String groupId) {
        identityService.deleteGroup(groupId);
    }

    @Override
    public void createMembership(String userId, String groupId) {
        identityService.createMembership(userId, groupId);
    }

    @Override
    public void deleteMembership(String userId, String groupId) {
        identityService.deleteMembership(userId, groupId);
    }

    @Override
    public boolean checkPassword(String userId, String password) {
        return identityService.checkPassword(userId, password);
    }

    @Override
    public void setAuthenticatedUserId(String authenticatedUserId) {
        identityService.setAuthenticatedUserId(authenticatedUserId);
    }

    public void setUserPicture(String userId, Picture picture) {
        identityService.setUserPicture(userId, picture);
    }

    public Picture getUserPicture(String userId) {
        return identityService.getUserPicture(userId);
    }

    @Override
    public void setUserInfo(String userId, String key, String value) {
        identityService.setUserInfo(userId, key, value);
    }

    @Override
    public String getUserInfo(String userId, String key) {
        return identityService.getUserInfo(userId, key);
    }

    @Override
    public List<String> getUserInfoKeys(String userId) {
        return identityService.getUserInfoKeys(userId);
    }

    @Override
    public void deleteUserInfo(String userId, String key) {
        identityService.deleteUserInfo(userId, key);
    }

    public IdentityServiceWrapperImpl(IdentityService identityService) {
        this.identityService = identityService;
    }
}
