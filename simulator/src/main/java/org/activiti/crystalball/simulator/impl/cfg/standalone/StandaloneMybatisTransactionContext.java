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
package org.activiti.crystalball.simulator.impl.cfg.standalone;

import org.activiti.crystalball.simulator.impl.cfg.TransactionContext;
import org.activiti.crystalball.simulator.impl.cfg.TransactionListener;
import org.activiti.crystalball.simulator.impl.cfg.TransactionState;
import org.activiti.crystalball.simulator.impl.db.DbSimulatorSqlSession;
import org.activiti.crystalball.simulator.impl.interceptor.CommandContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


/**
 * @author Tom Baeyens
 */
public class StandaloneMybatisTransactionContext implements TransactionContext {
  
  private static Logger log = Logger.getLogger(StandaloneMybatisTransactionContext.class.getName());

  protected CommandContext commandContext;
  protected Map<TransactionState,List<TransactionListener>> stateTransactionListeners = null;
  
  public StandaloneMybatisTransactionContext(CommandContext commandContext) {
    this.commandContext = commandContext;
  }

  public void addTransactionListener(TransactionState transactionState, TransactionListener transactionListener) {
    if (stateTransactionListeners==null) {
      stateTransactionListeners = new HashMap<TransactionState, List<TransactionListener>>();
    }
    List<TransactionListener> transactionListeners = stateTransactionListeners.get(transactionState);
    if (transactionListeners==null) {
      transactionListeners = new ArrayList<TransactionListener>();
      stateTransactionListeners.put(transactionState, transactionListeners);
    }
    transactionListeners.add(transactionListener);
  }
  
  public void commit() {
    log.fine("firing event committing...");
    fireTransactionEvent(TransactionState.COMMITTING);
    log.fine("committing the ibatis sql session...");
    getDbSqlSession().commit();
    log.fine("firing event committed...");
    fireTransactionEvent(TransactionState.COMMITTED);
  }

  protected void fireTransactionEvent(TransactionState transactionState) {
    if (stateTransactionListeners==null) {
      return;
    }
    List<TransactionListener> transactionListeners = stateTransactionListeners.get(transactionState);
    if (transactionListeners==null) {
      return;
    }
    for (TransactionListener transactionListener: transactionListeners) {
      transactionListener.execute(commandContext);
    }
  }

  private DbSimulatorSqlSession getDbSqlSession() {
    return commandContext.getSession(DbSimulatorSqlSession.class);
  }

  public void rollback() {
    try {
      try {
        log.fine("firing event rolling back...");
        fireTransactionEvent(TransactionState.ROLLINGBACK);
        
      } catch (Throwable exception) {
        log.info("Exception during transaction: " + exception.getMessage());
        commandContext.exception(exception);
      } finally {
        log.fine("rolling back ibatis sql session...");
        getDbSqlSession().rollback();
      }
      
    } catch (Throwable exception) {
      log.info("Exception during transaction: " + exception.getMessage());
      commandContext.exception(exception);

    } finally {
      log.fine("firing event rolled back...");
      fireTransactionEvent(TransactionState.ROLLED_BACK);
    }
  }
}
