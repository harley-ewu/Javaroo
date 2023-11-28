package javaroo.cmd;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.lang.StringBuilder;
import java.util.List;

public class UMLClass {

    private String name;
    private ArrayList<UMLFields> fields;
    private ArrayList<UMLMethods> methods;

    // Make sure to have the corresponding fields in your UMLClass class
    private double x;
    private double y;
    private double width;
    private double height;

    // constructor

    public UMLClass(String name) {
        this.name = name;
        this.fields = new ArrayList<>();
        this.methods = new ArrayList<>();
    }

    public UMLClass(String name, double x, double y) {
        this.name = name;
        this.fields = new ArrayList<>();
        this.methods = new ArrayList<>();
        this.x = x;
        this.y = y;
        this.width = 150;  // Default width, adjust as necessary
        this.height = 100; // Default height, adjust as necessary
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

    public ArrayList<UMLMethods> getMethods() {
        return this.methods;
    }

    // setters
    public void setName(String name) {
        this.name = name;

    }

    public static void addClassWithCoordinates(String name, double x, double y) {
        if (!UMLDiagram.getClasses().containsKey(name)) {
            UMLClass newClass = new UMLClass(name, x, y);
            UMLDiagram.getClasses().put(name, newClass);
            System.out.println("Class added: " + name);
        } else {
            System.out.println("Class '" + name + "' already exists.");
        }
    }


    // method to check if a field exists
    public UMLFields fieldExists(String name) {
        for (UMLFields f : fields) {
            if (name != null && name.equals(f.getName())) {
                return f;
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

        // renameField method
    public void renameField(String oldName, String newName) {
        // statement to check if field exists
        if(fieldExists(oldName) == null || newName.trim().isEmpty()) {
            System.out.println("Field does not exist or name is empty");
            return;
        }
        // if field exists, fields will be fetched and removed
        fieldExists(oldName).setName(newName);
        System.out.println("Field renamed from: " + oldName + " to " + newName);

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

    public void removeMethod(String methodName) {
        // Find the index of the method with the given name
        int index = -1;
        for (int i = 0; i < this.methods.size(); i++) {
            if (this.methods.get(i).getName().equals(methodName)) {
                index = i;
                break;
            }
        }

        // Check if the method was found
        if (index == -1) {
            System.out.println("Method not found: " + methodName);
            return;
        }

        // Remove the method at the found index
        this.methods.remove(index);
        System.out.println("Method removed: " + methodName);
    }


    // rename method that will rename an UMLMethod from a class at a given index
    public void renameMethod(int index, String newName) {
        // check if index is out of bounds or empty string
        if (index < 0 || index >= this.methods.size() || newName.trim().isEmpty()) {
            System.out.println("Invalid index or empty name");
            return;
        }
        // rename method at index
        this.methods.get(index).setName(newName);
        System.out.println("Method renamed to: " + newName);
    }

    public void renameMethod(String oldName, String newName) {
        // check if oldName or newName is null or empty
        if (oldName == null || oldName.trim().isEmpty() || newName == null || newName.trim().isEmpty()) {
            System.out.println("Invalid old name or empty new name");
            return;
        }

        // find the method with the old name
        for (UMLMethods method : this.methods) {
            if (method.getName().equals(oldName)) {
                // rename method
                method.setName(newName);
                System.out.println("Method renamed from '" + oldName + "' to '" + newName + "'");
                return;
            }
        }

        // if method with old name is not found
        System.out.println("Method named '" + oldName + "' not found");
    }


    // method that lists all the methods in a class
    public void listMethods() {
        // check if methods is empty
        if (this.methods.isEmpty()) {
            System.out.println("No methods found");
            return;
        }
        // print out all methods
        for (int i = 0; i < this.methods.size(); i++) {
            System.out.println((i + 1) + ". " + this.methods.get(i) + "\n");
        }
    }

    // toString method
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Class: ").append(this.name).append("\n");
        sb.append("Fields :\n");
        for (UMLFields field : this.fields) {
            sb.append(field).append("\n");
        }
        sb.append("Methods :\n");
        for (UMLMethods method : this.methods) {
            sb.append(" - ").append(method).append("\n");
        }
        return sb.toString();

    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Also, add getters for x, y, width, and height
    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double x) {
        this.x = x;
    }

    public void setMethods(ArrayList<UMLMethods> methods) {
        this.methods = methods;
    }

    public void setFields(ArrayList<UMLFields> fields) {
        this.fields = fields;
    }

}

