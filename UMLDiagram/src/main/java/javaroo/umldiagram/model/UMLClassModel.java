package javaroo.umldiagram.model;

import java.util.ArrayList;
import java.util.List;

public class UMLClassModel {
    public String className;
    public List<String> attributes;
    public List<String> relationships;

    public UMLClassModel(String className, ArrayList<String> attributes, ArrayList<String> methods) {
        this.className = className;
        this.attributes = attributes;
        this.relationships = methods;
    }

    public UMLClassModel() {
    }

    @Override
    public String toString() {
        return "UMLClassModel{" +
                "className='" + className + '\'' +
                '}';
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }

    public List<String> getRelationships() {
        return relationships;
    }

    public void setRelationships(List<String> methods) {
        this.relationships = methods;
    }


}
