package org.activiti.crystalball.simulator.parse;

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


import org.activiti.crystalball.simulator.SimulationEvent;
import org.activiti.crystalball.simulator.delegate.AbstractSimulationActivityBehavior;
import org.activiti.crystalball.simulator.delegate.UserTaskExecutionListener;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.bpmn.parser.AbstractBpmnParseListener;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ScopeImpl;
import org.activiti.engine.impl.util.xml.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * parse listener to change behavior of script tasks..., to support simulation
 * 
 */
public class SimulationBpmnParseListener extends AbstractBpmnParseListener {

	/**
	 * The namespace of the simulator custom BPMN extensions.
	 */
	public static final String SIMULATION_BPMN_EXTENSIONS_NS = "http://crystalball.org/simulation";

	/** 
	 * behavior used for node during simulation
	 */
	public static final String SIMULATION_BEHAVIOR = "behavior";

	private static Logger log = LoggerFactory.getLogger(SimulationBpmnParseListener.class);
		
	public SimulationBpmnParseListener() {
	}
	
	@Override
    public void parseServiceTask(Element serviceTaskElement, ScopeImpl scope, ActivityImpl activity) {
	  	setSimulationBehavior(serviceTaskElement, scope, activity);
	}
	
	public void parseUserTask(Element userTaskElement, ScopeImpl scope, ActivityImpl activity) {
		    // add create task execution listener to schedule task createEvent
			UserTaskActivityBehavior userTaskActivity = (UserTaskActivityBehavior) activity.getActivityBehavior();
			userTaskActivity.getTaskDefinition().addTaskListener( TaskListener.EVENTNAME_CREATE, new  UserTaskExecutionListener(SimulationEvent.TYPE_TASK_CREATE));
		  	
		  	setSimulationBehavior(userTaskElement, scope, activity);
	  }	
	  
	public void parseScriptTask(Element scriptTaskElement, ScopeImpl scope, ActivityImpl activity) {
		setSimulationBehavior(scriptTaskElement, scope, activity);
	}

	@SuppressWarnings("unchecked")
	private void setSimulationBehavior(Element scriptTaskElement,
			ScopeImpl scope, ActivityImpl activity) {
		String behaviorClassName = scriptTaskElement.attributeNS( SIMULATION_BPMN_EXTENSIONS_NS, SIMULATION_BEHAVIOR);
		if (behaviorClassName != null) {
			log.debug("Scripting task ["+ activity.getId()+"] setting behavior to ["+behaviorClassName+"]");
			Class<AbstractSimulationActivityBehavior> behaviorClass = null;
			Constructor<AbstractSimulationActivityBehavior> constructor = null;
		    try {
				behaviorClass = (Class<AbstractSimulationActivityBehavior>) Class.forName(behaviorClassName);
				constructor = behaviorClass.getDeclaredConstructor(Element.class, ScopeImpl.class, ActivityImpl.class);			
				activity.setActivityBehavior( (ActivityBehavior) constructor.newInstance(scriptTaskElement, scope, activity));			
			} catch (ClassNotFoundException e) {
				log.error( "unable to set simulation behavior class[" + behaviorClassName +"]", e);
			} catch (SecurityException e) {
				log.error( "unable to set simulation behavior class[" + behaviorClassName +"]", e);
			} catch (NoSuchMethodException e) {
				log.error( "unable to set simulation behavior class[" + behaviorClassName +"]", e);
			} catch (IllegalArgumentException e) {
				log.error( "unable to set simulation behavior class[" + behaviorClassName +"]", e);
			} catch (InstantiationException e) {
				log.error( "unable to set simulation behavior class[" + behaviorClassName +"]", e);
			} catch (IllegalAccessException e) {
				log.error( "unable to set simulation behavior class[" + behaviorClassName +"]", e);
			} catch (InvocationTargetException e) {
				log.error( "unable to set simulation behavior class[" + behaviorClassName +"]", e);
			}
		}
	}

}
