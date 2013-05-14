package org.activiti.crystalball.anttasks;

/*
 * #%L
 * anttasks
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


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.activiti.crystalball.generator.AbstractProcessEngineGraphGenerator;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * ant task to generate graph 
 *
 */
public class GenerateGraphTask extends Task {
	/** app context which will be loaded */ 
    protected String appContext;
    /** graph generator bean */
    protected String generatorBean;
    /** report generation start date */
    protected String startDate;
    /** report generation end date */
    protected String endDate = null;   
    /** date time format for simpleDateFormatter */
    protected String dateFormat = "MMMM dd yyyy"; 
    
	/** process definition Id for which report is generated */
    protected String processDefinitionId;
    /** report filename where generator output is stored.*/
    protected String reportFileName;
   
    public void execute() {
    	
        log("Starting generator [" + generatorBean + "] from application context [" + appContext + "].", Project.MSG_INFO);

        // seting app context
        if (appContext==null) {
            throw new BuildException("No application context set.");
        }        
        FileSystemXmlApplicationContext applicationContext = new FileSystemXmlApplicationContext( appContext );		
		try {
			// getting generator
			AbstractProcessEngineGraphGenerator generator = null;		
			if ( generatorBean != null)
				generator = (AbstractProcessEngineGraphGenerator) applicationContext.getBean( generatorBean );
			if ( generator == null ) {
				applicationContext.close();
	            throw new BuildException("unable to get generator bean");
			}
			
			// running report generate
			try {
				generator.generateReport(processDefinitionId, getStartDate(), getEndDate(), reportFileName);
			} catch (IOException e) {
				log("Generator exception", Project.MSG_ERR);
	            throw new BuildException(e);
			} catch (ParseException e) {
				log("Generator exception - parsing dates", Project.MSG_ERR);
	            throw new BuildException(e);
			}
		} finally {
			applicationContext.close();
		}
		
        log("Generator [" + generatorBean + "] execution from application context [" + appContext + "] done.", Project.MSG_INFO);
    }
    
    public void setAppContext(String appContext) {
        this.appContext = appContext;
    }

	public void setGeneratorBean(String generatorBean) {
		this.generatorBean = generatorBean;
	}
	
    public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public void setReportFileName(String reportFileName) {
		this.reportFileName = reportFileName;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	protected Date getStartDate() throws ParseException {
		if (startDate != null)
			return new SimpleDateFormat(dateFormat).parse(startDate);
		return null;
	}

	protected Date getEndDate() throws ParseException {
		if (endDate != null)
			return new SimpleDateFormat(dateFormat).parse(endDate);
		return null;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
