package org.activiti.crystalball.processengine.wrapper.queries;


public interface ExecutionWrapper {

    /**
     * The unique identifier of the execution.
     */
    String getId();

    /**
     * Indicates if the execution is suspended.
     */
    boolean isSuspended();

    /**
     * Indicates if the execution is ended.
     */
    boolean isEnded();

    /** Id of the root of the execution tree representing the process instance.
     * It is the same as {@link #getId()} if this execution is the process instance. */
    String getProcessInstanceId();
}
