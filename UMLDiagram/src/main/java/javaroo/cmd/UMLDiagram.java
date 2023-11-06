package javaroo.cmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UMLDiagram {
    // Here we declare a Map named 'classes'. It stores pairs of String and UMLClass.
    // The String is the key (representing the name of the UMLClass), and the UMLClass object is the value.
    // This allows for easy retrieval of UMLClass objects by using their name.
    private static Map<String, UMLClass> classes = new HashMap<>();
    // A list to store objects representing relationships between UML classes.
    private static List<UMLRelationships> relationships = new ArrayList<>();

    public static Map<String, UMLClass> getClasses() {
        return classes;
    }

    public static void setClasses(Map<String, UMLClass> classes) {
        UMLDiagram.classes = classes;
    }

    public static List<UMLRelationships> getRelationships() {
        return relationships;
    }

    public static void setRelationships(List<UMLRelationships> relationships) {
        UMLDiagram.relationships = relationships;
    }

    //Method to see if a specific class exists in the UML diagram.
    public UMLClass classExists(String name)
    {
        //check if name is null
//        if(name == null)
//        {
//            System.out.println("Sorry but we could not find a valid name for this class");
//            return null;
//        }

        for(UMLClass c : classes.values())
        {
            if(c.getName().equals(name))
            {
                return c;
            }
        }
        return null;

    }

    //Method to create a UMLClass object and add it to the classes map.
    public void addClass(String name)
    {
        // Remove all spaces and tabs from the name for the checks
        String processedName = name.replaceAll("\\s+", "");

        // Check if the name is null or empty after removing spaces/tabs
        if(processedName == null || processedName.isEmpty())
        {
            System.out.println("Sorry but we could not find a valid name for this class");
            return;
        }
        // Check if the class already exists.
        if(classExists(processedName) != null)
        {
            System.out.println("Sorry but this class already exists");
            return;
        }
        // Create a new UMLClass object and add it to the classes map.
        getClasses().put(processedName, new UMLClass(processedName));
        System.out.println("Class added: " + processedName);
    }

    //Method to remove a UMLClass object from the classes map via its name.
    public void removeClass(String name)
    {
        //check if name is null
        if(name == null)
        {
            System.out.println("Sorry but we could not find a valid name for this class");
            return;
        }
        //Check if the class exists.
        if(classExists(name) == null)
        {
            System.out.println("Sorry but this class does not exist");
            return;
        }
        //Remove the UMLClass object from the classes map.
        getClasses().remove(name);
        System.out.println("Class deleted: " + name);
    }

    //Method to rename a UMLClass object in the classes map via its name.
    public void renameClass(String oldName, String newName)
    {
        //Check if the oldName and newName are null.
        if(oldName == null || newName == null)
        {
            System.out.println("Sorry but we could not find a valid name for this class");
            return;
        }
        //Check if the class exists.
        if(classExists(oldName) == null)
        {
            System.out.println("Sorry but this class does not exist");
            return;
        }
        //Check if the newName already exists.
        if(classExists(newName) != null)
        {
            System.out.println("Sorry but this class already exists");
            return;
        }
        //Rename the UMLClass object in the classes map.
        UMLClass c = classExists(oldName);
        c.setName(newName);
        getClasses().remove(oldName);
        getClasses().put(newName, c);
        System.out.println("Class renamed: " + oldName + " to " + newName);
    }

    //Method to see if a specific relationship exists in the UML diagram.
    public UMLRelationships relationshipExists(String id)
    {
        //check if id is null
        if(id == null)
        {
            System.out.println("Sorry but we could not find a valid id for this relationship");
            return null;
        }

        for(UMLRelationships r : relationships)
        {
            if(r.getId().equals(id))
            {
                return r;
            }
        }
        return null;
    }

    //Method to create a UMLRelationships object and add it to the relationships list.
    public void addRelationship(UMLClass src, UMLClass dest, UMLRelationships.RelationshipType type)
    {
        //Check if the source and destination classes exist.
        if(src == null || dest == null)
        {
            System.out.println("Sorry but we could not find a valid source or destination for this diagram");
            return;
        }
        //Check if the relationship type is valid.
        if(type == null)
        {
            System.out.println("Sorry but we could not find a valid type for this relationship");
            return;
        }
        //Check if the relationship already exists.
        if(relationshipExists(src.getName() + dest.getName()) != null)
        {
            System.out.println("Sorry but this relationship already exists");
            return;
        }
        //Create a new UMLRelationships object and add it to the relationships list.
        relationships.add(new UMLRelationships(src, dest, type));
        System.out.println("\nRelationship added.");
    }

    //Method to remove a UMLRelationships object from the relationships list via its index.
    public void removeRelationship(int index)
    {
        //Check if the index is valid.
        if(index < 0 || index >= relationships.size())
        {
            System.out.println("Sorry but we could not find a valid index for this relationship");
            return;
        }
        //Remove the UMLRelationships object from the relationships list.
        relationships.remove(index);
    }

    public void listClasses() {
        // Check if the classes map is empty, meaning no classes have been added.
        if (classes.isEmpty()) {
            System.out.println("No classes defined.");
        } else {
            // If classes are available, iterate through the key set of the classes map,
            // which returns all keys (class names). This loop will run once for each class name in the map.
            for (String className : classes.keySet()) {
                // Call the listClassContents method to print details about each class.
                // We pass the className, which serves as a unique identifier for retrieving class information from the map.
                listClassContents(className);
                // Print an empty line to separate the printed information of different classes for better readability.
                System.out.println();
            }
        }
    }

    // Method to list all classes and their details present in the UML diagram.
    void listClassContents(String className) {
        // Check if the provided className exists in the classes map.
        UMLClass UMLClassEntity = classExists(className);
        if (UMLClassEntity != null) {
            // Print basic class information (name).
            System.out.println(UMLClassEntity.toString());
        } else {
            // Inform the user if the specified className does not exist within the classes map.
            System.out.println("Class '" + className + "' does not exist.");
        }
    }

    // Method to check if a specific class exists in the UML diagram.


    // Method to list all relationships defined in the UML diagram.
    void listRelationships() {
        int index = 0;
        // Check if the relationships list is empty, implying no relationships have been defined.
        if (relationships.isEmpty()) {
            System.out.println("No relationships defined.");
        } else {
            System.out.println("Relationships:");
            // If relationships are present, iterate through the relationships list.
            // This loop will run once for each UMLRelationship object stored in the list.
            for (UMLRelationships relationship : relationships) {
                // Print each relationship, showcasing the source class and destination class.
                System.out.println(index + ":"+ relationship.getSource().getName() + " --> " +
                        relationship.getDest().getName() + "::"+ relationship.getType());
                index ++;
            }
        }
    }

}