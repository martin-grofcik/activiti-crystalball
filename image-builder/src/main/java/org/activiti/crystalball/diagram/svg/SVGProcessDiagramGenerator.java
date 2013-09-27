package org.activiti.crystalball.diagram.svg;

import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramCanvas;
import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.*;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 */
public class SVGProcessDiagramGenerator extends ProcessDiagramGenerator {

    public static InputStream generateDiagram(ProcessDefinitionEntity processDefinition, String imageType, List<String> highLightedActivities) {
        return generateDiagram(processDefinition, highLightedActivities, Collections.<String> emptyList()).generateImage(imageType);
    }

    public static InputStream generateDiagram(ProcessDefinitionEntity processDefinition, String imageType, List<String> highLightedActivities, List<String> highLightedFlows) {
        return generateDiagram(processDefinition, highLightedActivities, highLightedFlows).generateImage(imageType);
    }

    protected static ProcessDiagramCanvas generateDiagram(ProcessDefinitionEntity processDefinition, List<String> highLightedActivities, List<String> highLightedFlows) {
        ProcessDiagramCanvas processDiagramCanvas = initProcessDiagramCanvas(processDefinition);

        // Draw pool shape, if process is participant in collaboration
        if(processDefinition.getParticipantProcess() != null) {
            ParticipantProcess pProc = processDefinition.getParticipantProcess();
            processDiagramCanvas.drawPoolOrLane(pProc.getName(), pProc.getX(), pProc.getY(), pProc.getWidth(), pProc.getHeight());
        }

        // Draw lanes
        if(processDefinition.getLaneSets() != null && processDefinition.getLaneSets().size() > 0) {
            for(LaneSet laneSet : processDefinition.getLaneSets()) {
                if(laneSet.getLanes() != null && laneSet.getLanes().size() > 0) {
                    for(Lane lane : laneSet.getLanes()) {
                        processDiagramCanvas.drawPoolOrLane(lane.getName(), lane.getX(), lane.getY(), lane.getWidth(), lane.getHeight());
                    }
                }
            }
        }

        // Draw activities and their sequence-flows
        for (ActivityImpl activity : processDefinition.getActivities()) {
            drawActivity(processDiagramCanvas, activity, highLightedActivities, highLightedFlows);
        }
        return processDiagramCanvas;
    }

    protected static ProcessDiagramCanvas initProcessDiagramCanvas(ProcessDefinitionEntity processDefinition) {
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

        // Special case, see http://jira.codehaus.org/browse/ACT-1431
        if ( (processDefinition.getActivities() == null || processDefinition.getActivities().size() == 0)
                && (processDefinition.getLaneSets() == null || processDefinition.getLaneSets().size() == 0)) {
            // Nothing to show
            minX = 0;
            minY = 0;
        }

        return new SVGProcessDiagramCanvas(maxX + 10, maxY + 10, minX, minY);
    }
}
