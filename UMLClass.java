import java.util.ArrayList;

public class Class {

    private String name;
    private ArrayList<UMLAttributes> attributes;

    public Class(String name) {
        this.name = name;
        this.attributes = new ArrayList<>();
    }

    // rename method
    public String getName() {
        return this.name;

    }

    public void setName(String name) {
        this.name = name;

    }

     public static void addClass(String name, UMLDiagram diagram) {
        diagram.getClasses().add(new Class(name));
        System.out.println("Class added: " + name);
        
    }

    public void deleteClass(UMLClass c, UMLDiagram diagram) {
        diagram.getClasses().remove(c);
        System.out.println("Class deleted: " + c.getName());

    }

}