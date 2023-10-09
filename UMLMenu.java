import java.util.*;

public class UMLMenu {
    private UMLDiagram diagram;

    public UMLMenu() {
        this.diagram = new UMLDiagram();
    }

    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("UML Diagram Menu:");
            System.out.println("1: Class options");
            System.out.println("2: Attribute options");
            System.out.println("3: Relationship options");
            System.out.println("4: Save/Load options");
            System.out.println("5: Exit");
            
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    displayClassMenu(scanner);
                    break;
                case "2":
                    displayAttributeMenu(scanner);
                    break;
                case "3":
                    displayRelationshipMenu(scanner);
                    break;
                case "4":
                    displaySaveLoadMenu(scanner);
                    break;
                case "5":
                    System.out.println("Exiting the UML Diagram Menu.");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }
    
    private void displayClassMenu(Scanner scanner) {
        boolean back = false;

        while (!back) {
            System.out.println("\nClass Operations:");
            System.out.println("1: Add");
            System.out.println("2: Delete");
            System.out.println("3: Rename");
            System.out.println("4: List Classes");
            System.out.println("5: List Specific Class Contents");
            System.out.println("6: Help");
            System.out.println("7: Back to Main Menu");
            
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    System.out.print("Enter the class name to be added: ");
                    String addClassName = scanner.nextLine();
                    diagram.addClass(new UMLClass(addClassName));
                    break;
                case "2":
                    System.out.print("Enter the class name to be deleted: ");
                    String deleteClassName = scanner.nextLine();
                    diagram.deleteClass(deleteClassName); 
                    break;
                case "3":
                    System.out.print("Enter the class name to be renamed: ");
                    String oldClassName = scanner.nextLine();
                    System.out.print("Enter the new class name: ");
                    String newClassName = scanner.nextLine();
                    diagram.renameClass(oldClassName, newClassName); 
                    break;
                case "4":
                    diagram.listClasses();
                    break;
                case "5":
                    System.out.print("Enter the class name: ");
                    String className = scanner.nextLine();
                    String classDetails = diagram.getClassDetails(className); 
                    System.out.println(classDetails);
                    break;
                case "6":
                    System.out.println("Help with class operations:");
                    System.out.println("[1: Add] To add a class, select option '1' and provide the class name.");
                    System.out.println("[2: Delete] To delete a class, select option '2' and provide the class name.");
                    System.out.println("[3: Rename] To rename a class, select option '3' and provide the old and new class names.");
                    System.out.println("[4: List Classes] To list all classes, select option '4'.");
                    System.out.println("[5: List Class Contents] To list contents of a specific class, select option '5' and provide the class name.");
                    break;
                case "7":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void displayAttributeMenu(Scanner scanner) {
        boolean back = false;
        
        while (!back) {
            System.out.println("Attribute Operations:");
            System.out.println("1. Add Attribute");
            System.out.println("2. Delete Attribute");
            System.out.println("3. Help");
            System.out.println("4. Back to Main Menu");
    
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();
    
            switch (choice) {
                case "1":
                    // Add attribute logic
                    System.out.print("Enter the class name: ");
                    String className = scanner.nextLine();
                    System.out.print("Enter the attribute name: ");
                    String attributeName = scanner.nextLine();
    
                    // Assuming UMLDiagram has a method getClassByName and UMLClass has a method addAttribute
                    UMLClass umlClass = diagram.getClassByName(className);
                    if (umlClass != null) {
                        umlClass.addAttribute(new UMLAttributes(attributeName)); 
                        System.out.println("Attribute " + attributeName + " added to class " + className + ".");
                    } else {
                        System.out.println("Class " + className + " not found.");
                    }
                    break;
                case "2":
                    // Delete attribute logic
                    System.out.println("Enter the class name: ");
                    String deleteClassName = scanner.nextLine();
                    System.out.println("Enter the attribute name to be deleted: ");
                    String deleteAttributeName = scanner.nextLine();
                    
                    UMLClass deleteClass = diagram.getClassByName(deleteClassName);
                    if (deleteClass != null) {
                        if (deleteClass.deleteAttribute(deleteAttributeName)) {
                            System.out.println("Attribute " + deleteAttributeName + " deleted from class " + deleteClassName + ".");
                        } else {
                            System.out.println("Attribute " + deleteAttributeName + " not found in class " + deleteClassName + ".");
                        }
                    } else {
                        System.out.println("Class " + deleteClassName + " not found.");
                    }
                    break;
                case "3":
                    // Help logic for attribute operations
                    System.out.println("Help with attribute operations:");
                    System.out.println("[1: Add Attribute] To add an attribute, select option '1' and provide the class name and attribute name.");
                    System.out.println("[2: Delete Attribute] To delete an attribute, select option '2' and provide the class name and attribute name.");
                    break;
                case "4":
                    // Back to main menu
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private void displayRelationshipMenu(Scanner scanner) {
        boolean back = false;
        
        while (!back) {
            System.out.println("\nRelationship Operations:");
            System.out.println("1: Add Relationship");
            System.out.println("2: Delete Relationship");
            System.out.println("3: List Relationships");
            System.out.println("4: Help");
            System.out.println("5: Back to Main Menu");
            
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();
    
            switch (choice) {
                case "1":
                    // Add relationship logic
                    System.out.print("Enter the source class name: ");
                    String sourceClassName = scanner.nextLine();
                    System.out.print("Enter the destination class name: ");
                    String destinationClassName = scanner.nextLine();
                    System.out.print("Enter the relationship type: ");
                    String relationshipType = scanner.nextLine();
                    
                    diagram.addRelationship(sourceClassName, destinationClassName, relationshipType);
                    break;
    
                case "2":
                    // Delete relationship logic
                    System.out.print("Enter the source class name: ");
                    String deleteSourceClassName = scanner.nextLine();
                    System.out.print("Enter the destination class name: ");
                    String deleteDestinationClassName = scanner.nextLine();
    
                    diagram.deleteRelationship(deleteSourceClassName, deleteDestinationClassName);
                    break;
    
                case "3":
                    String relationshipsList = diagram.getRelationships();
                    System.out.println(relationshipsList);
                    break;
    
                case "4":
                    // Help logic for relationship operations
                    System.out.println("Help with relationship operations:");
                    System.out.println("[1: Add Relationship] To add a relationship, select option '1' and provide source class, destination class, and relationship type.");
                    System.out.println("[2: Delete Relationship] To delete a relationship, select option '2' and provide source class and destination class.");
                    System.out.println("[3: List Relationships] To list all relationships, select option '3'.");
                    break;
    
                case "5":
                    // Back to main menu
                    back = true;
                    break;
    
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    

    private void displaySaveLoadMenu(Scanner scanner) {
        System.out.println("Save/Load Operations:");
        System.out.println("1: Save Diagram");
        System.out.println("2: Load Diagram");
        System.out.println("3: Back to Main Menu");
        
        System.out.print("Enter your choice: ");
        String choice = scanner.nextLine();
        }
    

    public static void main(String[] args) {
        // Create an instance of UMLMenu
        UMLMenu menu = new UMLMenu();
        // Call the displayMenu method
        menu.displayMenu();
    }
}
