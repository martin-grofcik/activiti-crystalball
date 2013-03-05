package org.processmonitor.simulator.delegate;

import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ScopeImpl;
import org.activiti.engine.impl.util.xml.Element;

public abstract class AbstractSimulationActivityBehavior implements ActivityBehavior {

	public AbstractSimulationActivityBehavior(Element scriptTaskElement, ScopeImpl scope, ActivityImpl activity) {
		
	}
	
	abstract public void execute(ActivityExecution execution) throws Exception;
}
