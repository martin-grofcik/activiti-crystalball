package org.activiti.crystalball.processengine.wrapper.queries;

/**
 */
public interface HistoricActivityInstanceQueryWrapper  extends Query<HistoricActivityInstanceQueryWrapper, HistoricActivityInstanceWrapper>{

    /** Only select historic activity instances with the given id (primary key within history tables). */
    HistoricActivityInstanceQueryWrapper activityInstanceId(String processInstanceId);

    /** Only select historic activity instances with the given process instance.
     * {@link ProcessInstanceWrapper ) ids and {@link HistoricProcessInstance} ids match. */
    HistoricActivityInstanceQueryWrapper processInstanceId(String processInstanceId);

    /** Only select historic activity instances for the given process definition */
    HistoricActivityInstanceQueryWrapper processDefinitionId(String processDefinitionId);

    /** Only select historic activity instances for the given execution */
    HistoricActivityInstanceQueryWrapper executionId(String executionId);

    /** Only select historic activity instances for the given activity (id from BPMN 2.0 XML) */
    HistoricActivityInstanceQueryWrapper activityId(String activityId);

    /** Only select historic activity instances for activities with the given name */
    HistoricActivityInstanceQueryWrapper activityName(String activityName);

    /** Only select historic activity instances for activities with the given activity type */
    HistoricActivityInstanceQueryWrapper activityType(String activityType);

    /** Only select historic activity instances for userTask activities assigned to the given user */
    HistoricActivityInstanceQueryWrapper taskAssignee(String userId);

    /** Only select historic activity instances that are finished. */
    HistoricActivityInstanceQueryWrapper finished();

    /** Only select historic activity instances that are not finished yet. */
    HistoricActivityInstanceQueryWrapper unfinished();

    // ordering /////////////////////////////////////////////////////////////////
    /** Order by id (needs to be followed by {@link #asc()} or {@link #desc()}). */
    HistoricActivityInstanceQueryWrapper orderByHistoricActivityInstanceId();

    /** Order by processInstanceId (needs to be followed by {@link #asc()} or {@link #desc()}). */
    HistoricActivityInstanceQueryWrapper orderByProcessInstanceId();

    /** Order by executionId (needs to be followed by {@link #asc()} or {@link #desc()}). */
    HistoricActivityInstanceQueryWrapper orderByExecutionId();

    /** Order by activityId (needs to be followed by {@link #asc()} or {@link #desc()}). */
    HistoricActivityInstanceQueryWrapper orderByActivityId();

    /** Order by activityName (needs to be followed by {@link #asc()} or {@link #desc()}). */
    HistoricActivityInstanceQueryWrapper orderByActivityName();

    /** Order by activityType (needs to be followed by {@link #asc()} or {@link #desc()}). */
    HistoricActivityInstanceQueryWrapper orderByActivityType();

    /** Order by start (needs to be followed by {@link #asc()} or {@link #desc()}). */
    HistoricActivityInstanceQueryWrapper orderByHistoricActivityInstanceStartTime();

    /** Order by end (needs to be followed by {@link #asc()} or {@link #desc()}). */
    HistoricActivityInstanceQueryWrapper orderByHistoricActivityInstanceEndTime();

    /** Order by duration (needs to be followed by {@link #asc()} or {@link #desc()}). */
    HistoricActivityInstanceQueryWrapper orderByHistoricActivityInstanceDuration();

    /** Order by processDefinitionId (needs to be followed by {@link #asc()} or {@link #desc()}). */
    HistoricActivityInstanceQueryWrapper orderByProcessDefinitionId();
}
