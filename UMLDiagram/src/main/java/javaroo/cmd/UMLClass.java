import java.util.ArrayList;
import java.lang.StringBuilder;

public class UMLClass {

    private String name;
    private ArrayList<UMLFields> fields;

    // constructor
    public UMLClass(String name) {
        this.name = name;
        this.fields = new ArrayList<>();
    }
    // getters method that will work as a rename method
    public String getName() {
        return this.name;
    }

    public static UMLClass getClass(String className) {
        return UMLDiagram.getClasses().get(className); // this will return null if the class does not exist.
    }

    public ArrayList<UMLFields> getFields() {
        return this.fields;
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

    public static void renameClass(String oldName, String newName) {
        if (UMLDiagram.getClasses().containsKey(oldName)) {
            if (!UMLDiagram.getClasses().containsKey(newName)) {
                UMLDiagram.getClasses().remove(oldName);
                addClass(newName);
                System.out.println("Class '" + oldName + "' renamed to '" + newName + "'.");
            } else {
                System.out.println("Class '" + newName + "' already exists.");
            }
        } else {
            System.out.println("Class '" + oldName + "' does not exist.");
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

    // method to check if a field exists
    public UMLFields fieldExists(String name)
    {
        for(UMLFields a : this.fields)
        {
            if(a.getName().equals(name))
            {
                return a;
            }
        }
        return null;
    }


    //Method to add a UMLFields object to the fields ArrayList with a name parameter
    public void addField(String name, String type, String visibility)
    {
        //check for the empty string in paramters or if input contains only spaces
        if(name.trim().isEmpty() || type.trim().isEmpty() || visibility.trim().isEmpty())
        {
            System.out.println("Invalid input");
            return;
        }
        
        //check if field already exists
        if(fieldExists(name) == null)
        {
            this.fields.add(new UMLFields(name, type, visibility));
            System.out.println("Field added: " + name);
        }
        else
        {
            System.out.println("Field already exists");
        }
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