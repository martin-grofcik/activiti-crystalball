package org.activiti.crystalball.processengine.wrapper;

import org.activiti.crystalball.processengine.wrapper.queries.GroupQueryWrapper;
import org.activiti.crystalball.processengine.wrapper.queries.GroupWrapper;
import org.activiti.crystalball.processengine.wrapper.queries.UserQueryWrapper;
import org.activiti.crystalball.processengine.wrapper.queries.UserWrapper;


import java.util.List;
import java.util.Map;

/**
 * Identity service wrapper
 */
public interface IdentityServiceWrapper {

    /**
     * Creates a new user. The user is transient and must be saved using
     * {@#link #saveUser(User)}.
     * @param userId id for the new user, cannot be null.
     */
    UserWrapper newUser(String userId);

    /**
     * Saves the user. If the user already existed, the user is updated.
     * @param user user to save, cannot be null.
     * @throws RuntimeException when a user with the same name already exists.
     */
    void saveUser(UserWrapper user);

    /**
     * Creates a {@#link UserQuery} that allows to programmatically query the users.
     */
    UserQueryWrapper createUserQuery();

    /**
     * @param userId id of user to delete, cannot be null. When an id is passed
     * for an unexisting user, this operation is ignored.
     */
    void deleteUser(String userId);

    /**
     * Creates a new group. The group is transient and must be saved using
     * {@#link #saveGroup(Group)}.
     * @param groupId id for the new group, cannot be null.
     */
    GroupWrapper newGroup(String groupId);

    /**
     * Creates a {@link GroupQueryWrapper} thats allows to programmatically query the groups.
     */
    GroupQueryWrapper createGroupQuery();

    /**
     * Saves the group. If the group already existed, the group is updated.
     * @param group group to save. Cannot be null.
     * @throws RuntimeException when a group with the same name already exists.
     */
    void saveGroup(GroupWrapper group);

    /**
     * Deletes the group. When no group exists with the given id, this operation
     * is ignored.
     * @param groupId id of the group that should be deleted, cannot be null.
     */
    void deleteGroup(String groupId);

    /**
     * @param userId the userId, cannot be null.
     * @param groupId the groupId, cannot be null.
     * @throws RuntimeException when the given user or group doesn't exist or when the user
     * is already member of the group.
     */
    void createMembership(String userId, String groupId);

    /**
     * Delete the membership of the user in the group. When the group or user don't exist
     * or when the user is not a member of the group, this operation is ignored.
     * @param userId the user's id, cannot be null.
     * @param groupId the group's id, cannot be null.
     */
    void deleteMembership(String userId, String groupId);

    /**
     * Checks if the password is valid for the given user. Arguments userId
     * and password are nullsafe.
     */
    boolean checkPassword(String userId, String password);

    /**
     * Passes the authenticated user id for this particular thread.
     * All service method (from any service) invocations done by the same
     * thread will have access to this authenticatedUserId.
     */
    void setAuthenticatedUserId(String authenticatedUserId);

    /** Generic extensibility key-value pairs associated with a user */
    void setUserInfo(String userId, String key, String value);

    /** Generic extensibility key-value pairs associated with a user */
    String getUserInfo(String userId, String key);

    /** Generic extensibility keys associated with a user */
    List<String> getUserInfoKeys(String userId);

    /** Delete an entry of the generic extensibility key-value pairs associated with a user */
    void deleteUserInfo(String userId, String key);
}
