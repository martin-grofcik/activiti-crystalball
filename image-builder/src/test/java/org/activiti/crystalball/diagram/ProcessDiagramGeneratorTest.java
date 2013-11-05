package org.activiti.crystalball.diagram;

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


import org.activiti.crystalball.diagram.svg.SVGCanvasFactory;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.test.PluggableActivitiTestCase;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessDiagramGeneratorTest extends PluggableActivitiTestCase {

	private static String FINANCIALREPORT_PROCESS_KEY = "financialReport";

	private List<String> processInstanceIds;

  @Before
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

  @Override
  @After
	protected void tearDown() throws Exception {
		for (org.activiti.engine.repository.Deployment deployment : repositoryService
				.createDeploymentQuery().list()) {
			repositoryService.deleteDeployment(deployment.getId(), true);
		}
		super.tearDown();
	}

  public void testGenerateProcessDefinitionSVG() throws IOException {
        String id = FINANCIALREPORT_PROCESS_KEY;
        DiagramLayerGenerator generator = new BasicProcessDiagramGenerator( (RepositoryServiceImpl) repositoryService, new SVGCanvasFactory());
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(AbstractProcessDiagramLayerGenerator.PROCESS_DEFINITION_ID, id);

        File expectedFile = new File("src/test/resources/org/activiti/crystalball/diagram/BasicProcessDiagramGeneratorTest.testSimpleProcessDefinition.svg" );
        File generatedFile = new File("target/BasicProcessDiagramGeneratorTest.testSimpleProcessDefinition.svg" );
        FileUtils.copyInputStreamToFile( generator.generateLayer("svg", params), generatedFile);

        assertTrue(FileUtils.contentEqualsIgnoreEOL(expectedFile, generatedFile, "UTF-8"));
    }
	
	public void testOneNodeHighlight() throws IOException {
	    String id = FINANCIALREPORT_PROCESS_KEY;
	    DiagramLayerGenerator generator = new HighlightNodeDiagramLayer( (RepositoryServiceImpl) repositoryService );
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put( HighlightNodeDiagramLayer.PROCESS_DEFINITION_ID, id);
	    List<String> highlightedActivities = new ArrayList<String>();
	    highlightedActivities.add("writeReportTask");
	    params.put( HighlightNodeDiagramLayer.HIGHLIGHTED_ACTIVITIES, highlightedActivities);
	    
	    File expectedFile = new File("src/test/resources/org/activiti/crystalball/diagram/HighlightNodeDiagramLayer.testOneNodeHighlight.png" );   
	    File generatedFile = new File("target/HighlightNodeDiagramLayer.testOneNodeHighlight.png" );   
	    ImageIO.write( ImageIO.read( generator.generateLayer("png", params))
				, "png"
				, generatedFile);
	    assertTrue( FileUtils.contentEquals(expectedFile, generatedFile));	    
	}

    public void testOneNodeHighlightSVG() throws IOException {
        String id = FINANCIALREPORT_PROCESS_KEY;
        DiagramLayerGenerator generator = new HighlightNodeDiagramLayer( (RepositoryServiceImpl) repositoryService, null, new SVGCanvasFactory());
        Map<String, Object> params = new HashMap<String, Object>();
        params.put( HighlightNodeDiagramLayer.PROCESS_DEFINITION_ID, id);
        List<String> highlightedActivities = new ArrayList<String>();
        highlightedActivities.add("writeReportTask");
        params.put( HighlightNodeDiagramLayer.HIGHLIGHTED_ACTIVITIES, highlightedActivities);

        File expectedFile = new File("src/test/resources/org/activiti/crystalball/diagram/HighlightNodeDiagramLayer.testOneNodeHighlight.svg" );
        File generatedFile = new File("target/HighlightNodeDiagramLayer.testOneNodeHighlight.svg" );
        FileUtils.copyInputStreamToFile( generator.generateLayer("svg", params), generatedFile);
        assertTrue( FileUtils.contentEqualsIgnoreEOL(expectedFile, generatedFile, "UTF-8"));
    }

    public void testMergeLayers() throws IOException {
	    DiagramLayerGenerator generator = new MergeLayersGenerator();
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put( "img1", getBytes("src/test/resources/org/activiti/crystalball/diagram/MergeLayerGenerator.testMergeLayers1.png"));
	    params.put( "img2", getBytes("src/test/resources/org/activiti/crystalball/diagram/MergeLayerGenerator.testMergeLayers2.png"));
	    
	    File expectedFile = new File("src/test/resources/org/activiti/crystalball/diagram/MergeLayerGenerator.testMergeLayers.png" );  
	    File generatedFile = new File("target/MergeLayerGenerator.testMergeLayers.png" );  
	    ImageIO.write( ImageIO.read( generator.generateLayer("png", params))
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
		     
		       return fin;
		    }
		    catch(FileNotFoundException e)
		    {
		      System.out.println("File not found" + e);
                try {
                    fin.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
		    } finally {
		    }
		 	return null;
		 }
	

	@Ignore("Generator provides platform dependent images (fonts) see http://forums.activiti.org/en/viewtopic.php?f=6109t=4647&start=0")
	public void testOneNodeCount() throws IOException {
	    String id = FINANCIALREPORT_PROCESS_KEY;
	    DiagramLayerGenerator generator = new WriteNodeDescriptionDiagramLayer( (RepositoryServiceImpl) repositoryService );
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put( WriteNodeDescriptionDiagramLayer.PROCESS_DEFINITION_ID, id);
	    params.put( "writeReportTask", 5);
	    
	    File expectedFile = new File("src/test/resources/org/activiti/crystalball/diagram/WriteCountLayerGeneratorTest.oneNumber.png" );
	    File generatedFile = new File("target/WriteCountLayerGeneratorTest.oneNumber.png" );
	    ImageIO.write( ImageIO.read( generator.generateLayer("png", params))
				, "png"
				, generatedFile);
	    assertTrue( FileUtils.contentEquals(expectedFile, generatedFile));	    	    
	}

    public void testOneNodeCountSVG() throws IOException {
        String id = FINANCIALREPORT_PROCESS_KEY;
        DiagramLayerGenerator generator = new WriteNodeDescriptionDiagramLayer( (RepositoryServiceImpl) repositoryService, new SVGCanvasFactory() );
        Map<String, Object> params = new HashMap<String, Object>();
        params.put( WriteNodeDescriptionDiagramLayer.PROCESS_DEFINITION_ID, id);
        params.put( "writeReportTask", 5);

        File expectedFile = new File("src/test/resources/org/activiti/crystalball/diagram/WriteCountLayerGeneratorTest.oneNumber.svg" );
        File generatedFile = new File("target/WriteCountLayerGeneratorTest.oneNumber.svg" );
        FileUtils.copyInputStreamToFile( generator.generateLayer("svg", params), generatedFile);
        assertTrue( FileUtils.contentEqualsIgnoreEOL(expectedFile, generatedFile, "UTF-8"));
    }
}
