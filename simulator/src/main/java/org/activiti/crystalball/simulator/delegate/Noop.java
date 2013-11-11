package org.activiti.crystalball.simulator.delegate;

/*
 * #%L
 * simulator
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


import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ScopeImpl;

/**
 * perform no operation and take the first next transition
 */
public class Noop extends AbstractSimulationActivityBehavior {

	public Noop( ScopeImpl scope, ActivityImpl activity) {
		super( scope, activity);
	}

	public void execute(ActivityExecution execution) throws Exception {
	    PvmTransition transition = execution.getActivity().getOutgoingTransitions().get(0);
	    execution.take(transition);
	  }

}
