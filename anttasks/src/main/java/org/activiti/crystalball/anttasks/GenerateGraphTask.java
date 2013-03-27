package org.activiti.crystalball.anttasks;

import java.io.IOException;
import java.util.Date;

import org.activiti.crystalball.generator.AbstractGraphGenerator;
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
    protected Date startDate;
    /** report generation end date */
    protected Date endDate = null;   
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
			AbstractGraphGenerator generator = null;		
			if ( generatorBean != null)
				generator = (AbstractGraphGenerator) applicationContext.getBean( generatorBean );
			if ( generator == null ) {
				applicationContext.close();
	            throw new BuildException("unable to get generator bean");
			}
			
			// running report generate
			try {
				generator.generateReport(processDefinitionId, startDate, endDate, reportFileName);
			} catch (IOException e) {
				log("Generator exception", Project.MSG_ERR);
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

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
    public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public void setReportFileName(String reportFileName) {
		this.reportFileName = reportFileName;
	}


}
