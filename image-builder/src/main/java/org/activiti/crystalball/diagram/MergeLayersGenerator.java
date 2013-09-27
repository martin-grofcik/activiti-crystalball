package org.activiti.crystalball.diagram;

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


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;

/**
 * 
 *
 */
public class MergeLayersGenerator extends AbstractProcessDiagramLayerGenerator {

	/**
	 * merge all provided image layer into one.
	 * images are merged in alphabetical order
	 */
	@Override
	public InputStream generateLayer(String imageType, Map<String, Object> params) {
		if (params == null )
			return null;
		Object[] keys = params.keySet().toArray();
		Arrays.sort( keys );
		InputStream[] layers = new InputStream[params.size()];
		int i = 0;
		for ( Object key : keys) {
			layers[ i++ ] = (InputStream) params.get( key.toString() );
		}
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		InputStream fis = null;
		BufferedImage image;
		try {
			image = createBufferedImage( layers);
			ImageIO.write(image,"png", os);
			os.flush();
			fis= new ByteArrayInputStream(os.toByteArray());
			
			return fis;
		} catch (IOException e) {
			log.log(Level.SEVERE, "Unable to merge image layers ", e);
			return null;
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					log.log(Level.SEVERE, "Unable to merge image layers ", e);
				}
			}
			
		}
		
	}

	protected BufferedImage createBufferedImage(InputStream[] isLayers)
			throws IOException {

		Image[] layers = new Image[isLayers.length];

		for (int i = 0; i < isLayers.length; i++) {
			layers[i] = ImageIO.read( isLayers[i]);
		}

		Dimension maxSize = new Dimension();
		for (int i = 0; i < layers.length; i++) {
			Dimension d = new Dimension(layers[i].getWidth(null),
					layers[i].getHeight(null));
			maxSize = max(maxSize, d);
		}
		BufferedImage image = new BufferedImage(maxSize.width, maxSize.height,
				BufferedImage.TYPE_INT_ARGB);

		Graphics g = image.getGraphics();

		for (int i = 0; i < layers.length; i++) {
			g.drawImage(layers[i], 0, 0, null);
		}
		g.dispose();
		return image;
	}
	
	static protected Dimension max(Dimension a, Dimension b) {
		Dimension d = new Dimension();
		d.width = Math.max(a.width, b.width);
		d.height = Math.max(a.height, b.height);
		return d;
	}
}
