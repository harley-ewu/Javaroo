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
    private static List<UMLRelationship> relationships = new ArrayList<>();

    public static Map<String, UMLClass> getClasses() {
        return classes;
    }

    public static void setClasses(Map<String, UMLClass> classes) {
        UMLDiagram.classes = classes;
    }

    public static List<UMLRelationship> getRelationships() {
        return relationships;
    }

    public static void setRelationships(List<UMLRelationship> relationships) {
        UMLDiagram.relationships = relationships;
    }

    // Method to list all classes and their details present in the UML diagram.
    private void listClasses() {
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

    // Method to display the attributes and relationships of a specified class.
    private void listClassContents(String className) {
        // Check if the provided className exists in the classes map.
        if (classes.containsKey(className)) {
            // Retrieve the UMLClass object corresponding to the className from the classes map.
            UMLClass classEntity = classes.get(className);
            // Print basic class information (name).
            System.out.println("Class: " + className);
            System.out.println("Attributes:");
            // Loop through all attributes of the UMLClass object.
            // The attributes are stored in a map within the UMLClass object, where each entry corresponds to an attribute name and its type.
            for (Map.Entry<String, String> entry : classEntity.getAttributes().entrySet()) {
                // Print each attribute with its name and type.
                System.out.println("- " + entry.getKey() + " (" + entry.getValue() + ")");
            }
            System.out.println("Relationships:");
            // Loop through all UMLRelationship objects stored in the relationships list.
            for (UMLRelationship relationship : relationships) {
                // Check if the current relationship object involves the class we are currently describing.
                // It checks both the source and destination of the relationship to ensure all relevant relationships are included.
                if (relationship.getSource().equals(className) || relationship.getDestination().equals(className)) {
                    // Print the relationship in a readable format, showing the source and destination class names.
                    System.out.println("- " + relationship.getSource() + " --> " + relationship.getDestination());
                }
            }
        } else {
            // Inform the user if the specified className does not exist within the classes map.
            System.out.println("Class '" + className + "' does not exist.");
        }
    }

    // Method to list all relationships defined in the UML diagram.
    private void listRelationships() {
        // Check if the relationships list is empty, implying no relationships have been defined.
        if (relationships.isEmpty()) {
            System.out.println("No relationships defined.");
        } else {
            System.out.println("Relationships:");
            // If relationships are present, iterate through the relationships list.
            // This loop will run once for each UMLRelationship object stored in the list.
            for (UMLRelationship relationship : relationships) {
                // Print each relationship, showcasing the source class and destination class.
                System.out.println("- " + relationship.getSource() + " --> " + relationship.getDestination());
            }
        }
    }

}