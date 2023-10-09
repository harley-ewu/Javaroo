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
            System.out.println("Class Operations:");
            System.out.println("1. Add Class");
            System.out.println("2. Delete Class");
            System.out.println("3. Rename Class");

            System.out.println("Relationship Operations:");
            System.out.println("4. Add Relationship");
            System.out.println("5. Delete Relationship");

            System.out.println("Attribute Operations:");
            System.out.println("6. Add Attribute");
            System.out.println("7. Delete Attribute");
            System.out.println("8. Rename Attribute");

            System.out.println("Save & Load:");
            System.out.println("9. Save UML Model");
            System.out.println("10. Load UML Model");

            System.out.println("Interface:");
            System.out.println("11. List Classes");
            System.out.println("12. List Class Contents");
            System.out.println("13. List Relationships");
            System.out.println("14. Help");

            System.out.println("15. Exit (F4 or type 'exit')");

            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            //menu choice implementation
            switch (choice) {
                case 1:
                    // add class logic
                    System.out.println("Enter the class name to be added: ");
                    String addClassName = scanner.nextLine();
                    Class newClass = new Class(addClassName);
                    Class.add(newClass);
                    System.out.println("Class added: " + newClass);
                    break;
                case 2:
                    // delete class logic
                    //check if empty
                    if(Class.isEmpty()){
                        System.out.println("There are no classes to delete.");
                        break;
                    }
                    else{
                        System.out.println("Enter the class name to be deleted: ");
                        String deleteClassName = scanner.nextLine();
                        //check if class exists
                        if(Class.contains(deleteClassName)){
                            Class.remove(deleteClassName);
                            System.out.println("Class deleted: " + deleteClassName);
                        }
                        else{
                            System.out.println("Class does not exist.");
                        }
                    }
                    break;
                case 3:
                    //rename class logic
                    //check if empty
                    if(Class.isEmpty()){
                        System.out.println("There are no classes to rename.");
                        break;
                    }
                    else{
                        System.out.println("Enter the class name to be renamed: ");
                        String renameClassName = scanner.nextLine();
                        //check if class exists
                        if(Class.contains(renameClassName)){
                            System.out.println("Enter the new class name: ");
                            String newClassName = scanner.nextLine();
                            Class.rename(renameClassName, newClassName);
                            System.out.println("Class renamed: " + renameClassName + " to " + newClassName);
                        }
                        else{
                            System.out.println("Class does not exist.");
                        }
                    }
                    break;
                case 4:
                
                    break;
                case 5:
                    // Implement logic to delete a relationship
                    break;
                case 6:
                    // Implement logic to add an attribute
                    break;
                case 7:
                    // Implement logic to delete an attribute
                    break;
                case 8:
                    // Implement logic to rename an attribute
                    break;
                case 9:
                    // Implement logic to save UML model
                    break;
                case 10:
                    // Implement logic to load UML model
                    break;
                case 11:
                    // Implement logic to list classes alphabetically
                    break;
                case 12:
                    // Implement logic to list class contents
                    break;
                case 13:
                    // Implement logic to list relationships
                    break;
                case 14:
                    System.out.println("To obtain help, type 'help' and press Enter.");
                    break;
                case 15:
                    System.out.println("Exiting the UML Diagram Menu.");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    public static void main(String[] args) {
        // Create an instance of UMLMenu
        UMLMenu menu = new UMLMenu();
        // Call the displayMenu method
        menu.displayMenu();
    }
}
