import java.util.ArrayList;
import java.lang.StringBuilder;

public class UMLClass {

    private String name;
    private ArrayList<UMLFields> fields;
    private ArrayList<UMLMethods> methods;

    // constructor
    public UMLClass(String name) {
        this.name = name;
        this.fields = new ArrayList<>();
        this.methods = new ArrayList<>();
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

    //Method to remove a UMLFields object from the fields ArrayList with a name parameter
    public void removeField(String name)
    {
        //check for the empty string in paramters or if input contains only spaces
        if(name.trim().isEmpty())
        {
            System.out.println("Invalid input");
            return;
        }
        
        //check if field exists
        if(fieldExists(name) != null)
        {
            this.fields.remove(fieldExists(name));
            System.out.println("Field removed: " + name);
        }
        else
        {
            System.out.println("Field does not exist");
        }
    }

    //Method to check if a UMLMethods object exists in the methods ArrayList
    public UMLMethods methodExists(String name, ArrayList<String> parameters)
    {
        for(UMLMethods m : this.methods)
        {
            if(m.getName().equals(name) && m.getParameters().equals(parameters))
            {
                return m;
            }
        }
        return null;
    }

    //Method to add a UMLMethods object to the methods ArrayList with a name, returnType, and parameters parameter
    public void addMethod(String name, String returnType, ArrayList<String> parameters)
    {
        //check for the empty string in paramters or if input contains only spaces
        if(name.trim().isEmpty() || returnType.trim().isEmpty() || parameters == null)
        {
            System.out.println("Invalid input");
            return;
        }
        
        //check if method already exists
        if(methodExists(name, parameters) == null)
        {
            this.methods.add(new UMLMethods(name, returnType, parameters));
            System.out.println("Method added: " + name);
        }
        else
        {
            System.out.println("Method with that name and those parameters already exists");
        }
    }

    //Method that takes an int as a parameter and removes the UMLMethods object at that index from the methods ArrayList
    public void removeMethod(int index)
    {
        //check if index is out of bounds
        if(index < 0 || index >= this.methods.size())
        {
            System.out.println("Invalid index");
            return;
        }
        
        //remove method at index
        this.methods.remove(index);
        System.out.println("Method removed");
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