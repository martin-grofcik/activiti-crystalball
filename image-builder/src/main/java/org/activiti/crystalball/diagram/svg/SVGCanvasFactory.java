package org.activiti.crystalball.diagram.svg;

import org.activiti.crystalball.diagram.ProcessDiagramCanvas;
import org.activiti.crystalball.diagram.ProcessDiagramCanvasFactory;

/**
 *
 */
public class SVGCanvasFactory implements ProcessDiagramCanvasFactory {
    @Override
    public ProcessDiagramCanvas createCanvas(int width, int height, int minX, int minY) {
        return new SVGProcessDiagramCanvas(width, height, minX, minY);
    }
}
