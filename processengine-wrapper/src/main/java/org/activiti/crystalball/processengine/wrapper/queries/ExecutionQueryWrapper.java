package org.activiti.crystalball.processengine.wrapper.queries;

public interface ExecutionQueryWrapper extends Query<ExecutionQueryWrapper, ExecutionWrapper> {
    /** Only select executions which have the given process definition key. **/
    ExecutionQueryWrapper processDefinitionKey(String processDefinitionKey);

    /** Only select executions which have the given process definition id. **/
    ExecutionQueryWrapper processDefinitionId(String processDefinitionId);

    /** Only select executions which have the given process instance id. **/
    ExecutionQueryWrapper processInstanceId(String processInstanceId);

    /** Only executions with the given business key */
    ExecutionQueryWrapper processInstanceBusinessKey(String processInstanceBusinessKey);

    /** Only select executions with the given id. **/
    ExecutionQueryWrapper executionId(String executionId);

    /** Only select executions which contain an activity with the given id. **/
    ExecutionQueryWrapper activityId(String activityId);

    /**
     * Only select executions which have a local variable with the given value. The type
     * of variable is determined based on the value, using types configured in
     * {@#link ProcessEngineConfiguration#getVariableTypes()}.
     * Byte-arrays and {@#link java.io.Serializable} objects (which are not primitive type wrappers)
     * are not supported.
     * @param name name of the variable, cannot be null.
     */
    ExecutionQueryWrapper variableValueEquals(String name, Object value);

    /**
     * Only select executions which have a local string variable with the given value,
     * case insensitive.
     * @param name name of the variable, cannot be null.
     * @param value value of the variable, cannot be null.
     */
    ExecutionQueryWrapper variableValueEqualsIgnoreCase(String name, String value);

    /**
     * Only select executions which have at least one local variable with the given value. The type
     * of variable is determined based on the value, using types configured in
     * {@#link ProcessEngineConfiguration#getVariableTypes()}.
     * Byte-arrays and {@#link java.io.Serializable} objects (which are not primitive type wrappers)
     * are not supported.
     */
    ExecutionQueryWrapper variableValueEquals(Object value);

    /**
     * Only select executions which have a local variable with the given name, but
     * with a different value than the passed value.
     * Byte-arrays and {@#link java.io.Serializable} objects (which are not primitive type wrappers)
     * are not supported.
     * @param name name of the variable, cannot be null.
     */
    ExecutionQueryWrapper variableValueNotEquals(String name, Object value);

    /**
     * Only select executions which have a local string variable which is not the given value,
     * case insensitive.
     * @param name name of the variable, cannot be null.
     * @param value value of the variable, cannot be null.
     */
    ExecutionQueryWrapper variableValueNotEqualsIgnoreCase(String name, String value);


    /**
     * Only select executions which have a local variable value greater than the passed value.
     * Booleans, Byte-arrays and {@#link java.io.Serializable} objects (which are not primitive type wrappers)
     * are not supported.
     * @param name variable name, cannot be null.
     * @param value variable value, cannot be null.
     */
    ExecutionQueryWrapper variableValueGreaterThan(String name, Object value);

    /**
     * Only select executions which have a local variable value greater than or equal to
     * the passed value. Booleans, Byte-arrays and {@#link java.io.Serializable} objects (which
     * are not primitive type wrappers) are not supported.
     * @param name variable name, cannot be null.
     * @param value variable value, cannot be null.
     */
    ExecutionQueryWrapper variableValueGreaterThanOrEqual(String name, Object value);

    /**
     * Only select executions which have a local variable value less than the passed value.
     * Booleans, Byte-arrays and {@#link java.io.Serializable} objects (which are not primitive type wrappers)
     * are not supported.
     * @param name variable name, cannot be null.
     * @param value variable value, cannot be null.
     */
    ExecutionQueryWrapper variableValueLessThan(String name, Object value);

    /**
     * Only select executions which have a local variable value less than or equal to the passed value.
     * Booleans, Byte-arrays and {@#link java.io.Serializable} objects (which are not primitive type wrappers)
     * are not supported.
     * @param name variable name, cannot be null.
     * @param value variable value, cannot be null.
     */
    ExecutionQueryWrapper variableValueLessThanOrEqual(String name, Object value);

    /**
     * Only select executions which have a local variable value like the given value.
     * This be used on string variables only.
     * @param name variable name, cannot be null.
     * @param value variable value, cannot be null. The string can include the
     * wildcard character '%' to express like-strategy:
     * starts with (string%), ends with (%string) or contains (%string%).
     */
    ExecutionQueryWrapper variableValueLike(String name, String value);

    /**
     * Only select executions which are part of a process that have a variable
     * with the given name set to the given value.
     * Byte-arrays and {@#link java.io.Serializable} objects (which are not primitive type wrappers)
     * are not supported.
     */
    ExecutionQueryWrapper processVariableValueEquals(String variableName, Object variableValue);

    /**
     * Only select executions which are part of a process that have at least one variable
     * with the given value.
     * Byte-arrays and {@#link java.io.Serializable} objects (which are not primitive type wrappers)
     * are not supported.
     */
    ExecutionQueryWrapper processVariableValueEquals(Object variableValue);

    /**
     * Only select executions which are part of a process that have a variable  with the given name, but
     * with a different value than the passed value.
     * Byte-arrays and {@#link java.io.Serializable} objects (which are not primitive type wrappers)
     * are not supported.
     */
    ExecutionQueryWrapper processVariableValueNotEquals(String variableName, Object variableValue);

    /**
     * Only select executions which are part of a process that have a local string variable with
     * the given value, case insensitive.
     * @param name name of the variable, cannot be null.
     * @param value value of the variable, cannot be null.
     */
    ExecutionQueryWrapper processVariableValueEqualsIgnoreCase(String name, String value);

    /**
     * Only select executions which are part of a process that have a local string variable which is not
     * the given value, case insensitive.
     * @param name name of the variable, cannot be null.
     * @param value value of the variable, cannot be null.
     */
    ExecutionQueryWrapper processVariableValueNotEqualsIgnoreCase(String name, String value);

    // event subscriptions //////////////////////////////////////////////////


    /**
     * Only select executions which have a signal event subscription
     * for the given signal name.
     *
     * (The signalName is specified using the 'name' attribute of the signal element
     * in the BPMN 2.0 XML.)
     *
     * @param signalName the name of the signal the execution has subscribed to
     */
    ExecutionQueryWrapper signalEventSubscriptionName(String signalName);

    /**
     * Only select executions which have a message event subscription
     * for the given messageName.
     *
     * (The messageName is specified using the 'name' attribute of the message element
     * in the BPMN 2.0 XML.)
     *
     * @param messageName the name of the message the execution has subscribed to
     */
    ExecutionQueryWrapper messageEventSubscriptionName(String messageName);

    //ordering //////////////////////////////////////////////////////////////

    /** Order by id (needs to be followed by {@#link #asc()} or {@#link #desc()}). */
    ExecutionQueryWrapper orderByProcessInstanceId();

    /** Order by process definition key (needs to be followed by {@#link #asc()} or {@#link #desc()}). */
    ExecutionQueryWrapper orderByProcessDefinitionKey();

    /** Order by process definition id (needs to be followed by {@#link #asc()} or {@#link #desc()}). */
    ExecutionQueryWrapper orderByProcessDefinitionId();

}
