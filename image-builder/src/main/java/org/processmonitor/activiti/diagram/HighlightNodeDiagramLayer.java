package org.processmonitor.activiti.diagram;

import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramCanvas;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.Lane;
import org.activiti.engine.impl.pvm.process.LaneSet;
import org.activiti.engine.impl.pvm.process.ParticipantProcess;
import org.activiti.engine.impl.pvm.process.TransitionImpl;

/**
 * highlight nodes in the process diagram 
 *
 */
public class HighlightNodeDiagramLayer extends AbstractProcessDiagramLayerGenerator implements DiagramLayerGenerator {

	public static String PROCESS_DEFINITION_ID = "processDefinitionId";
	public static String HIGHLIGHTED_ACTIVITIES = "highLightedActivities";

	public HighlightNodeDiagramLayer() {
	}
	
	public HighlightNodeDiagramLayer( RepositoryServiceImpl repositoryService) {
		this.repositoryService = repositoryService;
	}
	
	/**
	 * 
	 */
	@Override
	public byte[] generateLayer(String imageType, Map<String, Object> params) {

		// get parameters
		String processDefinitionId = (String) params.get( PROCESS_DEFINITION_ID );
	    @SuppressWarnings("unchecked")
		List<String> highLightedActivities = (List<String>) params.get( HIGHLIGHTED_ACTIVITIES );

		// get process definition entity
	    ProcessDefinitionEntity pde = (ProcessDefinitionEntity) ( ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition( processDefinitionId ));
	    
		org.activiti.engine.impl.bpmn.diagram.ProcessDiagramCanvas canvas = initProcessDiagramCanvas( pde );

		for (ActivityImpl activity : pde.getActivities()) {
	      // Draw highlighted activities
	      if (highLightedActivities.contains(activity.getId())) {
	    	  canvas.drawHighLight(activity.getX(), activity.getY(), activity.getWidth(), activity.getHeight());	      }
	    }
	    return convertToByteArray( imageType, canvas.generateImage(imageType));
	}

	private ProcessDiagramCanvas initProcessDiagramCanvas(
			ProcessDefinitionEntity processDefinition) {
	    int minX = Integer.MAX_VALUE;
	    int maxX = 0;
	    int minY = Integer.MAX_VALUE;
	    int maxY = 0;
	    
	    if(processDefinition.getParticipantProcess() != null) {
	      ParticipantProcess pProc = processDefinition.getParticipantProcess();
	      
	      minX = pProc.getX();
	      maxX = pProc.getX() + pProc.getWidth();
	      minY = pProc.getY();
	      maxY = pProc.getY() + pProc.getHeight();
	    }
	    
	    for (ActivityImpl activity : processDefinition.getActivities()) {

	      // width
	      if (activity.getX() + activity.getWidth() > maxX) {
	        maxX = activity.getX() + activity.getWidth();
	      }
	      if (activity.getX() < minX) {
	        minX = activity.getX();
	      }
	      // height
	      if (activity.getY() + activity.getHeight() > maxY) {
	        maxY = activity.getY() + activity.getHeight();
	      }
	      if (activity.getY() < minY) {
	        minY = activity.getY();
	      }

	      for (PvmTransition sequenceFlow : activity.getOutgoingTransitions()) {
	        List<Integer> waypoints = ((TransitionImpl) sequenceFlow).getWaypoints();
	        for (int i = 0; i < waypoints.size(); i += 2) {
	          // width
	          if (waypoints.get(i) > maxX) {
	            maxX = waypoints.get(i);
	          }
	          if (waypoints.get(i) < minX) {
	            minX = waypoints.get(i);
	          }
	          // height
	          if (waypoints.get(i + 1) > maxY) {
	            maxY = waypoints.get(i + 1);
	          }
	          if (waypoints.get(i + 1) < minY) {
	            minY = waypoints.get(i + 1);
	          }
	        }
	      }
	    }
	    
	    if(processDefinition.getLaneSets() != null && processDefinition.getLaneSets().size() > 0) {
	      for(LaneSet laneSet : processDefinition.getLaneSets()) {
	        if(laneSet.getLanes() != null && laneSet.getLanes().size() > 0) {
	          for(Lane lane : laneSet.getLanes()) {
	            // width
	            if (lane.getX() + lane.getWidth() > maxX) {
	              maxX = lane.getX() + lane.getWidth();
	            }
	            if (lane.getX() < minX) {
	              minX = lane.getX();
	            }
	            // height
	            if (lane.getY() + lane.getHeight() > maxY) {
	              maxY = lane.getY() + lane.getHeight();
	            }
	            if (lane.getY() < minY) {
	              minY = lane.getY();
	            }
	          }
	        }
	      }
	    }
	    
	    return new ProcessDiagramCanvas(maxX + 10, maxY + 10, minX, minY);
	}
		

}
