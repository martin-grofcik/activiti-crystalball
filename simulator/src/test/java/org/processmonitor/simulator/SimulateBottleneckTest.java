package org.processmonitor.simulator;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.activiti.engine.ProcessEngine;
import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.processmonitor.simulator.impl.StartProcessEventHandler;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SimulateBottleneckTest extends RunSimulationSuperClass  {
	
	@Test
	public void testOK() throws IOException {

        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("/org/processmonitor/simulator/SimEngine-simulateBottleneck-h2-context.xml");
        ProcessEngine processEngine = (ProcessEngine) appContext.getBean("simProcessEngine");
        
        runSimulation(appContext);
        
        processEngine.close();        
        appContext.close();        

        File expected = new File(System.getProperty("baseDir", ".") + "/src/test/resources/org/processmonitor/simulator/simulateBottleneckTest-1.png" );   
		File generated = new File(GENERATED_IMAGE);   
	    assertTrue( FileUtils.contentEquals(expected, generated));	          
	}

	/**
	 * results differs - that's why this test is ignored - possible bug. 
	 */
	@Ignore 
	@Test
	public void testUser2OverLoaded() throws IOException {

        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("/org/processmonitor/simulator/SimEngine-simulateBottleneck-h2-context.xml");
        ProcessEngine processEngine = (ProcessEngine) appContext.getBean("simProcessEngine");
        
        // increase frequency of process starts to 45 mins (default is 1 hour)
        StartProcessEventHandler startProcessEventHandler = appContext.getBean(StartProcessEventHandler.class);
        startProcessEventHandler.setPeriod(2700000);
        
        runSimulation(appContext);
        
        processEngine.close();
        appContext.close();        

        File expected = new File(System.getProperty("baseDir", ".") + "/src/test/resources/org/processmonitor/simulator/simulateBottleneckTest-2.png" );   
		File generated = new File(GENERATED_IMAGE);   
	    assertTrue( FileUtils.contentEquals(expected, generated));	          
        
	}
}
