package org.activiti.crystalball.diagram;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.activiti.engine.impl.RepositoryServiceImpl;

public abstract class AbstractProcessDiagramLayerGenerator implements DiagramLayerGenerator{

	public static String PROCESS_DEFINITION_ID = "processDefinitionId";
	protected RepositoryServiceImpl repositoryService;
	protected static Logger log = Logger.getLogger(AbstractProcessDiagramLayerGenerator.class.getName());

	public AbstractProcessDiagramLayerGenerator() {
		super();
	}

	protected byte[] convertToByteArray(String imageType, InputStream diagramIs) {
		ByteArrayOutputStream baos = null;
	    byte[] imageInByte = null;
	    try {
		    BufferedImage image = ImageIO.read( diagramIs);
		    baos = new ByteArrayOutputStream();
		    ImageIO.write( image, imageType, baos );
		    baos.flush();
		    imageInByte = baos.toByteArray();
	    } catch( IOException e) {
	    	log.log( Level.SEVERE, "problem in getting processImage", e);
	    } finally
	    {
		    if (baos != null) {
		    	try {
		    	baos.close();
			    } catch( IOException e) {
			    	log.log( Level.SEVERE, "problem in getting processImage", e);
			    }
		    }
		    if (diagramIs != null) {
		    	try {
		    	diagramIs.close();
		    	} catch( IOException e) {
		    		log.log( Level.SEVERE, "problem in getting processImage", e);
		    	}
		    }
	    }
		return imageInByte;
	}

	public RepositoryServiceImpl getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryServiceImpl repositoryService) {
		this.repositoryService = repositoryService;
	}

	abstract public byte[] generateLayer(String imageType, Map<String, Object> params);
}