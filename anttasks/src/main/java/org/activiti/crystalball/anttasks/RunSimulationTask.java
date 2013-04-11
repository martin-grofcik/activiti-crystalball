package org.activiti.crystalball.anttasks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.activiti.crystalball.simulator.SimulationRun;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * run simulation ant task 
 *
 */
public class RunSimulationTask extends Task {
	
	/** app context which will be loaded */ 
    protected String appContext;
    /** simulation run bean */
    protected String simRunBean;

    /** simulation generation start date string*/
    protected String startDate;
    /** simulation generation end date string*/
    protected String endDate = null;
    /** date time format for SimpleDateFormatter */
    protected String dateFormat = "MMMM dd yyyy";  
    
    public void execute() {
    	
        log("Starting simulation run [" + simRunBean + "] from application context [" + appContext + "].");

    	// seting app context
        if (appContext==null) {
            throw new BuildException("No application context set.");
        }
        FileSystemXmlApplicationContext applicationContext = new FileSystemXmlApplicationContext( appContext );	

		try {
			// getting simulation run
			SimulationRun simRun = null;		
			if ( simRunBean != null)
				simRun = (SimulationRun) applicationContext.getBean( simRunBean );
			if ( simRun == null ) {
				log("Using default simulation run bean", Project.MSG_WARN);
				simRun = applicationContext.getBean( SimulationRun.class);
			}
			if ( simRun  == null ) {
	            throw new BuildException("unable to get sim run bean");
			}
			
			//
			// execute simulation run
			//
			simRun.execute(getStartDate(), getEndDate());
		} catch (ParseException e) {
			log("Simulation task exception - parsing dates", Project.MSG_ERR);
            throw new BuildException(e);
		} finally {
			applicationContext.close();
		}
		
        log("Simulation run [" + simRunBean + "] from application context [" + appContext + "] done.");
        
    }
    
    public void setAppContext(String appContext) {
        this.appContext = appContext;
    }

	public void setSimRunBean(String simRunBean) {
		this.simRunBean = simRunBean;
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
