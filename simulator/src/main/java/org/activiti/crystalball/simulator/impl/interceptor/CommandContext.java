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
package org.activiti.crystalball.simulator.impl.interceptor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.activiti.crystalball.simulator.impl.cfg.SimulationEngineConfigurationImpl;
import org.activiti.crystalball.simulator.impl.cfg.TransactionContext;
import org.activiti.crystalball.simulator.impl.db.DbSimulatorSqlSession;
import org.activiti.crystalball.simulator.impl.persistence.entity.JobManager;
import org.activiti.crystalball.simulator.impl.persistence.entity.PropertyManager;
import org.activiti.crystalball.simulator.impl.persistence.entity.ResultEntityManager;
import org.activiti.crystalball.simulator.impl.persistence.entity.SimulationInstanceEntityManager;
import org.activiti.crystalball.simulator.impl.persistence.entity.SimulationRunEntityManager;
import org.activiti.crystalball.simulator.impl.simulationexecutor.FailedJobCommandFactory;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.ActivitiTaskAlreadyClaimedException;
import org.activiti.engine.JobNotFoundException;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.ByteArrayManager;
import org.activiti.engine.impl.persistence.entity.DeploymentManager;
import org.activiti.engine.impl.persistence.entity.ModelManager;
import org.activiti.engine.impl.persistence.entity.ResourceManager;
import org.activiti.engine.impl.persistence.entity.TableDataManager;
import org.activiti.engine.impl.pvm.runtime.AtomicOperation;
import org.activiti.engine.impl.pvm.runtime.InterpretableExecution;

/**
 * @author Tom Baeyens
 * @author Agim Emruli
 */
public class CommandContext {

  private static Logger log = Logger.getLogger(CommandContext.class.getName());

  protected Command< ? > command;
  protected TransactionContext transactionContext;
  protected Map<Class< ? >, SessionFactory> sessionFactories;
  protected Map<Class< ? >, Session> sessions = new HashMap<Class< ? >, Session>();
  protected Throwable exception = null;
  protected LinkedList<AtomicOperation> nextOperations = new LinkedList<AtomicOperation>();
  protected SimulationEngineConfigurationImpl processEngineConfiguration;
  protected FailedJobCommandFactory failedJobCommandFactory;

  
  public void performOperation(AtomicOperation executionOperation, InterpretableExecution execution) {
    nextOperations.add(executionOperation);
    if (nextOperations.size()==1) {
      try {
        Context.setExecutionContext(execution);
        while (!nextOperations.isEmpty()) {
          AtomicOperation currentOperation = nextOperations.removeFirst();
          if (log.isLoggable(Level.FINEST)) {
            log.finest("AtomicOperation: " + currentOperation + " on " + this);
          }
          currentOperation.execute(execution);
        }
      } finally {
        Context.removeExecutionContext();
      }
    }
  }

  public CommandContext(Command<?> command, SimulationEngineConfigurationImpl simulationEngineConfiguration) {
    this.command = command;
    this.processEngineConfiguration = simulationEngineConfiguration;
    sessionFactories = simulationEngineConfiguration.getSessionFactories();
    this.failedJobCommandFactory = simulationEngineConfiguration.getFailedJobCommandFactory();

    this.transactionContext = simulationEngineConfiguration
      .getTransactionContextFactory()
      .openTransactionContext(this);
  }

  public void close() {
    // the intention of this method is that all resources are closed properly,
    // even
    // if exceptions occur in close or flush methods of the sessions or the
    // transaction context.

    try {
      try {
        try {

          if (exception == null) {
            flushSessions();
          }

        } catch (Throwable exception) {
          exception(exception);
        } finally {

          try {
            if (exception == null) {
              transactionContext.commit();
            }
          } catch (Throwable exception) {
            exception(exception);
          }

          if (exception != null) {
            Level loggingLevel = Level.SEVERE;
            if (exception instanceof JobNotFoundException) {
              // reduce log level, because this may have been caused because of job deletion due to cancelActiviti="true"
              loggingLevel = Level.INFO;
              
            } else if (exception instanceof ActivitiTaskAlreadyClaimedException) {
              loggingLevel = Level.INFO; // reduce log level, because this is not really a technical exception
            }

            log.log(loggingLevel, "Error while closing command context", exception);
            transactionContext.rollback();
          }
        }
      } catch (Throwable exception) {
        exception(exception);
      } finally {
        closeSessions();

      }
    } catch (Throwable exception) {
      exception(exception);
    } 

    // rethrow the original exception if there was one
    if (exception != null) {
      if (exception instanceof Error) {
        throw (Error) exception;
      } else if (exception instanceof RuntimeException) {
        throw (RuntimeException) exception;
      } else {
        throw new ActivitiException("exception while executing command " + command, exception);
      }
    }
  }
 
  protected void flushSessions() {
    for (Session session : sessions.values()) {
      session.flush();
    }
  }

  protected void closeSessions() {
    for (Session session : sessions.values()) {
      try {
        session.close();
      } catch (Throwable exception) {
        exception(exception);
      }
    }
  }

  public void exception(Throwable exception) {
    if (this.exception == null) {
      this.exception = exception;
    } else {
      log.log(Level.SEVERE, "masked exception in command context. for root cause, see below as it will be rethrown later.", exception);
    }
  }

  @SuppressWarnings({"unchecked"})
  public <T> T getSession(Class<T> sessionClass) {
    Session session = sessions.get(sessionClass);
    if (session == null) {
      SessionFactory sessionFactory = sessionFactories.get(sessionClass);
      if (sessionFactory==null) {
        throw new ActivitiException("no session factory configured for "+sessionClass.getName());
      }
      session = sessionFactory.openSession();
      sessions.put(sessionClass, session);
    }

    return (T) session;
  }
  
  public DbSimulatorSqlSession getDbSqlSession() {
    return getSession(DbSimulatorSqlSession.class);
  }
  
  public DeploymentManager getDeploymentManager() {
    return getSession(DeploymentManager.class);
  }

  public ResourceManager getResourceManager() {
    return getSession(ResourceManager.class);
  }
  
  public ByteArrayManager getByteArrayManager() {
    return getSession(ByteArrayManager.class);
  }
  
  public SimulationInstanceEntityManager getSimulationInstanceManager() {
    return getSession(SimulationInstanceEntityManager.class);
  }

  public SimulationRunEntityManager getSimulationRunManager() {
	    return getSession(SimulationRunEntityManager.class);
  }
  
  public ResultEntityManager getResultEntityManager() {
	    return getSession(ResultEntityManager.class);
  }
  
  public ModelManager getModelManager() {
    return getSession(ModelManager.class);
  }
  
  public JobManager getJobManager() {
    return getSession(JobManager.class);
  }

  public TableDataManager getTableDataManager() {
    return getSession(TableDataManager.class);
  }

  public Map<Class< ? >, SessionFactory> getSessionFactories() {
    return sessionFactories;
  }

  public PropertyManager getPropertyManager() {
    return getSession(PropertyManager.class);
  }
  
  // getters and setters //////////////////////////////////////////////////////

  public TransactionContext getTransactionContext() {
    return transactionContext;
  }
  public Command< ? > getCommand() {
    return command;
  }
  public Map<Class< ? >, Session> getSessions() {
    return sessions;
  }
  public Throwable getException() {
    return exception;
  }
  
  public FailedJobCommandFactory getFailedJobCommandFactory() {
	    return failedJobCommandFactory;
	  }

}
