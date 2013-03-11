package org.activiti.crystalball.diagram;

import java.util.Map;

/**
 * Generate layer to process diagram 
 *
 */
public interface DiagramLayerGenerator {
	/**
	 * generate layer to process diagram (byte[])
	 * 
	 * @param imageType type of the image produced
	 * @param params parameters map  
	 * @return byte array which represents image  
	 */
	public byte[] generateLayer(String imageType, Map<String, Object> params);
}
