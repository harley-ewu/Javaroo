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
        for(UMLClass c : classes.values())
        {
            if(c.getName().equals(name))
            {
                return c;
            }
        }
        return null;

    }

    //Method to see if a specific relationship exists in the UML diagram.
    public UMLRelationships relationshipExists(String id)
    {
        for(UMLRelationships r : relationships)
        {
            if(r.getId().equals(id))
            {
                return r;
            }
        }
        return null;
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
        // Check if the relationships list is empty, implying no relationships have been defined.
        if (relationships.isEmpty()) {
            System.out.println("No relationships defined.");
        } else {
            System.out.println("Relationships:");
            // If relationships are present, iterate through the relationships list.
            // This loop will run once for each UMLRelationship object stored in the list.
            for (UMLRelationships relationship : relationships) {
                // Print each relationship, showcasing the source class and destination class.
                System.out.println("- " + relationship.getSource().getName() + " --> " + relationship.getDest().getName());
            }
        }
    }

}