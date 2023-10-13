import java.util.ArrayList;
import java.lang.StringBuilder;

public class UMLClass {

    private String name;
    private ArrayList<UMLAttributes> attributes;

    // constructor
    public UMLClass(String name) {
        this.name = name;
        this.attributes = new ArrayList<>();
    }
    // getters method that will work as a rename method
    public String getName() {
        return this.name;

    }

    public ArrayList<UMLAttributes> getAttributes() {
        return attributes;
    }

    // setters
    public void setName(String name) {
        this.name = name;

    }

     public static void addClass(String name) {
        if(!UMLDiagram.getClasses().containsKey(name)){
            UMLDiagram.getClasses().put(name, new UMLClass(name));
            System.out.println("Class added: " + name);
        } else {
            System.out.println("Class" + name +" exits");
        }
    }

    public static void deleteClass(UMLClass c, UMLDiagram diagram) {
        // statement to check if class exists
        if(c == null) {
            System.out.println("Class not found");
            return;
        }
        // if class exists, classes will be fetched and removed
        diagram.getClasses().remove(c.getName());
        System.out.println("Class deleted: " + c.getName());

    }

    // method to check if attribute exists
    public UMLAttributes attributesExists(String name)
    {
        for(UMLAttributes a : attributes)
        {
            if(a.getName().equals(name))
            {
                return a;
            }
        }
        return null;
    }

    // toString method
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Class: ").append(this.name).append("\n");
        sb.append("Attributes:\n");
        for (UMLAttributes attribute : this.attributes) {
            sb.append(" • ").append(attribute).append("\n");
        }
        return sb.toString();

}


}