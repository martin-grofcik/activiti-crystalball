package org.activiti.crystalball.processengine.wrapper.queries;

/**
 */
public interface ProcessDefinitionQueryWrapper extends Query<ProcessDefinitionQueryWrapper, ProcessDefinitionWrapper>{

    /** Only select process definiton with the given id.  */
    ProcessDefinitionQueryWrapper processDefinitionId(String processDefinitionId);

    /** Only select process definitions with the given category. */
    ProcessDefinitionQueryWrapper processDefinitionCategory(String processDefinitionCategory);

    /**
     * Only select process definitions where the category matches the given parameter.
     * The syntax that should be used is the same as in SQL, eg. %activiti%
     */
    ProcessDefinitionQueryWrapper processDefinitionCategoryLike(String processDefinitionCategoryLike);

    /** Only select deployments that have a different category then the given one. 
     * @#see DeploymentBuilder#category(String) */
    ProcessDefinitionQueryWrapper processDefinitionCategoryNotEquals(String categoryNotEquals);

    /** Only select process definitions with the given name. */
    ProcessDefinitionQueryWrapper processDefinitionName(String processDefinitionName);

    /**
     * Only select process definitions where the name matches the given parameter.
     * The syntax that should be used is the same as in SQL, eg. %activiti%
     */
    ProcessDefinitionQueryWrapper processDefinitionNameLike(String processDefinitionNameLike);

    /**
     * Only select process definitions that are deployed in a deployment with the
     * given deployment id
     */
    ProcessDefinitionQueryWrapper deploymentId(String deploymentId);

    /**
     * Only select process definition with the given key.
     */
    ProcessDefinitionQueryWrapper processDefinitionKey(String processDefinitionKey);

    /**
     * Only select process definitions where the key matches the given parameter.
     * The syntax that should be used is the same as in SQL, eg. %activiti%
     */
    ProcessDefinitionQueryWrapper processDefinitionKeyLike(String processDefinitionKeyLike);

    /**
     * Only select process definition with a certain version.
     * Particulary useful when used in combination with {@link #processDefinitionKey(String)}
     */
    ProcessDefinitionQueryWrapper processDefinitionVersion(Integer processDefinitionVersion);

    /**
     * Only select the process definitions which are the latest deployed
     * (ie. which have the highest version number for the given key).
     *
     * Can only be used in combination with {@link #processDefinitionKey(String)} of {@link #processDefinitionKeyLike(String)}.
     * Can also be used without any other criteria (ie. query.latest().list()), which
     * will then give all the latest versions of all the deployed process definitions.
     *
     * @#throws ActivitiException if used in combination with  {@#link #groupId(string)}, {@#link #processDefinitionVersion(int)}
     *                           or {@link #deploymentId(String)}
     */
    ProcessDefinitionQueryWrapper latestVersion();

    /** Only select process definition with the given resource name. */
    ProcessDefinitionQueryWrapper processDefinitionResourceName(String resourceName);

    /** Only select process definition with a resource name like the given . */
    ProcessDefinitionQueryWrapper processDefinitionResourceNameLike(String resourceNameLike);

    /**
     * Only selects process definitions which given userId is authoriezed to start
     */
    ProcessDefinitionQueryWrapper startableByUser(String userId);

    /**
     * Only selects process definitions which are suspended
     */
    ProcessDefinitionQueryWrapper suspended();

    /**
     * Only selects process definitions which are active
     */
    ProcessDefinitionQueryWrapper active();

    // Support for event subscriptions /////////////////////////////////////

    /**
     * @see #messageEventSubscriptionName(String)
     */
    @Deprecated
    ProcessDefinitionQueryWrapper messageEventSubscription(String messageName);

    /**
     * Selects the single process definition which has a start message event 
     * with the messageName.
     */
    ProcessDefinitionQueryWrapper messageEventSubscriptionName(String messageName);

    // ordering ////////////////////////////////////////////////////////////

    /** Order by the category of the process definitions (needs to be followed by {@link #asc()} or {@link #desc()}). */
    ProcessDefinitionQueryWrapper orderByProcessDefinitionCategory();

    /** Order by process definition key (needs to be followed by {@link #asc()} or {@link #desc()}). */
    ProcessDefinitionQueryWrapper orderByProcessDefinitionKey();

    /** Order by the id of the process definitions (needs to be followed by {@link #asc()} or {@link #desc()}). */
    ProcessDefinitionQueryWrapper orderByProcessDefinitionId();

    /** Order by the version of the process definitions (needs to be followed by {@link #asc()} or {@link #desc()}). */
    ProcessDefinitionQueryWrapper orderByProcessDefinitionVersion();

    /** Order by the name of the process definitions (needs to be followed by {@link #asc()} or {@link #desc()}). */
    ProcessDefinitionQueryWrapper orderByProcessDefinitionName();

    /** Order by deployment id (needs to be followed by {@link #asc()} or {@link #desc()}). */
    ProcessDefinitionQueryWrapper orderByDeploymentId();
}
