/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.crystalball.simulator.impl.context;

import java.util.Stack;

import org.activiti.crystalball.simulator.impl.cfg.SimulationEngineConfigurationImpl;
import org.activiti.crystalball.simulator.impl.interceptor.CommandContext;
import org.activiti.crystalball.simulator.impl.persistence.entity.SimulationRunEntity;
import org.activiti.crystalball.simulator.impl.simulationexecutor.JobExecutorContext;
import org.activiti.engine.impl.pvm.runtime.InterpretableExecution;


/**
 * @author Tom Baeyens
 * @author Daniel Meyer
 */
public class SimulationContext {

  protected static ThreadLocal<Stack<CommandContext>> commandContextThreadLocal = new ThreadLocal<Stack<CommandContext>>();
  protected static ThreadLocal<Stack<SimulationEngineConfigurationImpl>> simulationEngineConfigurationStackThreadLocal = new ThreadLocal<Stack<SimulationEngineConfigurationImpl>>();
  protected static ThreadLocal<Stack<ExecutionContext>> executionContextStackThreadLocal = new ThreadLocal<Stack<ExecutionContext>>();
  protected static ThreadLocal<JobExecutorContext> jobExecutorContextThreadLocal = new ThreadLocal<JobExecutorContext>();
  protected static ThreadLocal<SimulationRunEntity> simulationRunThreadLocal = new ThreadLocal<SimulationRunEntity>();

  public static CommandContext getCommandContext() {
    Stack<CommandContext> stack = getStack(commandContextThreadLocal);
    if (stack.isEmpty()) {
      return null;
    }
    return stack.peek();
  }

  public static void setCommandContext(CommandContext commandContext) {
    getStack(commandContextThreadLocal).push(commandContext);
  }

  public static void removeCommandContext() {
    getStack(commandContextThreadLocal).pop();
  }

  public static SimulationEngineConfigurationImpl getSimulationEngineConfiguration() {
    Stack<SimulationEngineConfigurationImpl> stack = getStack(simulationEngineConfigurationStackThreadLocal);
    if (stack.isEmpty()) {
      return null;
    }
    return stack.peek();
  }

  public static void setSimulationEngineConfiguration(SimulationEngineConfigurationImpl simulationEngineConfiguration) {
    getStack(simulationEngineConfigurationStackThreadLocal).push(simulationEngineConfiguration);
  }

  public static void removeSimulationEngineConfiguration() {
    getStack(simulationEngineConfigurationStackThreadLocal).pop();
  }

  public static ExecutionContext getExecutionContext() {
    return getStack(executionContextStackThreadLocal).peek();
  }

  public static void setExecutionContext(InterpretableExecution execution) {
    getStack(executionContextStackThreadLocal).push(new ExecutionContext(execution));
  }

  public static void removeExecutionContext() {
    getStack(executionContextStackThreadLocal).pop();
  }

  protected static <T> Stack<T> getStack(ThreadLocal<Stack<T>> threadLocal) {
    Stack<T> stack = threadLocal.get();
    if (stack==null) {
      stack = new Stack<T>();
      threadLocal.set(stack);
    }
    return stack;
  }

	public static JobExecutorContext getJobExecutorContext() {
		return jobExecutorContextThreadLocal.get();
	}

	public static void setJobExecutorContext(
			JobExecutorContext jobExecutorContext) {
		jobExecutorContextThreadLocal.set(jobExecutorContext);
	}

	public static void removeJobExecutorContext() {
		jobExecutorContextThreadLocal.remove();
	}
	public static SimulationRunEntity getSimulationRun() {
		return simulationRunThreadLocal.get();
	}

	public static void setSimulationRun(SimulationRunEntity simulationRun) {
		simulationRunThreadLocal.set(simulationRun);
	}

	public static void removeSimulationRun() {
		simulationRunThreadLocal.remove();
	}
}

