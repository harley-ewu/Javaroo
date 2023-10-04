import java.util.ArrayList;

public class Class {

    private String name;
    private ArrayList<UMLAttributes> attributes;
    private ArrayList<UMLRelationships> relationships;

    public Class(String name) {
        this.name = name;
        this.attributes = new ArrayList<UMLAttributes>();
        this.relationships = new ArrayList<UMLRelationships>(); 

    }

    public String getName() {
        return name;

    }

    //addName method should fail if name is invalid or duplicate
    public class addName(String name) {
        if (!isNameValid(name)) {
            return false;
        
        }

        this.name = name;

    }

    //helper method to make sure that name is not invalid(empty or null)
    private boolean isNameValid(String name) {
        if (name == null || name.equals("")) {
            return false;

        }
        return true;

    }
    

    public String toString() {
        return name + " [Class]";

    }

}