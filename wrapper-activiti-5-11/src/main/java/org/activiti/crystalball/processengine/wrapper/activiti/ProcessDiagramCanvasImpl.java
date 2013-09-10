package org.activiti.crystalball.processengine.wrapper.activiti;

/*
 * #%L
 * image-builder
 * %%
 * Copyright (C) 2012 - 2013 crystalball
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import org.activiti.crystalball.processengine.wrapper.ProcessDiagramCanvasInterface;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.RoundRectangle2D;



public class ProcessDiagramCanvasImpl extends org.activiti.engine.impl.bpmn.diagram.ProcessDiagramCanvas  implements ProcessDiagramCanvasInterface {
	
	  public ProcessDiagramCanvasImpl(int width, int height) {
		  super( width, height);
	  }


	  public ProcessDiagramCanvasImpl(int width, int height, int minX, int minY) {
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
	  
	public void drawHighLight(int x, int y, int width, int height, Color color) {
		Paint originalPaint = g.getPaint();
		Stroke originalStroke = g.getStroke();

		g.setPaint( color);
		g.setStroke(THICK_TASK_BORDER_STROKE);

		RoundRectangle2D rect = new RoundRectangle2D.Double(x, y, width,
				height, 20, 20);
		g.draw(rect);

		g.setPaint(originalPaint);
		g.setStroke(originalStroke);
	}
}
