package org.activiti.crystalball.diagram;



public class ProcessDiagramCanvas extends org.activiti.engine.impl.bpmn.diagram.ProcessDiagramCanvas {
	
	  public ProcessDiagramCanvas(int width, int height) {
		  super( width, height);
	  }


	  public ProcessDiagramCanvas(int width, int height, int minX, int minY) {
		  super(width, height, minX, minY);
	  }


	public void drawStringToNode(String count, int x, int y, int width, int height) {
	    // count
	    if (count != null) {
	      String text = fitTextToWidth(count, width);
	      int textX = x + ((width - fontMetrics.stringWidth(text)) / 2);
	      int textY = y + fontMetrics.getHeight();
	      g.drawString(text, textX, textY);
	    }		
	}
	  

}
