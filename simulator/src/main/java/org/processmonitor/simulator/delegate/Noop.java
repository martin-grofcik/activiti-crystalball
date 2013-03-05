package org.processmonitor.simulator.delegate;

import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ScopeImpl;
import org.activiti.engine.impl.util.xml.Element;

/**
 * perform no operation and take the first next transition
 */
public class Noop extends AbstractSimulationActivityBehavior {

	public Noop(Element scriptTaskElement, ScopeImpl scope, ActivityImpl activity) {
		super(scriptTaskElement, scope, activity);
	}

	public void execute(ActivityExecution execution) throws Exception {
	    PvmTransition transition = execution.getActivity().getOutgoingTransitions().get(0);
	    execution.take(transition);
	  }

}
