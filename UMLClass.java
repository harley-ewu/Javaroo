public class Class {

    private String name;
    private Attribute[] attributes;
    private Relationship[] relationships;

    public Class() {
        this.name = "Class";

    }

    public Class(String name) {
        this.name = name;

    }

    public String getName() {
        return name;

    }

    public Class addName(String name) {
        this.name = name;
        return this;


    }

    public String toString() {
        return name + " [Class]";

    }
























}