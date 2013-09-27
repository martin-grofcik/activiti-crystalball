package org.activiti.crystalball.diagram;

public enum ImageTypes {
    JPG ("jpg"),
    PNG ("png"),
    TIFF ("tiff"),
    SVG ("svg");


    private final String type;

    private ImageTypes(String type) {
        this.type = type;
    }

    public boolean equalsType(String type) {
        return (type==null)?false:this.type.equalsIgnoreCase(type);
    }

    public String toString() {
        return this.type;
    }
}
