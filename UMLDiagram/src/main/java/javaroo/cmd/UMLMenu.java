package javaroo.cmd;

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
                    UMLClass.addClass(addClassName);
                    break;
                case "2":
                    System.out.print("Enter the class name to be deleted: ");
                    String deleteClassName = scanner.nextLine();
                    UMLClass c = diagram.classExists(deleteClassName);
                    UMLClass.deleteClass(c, diagram);
                    break;
                case "3":
                    System.out.print("Enter the class name to be renamed: ");
                    String oldClassName = scanner.nextLine();
                    System.out.print("Enter the new class name: ");
                    String newClassName = scanner.nextLine();
                    UMLClass.renameClass(oldClassName, newClassName);
                    System.out.println("Class renamed to " + newClassName + ".");
                    break;
                case "4":
                    diagram.listClasses();
                    break;
                case "5":
                    System.out.print("Enter the class name: ");
                    String className = scanner.nextLine();
                    diagram.listClassContents(className); 
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
            System.out.println("3. Rename Attribute");
            System.out.println("4. Help");
            System.out.println("5. Back to Main Menu");
    
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();
    
            switch (choice) {
                case "1":
                    // Add attribute logic
                    System.out.print("Enter the class name: ");
                    String className = scanner.nextLine();
                    UMLClass c = diagram.classExists(className);
                    if(c != null)
                    {
                        System.out.print("Enter the attribute name: ");
                        String attributeName = scanner.nextLine();
                        UMLAttributes.addAttribute(c, attributeName);
                    }
                    else
                    {
                        System.out.println("Class " + className + " does not exist.");
                    }
                    break;
                case "2":
                    // Delete attribute logic
                    System.out.println("Enter the class name: ");
                    String deleteClassName = scanner.nextLine();
                    UMLClass cl = diagram.classExists(deleteClassName);
                    if(cl != null)
                    {
                        System.out.println("Enter the attribute name to be deleted: ");
                        String deleteAttributeName = scanner.nextLine();
                        UMLAttributes.removeAttribute(cl, deleteAttributeName);
                    }                   
                    break;
                case "3":
                    // Rename attribute logic
                    System.out.println("Enter the class name: ");
                    String class_Name = scanner.nextLine();
                    UMLClass cll = diagram.classExists(class_Name);
                    if(cll != null)
                    {
                        System.out.println("Enter the attribute name to be renamed: ");
                        String oldAttributeName = scanner.nextLine();
                        System.out.println("Enter the new attribute name: ");
                        String newAttributeName = scanner.nextLine();
                        UMLAttributes atr = cll.attributesExists(oldAttributeName);
                        if(atr != null)
                        {
                            atr.setName(newAttributeName);
                            System.out.println("Attribute renamed to " + newAttributeName + ".");
                        }
                        else
                        {
                            System.out.println("Attribute " + oldAttributeName + " does not exist in class " + class_Name + ".");
                        }
                    }
                    else
                    {
                        System.out.println("Class " + class_Name + " does not exist.");
                    }
                    break;
                case "4":
                    // Help logic for attribute operations
                    System.out.println("Help with attribute operations:");
                    System.out.println("[1: Add Attribute] To add an attribute, select option '1' and provide the class name and attribute name.");
                    System.out.println("[2: Delete Attribute] To delete an attribute, select option '2' and provide the class name and attribute name.");
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
                    UMLClass a = diagram.classExists(sourceClassName);
                    if(a != null)
                    {
                        System.out.print("Enter the destination class name: ");
                        String destinationClassName = scanner.nextLine();
                        UMLClass b = diagram.classExists(destinationClassName);
                        if(b != null)
                        {
                            UMLRelationships.addRelationship( a, b);
                            System.out.println("Relationship added.");
                        }
                        else{
                            System.out.println("Class " + destinationClassName + " does not exist.");
                        }
                    }
                    else {
                        System.out.println("Class " + sourceClassName + " does not exist.");
                    }
                    break;
    
                case "2":
                    // Delete relationship logic
                    System.out.print("Enter the source class name: ");
                    String deleteSourceClassName = scanner.nextLine();
                    UMLClass al = diagram.classExists(deleteSourceClassName);
                    if(al != null)
                    {
                        System.out.print("Enter the destination class name: ");
                        String deleteDestinationClassName = scanner.nextLine();
                        UMLClass b = diagram.classExists(deleteDestinationClassName);
                        if(b != null)
                        {
                            String id = deleteSourceClassName + deleteDestinationClassName;
                            UMLRelationships rel = diagram.relationshipExists(id);
                            if(rel != null)
                            {
                                UMLRelationships.deleteRelationship(diagram, rel);
                                System.out.println("Relationship deleted.");
                            }
                            else{
                                System.out.println("Relationship does not exist.");
                            }
                        }
                        else{
                            System.out.println("Class " + "destinationClassName "+ " does not exist.");
                        }
                    }
                    else {
                        System.out.println("Class " + "sourceClassName"  + " does not exist.");
                    }
                    break;
    
                case "3":
                    diagram.listRelationships();
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
        boolean back = false;
        while (!back) {
            System.out.println("Save/Load Operations:");
            System.out.println("1: Save Diagram");
            System.out.println("2: Load Diagram");
            System.out.println("3: Back to Main Menu");
            
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();
            switch(choice)
            {
                case "1":
                    System.out.print("Enter the file name: ");
                    String fileName = scanner.nextLine();
                    UMLSaveLoad.saveData(fileName);
                    break;
                case "2":
                    System.out.print("Enter the file name: ");
                    String file_Name = scanner.nextLine();
                    UMLSaveLoad.loadData(file_Name);
                    break;
                case "3":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    

    public static void main(String[] args) {
        // Create an instance of UMLMenu
        UMLMenu menu = new UMLMenu();
        // Call the displayMenu method
        menu.displayMenu();
    }
}
