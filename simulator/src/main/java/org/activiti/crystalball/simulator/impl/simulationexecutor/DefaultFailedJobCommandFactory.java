package org.activiti.crystalball.simulator.impl.simulationexecutor;

import org.activiti.crystalball.simulator.impl.cmd.DecrementJobRetriesCmd;
import org.activiti.crystalball.simulator.impl.interceptor.Command;



public class DefaultFailedJobCommandFactory implements FailedJobCommandFactory {

  public Command<Object> getCommand(String jobId, Throwable exception) {
    return new DecrementJobRetriesCmd(jobId, exception);
  }

}
