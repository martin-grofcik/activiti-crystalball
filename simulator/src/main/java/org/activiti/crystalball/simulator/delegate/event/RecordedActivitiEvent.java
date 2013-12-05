package org.activiti.crystalball.simulator.delegate.event;

import org.activiti.engine.delegate.event.ActivitiEvent;

public interface RecordedActivitiEvent {

  /**
   * time when event has occurred.
   * in milliseconds. 0 is January 1, 1970, 00:00:00 GMT
   */
  long getTime();

  /**
   *
   * @return event which has occured
   */
  ActivitiEvent getActivitiEvent();
}
