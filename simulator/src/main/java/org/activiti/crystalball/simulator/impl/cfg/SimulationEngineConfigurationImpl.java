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

package org.activiti.crystalball.simulator.impl.cfg;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.activiti.crystalball.simulator.RuntimeService;
import org.activiti.crystalball.simulator.SimulationEngine;
import org.activiti.crystalball.simulator.SimulationEngineConfiguration;
import org.activiti.crystalball.simulator.executor.impl.ServiceImpl;
import org.activiti.crystalball.simulator.impl.RuntimeServiceImpl;
import org.activiti.crystalball.simulator.impl.SimulationEngineImpl;
import org.activiti.crystalball.simulator.impl.cfg.standalone.StandaloneMybatisTransactionContextFactory;
import org.activiti.crystalball.simulator.impl.db.DbIdGenerator;
import org.activiti.crystalball.simulator.impl.db.DbSimulatorSqlSessionFactory;
import org.activiti.crystalball.simulator.impl.db.IbatisVariableTypeHandler;
import org.activiti.crystalball.simulator.impl.interceptor.CommandContextFactory;
import org.activiti.crystalball.simulator.impl.interceptor.CommandExecutor;
import org.activiti.crystalball.simulator.impl.interceptor.CommandExecutorImpl;
import org.activiti.crystalball.simulator.impl.interceptor.CommandInterceptor;
import org.activiti.crystalball.simulator.impl.persistence.entity.JobManager;
import org.activiti.crystalball.simulator.impl.persistence.entity.PropertyManager;
import org.activiti.crystalball.simulator.impl.persistence.entity.ResultEntityManager;
import org.activiti.crystalball.simulator.impl.persistence.entity.SimulationInstanceEntityManager;
import org.activiti.crystalball.simulator.impl.persistence.entity.SimulationRunEntityManager;
import org.activiti.crystalball.simulator.impl.persistence.entity.VariableInstanceManager;
import org.activiti.crystalball.simulator.impl.simulationexecutor.CallerRunsRejectedJobsHandler;
import org.activiti.crystalball.simulator.impl.simulationexecutor.DefaultFailedJobCommandFactory;
import org.activiti.crystalball.simulator.impl.simulationexecutor.DefaultJobExecutor;
import org.activiti.crystalball.simulator.impl.simulationexecutor.FailedJobCommandFactory;
import org.activiti.crystalball.simulator.impl.simulationexecutor.JobExecutor;
import org.activiti.crystalball.simulator.impl.simulationexecutor.JobHandler;
import org.activiti.crystalball.simulator.impl.simulationexecutor.RejectedJobsHandler;
import org.activiti.crystalball.simulator.impl.simulationexecutor.SimulationRunExecuteJobHandler;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.bpmn.data.ItemInstance;
import org.activiti.engine.impl.bpmn.webservice.MessageInstance;
import org.activiti.engine.impl.cfg.IdGenerator;
import org.activiti.engine.impl.cfg.JpaHelper;
import org.activiti.engine.impl.delegate.DefaultDelegateInterceptor;
import org.activiti.engine.impl.el.ExpressionManager;
import org.activiti.engine.impl.event.CompensationEventHandler;
import org.activiti.engine.impl.event.EventHandler;
import org.activiti.engine.impl.event.MessageEventHandler;
import org.activiti.engine.impl.event.SignalEventHandler;
import org.activiti.engine.impl.interceptor.DelegateInterceptor;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.GenericManagerFactory;
import org.activiti.engine.impl.scripting.BeansResolverFactory;
import org.activiti.engine.impl.scripting.ResolverFactory;
import org.activiti.engine.impl.scripting.ScriptBindingsFactory;
import org.activiti.engine.impl.scripting.ScriptingEngines;
import org.activiti.engine.impl.scripting.VariableScopeResolverFactory;
import org.activiti.engine.impl.util.IoUtil;
import org.activiti.engine.impl.util.ReflectUtil;
import org.activiti.engine.impl.variable.BooleanType;
import org.activiti.engine.impl.variable.ByteArrayType;
import org.activiti.engine.impl.variable.CustomObjectType;
import org.activiti.engine.impl.variable.DateType;
import org.activiti.engine.impl.variable.DefaultVariableTypes;
import org.activiti.engine.impl.variable.DoubleType;
import org.activiti.engine.impl.variable.EntityManagerSession;
import org.activiti.engine.impl.variable.EntityManagerSessionFactory;
import org.activiti.engine.impl.variable.IntegerType;
import org.activiti.engine.impl.variable.JPAEntityVariableType;
import org.activiti.engine.impl.variable.LongType;
import org.activiti.engine.impl.variable.NullType;
import org.activiti.engine.impl.variable.SerializableType;
import org.activiti.engine.impl.variable.ShortType;
import org.activiti.engine.impl.variable.StringType;
import org.activiti.engine.impl.variable.VariableType;
import org.activiti.engine.impl.variable.VariableTypes;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;
import org.apache.ibatis.type.JdbcType;


public abstract class SimulationEngineConfigurationImpl extends SimulationEngineConfiguration {  

  private static Logger log = Logger.getLogger(SimulationEngineConfigurationImpl.class.getName());
  
  public static final String DB_SCHEMA_UPDATE_CREATE = "create";
  public static final String DB_SCHEMA_UPDATE_DROP_CREATE = "drop-create";

  public static final String DEFAULT_WS_SYNC_FACTORY = "org.activiti.engine.impl.webservice.CxfWebServiceClientFactory";
  
  public static final String DEFAULT_MYBATIS_MAPPING_FILE = "org/activiti/crystalball/db/mapping/mappings.xml";
  
  // SERVICES /////////////////////////////////////////////////////////////////

  protected RuntimeService runtimeService = new RuntimeServiceImpl();

  // COMMAND EXECUTORS ////////////////////////////////////////////////////////
  
  // Command executor and interceptor stack
  /** the configurable list which will be {@link #initInterceptorChain(java.util.List) processed} to build the {@link #commandExecutorTxRequired} */
  protected List<CommandInterceptor> customPreCommandInterceptorsTxRequired;
  protected List<CommandInterceptor> customPostCommandInterceptorsTxRequired;
  
  protected List<CommandInterceptor> commandInterceptorsTxRequired;

  /** this will be initialized during the configurationComplete() */
  protected CommandExecutor commandExecutorTxRequired;
  
  /** the configurable list which will be {@link #initInterceptorChain(List) processed} to build the {@link #commandExecutorTxRequiresNew} */
  protected List<CommandInterceptor> customPreCommandInterceptorsTxRequiresNew;
  protected List<CommandInterceptor> customPostCommandInterceptorsTxRequiresNew;

  protected List<CommandInterceptor> commandInterceptorsTxRequiresNew;

  /** this will be initialized during the configurationComplete() */
  protected CommandExecutor commandExecutorTxRequiresNew;
  
  // SESSION FACTORIES ////////////////////////////////////////////////////////

  protected List<SessionFactory> customSessionFactories;
  protected DbSimulatorSqlSessionFactory dbSqlSessionFactory;
  protected Map<Class<?>, SessionFactory> sessionFactories;
  
  // SIMULATION EXECUTOR /////////////////////////////////////////////////////////////
  
  protected List<JobHandler> customJobHandlers;
  protected Map<String, JobHandler> jobHandlers;
  protected JobExecutor jobExecutor;
  
  // MYBATIS SQL SESSION FACTORY //////////////////////////////////////////////
  
  protected SqlSessionFactory sqlSessionFactory;
  protected TransactionFactory transactionFactory;


  // ID GENERATOR /////////////////////////////////////////////////////////////
  protected IdGenerator idGenerator;
  protected DataSource idGeneratorDataSource;
  protected String idGeneratorDataSourceJndiName;
  
  // OTHER ////////////////////////////////////////////////////////////////////
  
  protected List<VariableType> customPreVariableTypes;
  protected List<VariableType> customPostVariableTypes;
  protected VariableTypes variableTypes;
  
  protected ExpressionManager expressionManager;
  protected List<String> customScriptingEngineClasses;
  protected ScriptingEngines scriptingEngines;
  protected List<ResolverFactory> resolverFactories;
  
  protected String wsSyncFactoryClassName = DEFAULT_WS_SYNC_FACTORY;

  protected CommandContextFactory commandContextFactory;
  protected TransactionContextFactory transactionContextFactory;

  protected Map<Object, Object> beans;

  protected boolean isDbIdentityUsed = true;
  protected boolean isDbHistoryUsed = true;
  
  protected DelegateInterceptor delegateInterceptor;
  
  protected RejectedJobsHandler customRejectedJobsHandler;

  protected CommandInterceptor actualCommandExecutor;
    
  protected Map<String, EventHandler> eventHandlers;
  protected List<EventHandler> customEventHandlers;

  protected FailedJobCommandFactory failedJobCommandFactory;

  protected String databaseTablePrefix = "";
  
  /**
   * The following settings will determine the amount of entities loaded at once when the engine 
   * needs to load multiple entities (eg. when suspending a process definition with all its process instances).
   * 
   * The default setting is quite low, as not to surprise anyone with sudden memory spikes.
   * Change it to something higher if the environment Activiti runs in allows it.
   */
  protected int batchSizeProcessInstances = 25;
  protected int batchSizeTasks = 25;
  
  /**
   * In some situations you want to set the schema to use for table checks / generation if the database metadata
   * doesn't return that correctly, see https://jira.codehaus.org/browse/ACT-1220,
   * https://jira.codehaus.org/browse/ACT-1062
   */
  protected String databaseSchema = null;
  
  // buildProcessEngine ///////////////////////////////////////////////////////
  
  public SimulationEngine buildSimulationEngine() {
    init();
    databaseSchemaUpdate = DB_SCHEMA_UPDATE_CREATE;
    return new SimulationEngineImpl(this);
  }
  
  // init /////////////////////////////////////////////////////////////////////
  
  protected void init() {
    initExpressionManager();
    initVariableTypes();
    initBeans();
    initScriptingEngines();
    initCommandContextFactory();
    initTransactionContextFactory();
    initCommandExecutors();
    initServices();
    initIdGenerator();
    initJobExecutor();
    initDataSource();
    initTransactionFactory();
    initSqlSessionFactory();
    initSessionFactories();
    initJpa();
    initDelegateInterceptor();
    initFailedJobCommandFactory();
  }

  // failedJobCommandFactory ////////////////////////////////////////////////////////
  
  protected void initFailedJobCommandFactory() {
    if (failedJobCommandFactory == null) {
      failedJobCommandFactory = new DefaultFailedJobCommandFactory();
    }
  }

  // command executors ////////////////////////////////////////////////////////
  
  protected abstract Collection< ? extends CommandInterceptor> getDefaultCommandInterceptorsTxRequired();
  protected abstract Collection< ? extends CommandInterceptor> getDefaultCommandInterceptorsTxRequiresNew();
  
  protected void initCommandExecutors() {
    initActualCommandExecutor();
    initCommandInterceptorsTxRequired();
    initCommandExecutorTxRequired();
    initCommandInterceptorsTxRequiresNew();
    initCommandExecutorTxRequiresNew();
  }

  protected void initActualCommandExecutor() {
    actualCommandExecutor = new CommandExecutorImpl();
  }

  protected void initCommandInterceptorsTxRequired() {
    if (commandInterceptorsTxRequired==null) {
      if (customPreCommandInterceptorsTxRequired!=null) {
        commandInterceptorsTxRequired = new ArrayList<CommandInterceptor>(customPreCommandInterceptorsTxRequired);
      } else {
        commandInterceptorsTxRequired = new ArrayList<CommandInterceptor>();
      }
      commandInterceptorsTxRequired.addAll(getDefaultCommandInterceptorsTxRequired());
      if (customPostCommandInterceptorsTxRequired!=null) {
        commandInterceptorsTxRequired.addAll(customPostCommandInterceptorsTxRequired);
      }
      commandInterceptorsTxRequired.add(actualCommandExecutor);
    }
  }

  protected void initCommandInterceptorsTxRequiresNew() {
    if (commandInterceptorsTxRequiresNew==null) {
      if (customPreCommandInterceptorsTxRequiresNew!=null) {
        commandInterceptorsTxRequiresNew = new ArrayList<CommandInterceptor>(customPreCommandInterceptorsTxRequiresNew);
      } else {
        commandInterceptorsTxRequiresNew = new ArrayList<CommandInterceptor>();
      }
      commandInterceptorsTxRequiresNew.addAll(getDefaultCommandInterceptorsTxRequiresNew());
      if (customPostCommandInterceptorsTxRequiresNew!=null) {
        commandInterceptorsTxRequiresNew.addAll(customPostCommandInterceptorsTxRequiresNew);
      }
      commandInterceptorsTxRequiresNew.add(actualCommandExecutor);
    }
  }

  protected void initCommandExecutorTxRequired() {
    if (commandExecutorTxRequired==null) {
      commandExecutorTxRequired = initInterceptorChain(commandInterceptorsTxRequired);
    }
  }

  protected void initCommandExecutorTxRequiresNew() {
    if (commandExecutorTxRequiresNew==null) {
      commandExecutorTxRequiresNew = initInterceptorChain(commandInterceptorsTxRequiresNew);
    }
  }

  protected CommandInterceptor initInterceptorChain(List<CommandInterceptor> chain) {
    if (chain==null || chain.isEmpty()) {
      throw new ActivitiException("invalid command interceptor chain configuration: "+chain);
    }
    for (int i = 0; i < chain.size()-1; i++) {
      chain.get(i).setNext( chain.get(i+1) );
    }
    return chain.get(0);
  }
  // services /////////////////////////////////////////////////////////////////
  
  protected void initServices() {
    initService(runtimeService);
  }

  protected void initService(Object service) {
    if (service instanceof ServiceImpl) {
      ((ServiceImpl)service).setCommandExecutor(commandExecutorTxRequired);
    }
  }

  // job executor /////////////////////////////////////////////////////////////
  
  protected void initJobExecutor() {
    if (jobExecutor==null) {
      jobExecutor = new DefaultJobExecutor();
    }

    jobHandlers = new HashMap<String, JobHandler>();
    SimulationRunExecuteJobHandler simulationexecuteJobHandler = new SimulationRunExecuteJobHandler();
    jobHandlers.put(simulationexecuteJobHandler.getType(), simulationexecuteJobHandler);
    
    // if we have custom job handlers, register them
    if (getCustomJobHandlers()!=null) {
      for (JobHandler customJobHandler : getCustomJobHandlers()) {
        jobHandlers.put(customJobHandler.getType(), customJobHandler);      
      }
    }

    jobExecutor.setCommandExecutor(commandExecutorTxRequired);
    jobExecutor.setAutoActivate(jobExecutorActivate);
    
    if(jobExecutor.getRejectedJobsHandler() == null) {
      if(customRejectedJobsHandler != null) {
        jobExecutor.setRejectedJobsHandler(customRejectedJobsHandler);
      } else {
        jobExecutor.setRejectedJobsHandler(new CallerRunsRejectedJobsHandler());
      }
    }
    
  }
 
  // DataSource ///////////////////////////////////////////////////////////////
  
  protected void initDataSource() {
    if (dataSource==null) {
      if (dataSourceJndiName!=null) {
        try {
          dataSource = (DataSource) new InitialContext().lookup(dataSourceJndiName);
        } catch (Exception e) {
          throw new ActivitiException("couldn't lookup datasource from "+dataSourceJndiName+": "+e.getMessage(), e);
        }
        
      } else if (jdbcUrl!=null) {
        if ( (jdbcDriver==null) || (jdbcUrl==null) || (jdbcUsername==null) ) {
          throw new ActivitiException("DataSource or JDBC properties have to be specified in a process engine configuration");
        }
        
        log.fine("initializing datasource to db: "+jdbcUrl);
        
        PooledDataSource pooledDataSource = 
          new PooledDataSource(ReflectUtil.getClassLoader(), jdbcDriver, jdbcUrl, jdbcUsername, jdbcPassword );
        
        if (jdbcMaxActiveConnections > 0) {
          pooledDataSource.setPoolMaximumActiveConnections(jdbcMaxActiveConnections);
        }
        if (jdbcMaxIdleConnections > 0) {
          pooledDataSource.setPoolMaximumIdleConnections(jdbcMaxIdleConnections);
        }
        if (jdbcMaxCheckoutTime > 0) {
          pooledDataSource.setPoolMaximumCheckoutTime(jdbcMaxCheckoutTime);
        }
        if (jdbcMaxWaitTime > 0) {
          pooledDataSource.setPoolTimeToWait(jdbcMaxWaitTime);
        }
        if (jdbcPingEnabled == true) {
          pooledDataSource.setPoolPingEnabled(true);
          if (jdbcPingQuery != null) {
            pooledDataSource.setPoolPingQuery(jdbcPingQuery);
          }
          pooledDataSource.setPoolPingConnectionsNotUsedFor(jdbcPingConnectionNotUsedFor);
        }        
        dataSource = pooledDataSource;
      }
      
      if (dataSource instanceof PooledDataSource) {
        // ACT-233: connection pool of Ibatis is not properely initialized if this is not called!
        ((PooledDataSource)dataSource).forceCloseAll();
      }
    }

    if (databaseType == null) {
      initDatabaseType();
    }
  }
  
  protected static Properties databaseTypeMappings = getDefaultDatabaseTypeMappings();
  
  protected static Properties getDefaultDatabaseTypeMappings() {
    Properties databaseTypeMappings = new Properties();
    databaseTypeMappings.setProperty("H2","h2");
    databaseTypeMappings.setProperty("MySQL","mysql");
    databaseTypeMappings.setProperty("Oracle","oracle");
    databaseTypeMappings.setProperty("PostgreSQL","postgres");
    databaseTypeMappings.setProperty("Microsoft SQL Server","mssql");
    databaseTypeMappings.setProperty("DB2","db2");
    databaseTypeMappings.setProperty("DB2","db2");
    databaseTypeMappings.setProperty("DB2/NT","db2");
    databaseTypeMappings.setProperty("DB2/NT64","db2");
    databaseTypeMappings.setProperty("DB2 UDP","db2");
    databaseTypeMappings.setProperty("DB2/LINUX","db2");
    databaseTypeMappings.setProperty("DB2/LINUX390","db2");
    databaseTypeMappings.setProperty("DB2/LINUXX8664","db2");
    databaseTypeMappings.setProperty("DB2/LINUXZ64","db2");
    databaseTypeMappings.setProperty("DB2/400 SQL","db2");
    databaseTypeMappings.setProperty("DB2/6000","db2");
    databaseTypeMappings.setProperty("DB2 UDB iSeries","db2");
    databaseTypeMappings.setProperty("DB2/AIX64","db2");
    databaseTypeMappings.setProperty("DB2/HPUX","db2");
    databaseTypeMappings.setProperty("DB2/HP64","db2");
    databaseTypeMappings.setProperty("DB2/SUN","db2");
    databaseTypeMappings.setProperty("DB2/SUN64","db2");
    databaseTypeMappings.setProperty("DB2/PTX","db2");
    databaseTypeMappings.setProperty("DB2/2","db2");
    return databaseTypeMappings;
  }

  public void initDatabaseType() {
    Connection connection = null;
    try {
      connection = dataSource.getConnection();
      DatabaseMetaData databaseMetaData = connection.getMetaData();
      String databaseProductName = databaseMetaData.getDatabaseProductName();
      log.fine("database product name: '"+databaseProductName+"'");
      databaseType = databaseTypeMappings.getProperty(databaseProductName);
      if (databaseType==null) {
        throw new ActivitiException("couldn't deduct database type from database product name '"+databaseProductName+"'");
      }
      log.fine("using database type: "+databaseType);

    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (connection!=null) {
          connection.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }
  
  // myBatis SqlSessionFactory ////////////////////////////////////////////////
  
  protected void initTransactionFactory() {
    if (transactionFactory==null) {
      if (transactionsExternallyManaged) {
        transactionFactory = new ManagedTransactionFactory();
      } else {
        transactionFactory = new JdbcTransactionFactory();
      }
    }
  }

  protected void initSqlSessionFactory() {
    if (sqlSessionFactory==null) {
      InputStream inputStream = null;
      try {
        inputStream = getMyBatisXmlConfigurationSteam();

        // update the jdbc parameters to the configured ones...
        Environment environment = new Environment("default", transactionFactory, dataSource);
        Reader reader = new InputStreamReader(inputStream);
        Properties properties = new Properties();
        properties.put("prefix", databaseTablePrefix);
        if(databaseType != null) {
          properties.put("limitBefore" , DbSimulatorSqlSessionFactory.databaseSpecificLimitBeforeStatements.get(databaseType));
          properties.put("limitAfter" , DbSimulatorSqlSessionFactory.databaseSpecificLimitAfterStatements.get(databaseType));
          properties.put("limitBetween" , DbSimulatorSqlSessionFactory.databaseSpecificLimitBetweenStatements.get(databaseType));
          properties.put("orderBy" , DbSimulatorSqlSessionFactory.databaseSpecificOrderByStatements.get(databaseType));
        }
        XMLConfigBuilder parser = new XMLConfigBuilder(reader,"", properties);
        Configuration configuration = parser.getConfiguration();
        configuration.setEnvironment(environment);
        configuration.getTypeHandlerRegistry().register(VariableType.class, JdbcType.VARCHAR, new IbatisVariableTypeHandler());
        configuration = parser.parse();

        sqlSessionFactory = new DefaultSqlSessionFactory(configuration);

      } catch (Exception e) {
        throw new ActivitiException("Error while building ibatis SqlSessionFactory: " + e.getMessage(), e);
      } finally {
        IoUtil.closeSilently(inputStream);
      }
    }
  }
  
  protected InputStream getMyBatisXmlConfigurationSteam() {
    return ReflectUtil.getResourceAsStream(DEFAULT_MYBATIS_MAPPING_FILE);
  }

  // session factories ////////////////////////////////////////////////////////
  
  protected void initSessionFactories() {
    if (sessionFactories==null) {
      sessionFactories = new HashMap<Class<?>, SessionFactory>();

      dbSqlSessionFactory = new DbSimulatorSqlSessionFactory();
      dbSqlSessionFactory.setDatabaseType(databaseType);
      dbSqlSessionFactory.setIdGenerator(idGenerator);
      dbSqlSessionFactory.setSqlSessionFactory(sqlSessionFactory);
      dbSqlSessionFactory.setDatabaseTablePrefix(databaseTablePrefix);
      dbSqlSessionFactory.setDatabaseSchema(databaseSchema);
      addSessionFactory(dbSqlSessionFactory);
  
      addSessionFactory(new GenericManagerFactory(SimulationInstanceEntityManager.class));
      addSessionFactory(new GenericManagerFactory(SimulationRunEntityManager.class));
      addSessionFactory(new GenericManagerFactory(PropertyManager.class));
      addSessionFactory(new GenericManagerFactory(JobManager.class));
      addSessionFactory(new GenericManagerFactory(ResultEntityManager.class));
      addSessionFactory(new GenericManagerFactory(VariableInstanceManager.class));
      
//      addSessionFactory(new GenericManagerFactory(AttachmentManager.class));
//      addSessionFactory(new GenericManagerFactory(CommentManager.class));
//      addSessionFactory(new GenericManagerFactory(DeploymentManager.class));
//      addSessionFactory(new GenericManagerFactory(ModelManager.class));
//      addSessionFactory(new GenericManagerFactory(ExecutionManager.class));
//      addSessionFactory(new GenericManagerFactory(HistoricActivityInstanceManager.class));
//      addSessionFactory(new GenericManagerFactory(HistoricDetailManager.class));
//      addSessionFactory(new GenericManagerFactory(HistoricProcessInstanceManager.class));
//      addSessionFactory(new GenericManagerFactory(HistoricVariableInstanceManager.class));
//      addSessionFactory(new GenericManagerFactory(HistoricTaskInstanceManager.class));
//      addSessionFactory(new GenericManagerFactory(IdentityInfoManager.class));
//      addSessionFactory(new GenericManagerFactory(IdentityLinkManager.class));
//      addSessionFactory(new GenericManagerFactory(GroupManager.class));
//      addSessionFactory(new GenericManagerFactory(MembershipManager.class));
//      addSessionFactory(new GenericManagerFactory(ResourceManager.class));
//      addSessionFactory(new GenericManagerFactory(ByteArrayManager.class));
//      addSessionFactory(new GenericManagerFactory(TableDataManager.class));
//      addSessionFactory(new GenericManagerFactory(TaskManager.class));
//      addSessionFactory(new GenericManagerFactory(UserManager.class));
//      addSessionFactory(new GenericManagerFactory(EventSubscriptionManager.class));
//      addSessionFactory(new GenericManagerFactory(HistoryManager.class));
    }
    if (customSessionFactories!=null) {
      for (SessionFactory sessionFactory: customSessionFactories) {
    	  addSessionFactory(sessionFactory);
      }
    }
  }
  
  protected void addSessionFactory(SessionFactory sessionFactory) {
    sessionFactories.put(sessionFactory.getSessionType(), sessionFactory);
  }
  
  // id generator /////////////////////////////////////////////////////////////
  
  protected void initIdGenerator() {
    if (idGenerator==null) {
      CommandExecutor idGeneratorCommandExecutor = null;
      if (idGeneratorDataSource!=null) {
        SimulationEngineConfigurationImpl simulationEngineConfiguration = new StandaloneSimulationEngineConfiguration();
        simulationEngineConfiguration.setDataSource(idGeneratorDataSource);
        simulationEngineConfiguration.setDatabaseSchemaUpdate(DB_SCHEMA_UPDATE_FALSE);
        simulationEngineConfiguration.init();
        idGeneratorCommandExecutor = simulationEngineConfiguration.getCommandExecutorTxRequiresNew();
      } else if (idGeneratorDataSourceJndiName!=null) {
        SimulationEngineConfigurationImpl simulationEngineConfiguration = new StandaloneSimulationEngineConfiguration();
        simulationEngineConfiguration.setDataSourceJndiName(idGeneratorDataSourceJndiName);
        simulationEngineConfiguration.setDatabaseSchemaUpdate(DB_SCHEMA_UPDATE_FALSE);
        simulationEngineConfiguration.init();
        idGeneratorCommandExecutor = simulationEngineConfiguration.getCommandExecutorTxRequiresNew();
      } else {
        idGeneratorCommandExecutor = commandExecutorTxRequiresNew;
      }
      
      DbIdGenerator dbIdGenerator = new DbIdGenerator();
      dbIdGenerator.setIdBlockSize(idBlockSize);
      dbIdGenerator.setCommandExecutor(idGeneratorCommandExecutor);
      idGenerator = dbIdGenerator;
    }
  }

  // OTHER ////////////////////////////////////////////////////////////////////
  
  protected void initCommandContextFactory() {
    if (commandContextFactory==null) {
      commandContextFactory = new CommandContextFactory();
      commandContextFactory.setSimulationEngineConfiguration(this);
    }
  }

  protected void initTransactionContextFactory() {
    if (transactionContextFactory==null) {
      transactionContextFactory = new StandaloneMybatisTransactionContextFactory();
    }
  }

  protected void initVariableTypes() {
	    if (variableTypes==null) {
	      variableTypes = new DefaultVariableTypes();
	      if (customPreVariableTypes!=null) {
	        for (VariableType customVariableType: customPreVariableTypes) {
	          variableTypes.addType(customVariableType);
	        }
	      }
	      variableTypes.addType(new NullType());
	      variableTypes.addType(new StringType());
	      variableTypes.addType(new BooleanType());
	      variableTypes.addType(new ShortType());
	      variableTypes.addType(new IntegerType());
	      variableTypes.addType(new LongType());
	      variableTypes.addType(new DateType());
	      variableTypes.addType(new DoubleType());
	      variableTypes.addType(new ByteArrayType());
	      variableTypes.addType(new SerializableType());
	      variableTypes.addType(new CustomObjectType("item", ItemInstance.class));
	      variableTypes.addType(new CustomObjectType("message", MessageInstance.class));
	      if (customPostVariableTypes!=null) {
	        for (VariableType customVariableType: customPostVariableTypes) {
	          variableTypes.addType(customVariableType);
	        }
	      }
	    }
	  }

  protected void initScriptingEngines() {
    if (resolverFactories==null) {
      resolverFactories = new ArrayList<ResolverFactory>();
      resolverFactories.add(new VariableScopeResolverFactory());
      resolverFactories.add(new BeansResolverFactory());
    }
    if (scriptingEngines==null) {
      scriptingEngines = new ScriptingEngines(new ScriptBindingsFactory(resolverFactories));
    }
  }

  protected void initExpressionManager() {
    if (expressionManager==null) {
      expressionManager = new ExpressionManager(beans);
    }
  }

  protected void initDelegateInterceptor() {
    if(delegateInterceptor == null) {
      delegateInterceptor = new DefaultDelegateInterceptor();
    }
  }
  
  protected void initEventHandlers() {
    if(eventHandlers == null) {
      eventHandlers = new HashMap<String, EventHandler>();
      
      SignalEventHandler signalEventHander = new SignalEventHandler();
      eventHandlers.put(signalEventHander.getEventHandlerType(), signalEventHander);
      
      CompensationEventHandler compensationEventHandler = new CompensationEventHandler();
      eventHandlers.put(compensationEventHandler.getEventHandlerType(), compensationEventHandler);
      
      MessageEventHandler messageEventHandler = new MessageEventHandler();
      eventHandlers.put(messageEventHandler.getEventHandlerType(), messageEventHandler);
      
    }
    if(customEventHandlers != null) {
      for (EventHandler eventHandler : customEventHandlers) {
        eventHandlers.put(eventHandler.getEventHandlerType(), eventHandler);        
      }
    }
  }
  
  // JPA //////////////////////////////////////////////////////////////////////
  
  protected void initJpa() {
    if(jpaPersistenceUnitName!=null) {
      jpaEntityManagerFactory = JpaHelper.createEntityManagerFactory(jpaPersistenceUnitName);
    }
    if(jpaEntityManagerFactory!=null) {
      sessionFactories.put(EntityManagerSession.class, new EntityManagerSessionFactory(jpaEntityManagerFactory, jpaHandleTransaction, jpaCloseEntityManager));
      VariableType jpaType = variableTypes.getVariableType(JPAEntityVariableType.TYPE_NAME);
      // Add JPA-type
      if(jpaType == null) {
        // We try adding the variable right before SerializableType, if available
        int serializableIndex = variableTypes.getTypeIndex(SerializableType.TYPE_NAME);
        if(serializableIndex > -1) {
          variableTypes.addType(new JPAEntityVariableType(), serializableIndex);
        } else {
          variableTypes.addType(new JPAEntityVariableType());
        }        
      }
    }
  }
  
  protected void initBeans() {
    if (beans == null) {
      beans = new HashMap<Object, Object>();
    }
  }

  // getters and setters //////////////////////////////////////////////////////
  
  public String getProcessEngineName() {
    return simulationEngineName;
  }

  public SimulationEngineConfigurationImpl setProcessEngineName(String simulationEngineName) {
    this.simulationEngineName = simulationEngineName;
    return this;
  }
  
  public List<CommandInterceptor> getCustomPreCommandInterceptorsTxRequired() {
    return customPreCommandInterceptorsTxRequired;
  }
  
  public SimulationEngineConfigurationImpl setCustomPreCommandInterceptorsTxRequired(List<CommandInterceptor> customPreCommandInterceptorsTxRequired) {
    this.customPreCommandInterceptorsTxRequired = customPreCommandInterceptorsTxRequired;
    return this;
  }
  
  public List<CommandInterceptor> getCustomPostCommandInterceptorsTxRequired() {
    return customPostCommandInterceptorsTxRequired;
  }
  
  public SimulationEngineConfigurationImpl setCustomPostCommandInterceptorsTxRequired(List<CommandInterceptor> customPostCommandInterceptorsTxRequired) {
    this.customPostCommandInterceptorsTxRequired = customPostCommandInterceptorsTxRequired;
    return this;
  }
  
  public List<CommandInterceptor> getCommandInterceptorsTxRequired() {
    return commandInterceptorsTxRequired;
  }
  
  public SimulationEngineConfigurationImpl setCommandInterceptorsTxRequired(List<CommandInterceptor> commandInterceptorsTxRequired) {
    this.commandInterceptorsTxRequired = commandInterceptorsTxRequired;
    return this;
  }
  
  public CommandExecutor getCommandExecutorTxRequired() {
    return commandExecutorTxRequired;
  }
  
  public SimulationEngineConfigurationImpl setCommandExecutorTxRequired(CommandExecutor commandExecutorTxRequired) {
    this.commandExecutorTxRequired = commandExecutorTxRequired;
    return this;
  }
  
  public List<CommandInterceptor> getCustomPreCommandInterceptorsTxRequiresNew() {
    return customPreCommandInterceptorsTxRequiresNew;
  }
  
  public SimulationEngineConfigurationImpl setCustomPreCommandInterceptorsTxRequiresNew(List<CommandInterceptor> customPreCommandInterceptorsTxRequiresNew) {
    this.customPreCommandInterceptorsTxRequiresNew = customPreCommandInterceptorsTxRequiresNew;
    return this;
  }
  
  public List<CommandInterceptor> getCustomPostCommandInterceptorsTxRequiresNew() {
    return customPostCommandInterceptorsTxRequiresNew;
  }
  
  public SimulationEngineConfigurationImpl setCustomPostCommandInterceptorsTxRequiresNew(List<CommandInterceptor> customPostCommandInterceptorsTxRequiresNew) {
    this.customPostCommandInterceptorsTxRequiresNew = customPostCommandInterceptorsTxRequiresNew;
    return this;
  }
  
  public List<CommandInterceptor> getCommandInterceptorsTxRequiresNew() {
    return commandInterceptorsTxRequiresNew;
  }
  
  public SimulationEngineConfigurationImpl setCommandInterceptorsTxRequiresNew(List<CommandInterceptor> commandInterceptorsTxRequiresNew) {
    this.commandInterceptorsTxRequiresNew = commandInterceptorsTxRequiresNew;
    return this;
  }
  
  public CommandExecutor getCommandExecutorTxRequiresNew() {
    return commandExecutorTxRequiresNew;
  }
  
  public SimulationEngineConfigurationImpl setCommandExecutorTxRequiresNew(CommandExecutor commandExecutorTxRequiresNew) {
    this.commandExecutorTxRequiresNew = commandExecutorTxRequiresNew;
    return this;
  }
  
  public Map<Class< ? >, SessionFactory> getSessionFactories() {
    return sessionFactories;
  }
  
  public SimulationEngineConfigurationImpl setSessionFactories(Map<Class< ? >, SessionFactory> sessionFactories) {
    this.sessionFactories = sessionFactories;
    return this;
  }
    
  public IdGenerator getIdGenerator() {
    return idGenerator;
  }
  
  public SimulationEngineConfigurationImpl setIdGenerator(IdGenerator idGenerator) {
    this.idGenerator = idGenerator;
    return this;
  }
  
  public String getWsSyncFactoryClassName() {
    return wsSyncFactoryClassName;
  }
  
  public SimulationEngineConfigurationImpl setWsSyncFactoryClassName(String wsSyncFactoryClassName) {
    this.wsSyncFactoryClassName = wsSyncFactoryClassName;
    return this;
  }
    
  public ScriptingEngines getScriptingEngines() {
    return scriptingEngines;
  }
  
  public SimulationEngineConfigurationImpl setScriptingEngines(ScriptingEngines scriptingEngines) {
    this.scriptingEngines = scriptingEngines;
    return this;
  }
  
  public VariableTypes getVariableTypes() {
    return variableTypes;
  }
  
  public SimulationEngineConfigurationImpl setVariableTypes(VariableTypes variableTypes) {
    this.variableTypes = variableTypes;
    return this;
  }
  
  public ExpressionManager getExpressionManager() {
    return expressionManager;
  }
  
  public SimulationEngineConfigurationImpl setExpressionManager(ExpressionManager expressionManager) {
    this.expressionManager = expressionManager;
    return this;
  }
    
  public CommandContextFactory getCommandContextFactory() {
    return commandContextFactory;
  }
  
  public SimulationEngineConfigurationImpl setCommandContextFactory(CommandContextFactory commandContextFactory) {
    this.commandContextFactory = commandContextFactory;
    return this;
  }
  
  public TransactionContextFactory getTransactionContextFactory() {
    return transactionContextFactory;
  }
  
  public SimulationEngineConfigurationImpl setTransactionContextFactory(TransactionContextFactory transactionContextFactory) {
    this.transactionContextFactory = transactionContextFactory;
    return this;
  }
  
  public SqlSessionFactory getSqlSessionFactory() {
    return sqlSessionFactory;
  }

  
  public SimulationEngineConfigurationImpl setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
    this.sqlSessionFactory = sqlSessionFactory;
    return this;
  }

  
  public DbSimulatorSqlSessionFactory getDbSqlSessionFactory() {
    return dbSqlSessionFactory;
  }

  public SimulationEngineConfigurationImpl setDbSqlSessionFactory(DbSimulatorSqlSessionFactory dbSqlSessionFactory) {
    this.dbSqlSessionFactory = dbSqlSessionFactory;
    return this;
  }
  
  public TransactionFactory getTransactionFactory() {
    return transactionFactory;
  }

  public SimulationEngineConfigurationImpl setTransactionFactory(TransactionFactory transactionFactory) {
    this.transactionFactory = transactionFactory;
    return this;
  }

  public List<SessionFactory> getCustomSessionFactories() {
    return customSessionFactories;
  }
  
  public SimulationEngineConfigurationImpl setCustomSessionFactories(List<SessionFactory> customSessionFactories) {
    this.customSessionFactories = customSessionFactories;
    return this;
  }
  
  public List<String> getCustomScriptingEngineClasses() {
    return customScriptingEngineClasses;
  }

  
  public SimulationEngineConfigurationImpl setCustomScriptingEngineClasses(List<String> customScriptingEngineClasses) {
    this.customScriptingEngineClasses = customScriptingEngineClasses;
    return this;
  }

  public List<VariableType> getCustomPreVariableTypes() {
    return customPreVariableTypes;
  }

  
  public SimulationEngineConfigurationImpl setCustomPreVariableTypes(List<VariableType> customPreVariableTypes) {
    this.customPreVariableTypes = customPreVariableTypes;
    return this;
  }

  
  public List<VariableType> getCustomPostVariableTypes() {
    return customPostVariableTypes;
  }

  
  public SimulationEngineConfigurationImpl setCustomPostVariableTypes(List<VariableType> customPostVariableTypes) {
    this.customPostVariableTypes = customPostVariableTypes;
    return this;
  }
  
  public Map<Object, Object> getBeans() {
    return beans;
  }

  public void setBeans(Map<Object, Object> beans) {
    this.beans = beans;
  }

  @Override
  public SimulationEngineConfigurationImpl setClassLoader(ClassLoader classLoader) {
    super.setClassLoader(classLoader);
    return this;
  }

  @Override
  public SimulationEngineConfigurationImpl setDatabaseType(String databaseType) {
    super.setDatabaseType(databaseType);
    return this;
  }

  @Override
  public SimulationEngineConfigurationImpl setDataSource(DataSource dataSource) {
    super.setDataSource(dataSource);
    return this;
  }

  @Override
  public SimulationEngineConfigurationImpl setDatabaseSchemaUpdate(String databaseSchemaUpdate) {
    super.setDatabaseSchemaUpdate(databaseSchemaUpdate);
    return this;
  }

  @Override
  public SimulationEngineConfigurationImpl setIdBlockSize(int idBlockSize) {
    super.setIdBlockSize(idBlockSize);
    return this;
  }

  @Override
  public SimulationEngineConfigurationImpl setJdbcDriver(String jdbcDriver) {
    super.setJdbcDriver(jdbcDriver);
    return this;
  }

  @Override
  public SimulationEngineConfigurationImpl setJdbcPassword(String jdbcPassword) {
    super.setJdbcPassword(jdbcPassword);
    return this;
  }

  @Override
  public SimulationEngineConfigurationImpl setJdbcUrl(String jdbcUrl) {
    super.setJdbcUrl(jdbcUrl);
    return this;
  }

  @Override
  public SimulationEngineConfigurationImpl setJdbcUsername(String jdbcUsername) {
    super.setJdbcUsername(jdbcUsername);
    return this;
  }

  @Override
  public SimulationEngineConfigurationImpl setJdbcMaxActiveConnections(int jdbcMaxActiveConnections) {
    super.setJdbcMaxActiveConnections(jdbcMaxActiveConnections);
    return this;
  }

  @Override
  public SimulationEngineConfigurationImpl setJdbcMaxCheckoutTime(int jdbcMaxCheckoutTime) {
    super.setJdbcMaxCheckoutTime(jdbcMaxCheckoutTime);
    return this;
  }

  @Override
  public SimulationEngineConfigurationImpl setJdbcMaxIdleConnections(int jdbcMaxIdleConnections) {
    super.setJdbcMaxIdleConnections(jdbcMaxIdleConnections);
    return this;
  }

  @Override
  public SimulationEngineConfigurationImpl setJdbcMaxWaitTime(int jdbcMaxWaitTime) {
    super.setJdbcMaxWaitTime(jdbcMaxWaitTime);
    return this;
  }

  @Override
  public SimulationEngineConfigurationImpl setTransactionsExternallyManaged(boolean transactionsExternallyManaged) {
    super.setTransactionsExternallyManaged(transactionsExternallyManaged);
    return this;
  }

  @Override
  public SimulationEngineConfigurationImpl setJpaEntityManagerFactory(Object jpaEntityManagerFactory) {
    this.jpaEntityManagerFactory = jpaEntityManagerFactory;
    return this;
  }

  @Override
  public SimulationEngineConfigurationImpl setJpaHandleTransaction(boolean jpaHandleTransaction) {
    this.jpaHandleTransaction = jpaHandleTransaction;
    return this;
  }
  
  @Override
  public SimulationEngineConfigurationImpl setJpaCloseEntityManager(boolean jpaCloseEntityManager) {
    this.jpaCloseEntityManager = jpaCloseEntityManager;
    return this;
  }

  @Override
  public SimulationEngineConfigurationImpl setJdbcPingEnabled(boolean jdbcPingEnabled) {
    this.jdbcPingEnabled = jdbcPingEnabled;
    return this;
  }

  @Override
  public SimulationEngineConfigurationImpl setJdbcPingQuery(String jdbcPingQuery) {
    this.jdbcPingQuery = jdbcPingQuery;
    return this;
  }

  @Override
  public SimulationEngineConfigurationImpl setJdbcPingConnectionNotUsedFor(int jdbcPingNotUsedFor) {
    this.jdbcPingConnectionNotUsedFor = jdbcPingNotUsedFor;
    return this;
  }    
    
  public boolean isDbHistoryUsed() {
    return isDbHistoryUsed;
  }
  
  public void setDbHistoryUsed(boolean isDbHistoryUsed) {
    this.isDbHistoryUsed = isDbHistoryUsed;
  }
  
  public List<ResolverFactory> getResolverFactories() {
    return resolverFactories;
  }
  
  public void setResolverFactories(List<ResolverFactory> resolverFactories) {
    this.resolverFactories = resolverFactories;
  }

  public SimulationEngineConfigurationImpl setDelegateInterceptor(DelegateInterceptor delegateInterceptor) {
    this.delegateInterceptor = delegateInterceptor;
    return this;
  }
    
  public DelegateInterceptor getDelegateInterceptor() {
    return delegateInterceptor;
  }
    
  public EventHandler getEventHandler(String eventType) {
    return eventHandlers.get(eventType);
  }
  
  public void setEventHandlers(Map<String, EventHandler> eventHandlers) {
    this.eventHandlers = eventHandlers;
  }
    
  public Map<String, EventHandler> getEventHandlers() {
    return eventHandlers;
  }
    
  public List<EventHandler> getCustomEventHandlers() {
    return customEventHandlers;
  }
    
  public void setCustomEventHandlers(List<EventHandler> customEventHandlers) {
    this.customEventHandlers = customEventHandlers;
  }
  
  
  /**
   * Allows configuring a database table prefix which is used for all runtime operations of the process engine.
   * For example, if you specify a prefix named 'PRE1.', activiti will query for executions in a table named
   * 'PRE1.ACT_RU_EXECUTION_'. 
   * 
   * <p />
   * <strong>NOTE: the prefix is not respected by automatic database schema management. If you use 
   * {@link ProcessEngineConfiguration#DB_SCHEMA_UPDATE_CREATE_DROP} 
   * or {@link ProcessEngineConfiguration#DB_SCHEMA_UPDATE_TRUE}, activiti will create the database tables 
   * using the default names, regardless of the prefix configured here.</strong>  
   * 
   * @since 5.9
   */
  public SimulationEngineConfiguration setDatabaseTablePrefix(String databaseTablePrefix) {
    this.databaseTablePrefix = databaseTablePrefix;
    return this;
  }
    
  public String getDatabaseTablePrefix() {
    return databaseTablePrefix;
  }
  
  public String getDatabaseSchema() {
    return databaseSchema;
  }
  
  public void setDatabaseSchema(String databaseSchema) {
    this.databaseSchema = databaseSchema;
  }

  public DataSource getIdGeneratorDataSource() {
    return idGeneratorDataSource;
  }
  
  public void setIdGeneratorDataSource(DataSource idGeneratorDataSource) {
    this.idGeneratorDataSource = idGeneratorDataSource;
  }
  
  public String getIdGeneratorDataSourceJndiName() {
    return idGeneratorDataSourceJndiName;
  }

  public void setIdGeneratorDataSourceJndiName(String idGeneratorDataSourceJndiName) {
    this.idGeneratorDataSourceJndiName = idGeneratorDataSourceJndiName;
  }

  public int getBatchSizeProcessInstances() {
    return batchSizeProcessInstances;
  }

  public void setBatchSizeProcessInstances(int batchSizeProcessInstances) {
    this.batchSizeProcessInstances = batchSizeProcessInstances;
  }
  
  public int getBatchSizeTasks() {
    return batchSizeTasks;
  }
  
  public void setBatchSizeTasks(int batchSizeTasks) {
    this.batchSizeTasks = batchSizeTasks;
  }
  
  public RuntimeService getRuntimeService() {
	  return runtimeService;
  }
  
  public List<JobHandler> getCustomJobHandlers() {
	    return customJobHandlers;
  }
	  
  public SimulationEngineConfigurationImpl setCustomJobHandlers(List<JobHandler> customJobHandlers) {
    this.customJobHandlers = customJobHandlers;
    return this;
  }

public FailedJobCommandFactory getFailedJobCommandFactory() {
	return failedJobCommandFactory;
}

public SimulationEngineConfigurationImpl setFailedJobCommandFactory(
		FailedJobCommandFactory failedJobCommandFactory) {
	this.failedJobCommandFactory = failedJobCommandFactory;
	return this;
}

	public JobExecutor getJobExecutor() {
	    return jobExecutor;
  }
  
  public SimulationEngineConfigurationImpl setJobExecutor(JobExecutor jobExecutor) {
    this.jobExecutor = jobExecutor;
    return this;
  }
  
	public Map<String, JobHandler> getJobHandlers() {
	    return jobHandlers;
  }
  
  public SimulationEngineConfigurationImpl setJobExecutor(Map<String, JobHandler> jobHandlers) {
    this.jobHandlers = jobHandlers;
    return this;
  }
}