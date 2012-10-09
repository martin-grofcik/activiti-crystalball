package org.processmonitor.activiti.diagram;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MergeImages {


	public void merge(String outputFileName, String imageType,
			byte[] isLayer) throws IOException, FileNotFoundException {
		
		byte[][] layers = new byte[1][];
		layers[0] = isLayer;
		this.merge(outputFileName, imageType, layers);
	}
	
	public void merge(String outputFileName, String imageType,
			byte[]... isLayers) throws IOException, FileNotFoundException {

		// merge layers into process diagram and compare to expected results
		ImageIO.write(createBufferedImage(isLayers), imageType, new File(
				outputFileName));
	}

	protected BufferedImage createBufferedImage(byte[][] isLayers)
			throws IOException {

		Image[] layers = new Image[isLayers.length];

		for (int i = 0; i < isLayers.length; i++) {
			layers[i] = ImageIO.read(new ByteArrayInputStream(isLayers[i]));
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

	protected Dimension max(Dimension a, Dimension b) {
		Dimension d = new Dimension();
		d.width = Math.max(a.width, b.width);
		d.height = Math.max(a.height, b.height);
		return d;
	}

}
