package org.richfaces.tests.metamer.bean.issues.rf13098;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.richfaces.resource.SerializableResource;

@Named("cdiBean")
@RequestScoped
public class RF13098 implements SerializableResource {
    private static final long serialVersionUID = 1L;
    int height = 120;
    int width = 300;
    Integer color;
    float scale;
    String text;

    public RF13098() {
        text = "RichFaces 4.0";
        color = 1000;
        scale = 2;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
