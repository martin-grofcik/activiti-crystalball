package org.activiti.crystalball.processengine.wrapper;

import org.activiti.crystalball.processengine.wrapper.queries.ProcessDefinitionWrapper;

import java.io.InputStream;

/**
 */
public interface ProcessDiagramService {
    ProcessDiagramCanvasInterface createProcessDiagramCanvas(int width, int height, int minX, int minY);
    InputStream generatePngDiagram(ProcessDefinitionWrapper processDefinition);
}
