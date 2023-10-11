import java.util.ArrayList;

public class Class {

    private String name;
    private ArrayList<UMLAttributes> attributes;

    // constructor
    public Class(String name) {
        this.name = name;
        this.attributes = new ArrayList<>();
    }

    // getters method that will work as a rename method
    public String getName() {
        return this.name;

    }
    // setters 
    public void setName(String name) {
        this.name = name;

    }

     public static void addClass(String name, UMLDiagram diagram) {
        //diagram.getClasses().add(new Class(name));
        diagram.getClasses().put(name, new Class(name));
        System.out.println("Class added: " + name);
        
    }

    public void deleteClass(UMLClass c, UMLDiagram diagram) {
        // statement to check if class exists
        if(c == null) {
            System.out.println("Class not found");
            return;
        }
        // if class exists, classes will be fetched and removed
        diagram.getClasses().remove(c.getName());
        System.out.println("Class deleted: " + c.getName());

    }

    // toString method
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Class: ").append(this.name).append("\n");
        sb.append("Attributes:\n");
        for (String attribute : this.attributes) {
            sb.append(" • ").append(attribute).append("\n");
        }
        return sb.toString();

}


}