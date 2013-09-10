package org.activiti.crystalball.processengine.wrapper.activiti;

import org.activiti.crystalball.processengine.wrapper.ProcessDiagramCanvasInterface;
import org.activiti.crystalball.processengine.wrapper.ProcessDiagramService;
import org.activiti.crystalball.processengine.wrapper.activiti.queries.ProcessDefinitionWrapperImpl;
import org.activiti.crystalball.processengine.wrapper.queries.ProcessDefinitionWrapper;
import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator;

import java.io.InputStream;

public class ProcessDiagramServiceImpl implements ProcessDiagramService {
    @Override
    public ProcessDiagramCanvasInterface createProcessDiagramCanvas(int width, int height, int minX, int minY) {
        return new ProcessDiagramCanvasImpl(width, height, minX, minY);
    }

    @Override
    public InputStream generatePngDiagram(ProcessDefinitionWrapper processDefinitionWrapper) {
        return ProcessDiagramGenerator.generatePngDiagram( ((ProcessDefinitionWrapperImpl) processDefinitionWrapper).getProcessDefinitionEntity());
    }

}
