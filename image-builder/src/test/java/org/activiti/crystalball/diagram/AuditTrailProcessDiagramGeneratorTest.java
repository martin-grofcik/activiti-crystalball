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
import org.activiti.engine.task.Task;
import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AuditTrailProcessDiagramGeneratorTest extends PluggableActivitiTestCase {

		private static String FINANCIALREPORT_PROCESS_KEY = "financialReport";

		private String processInstanceId;

		protected void setUp() throws Exception {
			super.setUp();
			repositoryService
					.createDeployment()
					.addClasspathResource(
							"org/activiti/examples/bpmn/usertask/FinancialReportProcess1.bpmn20.xml")
					.addClasspathResource(
							"org/activiti/examples/bpmn/usertask/FinancialReportProcess1.png")
					.deploy();

			processInstanceId =runtimeService.startProcessInstanceByKey(FINANCIALREPORT_PROCESS_KEY, "BUSINESS-KEY-1").getId();
		}

		protected void tearDown() throws Exception {
			for (org.activiti.engine.repository.Deployment deployment : repositoryService
					.createDeploymentQuery().list()) {
				repositoryService.deleteDeployment(deployment.getId(), true);
			}
			super.tearDown();
		}
	@Test @Ignore("Order ")
	public void test() throws IOException {
		AuditTrailProcessDiagramGenerator generator = new AuditTrailProcessDiagramGenerator();
		generator.setHistoryService(processEngine.getHistoryService());
		generator.setRepositoryService((RepositoryServiceImpl) processEngine.getRepositoryService());

		Map<String, Object> params = new HashMap<String,Object>();
		params.put( AuditTrailProcessDiagramGenerator.PROCESS_INSTANCE_ID, processInstanceId);
		
		ImageIO.write( ImageIO.read( generator.generateLayer("png", params))
				, "png"
				, new File( "target/auditTrail1.png"));
		
		int i = 1;
		while (processEngine.getTaskService().createTaskQuery().count() >0 ) {
			Task task = processEngine.getTaskService().createTaskQuery().singleResult();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            processEngine.getTaskService().complete( task.getId());

			File generatedFile = new File( "target/auditTrail"+ ++i +".png");
			
			ImageIO.write( ImageIO.read( generator.generateLayer("png", params))
					, "png"
					, generatedFile);
			assertTrue(FileUtils.contentEquals(generatedFile, new File("src/test/resources/org/activiti/crystalball/diagram/auditTrail"+i+".png")));
		}
	}

    @Test
    public void testSVG() throws IOException {
        AuditTrailProcessDiagramGenerator generator = new AuditTrailProcessDiagramGenerator(new SVGCanvasFactory());
        generator.setHistoryService(processEngine.getHistoryService());
        generator.setRepositoryService((RepositoryServiceImpl) processEngine.getRepositoryService());

        Map<String, Object> params = new HashMap<String,Object>();
        params.put( AuditTrailProcessDiagramGenerator.PROCESS_INSTANCE_ID, processInstanceId);

        FileUtils.copyInputStreamToFile( generator.generateLayer("svg", params), new File( "target/auditTrail1.svg"));

        int i = 1;
        while (processEngine.getTaskService().createTaskQuery().count() >0 ) {
            Task task = processEngine.getTaskService().createTaskQuery().singleResult();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            processEngine.getTaskService().complete( task.getId());

            File generatedFile = new File( "target/auditTrail"+ ++i +".svg");

            FileUtils.copyInputStreamToFile( generator.generateLayer("svg", params), generatedFile);
            assertTrue(FileUtils.contentEquals(generatedFile, new File("src/test/resources/org/activiti/crystalball/diagram/auditTrail"+i+".svg")));
        }
    }

}
