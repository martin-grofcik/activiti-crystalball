package org.activiti.crystalball.simulator.delegate.event;

import junit.framework.AssertionFailedError;
import org.activiti.crystalball.simulator.FactoryBean;
import org.activiti.crystalball.simulator.PlaybackEventCalendarFactory;
import org.activiti.crystalball.simulator.SimpleSimulationRun;
import org.activiti.crystalball.simulator.SimulationEventComparator;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.impl.ProcessEngineImpl;
import org.activiti.engine.impl.db.DbSqlSession;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandConfig;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.test.AbstractActivitiTestCase;
import org.activiti.engine.impl.test.TestHelper;
import org.activiti.engine.impl.util.ClockUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;

/**
 * This class is supper class for all Playback tests
 */
public abstract class AbstractPlaybackTest extends AbstractActivitiTestCase {

  private static Logger log = LoggerFactory.getLogger(AbstractPlaybackTest.class);

  protected RecordActivitiEventTestListener listener = new RecordActivitiEventTestListener(ExecutionEntity.class);

  /**
   * increase clockUtils time
   * Simulation operates in milliseconds. Sometime could happen (especially during automated tests)
   * that 2 recorded events have the same time. In that case it is not possible to recognize order.
   */
  protected void increaseTime() {
    Calendar c = Calendar.getInstance();
    c.setTime(ClockUtil.getCurrentTime());
    c.add(Calendar.SECOND,1);
    ClockUtil.setCurrentTime(c.getTime());
  }

  @Override
  protected void initializeProcessEngine() {
    this.processEngine = (new RecordableProcessEngineFactory(listener)).getObject();
  }

  @Override
  public void runBare() throws Throwable {

    log.info("Running test {} to record events", getName());

    recordEvents();

    log.info("Events for {} recorded successfully", getName());
    log.info("Running playback simulation for {}", getName());

    runPlayback();

    log.info("Playback simulation for {} finished successfully.", getName());

  }

  private void runPlayback() throws Throwable {
    try {
      // init simulation run

      FactoryBean<ProcessEngine> simulationProcessEngineFactory = new DefaultSimulationProcessEngineFactory();
      this.processEngine = simulationProcessEngineFactory.getObject();
      initializeServices();
      deploymentId = TestHelper.annotationDeploymentSetUp(processEngine, getClass(), getName());

      final SimpleSimulationRun.Builder builder = new SimpleSimulationRun.Builder();
      builder.processEngineFactory(simulationProcessEngineFactory)
        .eventCalendarFactory(new PlaybackEventCalendarFactory(new SimulationEventComparator(), listener.getSimulationEvents()))
        .customEventHandlerMap(EventRecorderTestUtils.getHandlers());
      SimpleSimulationRun simRun = builder.build();

      simRun.execute(EventRecorderTestUtils.DO_NOT_CLOSE_PROCESS_ENGINE);

      _checkStatus(this.processEngine);
    } catch (AssertionFailedError e) {
      log.warn("Playback simulation {} has failed", getName());
      log.error(EMPTY_LINE);
      log.error("ASSERTION FAILED: {}", e, e);
      exception = e;
      throw e;

    } catch (Throwable e) {
      log.warn("Playback simulation {} has failed", getName());
      log.error(EMPTY_LINE);
      log.error("EXCEPTION: {}", e, e);
      exception = e;
      throw e;

    } finally {
      TestHelper.annotationDeploymentTearDown(processEngine, deploymentId, getClass(), getName());
      assertAndEnsureCleanDb();
      ClockUtil.reset();

      // Can't do this in the teardown, as the teardown will be called as part of the super.runBare
      closeDownProcessEngine();
    }
  }

  private void _checkStatus(ProcessEngine processEngine) throws InvocationTargetException, IllegalAccessException {
      Method method;
      try {
        method = getClass().getMethod(getName(), (Class<?>[]) null);
      } catch (Exception e) {
        log.warn("Could not get method by reflection. This could happen if you are using @Parameters in combination with annotations.", e);
        return;
      }
      CheckStatus checkStatusAnnotation = method.getAnnotation(CheckStatus.class);
      if (checkStatusAnnotation != null) {
        log.debug("annotation @CheckStatus checks status for {}.{}", getClass().getSimpleName(), getName());
        String checkStatusMethodName = checkStatusAnnotation.methodName();
        if (checkStatusMethodName.isEmpty()) {
          String name = method.getName();
          checkStatusMethodName = name + "CheckStatus";
        }

        try {
          method = getClass().getMethod(checkStatusMethodName);
        } catch (Exception e) {
          log.error("Could not get CheckStatus method: {} by reflection. This could happen if you are using @Parameters in combination with annotations.", checkStatusMethodName, e);
          throw new RuntimeException("Could not get CheckStatus method by reflection");
        }
        method.invoke(this);
      } else {
        log.warn("Check status annotation is not present - nothing is checked");
      }
    }

  private void recordEvents() throws Throwable {
    initializeProcessEngine();
    if (repositoryService == null) {
      initializeServices();
    }

    try {

      deploymentId = TestHelper.annotationDeploymentSetUp(processEngine, getClass(), getName());

      //super.runBare();
      Throwable exception = null;
      setUp();
      try {
        runTest();
      } catch (Throwable running) {
        exception = running;
      } finally {
        try {
          tearDown();
        } catch (Throwable tearingDown) {
          if (exception == null) exception = tearingDown;
        }
      }
      if (exception != null) throw exception;


      _checkStatus(processEngine);
    } catch (AssertionFailedError e) {
      log.error(EMPTY_LINE);
      log.error("ASSERTION FAILED: {}", e, e);
      exception = e;
      throw e;

    } catch (Throwable e) {
      log.warn("Record events {} has failed", getName());
      log.error(EMPTY_LINE);
      log.error("EXCEPTION: {}", e, e);
      exception = e;
      throw e;

    } finally {
      TestHelper.annotationDeploymentTearDown(processEngine, deploymentId, getClass(), getName());
      assertAndEnsureCleanDb();
      log.info("dropping and recreating db");

      ClockUtil.reset();

      // Can't do this in the teardown, as the teardown will be called as part of the super.runBare
      EventRecorderTestUtils.closeProcessEngine(processEngine, listener);
      closeDownProcessEngine();
    }
  }

  @Override
  protected void closeDownProcessEngine() {
    ProcessEngines.destroy();
  }
}
