package org.activiti.crystalball.simulator.parse;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.activiti.engine.impl.bpmn.parser.AbstractBpmnParseListener;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ScopeImpl;
import org.activiti.engine.impl.util.xml.Element;
import org.activiti.crystalball.simulator.delegate.AbstractSimulationActivityBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	  public void parseUserTask(Element userTaskElement, ScopeImpl scope, ActivityImpl activity) {
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
