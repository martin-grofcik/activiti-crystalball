package diagram;

/*
 * #%L
 * image-builder
 * %%
 * Copyright (C) 2012 - 2013 crystalball
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
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
 * #L%
 */


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.activiti.crystalball.processengine.wrapper.RepositoryServiceWrapper;
import org.activiti.crystalball.processengine.wrapper.activiti.ProcessDiagramServiceImpl;
import org.activiti.crystalball.processengine.wrapper.activiti.RepositoryServiceWrapperImpl;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.test.PluggableActivitiTestCase;
import org.apache.commons.io.FileUtils;
import org.activiti.crystalball.diagram.AbstractProcessDiagramLayerGenerator;
import org.activiti.crystalball.diagram.BasicProcessDiagramGenerator;
import org.activiti.crystalball.diagram.DiagramLayerGenerator;
import org.activiti.crystalball.diagram.HighlightNodeDiagramLayer;
import org.activiti.crystalball.diagram.MergeLayersGenerator;
import org.activiti.crystalball.diagram.WriteNodeDescriptionDiagramLayer;
import org.junit.Ignore;


public class ProcessDiagramGeneratorTest extends PluggableActivitiTestCase {

	private static String FINANCIALREPORT_PROCESS_KEY = "financialReport";

	private List<String> processInstanceIds;

	protected void setUp() throws Exception {
		super.setUp();
		repositoryService
				.createDeployment()
				.addClasspathResource(
						"org/activiti/examples/bpmn/usertask/FinancialReportProcess.bpmn20.xml")
				.addClasspathResource(
						"org/activiti/examples/bpmn/usertask/FinancialReportProcess.png")
				.deploy();

		processInstanceIds = new ArrayList<String>();

		for (int i = 0; i < 4; i++) {
			processInstanceIds.add(runtimeService.startProcessInstanceByKey(
					FINANCIALREPORT_PROCESS_KEY, "BUSINESS-KEY-" + i).getId());
		}
	}

	protected void tearDown() throws Exception {
		for (org.activiti.engine.repository.Deployment deployment : repositoryService
				.createDeploymentQuery().list()) {
			repositoryService.deleteDeployment(deployment.getId(), true);
		}
		super.tearDown();
	}
	
	@Ignore("Generator provides platform dependent images (fonts) see http://forums.activiti.org/en/viewtopic.php?f=6&t=4647&start=0")
	public void testGenerateProcessDefinition() throws IOException {
	    String id = FINANCIALREPORT_PROCESS_KEY;
	    DiagramLayerGenerator generator = new BasicProcessDiagramGenerator( new RepositoryServiceWrapperImpl( (RepositoryServiceImpl) repositoryService ), new ProcessDiagramServiceImpl());
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put(AbstractProcessDiagramLayerGenerator.PROCESS_DEFINITION_ID, id);
	    
	    File expectedFile = new File("src/org.activiti.crystalball.processengine.wrapper.test/resources/org/activiti/crystalball/diagram/BasicProcessDiagramGeneratorTest.testSimpleProcessDefinition.png" );
	    File generatedFile = new File("target/BasicProcessDiagramGeneratorTest.testSimpleProcessDefinition.png" );
	    ImageIO.write( ImageIO.read(new ByteArrayInputStream( generator.generateLayer( "png", params)))
				, "png"
				, generatedFile);
	    assertTrue(FileUtils.contentEquals(expectedFile, generatedFile));
	}
	
	public void testOneNodeHighlight() throws IOException {
	    String id = FINANCIALREPORT_PROCESS_KEY;
	    DiagramLayerGenerator generator = new HighlightNodeDiagramLayer( new RepositoryServiceWrapperImpl((RepositoryServiceImpl) repositoryService ), new ProcessDiagramServiceImpl());
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put( HighlightNodeDiagramLayer.PROCESS_DEFINITION_ID, id);
	    List<String> highlightedActivities = new ArrayList<String>();
	    highlightedActivities.add("writeReportTask");
	    params.put( HighlightNodeDiagramLayer.HIGHLIGHTED_ACTIVITIES, highlightedActivities);
	    
	    File expectedFile = new File("src/org.activiti.crystalball.processengine.wrapper.test/resources/org/activiti/crystalball/diagram/HighlightNodeDiagramLayer.testOneNodeHighlight.png" );
	    File generatedFile = new File("target/HighlightNodeDiagramLayer.testOneNodeHighlight.png" );   
	    ImageIO.write( ImageIO.read(new ByteArrayInputStream( generator.generateLayer( "png", params)))
				, "png"
				, generatedFile);
	    assertTrue( FileUtils.contentEquals(expectedFile, generatedFile));	    
	}
	
	public void testMergeLayers() throws IOException {
	    DiagramLayerGenerator generator = new MergeLayersGenerator();
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put( "img1", getBytes("src/org.activiti.crystalball.processengine.wrapper.test/resources/org/activiti/crystalball/diagram/MergeLayerGenerator.testMergeLayers1.png"));
	    params.put( "img2", getBytes("src/org.activiti.crystalball.processengine.wrapper.test/resources/org/activiti/crystalball/diagram/MergeLayerGenerator.testMergeLayers2.png"));
	    
	    File expectedFile = new File("src/org.activiti.crystalball.processengine.wrapper.test/resources/org/activiti/crystalball/diagram/MergeLayerGenerator.testMergeLayers.png" );
	    File generatedFile = new File("target/MergeLayerGenerator.testMergeLayers.png" );  
	    ImageIO.write( ImageIO.read(new ByteArrayInputStream( generator.generateLayer( "png", params)))
				, "png"
				, generatedFile);
	 	assertTrue( FileUtils.contentEquals(expectedFile, generatedFile));
	}
	
	/**
	 * get byte[] from fileName
	 * @param fileName
	 * @return
	 */
	private Object getBytes(String fileName) {
	    //create file object
	    File file = new File( fileName );
	      FileInputStream fin = null;
		 try
		    {
		      //create FileInputStream object
			 fin = new FileInputStream(fileName);
		     
		      /*
		       * Create byte array large enough to hold the content of the file.
		       * Use File.length to determine size of the file in bytes.
		       */
		     
		     
		       byte fileContent[] = new byte[(int)file.length()];
		     
		       /*
		        * To read content of the file in byte array, use
		        * int read(byte[] byteArray) method of java FileInputStream class.
		        *
		        */
		       fin.read(fileContent);
		     
		       return fileContent;
		    }
		    catch(FileNotFoundException e)
		    {
		      System.out.println("File not found" + e);
		    }
		    catch(IOException ioe)
		    {
		      System.out.println("Exception while reading the file " + ioe);
		    } finally {
		    	if (fin != null) {
		    		try {
						fin.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
		    	}
		    		
		    }
		 	return null;
		 }
	

	@Ignore("Generator provides platform dependent images (fonts) see http://forums.activiti.org/en/viewtopic.php?f=6&t=4647&start=0")
	public void testOneNodeCount() throws IOException {
	    String id = FINANCIALREPORT_PROCESS_KEY;
	    DiagramLayerGenerator generator = new WriteNodeDescriptionDiagramLayer( new RepositoryServiceWrapperImpl((RepositoryServiceImpl) repositoryService ), new ProcessDiagramServiceImpl());
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put( WriteNodeDescriptionDiagramLayer.PROCESS_DEFINITION_ID, id);
	    params.put( "writeReportTask", 5);
	    
	    File expectedFile = new File("src/org.activiti.crystalball.processengine.wrapper.test/resources/org/activiti/crystalball/diagram/WriteCountLayerGeneratorTest.oneNumber.png" );
	    File generatedFile = new File("target/WriteCountLayerGeneratorTest.oneNumber.png" );
	    ImageIO.write( ImageIO.read(new ByteArrayInputStream( generator.generateLayer( "png", params)))
				, "png"
				, generatedFile);
	    assertTrue( FileUtils.contentEquals(expectedFile, generatedFile));	    	    
	}
}
