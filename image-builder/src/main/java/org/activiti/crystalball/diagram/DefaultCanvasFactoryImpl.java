package org.activiti.crystalball.diagram;

/**
 * returns default canvas implementation
 */
public class DefaultCanvasFactoryImpl implements ProcessDiagramCanvasFactory {

    @Override
    public ProcessDiagramCanvas createCanvas(int width, int height, int minX, int minY) {
        return new ProcessDiagramCanvas(width, height, minX, minY);
    }
}
