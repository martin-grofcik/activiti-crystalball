package org.activiti.crystalball.processengine.wrapper.queries;

/**
 * process engine UserQuery Wrapper
 */
public interface UserQueryWrapper {

    /** Only select {@#link User}s with the given id/ */
    UserQueryWrapper userId(String id);

    /** Only select {@#link User}s with the given firstName. */
    UserQueryWrapper userFirstName(String firstName);

    /** Only select {@#link User}s where the first name matches the given parameter.
     * The syntax is that of SQL, eg. %activivi%.
     */
    UserQueryWrapper userFirstNameLike(String firstNameLike);

    /** Only select {@#link User}s with the given lastName. */
    UserQueryWrapper userLastName(String lastName);

    /** Only select {@#link User}s where the last name matches the given parameter.
     * The syntax is that of SQL, eg. %activivi%.
     */
    UserQueryWrapper userLastNameLike(String lastNameLike);

    /** Only those {@#link User}s with the given email addres. */
    UserQueryWrapper userEmail(String email);

    /** Only select {@#link User}s where the email matches the given parameter.
     * The syntax is that of SQL, eg. %activivi%.
     */
    UserQueryWrapper userEmailLike(String emailLike);

    /** Only select {@#link User}s that belong to the given group. */
    UserQueryWrapper memberOfGroup(String groupId);

    /** Only select {@#link User}S that are potential starter for the given process definition. */
    public UserQueryWrapper potentialStarter(String procDefId);

    //sorting ////////////////////////////////////////////////////////

    /** Order by user id (needs to be followed by {@#link #asc()} or {@#link #desc()}). */
    UserQueryWrapper orderByUserId();

    /** Order by user first name (needs to be followed by {@#link #asc()} or {@#link #desc()}). */
    UserQueryWrapper orderByUserFirstName();

    /** Order by user last name (needs to be followed by {@#link #asc()} or {@#link #desc()}). */
    UserQueryWrapper orderByUserLastName();

    /** Order by user email  (needs to be followed by {@#link #asc()} or {@#link #desc()}). */
    UserQueryWrapper orderByUserEmail();
}
