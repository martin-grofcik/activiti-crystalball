package org.activiti.crystalball.diagram.svg;

import org.activiti.crystalball.diagram.ImageTypes;
import org.activiti.crystalball.diagram.ProcessDiagramCanvas;
import org.activiti.engine.ActivitiException;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.transcoder.image.TIFFTranscoder;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import java.awt.*;
import java.io.*;

/**
 * SVG implementation of process diagram canvas.
 */
public class SVGProcessDiagramCanvas extends ProcessDiagramCanvas{
    public SVGProcessDiagramCanvas(int width, int height) {
        this(width, height, -1, -1);
    }

    public SVGProcessDiagramCanvas(int width, int height, int minX, int minY) {
        super(width, height, minX, minY);
        DOMImplementation domImpl =
                GenericDOMImplementation.getDOMImplementation();

        // Create an instance of org.w3c.dom.Document.
        String svgNS = "http://www.w3.org/2000/svg";
        Document document = domImpl.createDocument(svgNS, "svg", null);

        this.g = new SVGGraphics2D( document );
        ((SVGGraphics2D) this.g).setSVGCanvasSize( new Dimension( canvasWidth, canvasHeight));
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setPaint(Color.black);

        Font font = new Font("Arial", Font.BOLD, FONT_SIZE);
        g.setFont(font);
    }

    @Override
    public InputStream generateImage(String imageType) {
        if (closed) {
            throw new ActivitiException("ProcessDiagramGenerator already closed");
        }

        // Create the transcoder input.
        ByteArrayOutputStream ostream = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Writer writer = null;
        InputStream in = null;
        TranscoderOutput output = null;
        try {
            writer = new OutputStreamWriter(out, "UTF-8");
            ((SVGGraphics2D) g).stream(writer);
        } catch (SVGGraphics2DIOException e) {
            try {
                writer.close();
            } catch (IOException e1) {

            }
            throw new ActivitiException("Unable to generate image", e);
        } catch (UnsupportedEncodingException e) {
            throw new ActivitiException("Unsupported encoding for output stream", e);
        }

        in = new ByteArrayInputStream(out.toByteArray());

        if ((ImageTypes.SVG.equalsType(imageType))) {
            // SVG image is generated
            return in;
        }

        // Use trascoder to generate "real" image
        ostream = new ByteArrayOutputStream();
        TranscoderInput input = new TranscoderInput(in);

        // Create the transcoder output.
        output = new TranscoderOutput(ostream);

        // Save the image.
        ImageTranscoder t = null;
        if (ImageTypes.JPG.equalsType(imageType)) {
            // Create a JPEG transcoder
            t = new JPEGTranscoder();
            // Set the transcoding hints.
            t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, new Float(.8));
        } else if (ImageTypes.PNG.equalsType(imageType)) {
            t = new PNGTranscoder();
        } else if (ImageTypes.TIFF.equalsType(imageType)) {
            t = new TIFFTranscoder();
        }
        try {
            t.transcode(input, output);

            return new ByteArrayInputStream(ostream.toByteArray());

        } catch (TranscoderException e) {
            throw new ActivitiException("Unable to transcode image", e);
        } finally {
            try {
                if (ostream != null) {
                    ostream.flush();
                    ostream.close();
                }
                if (!ImageTypes.SVG.equalsType(imageType))
                    // close in stream only in the case when it is not needed anymore
                    in.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
