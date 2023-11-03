package javaroo.cmd;

import java.util.*;

public class UMLMenu {
    private UMLDiagram diagram;

    public static void main(String[] args) {
        UMLMenu menu = new UMLMenu();
        menu.displayMenu();
    }

    public UMLMenu() {
        this.diagram = new UMLDiagram();

    }

    // method to return to main automatically after each operation
    private void returnToMainMenu() {
        System.out.println("Returning to the main menu...");
    }

    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\n___ Welcome to the UML Diagram Menu!😘 ___");
            System.out.println("Type 'r' to return to the main menu at any time.");
            System.out.println(" ");
            System.out.println("1: Class Options");
            System.out.println("2: Relationship Options");
            System.out.println("3: Save/Load Options");
            System.out.println("4: Help");
            System.out.println("5: Exit Program");
            
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    displayClassMenu(scanner);
                    break;
                case "2":
                    displayRelationshipMenu(scanner);
                    break;
                case "3":
                    displaySaveLoadMenu(scanner);
                    break;
                case "4":
                    displayHelpMenu(scanner);
                case "5":
                    System.out.println("\n[Friendly Reminder], have you saved your hard work? (y/n)");
                    String save = scanner.nextLine();

                    if (save.equals("y")) {
                        System.out.println("\nExiting the UML Diagram Menu. Have a good day! :3 ");
                        exit = true;
                        break;
                    } else if (save.equals("n")) {
                        System.out.println("\nPlease save your progress before exiting the program.");
                        break;
                    } else {
                        System.out.println("\nInvalid choice. Please try again.");
                        break;
                    }
                default:
                    System.out.println("\nInvalid choice. Please try again.");
            }
        }
        scanner.close();

    }
    
    private void displayClassMenu(Scanner scanner) {
        boolean back = false;

        while (!back) {
            System.out.println("\n________Class Operations:________");
            System.out.println("1: Add Class");
            System.out.println("2: Remove Class");
            System.out.println("3: Rename Class");
            System.out.println("4: Field and Method Options");
            System.out.println("5: Help");
            
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            if (choice.equals("r")) {
                break; // Return to the main menu
            }
            
            switch (choice) {
                case "1":
                    // here user can add a class, they can also add fields and methods to the class if they wish
                    System.out.print("\nEnter the class name to be added: ");
                    String addClassName = scanner.nextLine();
                    UMLClass.addClass(addClassName);
                    System.out.println("\nClass added: " + addClassName);
                    System.out.println("\nWould you also like to add fields and methods to the class? (y/n)");
                    String addFieldsMethods = scanner.nextLine();
                    if (addFieldsMethods.equals("y")) {
                        System.out.println("\nHow many fields would you like to add?");
                        int numFields = scanner.nextInt();
                        scanner.nextLine();
                        for (int i = 0; i < numFields; i++) {
                            System.out.println("\nEnter the field name: ");
                            String fieldName = scanner.nextLine();
                            System.out.println("\nEnter the field type: ");
                            String fieldType = scanner.nextLine();
                            UMLClass.addField(addClassName, fieldName, fieldType);
                            System.out.println("\nField added.");
                        }
                        System.out.println("\nHow many methods would you like to add?");
                        int numMethods = scanner.nextInt();
                        scanner.nextLine();
                        for (int i = 0; i < numMethods; i++) {
                            System.out.println("\nEnter the method name: ");
                            String methodName = scanner.nextLine();
                            System.out.println("\nEnter the method type: ");
                            String methodType = scanner.nextLine();
                            UMLClass.addMethod(addClassName, methodName, methodType);
                            System.out.println("\nMethod added.");
                        }
                    }
                    // list added class with its fields and methods
                    diagram.listClasses();
                    returnToMainMenu();

                case "2":
                    diagram.listClasses();
                    System.out.print("\nEnter the class name to be removed: ");
                    String removeClassName = scanner.nextLine();
                    // print updated list of classes
                    diagram.listClasses();
                    returnToMainMenu();

                case "3":
                    diagram.listClasses();
                    System.out.print("\nEnter the class name you want to renamed: ");
                    String oldClassName = scanner.nextLine();
                    System.out.print("\nEnter the new class name: ");
                    String newClassName = scanner.nextLine();
                    System.out.println("\nClass renamed from: " + oldClassName + " to " + newClassName);
                    // print updated list of classes
                    diagram.listClasses();
                    returnToMainMenu();

                case "4":
                    // menu option for add field/method, remove field/method, rename field/method
                    System.out.println("\n______Field and Method Options:______");
                    System.out.println("1: Add Field");
                    System.out.println("2: Remove Field");
                    System.out.println("3: Rename Field");
                    System.out.println("4: Add Method");
                    System.out.println("5: Remove Method");
                    System.out.println("6: Rename Method");
                    System.out.println("7: Help");

                    System.out.print("Enter your choice: ");
                    String choice2 = scanner.nextLine();
                    
                     if (choice2.equals("r")) {
                        break; // Return to the main menu
                    }
                    // switch statement for field and method options
                    switch (choice2) {
                        
                        case "1":
                            // add field logic
                            System.out.print("\nEnter the class name: ");
                            String addFieldClassName = scanner.nextLine();
                            System.out.print("\nEnter the field name: ");
                            String addFieldName = scanner.nextLine();
                            System.out.print("\nEnter the field type: ");
                            String addFieldType = scanner.nextLine();
                            UMLClass.addField(addClassName, addFieldName, addFieldType);
                            System.out.println("\nField added.");
                            returnToMainMenu();
                           
                        case "2":
                            // remove field logic
                            System.out.print("\nEnter the class name: ");
                            String removeFieldClassName = scanner.nextLine();
                            System.out.print("\nEnter the field name: ");
                            String removeFieldName = scanner.nextLine();
                            System.out.println("\nField removed.");
                            returnToMainMenu();
                        
                        case "3":
                            // rename field logic
                            System.out.print("\nEnter the class name: ");
                            String renameFieldClassName = scanner.nextLine();
                            System.out.print("\nEnter the old field name: ");
                            String oldFieldName = scanner.nextLine();
                            System.out.print("\nEnter the new field name: ");
                            String newFieldName = scanner.nextLine();
                            System.out.println("\nField renamed from: " + oldFieldName + " to " + newFieldName);
                            returnToMainMenu();
                        
                        case "4":
                            // add method logic
                            System.out.print("\nEnter the class name: ");
                            String addMethodClassName = scanner.nextLine();
                            System.out.print("\nEnter the method name: ");
                            String addMethodName = scanner.nextLine();
                            System.out.print("\nEnter the method type: ");
                            String addMethodType = scanner.nextLine();
                            UMLClass.addMethod(addClassName, addMethodName, addMethodType);
                            System.out.println("\nMethod added.");
                            returnToMainMenu();
                        
                        case "5":
                            // remove method logic
                            System.out.print("\nEnter the class name: ");
                            String removeMethodClassName = scanner.nextLine();
                            System.out.print("\nEnter the method name: ");
                            String removeMethodName = scanner.nextLine();
                            System.out.println("\nMethod removed.");
                            returnToMainMenu();
                            
                        case "6":
                            // rename method logic
                            System.out.print("\nEnter the class name: ");
                            String renameMethodClassName = scanner.nextLine();
                            System.out.print("\nEnter the old method name: ");
                            String oldMethodName = scanner.nextLine();
                            System.out.print("\nEnter the new method name: ");
                            String newMethodName = scanner.nextLine();
                            System.out.println("\nMethod renamed from: " + oldMethodName + " to " + newMethodName);
                            returnToMainMenu();
                        
                        case "7":
                            // help with field and method options
                            System.out.println("\nHelp with field and method options:");
                            System.out.println("[1: Add Field] To add a field, select option '1' and provide the class name, field name, and field type.");
                            System.out.println("[2: Remove Field] To remove a field, select option '2' and provide the class name and field name.");
                            System.out.println("[3: Rename Field] To rename a field, select option '3' and provide the class name, old field name, and new field name.");
                            System.out.println("[4: Add Method] To add a method, select option '4' and provide the class name, method name, and method type.");
                            System.out.println("[5: Remove Method] To remove a method, select option '5' and provide the class name and method name.");
                            System.out.println("[6: Rename Method] To rename a method, select option '6' and provide the class name, old method name, and new method name.");
                            returnToMainMenu();

                    }
                    break;
                    
            }

                case "5":
                    System.out.println("\nHelp with class operations:");
                    System.out.println("[1: Add Class] To add a class, select option '1' and provide the class name.");
                    System.out.println("[2: Delete Class] To delete a class, select option '2' and provide the class name.");
                    System.out.println("[3: Rename Class] To rename a class, select option '3' and provide the old and new class names.");
                    System.out.println("[4: Field and Method Options] To access field and methods options, select option '4'.");
                    break;

                default:
                    System.out.println("\nInvalid choice. Please try again.");
            }
        }

    }

    private void displayRelationshipMenu(Scanner scanner) {
        boolean back = false;
        
        while (!back) {
            System.out.println("\n_______Relationship Operations:_______");
            System.out.println("1: Add Relationship");
            System.out.println("2: Remove Relationship");
            System.out.println("3: Rename Relationships");
            System.out.println("4: Help");
            
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            if (choice.equals("r")) {
                break; // Return to the main menu
            }
    
            switch (choice) {
                case "1":
                    // Add relationship logic
                    System.out.print("\nEnter the source class name: ");
                    String sourceClassName = scanner.nextLine();
                    System.out.print("\nEnter the destination class name: ");
                    String destinationClassName = scanner.nextLine();
                    System.out.print("\nEnter the relationship type: ");
                    String relationshipType = scanner.nextLine();
                    UMLRelationship.addRelationship(sourceClassName, destinationClassName, relationshipType);
                    System.out.println("\nRelationship added.");
                    returnToMainMenu();
                    
                case "2":
                    // Remove relationship logic
                    System.out.print("\nEnter the source class name: ");
                    String sourceClassName2 = scanner.nextLine();
                    System.out.print("\nEnter the destination class name: ");
                    String destinationClassName2 = scanner.nextLine();
                    UMLRelationship.removeRelationship(sourceClassName2, destinationClassName2);
                    System.out.println("\nRelationship removed.");
                    returnToMainMenu();
                    
    
                case "3":
                    // Rename relationship logic
                    System.out.print("\nEnter the old relationship name: ");
                    String oldRelationshipName = scanner.nextLine();
                    System.out.print("\nEnter the new relationship name: ");
                    String newRelationshipName = scanner.nextLine();
                    UMLRelationship.renameRelationship(oldRelationshipName, newRelationshipName);
                    System.out.println("\nRelationship renamed from: " + oldRelationshipName + " to " + newRelationshipName);
                    returnToMainMenu();
    
                case "4":
                    System.out.println("\nHelp with relationship operations:");
                    System.out.println("[1: Add Relationship] To add a relationship, select option '1' and provide source class, destination class, and relationship type.");
                    System.out.println("[2: Delete Relationship] To delete a relationship, select option '2' and provide source class and destination class.");
                    System.out.println("[3: Rename Relationships] To rename a relationship, select option '3' and provide the old and new relationship names.");
                    break;
                
                default:
                    System.out.println("\nInvalid choice. Please try again.");
            }
        }

    }
    

    private void displaySaveLoadMenu(Scanner scanner) {
        boolean back = false;
        while (!back) {
            System.out.println("\n______Save/Load Operations:______");
            System.out.println("1: Save Diagram");
            System.out.println("2: Load Diagram");
            System.out.println("3: Help");
            System.out.println("4: Back to Main Menu");
            
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            if (choice.equals("r")) {
                break; // Return to the main menu
            }

            switch(choice)
            {
                case "1":
                    System.out.print("\nEnter the file name: ");
                    String fileName = scanner.nextLine();
                    UMLSaveLoad.saveData(fileName);
                    returnToMainMenu();
                    break;
                case "2":
                    System.out.print("\nEnter the file name: ");
                    String file_Name = scanner.nextLine();
                    UMLSaveLoad.loadData(file_Name);
                    returnToMainMenu();
                    break;
                case "3":
                    System.out.println("\nHelp with save/load operations:");
                    System.out.println("[1: Save Diagram] To save the diagram, select option '1' and provide the file name.");
                    System.out.println("[2: Load Diagram] To load the diagram, select option '2' and provide the file name.");
                    break;
                case "4":
                    back = true;
                    break;
                default:
                    System.out.println("\nInvalid choice. Please try again.");
            }
        }

    }

    private void displayHelpMenu(Scanner scanner) {
        System.out.println("\nHelp with UML Diagram Menu:");
        System.out.println("[1: Class Options] To access class options, select option '1'.");
        System.out.println("[2: Relationship Options] To access relationship options, select option '2'.");
        System.out.println("[3: Save/Load Options] To access save/load options, select option '3'.");
        System.out.println("[4: Help] To access help, select option '4'.");
        System.out.println("[5: Exit Program] To exit the program, select option '5'.");
        returnToMainMenu();
        System.out.println(" ");
        
    }

}

