package org.processmonitor.generator;

import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:/org/processmonitor/generator/SimpleProcessDiagramGeneratorTest-context.xml")
public class SimpleProcessDiagramGeneratorTest {

	private static String FINANCIALREPORT_PROCESS_KEY = "financialReport";
	private static String GENERATOR_PROCESS_KEY = "simpleprocessdiagramgeneratortest";

	@Autowired 
	private ProcessEngine processEngine;
		
	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;

	@Before
	public void before() {
	  repositoryService.createDeployment()
	    .addClasspathResource("org/activiti/examples/bpmn/usertask/FinancialReportProcess.bpmn20.xml")
	    .addClasspathResource("org/activiti/examples/bpmn/usertask/FinancialReportProcess.png")
	    .addClasspathResource("org/processmonitor/generator/SimpleProcessDiagramGenerator.bpmn")
	    .addClasspathResource("org/processmonitor/generator/SimpleProcessDiagramGenerator.png")
	    .addClasspathResource("org/processmonitor/generator/SimpleProcessDiagramGeneratorTest.bpmn")
	    .deploy();
	}

	@After
  public void after() {
    for (Deployment deployment : repositoryService.createDeploymentQuery().list()) {
      repositoryService.deleteDeployment(deployment.getId(), true);
    }
    processEngine.close();
  }

	@Test
	public void testReturnedProcessInstance() throws Throwable {
		// prepare params
		Map<String, Object> params = new Hashtable<String, Object>();
		
		String id = FINANCIALREPORT_PROCESS_KEY;
		params.put( "processDefinitionId", id);
		List<String> highlightedActivities = new ArrayList<String>();
	    highlightedActivities.add("writeReportTask");
	    params.put( "highLightedActivities", highlightedActivities);
	    Map<String, Object> counts = new HashMap<String, Object>();
	    counts.put( "writeReportTask", 5);
	    counts.put( "verifyReportTask", 25);
	    params.put( "counts", counts);
	    params.put( "reportFileName", "target/SimpleProcessDiagramGeneratorTest.png");
	    
	    // start process
		runtimeService.startProcessInstanceByKey(
				GENERATOR_PROCESS_KEY, "GENERATOR-KEY-1", params).getId();
	    //check outputs
		InputStream expectedStream = new FileInputStream("src/test/resources/org/processmonitor/generator/SimpleProcessDiagramGeneratorTestExpected.png" );   
		InputStream generatedStream = new FileInputStream("target/SimpleProcessDiagramGeneratorTest.png");   
	    assertTrue( isEqual(expectedStream, generatedStream));	    	
	}
	
	static boolean isEqual(InputStream stream1, InputStream stream2)
	          throws IOException {
	  
	      ReadableByteChannel channel1 = Channels.newChannel(stream1);
	      ReadableByteChannel channel2 = Channels.newChannel(stream2);

	      ByteBuffer buffer1 = ByteBuffer.allocateDirect(1024);
	      ByteBuffer buffer2 = ByteBuffer.allocateDirect(1024);

	      try {
	          while (true) {

	              int bytesReadFromStream1 = channel1.read(buffer1);
	              int bytesReadFromStream2 = channel2.read(buffer2);

	              if (bytesReadFromStream1 == -1 || bytesReadFromStream2 == -1) 
	            	  return bytesReadFromStream1 == bytesReadFromStream2;

	              buffer1.flip();
	              buffer2.flip();

	              for (int i = 0; i < Math.min(bytesReadFromStream1, bytesReadFromStream2); i++)
	                  if (buffer1.get() != buffer2.get())
	                      return false;

	              buffer1.compact();
	              buffer2.compact();
	          }
	      } catch (IOException e) {
	    	  System.err.println( e.getStackTrace());
	    	  throw e;
	      } finally {
	          if (stream1 != null) stream1.close();
	          if (stream2 != null) stream2.close();
	      }
	  }
}

