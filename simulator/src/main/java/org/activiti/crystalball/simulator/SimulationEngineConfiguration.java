
package org.activiti.crystalball.simulator;

import java.io.InputStream;

import javax.sql.DataSource;

import org.activiti.crystalball.simulator.impl.cfg.BeansConfigurationHelper;
import org.activiti.crystalball.simulator.impl.cfg.StandaloneInMemSimulationEngineConfiguration;
import org.activiti.crystalball.simulator.impl.cfg.StandaloneSimulationEngineConfiguration;
import org.activiti.engine.ProcessEngines;


/** Configuration information from which a process engine can be build.
 * 
 * <p>Most common is to create a process engine based on the default configuration file:
 * <pre>ProcessEngine processEngine = ProcessEngineConfiguration
 *   .createProcessEngineConfigurationFromResourceDefault()
 *   .buildProcessEngine();
 * </pre>
 * </p>
 * 
 * <p>To create a process engine programatic, without a configuration file, 
 * the first option is {@link #createStandaloneProcessEngineConfiguration()}
 * <pre>ProcessEngine processEngine = ProcessEngineConfiguration
 *   .createStandaloneProcessEngineConfiguration()
 *   .buildProcessEngine();
 * </pre>
 * This creates a new process engine with all the defaults to connect to 
 * a remote h2 database (jdbc:h2:tcp://localhost/activiti) in standalone 
 * mode.  Standalone mode means that Activiti will manage the transactions 
 * on the JDBC connections that it creates.  One transaction per 
 * service method.
 * For a description of how to write the configuration files, see the 
 * userguide.
 * </p>
 * 
 * <p>The second option is great for testing: {@link #createStandalonInMemeProcessEngineConfiguration()}
 * <pre>ProcessEngine processEngine = ProcessEngineConfiguration
 *   .createStandaloneInMemProcessEngineConfiguration()
 *   .buildProcessEngine();
 * </pre>
 * This creates a new process engine with all the defaults to connect to 
 * an memory h2 database (jdbc:h2:tcp://localhost/activiti) in standalone 
 * mode.  The DB schema strategy default is in this case <code>create-drop</code>.  
 * Standalone mode means that Activiti will manage the transactions 
 * on the JDBC connections that it creates.  One transaction per 
 * service method.
 * </p>
 * 
 * <p>On all forms of creating a process engine, you can first customize the configuration 
 * before calling the {@link #buildSimulationEngine()} method by calling any of the 
 * setters like this:
 * <pre>ProcessEngine processEngine = ProcessEngineConfiguration
 *   .createProcessEngineConfigurationFromResourceDefault()
 *   .setMailServerHost("gmail.com")
 *   .setJdbcUsername("mickey")
 *   .setJdbcPassword("mouse")
 *   .buildProcessEngine();
 * </pre>
 * </p>
 * 
 * @see ProcessEngines 
 * @author Tom Baeyens
 */
public abstract class SimulationEngineConfiguration implements EngineServices {
  
  /** Checks the version of the DB schema against the library when 
   * the process engine is being created and throws an exception
   * if the versions don't match. */
  public static final String DB_SCHEMA_UPDATE_FALSE = "false";
  
  /** Creates the schema when the process engine is being created and 
   * drops the schema when the process engine is being closed. */
  public static final String DB_SCHEMA_UPDATE_CREATE_DROP = "create-drop";

  /** Upon building of the process engine, a check is performed and 
   * an update of the schema is performed if it is necessary. */
  public static final String DB_SCHEMA_UPDATE_TRUE = "true";

  protected String simulationEngineName = "default";
  protected int idBlockSize = 100;
  protected boolean jobExecutorActivate = true;
  
  protected String databaseType;
  protected String databaseSchemaUpdate = DB_SCHEMA_UPDATE_FALSE;
  protected String jdbcDriver = "org.h2.Driver";
//  protected String jdbcUrl = "jdbc:h2:tcp://localhost/crystalball";
  protected String jdbcUrl = "jdbc:h2:target/crystalball";
  protected String jdbcUsername = "sa";
  protected String jdbcPassword = "";
  protected String dataSourceJndiName = null;
  protected int jdbcMaxActiveConnections;
  protected int jdbcMaxIdleConnections;
  protected int jdbcMaxCheckoutTime;
  protected int jdbcMaxWaitTime;
  protected boolean jdbcPingEnabled = false;
  protected String jdbcPingQuery = null;
  protected int jdbcPingConnectionNotUsedFor;
  protected DataSource dataSource;
  protected boolean transactionsExternallyManaged = false;
  
  protected String jpaPersistenceUnitName;
  protected Object jpaEntityManagerFactory;
  protected boolean jpaHandleTransaction;
  protected boolean jpaCloseEntityManager;
  
  protected ClassLoader classLoader;

  /** use one of the static createXxxx methods instead */
  protected SimulationEngineConfiguration() {
  }

  public abstract SimulationEngine buildSimulationEngine();
  
  public static SimulationEngineConfiguration createSimulationEngineConfigurationFromResourceDefault() {
    return createSimulationEngineConfigurationFromResource("crystalball.cfg.xml", "simulationEngineConfiguration");
  }

  public static SimulationEngineConfiguration createSimulationEngineConfigurationFromResource(String resource) {
    return createSimulationEngineConfigurationFromResource(resource, "simulationEngineConfiguration");
  }

  public static SimulationEngineConfiguration createSimulationEngineConfigurationFromResource(String resource, String beanName) {
    return BeansConfigurationHelper.parseSimulationEngineConfigurationFromResource(resource, beanName);
  }
  
  public static SimulationEngineConfiguration createSimulationEngineConfigurationFromInputStream(InputStream inputStream) {
    return createSimulationEngineConfigurationFromInputStream(inputStream, "simulationEngineConfiguration");
  }

  public static SimulationEngineConfiguration createSimulationEngineConfigurationFromInputStream(InputStream inputStream, String beanName) {
    return BeansConfigurationHelper.parseSimulationEngineConfigurationFromInputStream(inputStream, beanName);
  }

  public static SimulationEngineConfiguration createStandaloneSimulationEngineConfiguration() {
    return new StandaloneSimulationEngineConfiguration();
  }

  public static SimulationEngineConfiguration createStandaloneInMemSimulationEngineConfiguration() {
    return new StandaloneInMemSimulationEngineConfiguration();
  }

// TODO add later when we have test coverage for this
//  public static ProcessEngineConfiguration createJtaProcessEngineConfiguration() {
//    return new JtaProcessEngineConfiguration();
//  }
  

  // getters and setters //////////////////////////////////////////////////////
  
  public String getSimulationEngineName() {
    return simulationEngineName;
  }

  public SimulationEngineConfiguration setSimulationEngineName(String simulationEngineName) {
    this.simulationEngineName = simulationEngineName;
    return this;
  }

  
  public int getIdBlockSize() {
    return idBlockSize;
  }

  
  public SimulationEngineConfiguration setIdBlockSize(int idBlockSize) {
    this.idBlockSize = idBlockSize;
    return this;
  }
    
  public String getDatabaseType() {
    return databaseType;
  }

  
  public SimulationEngineConfiguration setDatabaseType(String databaseType) {
    this.databaseType = databaseType;
    return this;
  }

  
  public String getDatabaseSchemaUpdate() {
    return databaseSchemaUpdate;
  }

  
  public SimulationEngineConfiguration setDatabaseSchemaUpdate(String databaseSchemaUpdate) {
    this.databaseSchemaUpdate = databaseSchemaUpdate;
    return this;
  }

  
  public DataSource getDataSource() {
    return dataSource;
  }

  
  public SimulationEngineConfiguration setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
    return this;
  }

  
  public String getJdbcDriver() {
    return jdbcDriver;
  }

  
  public SimulationEngineConfiguration setJdbcDriver(String jdbcDriver) {
    this.jdbcDriver = jdbcDriver;
    return this;
  }

  
  public String getJdbcUrl() {
    return jdbcUrl;
  }

  
  public SimulationEngineConfiguration setJdbcUrl(String jdbcUrl) {
    this.jdbcUrl = jdbcUrl;
    return this;
  }

  
  public String getJdbcUsername() {
    return jdbcUsername;
  }

  
  public SimulationEngineConfiguration setJdbcUsername(String jdbcUsername) {
    this.jdbcUsername = jdbcUsername;
    return this;
  }

  
  public String getJdbcPassword() {
    return jdbcPassword;
  }

  
  public SimulationEngineConfiguration setJdbcPassword(String jdbcPassword) {
    this.jdbcPassword = jdbcPassword;
    return this;
  }

  
  public boolean isTransactionsExternallyManaged() {
    return transactionsExternallyManaged;
  }

  
  public SimulationEngineConfiguration setTransactionsExternallyManaged(boolean transactionsExternallyManaged) {
    this.transactionsExternallyManaged = transactionsExternallyManaged;
    return this;
  }

  
  public int getJdbcMaxActiveConnections() {
    return jdbcMaxActiveConnections;
  }

  
  public SimulationEngineConfiguration setJdbcMaxActiveConnections(int jdbcMaxActiveConnections) {
    this.jdbcMaxActiveConnections = jdbcMaxActiveConnections;
    return this;
  }

  
  public int getJdbcMaxIdleConnections() {
    return jdbcMaxIdleConnections;
  }

  
  public SimulationEngineConfiguration setJdbcMaxIdleConnections(int jdbcMaxIdleConnections) {
    this.jdbcMaxIdleConnections = jdbcMaxIdleConnections;
    return this;
  }

  
  public int getJdbcMaxCheckoutTime() {
    return jdbcMaxCheckoutTime;
  }

  
  public SimulationEngineConfiguration setJdbcMaxCheckoutTime(int jdbcMaxCheckoutTime) {
    this.jdbcMaxCheckoutTime = jdbcMaxCheckoutTime;
    return this;
  }

  
  public int getJdbcMaxWaitTime() {
    return jdbcMaxWaitTime;
  }
  
  public SimulationEngineConfiguration setJdbcMaxWaitTime(int jdbcMaxWaitTime) {
    this.jdbcMaxWaitTime = jdbcMaxWaitTime;
    return this;
  }
  
  public boolean isJdbcPingEnabled() {
    return jdbcPingEnabled;
  }

  public SimulationEngineConfiguration setJdbcPingEnabled(boolean jdbcPingEnabled) {
    this.jdbcPingEnabled = jdbcPingEnabled;
    return this;
  }

  public String getJdbcPingQuery() {
      return jdbcPingQuery;
  }

  public SimulationEngineConfiguration setJdbcPingQuery(String jdbcPingQuery) {
    this.jdbcPingQuery = jdbcPingQuery;
    return this;
  }

  public int getJdbcPingConnectionNotUsedFor() {
      return jdbcPingConnectionNotUsedFor;
  }

  public SimulationEngineConfiguration setJdbcPingConnectionNotUsedFor(int jdbcPingNotUsedFor) {
    this.jdbcPingConnectionNotUsedFor = jdbcPingNotUsedFor;
    return this;
  }
  
  public ClassLoader getClassLoader() {
    return classLoader;
  }
  
  public SimulationEngineConfiguration setClassLoader(ClassLoader classLoader) {
    this.classLoader = classLoader;
    return this;
  }

  
  public Object getJpaEntityManagerFactory() {
    return jpaEntityManagerFactory;
  }

  
  public SimulationEngineConfiguration setJpaEntityManagerFactory(Object jpaEntityManagerFactory) {
    this.jpaEntityManagerFactory = jpaEntityManagerFactory;
    return this;
  }

  
  public boolean isJpaHandleTransaction() {
    return jpaHandleTransaction;
  }

  
  public SimulationEngineConfiguration setJpaHandleTransaction(boolean jpaHandleTransaction) {
    this.jpaHandleTransaction = jpaHandleTransaction;
    return this;
  }

  
  public boolean isJpaCloseEntityManager() {
    return jpaCloseEntityManager;
  }

  
  public SimulationEngineConfiguration setJpaCloseEntityManager(boolean jpaCloseEntityManager) {
    this.jpaCloseEntityManager = jpaCloseEntityManager;
    return this;
  }

  public String getJpaPersistenceUnitName() {
    return jpaPersistenceUnitName;
  }

  public void setJpaPersistenceUnitName(String jpaPersistenceUnitName) {
    this.jpaPersistenceUnitName = jpaPersistenceUnitName;
  }

  public String getDataSourceJndiName() {
    return dataSourceJndiName;
  }

  public void setDataSourceJndiName(String dataSourceJndiName) {
    this.dataSourceJndiName = dataSourceJndiName;
  }
  
  public boolean isJobExecutorActivate() {
	    return jobExecutorActivate;
	  }

	  
  public SimulationEngineConfiguration setJobExecutorActivate(boolean jobExecutorActivate) {
    this.jobExecutorActivate = jobExecutorActivate;
    return this;
  }


}
