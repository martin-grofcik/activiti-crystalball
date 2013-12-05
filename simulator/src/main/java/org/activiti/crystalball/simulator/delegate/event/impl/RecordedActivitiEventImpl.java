package org.activiti.crystalball.simulator.delegate.event.impl;

import org.activiti.crystalball.simulator.delegate.event.RecordedActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.impl.util.ClockUtil;


public class RecordedActivitiEventImpl implements RecordedActivitiEvent {
  protected ActivitiEvent activitiEvent;
  protected long time;

  public RecordedActivitiEventImpl(ActivitiEvent event) {
    if (event == null)
      throw new IllegalArgumentException("event can not be null");
    activitiEvent = event;
    time = ClockUtil.getCurrentTime().getTime();
  }

  @Override
  public long getTime() {
    return time;
  }

  @Override
  public ActivitiEvent getActivitiEvent() {
    return activitiEvent;
  }
}
