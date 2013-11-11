package org.activiti.crystalball.diagram.svg;

import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramCanvas;
import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.*;
import org.activiti.engine.impl.pvm.process.Lane;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 */
public class SVGProcessDiagramGenerator extends ProcessDiagramGenerator {

    public static InputStream generateDiagram(BpmnModel bpmnModel, String imageType, List<String> highLightedActivities) {
        return generateDiagram(bpmnModel, highLightedActivities, Collections.<String> emptyList()).generateImage(imageType);
    }

    public static InputStream generateDiagram(BpmnModel bpmnModel, String imageType, List<String> highLightedActivities, List<String> highLightedFlows) {
        return generateDiagram(bpmnModel, highLightedActivities, highLightedFlows).generateImage(imageType);
    }

    protected static ProcessDiagramCanvas generateDiagram(BpmnModel bpmnModel, List<String> highLightedActivities, List<String> highLightedFlows) {
      ProcessDiagramCanvas processDiagramCanvas = initProcessDiagramCanvas(bpmnModel);

//    // Draw pool shape, if process is participant in collaboration
      for (Pool pool : bpmnModel.getPools()) {
        GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(pool.getId());
        processDiagramCanvas.drawPoolOrLane(pool.getName(), (int) graphicInfo.getX(), (int) graphicInfo.getY(),
            (int) graphicInfo.getWidth(), (int) graphicInfo.getHeight());
      }

      // Draw lanes
      for (Process process : bpmnModel.getProcesses()) {
        for (org.activiti.bpmn.model.Lane lane : process.getLanes()) {
          GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(lane.getId());
          processDiagramCanvas.drawPoolOrLane(lane.getName(), (int) graphicInfo.getX(), (int) graphicInfo.getY(),
              (int) graphicInfo.getWidth(), (int) graphicInfo.getHeight());
        }
      }

      // Draw activities and their sequence-flows
      for (FlowNode flowNode : bpmnModel.getProcesses().get(0).findFlowElementsOfType(FlowNode.class)) {
        drawActivity(processDiagramCanvas, bpmnModel, flowNode, highLightedActivities, highLightedFlows);
      }

      // Draw artifacts
      for (Process process : bpmnModel.getProcesses()) {
        for (Artifact artifact : process.getArtifacts()) {
          drawArtifact(processDiagramCanvas, bpmnModel, artifact);
        }
      }

      return processDiagramCanvas;
    }

    protected static ProcessDiagramCanvas initProcessDiagramCanvas(BpmnModel bpmnModel) {
      // We need to calculate maximum values to know how big the image will be in its entirety
      double minX = Double.MAX_VALUE;
      double maxX = 0;
      double minY = Double.MAX_VALUE;
      double maxY = 0;

      for (Pool pool : bpmnModel.getPools()) {
        GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(pool.getId());
        minX = graphicInfo.getX();
        maxX = graphicInfo.getX() + graphicInfo.getWidth();
        minY = graphicInfo.getY();
        maxY = graphicInfo.getY() + graphicInfo.getHeight();
      }

      List<FlowNode> flowNodes = gatherAllFlowNodes(bpmnModel);
      for (FlowNode flowNode : flowNodes) {

        GraphicInfo flowNodeGraphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());

        // width
        if (flowNodeGraphicInfo.getX() + flowNodeGraphicInfo.getWidth() > maxX) {
          maxX = flowNodeGraphicInfo.getX() + flowNodeGraphicInfo.getWidth();
        }
        if (flowNodeGraphicInfo.getX() < minX) {
          minX = flowNodeGraphicInfo.getX();
        }
        // height
        if (flowNodeGraphicInfo.getY() + flowNodeGraphicInfo.getHeight() > maxY) {
          maxY = flowNodeGraphicInfo.getY() + flowNodeGraphicInfo.getHeight();
        }
        if (flowNodeGraphicInfo.getY() < minY) {
          minY = flowNodeGraphicInfo.getY();
        }

        for (SequenceFlow sequenceFlow : flowNode.getOutgoingFlows()) {
          List<GraphicInfo> graphicInfoList = bpmnModel.getFlowLocationGraphicInfo(sequenceFlow.getId());
          for (GraphicInfo graphicInfo : graphicInfoList) {
            // width
            if (graphicInfo.getX() > maxX) {
              maxX = graphicInfo.getX();
            }
            if (graphicInfo.getX() < minX) {
              minX = graphicInfo.getX();
            }
            // height
            if (graphicInfo.getY() > maxY) {
              maxY = graphicInfo.getY();
            }
            if (graphicInfo.getY() < minY) {
              minY = graphicInfo.getY();
            }
          }
        }
      }

      List<Artifact> artifacts = gatherAllArtifacts(bpmnModel);
      for (Artifact artifact : artifacts) {

        GraphicInfo artifactGraphicInfo = bpmnModel.getGraphicInfo(artifact.getId());

        if (artifactGraphicInfo != null) {
          // width
          if (artifactGraphicInfo.getX() + artifactGraphicInfo.getWidth() > maxX) {
            maxX = artifactGraphicInfo.getX() + artifactGraphicInfo.getWidth();
          }
          if (artifactGraphicInfo.getX() < minX) {
            minX = artifactGraphicInfo.getX();
          }
          // height
          if (artifactGraphicInfo.getY() + artifactGraphicInfo.getHeight() > maxY) {
            maxY = artifactGraphicInfo.getY() + artifactGraphicInfo.getHeight();
          }
          if (artifactGraphicInfo.getY() < minY) {
            minY = artifactGraphicInfo.getY();
          }
        }

        List<GraphicInfo> graphicInfoList = bpmnModel.getFlowLocationGraphicInfo(artifact.getId());
        if (graphicInfoList != null) {
          for (GraphicInfo graphicInfo : graphicInfoList) {
            // width
            if (graphicInfo.getX() > maxX) {
              maxX = graphicInfo.getX();
            }
            if (graphicInfo.getX() < minX) {
              minX = graphicInfo.getX();
            }
            // height
            if (graphicInfo.getY() > maxY) {
              maxY = graphicInfo.getY();
            }
            if (graphicInfo.getY() < minY) {
              minY = graphicInfo.getY();
            }
          }
        }
      }

      int nrOfLanes = 0;
      for (Process process : bpmnModel.getProcesses()) {
        for (org.activiti.bpmn.model.Lane l : process.getLanes()) {

          nrOfLanes++;

          GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(l.getId());
          // // width
          if (graphicInfo.getX() + graphicInfo.getWidth() > maxX) {
            maxX = graphicInfo.getX() + graphicInfo.getWidth();
          }
          if (graphicInfo.getX() < minX) {
            minX = graphicInfo.getX();
          }
          // height
          if (graphicInfo.getY() + graphicInfo.getHeight() > maxY) {
            maxY = graphicInfo.getY() + graphicInfo.getHeight();
          }
          if (graphicInfo.getY() < minY) {
            minY = graphicInfo.getY();
          }
        }
      }

      // Special case, see http://jira.codehaus.org/browse/ACT-1431
      if (flowNodes.size() == 0 && bpmnModel.getPools().size() == 0 && nrOfLanes == 0) {
        // Nothing to show
        minX = 0;
        minY = 0;
      }

      return new SVGProcessDiagramCanvas((int) maxX + 10, (int) maxY + 10, (int) minX, (int) minY);
    }
}
