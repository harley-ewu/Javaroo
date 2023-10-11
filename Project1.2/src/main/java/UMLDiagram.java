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

    //Method to see if a specific class exists in the UML diagram.
    public UMLClass classExists(String name)
    {
        for(UMLClass c : classes)
        {
            if(c.getName().equals(name))
            {
                return c;
            }
        }
        return null;

    }

    //Method to see if a specific relationship exists in the UML diagram.
    public UMLRelationship relationshipExists(String id)
    {
        for(UMLRelationship r : relationships)
        {
            if(r.getId().equals(id)
            {
                return r;
            }
        }
        return null;
    }

    // Method to list all classes and their details present in the UML diagram.
    private void listClassContents(String className) {
        // Check if the provided className exists in the classes map.
        UMLClass classEntity = classExists(className);
        if (classEntity != null) {
            // Print basic class information (name).
            return classEntity.toString();
            // Loop through all attributes of the UMLClass object.
        } else {
            // Inform the user if the specified className does not exist within the classes map.
            System.out.println("Class '" + className + "' does not exist.");
        }
    }

    // Method to check if a specific class exists in the UML diagram.


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