package org.activiti.crystalball.generator;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class ColorInterval {

	protected Color color = Color.red;
	protected Integer min = null;
	protected Integer max = null;
	
	protected static final Map<String, Color> colorMap;
	
	static {
		colorMap = new HashMap<String,Color>();
		colorMap.put("red", Color.red);
		colorMap.put("green", Color.green);
		colorMap.put("blue", Color.blue);
		colorMap.put("yellow", Color.yellow);
		colorMap.put("pink", Color.pink);
		colorMap.put("gray", Color.gray);
	}
	
	public ColorInterval() {
	}
	
	public ColorInterval(Integer min) {
		this();
		this.min = min;
	}

	public ColorInterval(Color color) {
		this();
		this.color = color;
	}

	public ColorInterval(String colorName) {
		this();
		this.color = colorMap.get(colorName);
	}
	
	public ColorInterval(Integer min, Color color) {
		this( color );
		this.min = min;
	}

	public ColorInterval(Integer min, String color) {
		this( color );
		this.min = min;
	}

	public ColorInterval(Integer min, Integer max, Color color) {
		this( min, color );
		this.max = max;		
	}

	public ColorInterval(Integer min, Integer max, String color) {
		this( min, color );
		this.max = max;		
	}

	/**
	 * is count inside the interval <min,max> are included
	 * in case min/max null -> all counts are OK
	 * @param percentage
	 * @return
	 */
	public boolean isInside(float count) {
		boolean inside = true;
		
		if ( min != null)
			inside = min <= count;
		if ( max != null && inside)
			inside = count <= max;
		return inside;
	}

}
