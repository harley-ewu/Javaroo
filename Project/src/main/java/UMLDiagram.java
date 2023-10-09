import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UMLDiagram {

    private static final Map<String, Class> classes = new HashMap<>();
    private static final List<UMLRelationships> relationships = new ArrayList<>();
    static final String safeFile = "class_manager.json";

    public static Map<String, Class> getClasses() {
        return classes;
    }

    public static List<UMLRelationships> getRelationships() {
        return relationships;
    }

    private void listClassContents(String className) {
        if (classes.containsKey(className)) {
            Class classEntity = classes.get(className);
            System.out.println("Class: " + className);
            System.out.println("Attributes:");
            for (Map.Entry<String, String> entry : classEntity.getAttributes().entrySet()) {
                System.out.println("- " + entry.getKey() + " (" + entry.getValue() + ")");
            }
            System.out.println("Relationships:");
            for (UMLRelationships relationship : relationships) {
                if (relationship.getSource().equals(className) || relationship.getDestination().equals(className)) {
                    System.out.println("- " + relationship.getSource() + " --> " + relationship.getDestination());
                }
            }
        } else {
            System.out.println("Class '" + className + "' does not exist.");
        }
    }

    private void listClasses() {
        if (classes.isEmpty()) {
            System.out.println("No classes defined.");
        } else {
            for (String className : classes.keySet()) {
                listClassContents(className);
                System.out.println();
            }
        }
    }
}