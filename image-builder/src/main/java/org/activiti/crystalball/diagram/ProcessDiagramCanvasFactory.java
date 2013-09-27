package org.activiti.crystalball.diagram;

/**
 * Factory interface for getting process diagram canvases
 * @see ProcessDiagramCanvas
 */
public interface ProcessDiagramCanvasFactory {
    ProcessDiagramCanvas createCanvas(int width, int height, int minX, int minY);
}
