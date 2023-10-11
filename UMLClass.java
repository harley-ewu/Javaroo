import java.util.ArrayList;

public class Class {

    private String name;
    private ArrayList<UMLAttributes> attributes;

    public Class(String name) {
        this.name = name;
        this.attributes = new ArrayList<>();
    }

    public void add(String name) {
        if (!isNameValid(name) || classList.contains(name)) {
            System.out.println("Invalid or duplicated name.");
        } else {
            classList.add(name);
            System.out.println("Name added: " + name);
        }
        
    }

    // dont need rename method
    /*public void rename(String oldName, String newName) {
        if (!isNameValid(newName) || classList.contains(newName)) {
            System.out.println("Invalid or duplicated name.");
        } 
        else if (!classList.contains(oldName)) {
            System.out.println("Name not found.");
        } 
        else {
            classList.set(classList.indexOf(oldName), newName);
            System.out.println("Name renamed from " + oldName + " to " + newName);
        }

    }*/

    // rename method
    public String getName() {
        return this.name;

    }

    public void setName(String name) {
        this.name = name;

    }

    public void delete(String name) {
        if (!classList.contains(name)) {
            System.out.println("Name not found.");
        } else {
            classList.remove(name);
            System.out.println("Name deleted: " + name);
        }

    }

    // helper method to check if name is valid
    private boolean isNameValid(String name) {
        return name != null && !name.isEmpty();
        
    }

}