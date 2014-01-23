package org.activiti.crystalball.simulator.impl;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.impl.ProcessEngineImpl;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.util.ClockUtil;

import java.util.*;

public final class EventRecorderTestUtils {



  protected EventRecorderTestUtils() {
    // static class
  }

  /**
   * increase clockUtils time
   * Simulation operates in milliseconds. Sometime could happen (especially during automated tests)
   * that 2 recorded events have the same time. In that case it is not possible to recognize order.
   */
  public static void increaseTime() {
    Calendar c = Calendar.getInstance();
    c.setTime(ClockUtil.getCurrentTime());
    c.add(Calendar.SECOND, 1);
    ClockUtil.setCurrentTime(c.getTime());
  }

  public static void closeProcessEngine(ProcessEngine processEngine, ActivitiEventListener listener) {
    if (listener != null) {
      final ProcessEngineConfigurationImpl processEngineConfiguration = ((ProcessEngineImpl) processEngine).getProcessEngineConfiguration();
      processEngineConfiguration.getEventDispatcher().removeEventListener(listener);
    }
    processEngine.close();
  }


}
