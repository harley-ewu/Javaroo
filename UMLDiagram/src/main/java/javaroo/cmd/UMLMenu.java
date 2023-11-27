package javaroo.cmd;

import javaroo.cmd.UMLDiagram;

import java.io.IOException;
import java.util.*;
import org.jline.reader.*;
import org.jline.reader.impl.LineReaderImpl;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public class UMLMenu {
    private UMLDiagram diagram;
    private static final List<String> CMDS = Arrays.asList("add class ", "add field ", "add method ", "add relationship ", "add parameter ",
            "delete class ", "delete field ", "delete method ", "delete relationship ", "delete parameter ",
    "rename class ", "rename field ", "rename method ", "rename parameter ", "list all",
    "list class ", "list relationships", "undo", "redo", "visualize", "save", "load ", "help", "exit");

    private static final String helpMenu = "Available Commands:\n" +
            "Add Commands:\n" +
            "  add class <ClassName>\n" +
            "  add field <class> <visibility> <type> <name>\n" +
            "  add method <class> <returnType> <methodName> <param1Type> <param1Name> etc.\n" +
            "  add relationship <sourceClass> <destinationClass> <type>\n" +
            //"  add parameter <ParameterName>\n\n" +

            "Delete Commands:\n" +
            "  delete class <ClassName>\n" +
            "  delete field <class> <fieldName>\n" +
            "  Please use the list class <name> command to show the list of methods. Then use this format: delete method <class> <numberInList>\n" +
            "  delete relationship <sourceClass> <destinationClass>\n" +
            //"  delete parameter <ParameterName>\n\n" +

            "Rename Commands:\n" +
            "  rename class <OldClassName> <NewClassName>\n" +
            "  rename field <class> <old> <new>\n" +
            "  Please use the list class <name> command to show the list of methods. Then use this format: rename method <class> <numberInList> <newName>\n" +
            //"  rename parameter <OldParameterName> <NewParameterName>\n\n" +

            "List Commands:\n" +
            "  list all\n" +
            "  list class <name>\n" +

            "Other Commands:\n" +
            "  undo\n" +
            "  redo\n" +
            "  save\n" +
            "  load <fileName>\n" +
            "  visualize\n\t-This command opens up the graphical user interface.\n" +
            "  exit";


    public static void main(String[] args) {
        UMLMenu menu = new UMLMenu();
        try {
            Terminal terminal = TerminalBuilder.builder().build();
            LineReader lineReader = LineReaderBuilder.builder().terminal(terminal).completer(new CommandCompleter()).build();

            System.out.println(helpMenu);
            String prompt = "Enter command: ";
            String line;
            while (!(line = lineReader.readLine(prompt)).equals("exit")) {
                menu.processCommandCLI(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static class CommandCompleter implements Completer {
        @Override
        public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
            String buffer = line.line();
            for (String command : CMDS) {
                if (command.startsWith(buffer)) {
                    candidates.add(new Candidate(command));
                }
            }
        }
    }

    public UMLMenu() {
        this.diagram = new UMLDiagram();

    }

    private void processCommandCLI(String command) {
        String[] parts = command.trim().split("\\s+");
        if (parts.length == 0) {
            System.out.println("Invalid command format.");
            return;
        }

        String action = parts[0].toLowerCase();

        switch (action) {
            case "add":
                processAddCommand(parts);
                break;
            case "delete":
                processDeleteCommand(parts);
                break;
            case "rename":
                processRenameCommand(parts);
                break;
            case "list":
                processListCommand(parts);
                break;
            case "undo":
                System.out.println("Executing 'undo' command");
                // Add logic for 'undo'
                break;
            case "redo":
                System.out.println("Executing 'redo' command");
                // Add logic for 'redo'
                break;
            case "visualize":
                System.out.println("Executing 'visualize' command");
                // Add logic for 'visualize', this is the command to open the GUI
                break;
            case "save":
                //add logic to save the diagram
                break;
            case "load":
                //add logic to load a diagram, load <fileName>
                break;
            case "help":
                // This should print out the help menu showing cmd usage
                System.out.println(helpMenu);
                break;
            default:
                System.out.println("Unknown command: " + action);
        }
    }

    private void processAddCommand(String[] parts) {
        if (parts.length < 3) {
            System.out.println("Invalid 'add' command format. Usage: add <type> <name>");
            return;
        }

        String type = parts[1].toLowerCase();
        String name = parts[2];


        switch (type) {
            case "class":
                // Add logic for 'add class' with the class name (name)
                diagram.addClass(name);
                break;
            case "field":
                // Add logic for 'add field' with the field name (name)
                if(parts.length >= 6) {
                    String visibility = parts[3];
                    String fieldType = parts[4];
                    String fieldName = parts[5];
                    UMLClass cls = diagram.classExists(name);
                    if(cls != null)
                    {
                        cls.addField(fieldName, fieldType, visibility);
                    }
                    else {
                        System.out.println("Class " + name + " doesn't exist.");
                    }
                }
                else {
                    System.out.println("Please use format: add field <class> <visibility> <type> <name>");
                }
                break;
            case "method":
                // Add logic for 'add method' with the method name (name)
                if(parts.length >= 5)
                {
                    String returnType = parts[3];
                    String methodName = parts[4];
                    ArrayList<String> params = new ArrayList<>();
                    for(int i = 5; i < parts.length; i += 2)
                    {
                        String param = parts[i] + " " + parts[i+1];
                        params.add(param);
                    }
                    UMLClass cls = diagram.classExists(name);
                    if(cls != null)
                    {
                        cls.addMethod(methodName, returnType, params);
                    }
                    else {
                        System.out.println("Class " + name + " doesn't exist.");
                    }
                }
                else{
                    System.out.println("Please use format: add method <class> <returnType> <methodName> <param1Type> <param1Name> etc.");
                }
                break;
            case "relationship":
                // Add logic for add relationship method
                if(parts.length > 4) {
                    UMLRelationships.RelationshipType relType = UMLRelationships.RelationshipType.valueOf(parts[4].toUpperCase());
                    diagram.addRelationship(diagram.classExists(name), diagram.classExists(parts[3]), relType);
                }
                else {
                    System.out.println("Please use format: add relationship <class> <class> <type>");
                }
                break;
            case "parameter":
                System.out.println("Add param");
                // Add logic for adding a parameter
                System.out.println("Sorry, not implemented yet :(");
                break;
            default:
                System.out.println("Unknown 'add' subcommand: " + type);
        }
    }

    private void processDeleteCommand(String[] parts) {
        if (parts.length < 3) {
            System.out.println("Invalid 'delete' command format. Usage: delete <type> <name>");
            return;
        }

        String type = parts[1].toLowerCase();
        String name = parts[2];

        switch (type) {
            case "class":
                // Add logic for 'delete class' with the class name (name)
                diagram.removeClass(name);
                break;
            case "field":
                // Add logic for 'delete field' with the field name (name)
                if(parts.length > 3)
                {
                    UMLClass cls = diagram.classExists(name);
                    String fieldName = parts[3];
                    if(cls != null)
                    {
                        cls.removeField(fieldName);
                    }
                    else {
                        System.out.println("Class " + name + " doesn't exist.");
                    }
                }
                else {
                    System.out.println("Please use format: delete field <class> <fieldName>");
                }
                break;
            case "method":
                // Add logic for 'delete method' with the method name (name)
                if(parts.length > 3)
                {
                    UMLClass cls = diagram.classExists(name);
                    if(cls != null)
                    {
                        int index = Integer.parseInt(parts[3]);
                        cls.removeMethod(index - 1);
                    }
                    else {
                        System.out.println("Class " + name + " doesn't exist.");
                    }
                }
                else {
                    System.out.println("Please use the list class <name> command to show the list of methods. Then " +
                            "use this format: delete method <class> <numberInList>");
                }
                break;
            case "relationship":
                // Add logic for deleting relationships
                UMLRelationships rel = diagram.relationshipExists(name + parts[3]);
                if(rel != null)
                {
                    int index = UMLDiagram.getRelationships().indexOf(rel);
                    diagram.removeRelationship(index);
                    System.out.println("Relationship removed.");
                }
                else {
                    System.out.println("There is no relationship between " + name + " and " + parts[3]);
                }
                break;
            case "parameter":
                System.out.println("Delete param");
                // Add logic for deleting a parameter
                System.out.println("Sorry, not implemented yet :(");
                break;
            default:
                System.out.println("Unknown 'delete' subcommand: " + type);
        }
    }

    private void processRenameCommand(String[] parts) {
        if (parts.length < 4) {
            System.out.println("Invalid 'rename' command format. Usage: rename <type> <oldName> <newName>");
            return;
        }

        String type = parts[1].toLowerCase();
        String oldName = parts[2];
        String newName = parts[3];

        switch (type) {
            case "class":
                // Add logic for 'rename class' with the old and new class names (oldName, newName)
                diagram.renameClass(oldName, newName);
                break;
            case "field":
                // Add logic for 'rename field' with the old and new field names (oldName, newName)
                if(parts.length > 4)
                {
                    String className = parts[2];
                    oldName = parts[3];
                    newName = parts[4];
                    UMLClass cls = diagram.classExists(className);
                    if(cls != null)
                    {
                        cls.renameField(oldName, newName);
                    }
                    else {
                        System.out.println("Class" + className + " doesn't exist.");
                    }
                }
                else {
                    System.out.println("Please use format: rename field <class> <old> <new>");
                }
                break;
            case "method":
                // Add logic for 'rename method' with the old and new method names (oldName, newName)
                if(parts.length > 4)
                {
                    String className = parts[2];
                    int index = Integer.parseInt(parts[3]);
                    newName = parts[4];
                    UMLClass cls = diagram.classExists(className);
                    if(cls != null)
                    {
                        cls.renameMethod(index - 1, newName);
                    }
                    else {
                        System.out.println("Class " + className + " doesn't exist.");
                    }
                }
                else {
                    System.out.println("Please use the list class <name> command to show the list of methods. Then " +
                            "use this format: rename method <class> <numberInList> <newName>");
                }
                break;
            case "parameter":
                System.out.println("Executing 'rename parameter' command from '" + oldName + "' to '" + newName + "'");
                // Add logic for 'rename parameter' with the old and new parameter names (oldName, newName)
                System.out.println("Sorry, not implemented yet :(");
                break;
            default:
                System.out.println("Unknown 'rename' subcommand: " + type);
        }
    }

    private void processListCommand(String[] parts) {
        if (parts.length < 2) {
            System.out.println("Invalid 'list' command format. Usage: list <type>, list class <name>");
            return;
        }

        String type = parts[1].toLowerCase();
        String name = null;
        if(parts.length > 2)
            name = parts[2];

        switch (type) {
            case "all":
                // Add logic for 'list all'
                System.out.println();
                diagram.listClasses();
                break;
            case "class":
                // Add logic for 'list class'
                System.out.println();
                diagram.listClassContents(name);
                break;
            case "relationships":
                // Add Logic for listing relationships
                diagram.listRelationships();
                break;
            default:
                System.out.println("Unknown 'list' subcommand: " + type);
        }
    }






//    // method to return to main automatically after each operation
//    private void returnToMainMenu() {
//        System.out.println("Returning to the main menu...");
//    }
//
//    public void displayMenu() {
//        Scanner scanner = new Scanner(System.in);
//        boolean exit = false;
//
//        while (!exit) {
//            System.out.println("\n___ Welcome to the UML Diagram Menu!😘 ___");
//            System.out.println("Type 'r' to return to the main menu at any time.");
//            System.out.println(" ");
//            System.out.println("1: Class Options");
//            System.out.println("2: Relationship Options");
//            System.out.println("3: Save/Load Options");
//            System.out.println("4: Help");
//            System.out.println("5: Exit Program");
//
//            System.out.print("Enter your choice: ");
//            String choice = scanner.nextLine();
//
//            switch (choice) {
//                case "1":
//                    displayClassMenu(scanner);
//                    break;
//                case "2":
//                    displayRelationshipMenu(scanner);
//                    break;
//                case "3":
//                    displaySaveLoadMenu(scanner);
//                    break;
//                case "4":
//                    displayHelpMenu(scanner);
//                case "5":
//                    System.out.println("\n[Friendly Reminder], have you saved your hard work? (y/n)");
//                    String save = scanner.nextLine();
//
//                    if (save.equals("y")) {
//                        System.out.println("\nExiting the UML Diagram Menu. Have a good day! :3 ");
//                        exit = true;
//                        break;
//                    } else if (save.equals("n")) {
//                        System.out.println("\nPlease save your progress before exiting the program.");
//                        break;
//                    } else {
//                        System.out.println("\nInvalid choice. Please try again.");
//                        break;
//                    }
//                default:
//                    System.out.println("\nInvalid choice. Please try again.");
//            }
//        }
//        scanner.close();
//
//    }
//
//    private void displayClassMenu(Scanner scanner) {
//        boolean back = false;
//
//        while (!back) {
//            System.out.println("\n________Class Operations:________");
//            System.out.println("1: Add Class");
//            System.out.println("2: Remove Class");
//            System.out.println("3: Rename Class");
//            System.out.println("4: Field and Method Options");
//            System.out.println("5: Help");
//
//            System.out.print("Enter your choice: ");
//            String choice = scanner.nextLine();
//
//            if (choice.equals("r")) {
//                break; // Return to the main menu
//            }
//
//            switch (choice) {
//                case "1":
//                    // here user can add a class, they can also add fields and methods to the class if they wish
//                    System.out.print("\nEnter the class name to be added: ");
//                    String addClassName = null;
//                    boolean classAdded = false;
//
//                    while (!classAdded) {
//                        addClassName = scanner.nextLine().trim();
//                        if (addClassName.isEmpty()) {
//                            System.out.println("The class name cannot be empty. Please try again.");
//                        } else {
//                            diagram.addClass(addClassName);
//                            classAdded = true; // Set flag to true to exit the loop
//                        }
//
//                        if (!classAdded) {
//                            System.out.print("\nEnter the class name to be added: ");
//                        }
//                    }
//
//                    System.out.println("\nWould you also like to add fields and methods to the class? (y/n)");
//                    String addFieldsMethods = scanner.nextLine();
//                    if (addFieldsMethods.equals("y")) {
//                        System.out.println("\nHow many fields would you like to add?");
//                        int numFields = 0;
//                        boolean validNumFields = false;
//                        while (!validNumFields) {
//                            try {
//                                numFields = Integer.parseInt(scanner.nextLine());
//                                validNumFields = true; // Set flag to true to exit the loop
//                            } catch (NumberFormatException e) {
//                                System.out.println("Invalid input for the number of fields. Please enter a valid integer.");
//                            }
//                        }
//
//                        for (int i = 0; i < numFields; i++) {
//                            System.out.println("\nEnter the field name: ");
//                            String fieldName = scanner.nextLine();
//                            System.out.println("\nEnter the field type: ");
//                            String fieldType = scanner.nextLine();
//                            System.out.println("\nEnter the field visibility: ");
//                            String visibility = scanner.nextLine();
//                            diagram.classExists(addClassName).addField(fieldName, fieldType, visibility);
//                            System.out.println("\nField added.");
//                        }
//
//                        System.out.println("\nHow many methods would you like to add?");
//                        int numMethods = 0;
//                        boolean validNumMethods = false;
//                        while (!validNumMethods) {
//                            try {
//                                numMethods = Integer.parseInt(scanner.nextLine());
//                                validNumMethods = true; // Set flag to true to exit the loop
//                            } catch (NumberFormatException e) {
//                                System.out.println("Invalid input for the number of methods. Please enter a valid integer.");
//                            }
//                        }
//
//                        for (int i = 0; i < numMethods; i++) {
//                            System.out.println("\nEnter the method name: ");
//                            String methodName = scanner.nextLine();
//                            System.out.println("\nEnter the method type: ");
//                            String methodType = scanner.nextLine();
//                            System.out.println("\nEnter the methods parameters (Type name, Type name, ...): ");
//                            String parameters = scanner.nextLine();
//                            // if empty string, then no parameters
//                            ArrayList<String> parametersList;
//                            if (!parameters.isEmpty()) {
//                                parametersList = new ArrayList<>(Arrays.asList(parameters.split(", ")));
//                            } else {
//                                parametersList = new ArrayList<>();
//                            }
//                            // parse parameters into a string array list on the commas
//                            diagram.classExists(addClassName).addMethod(methodName, methodType, parametersList);
//                            System.out.println("\nMethod added.");
//                        }
//                    }
//                    // list added class with its fields and methods
//                    diagram.listClasses();
//                    returnToMainMenu();
//                    break;
//
//                case "2":
//                    diagram.listClasses();
//                    System.out.print("\nEnter the class name to be removed: ");
//                    String removeClassName = scanner.nextLine();
//                    diagram.removeClass(removeClassName);
//                    // print updated list of classes
//                    diagram.listClasses();
//                    returnToMainMenu();
//                    break;
//
//                case "3":
//                    diagram.listClasses();
//                    System.out.print("\nEnter the class name you want to renamed: ");
//                    String oldClassName = scanner.nextLine();
//                    System.out.print("\nEnter the new class name: ");
//                    String newClassName = scanner.nextLine();
//                    diagram.renameClass(oldClassName, newClassName);
//                    // print updated list of classes
//                    diagram.listClasses();
//                    returnToMainMenu();
//                    break;
//
//                case "4":
//                    // menu option for add field/method, remove field/method, rename field/method
//                    System.out.println("\n______Field and Method Options:______");
//                    System.out.println("1: Add Field");
//                    System.out.println("2: Remove Field");
//                    System.out.println("3: Rename Field");
//                    System.out.println("4: Add Method");
//                    System.out.println("5: Remove Method");
//                    System.out.println("6: Rename Method");
//                    System.out.println("7: Help");
//
//                    System.out.print("Enter your choice: ");
//                    String choice2 = scanner.nextLine();
//
//                     if (choice2.equals("r")) {
//                        break; // Return to the main menu
//                    }
//                    // switch statement for field and method options
//                    switch (choice2) {
//
//                        case "1":
//                            // add field logic
//                            System.out.print("\nEnter the class name: ");
//                            String addFieldClassName = scanner.nextLine();
//                            System.out.print("\nEnter the field name: ");
//                            String addFieldName = scanner.nextLine();
//                            System.out.print("\nEnter the field type: ");
//                            String addFieldType = scanner.nextLine();
//                            System.out.print("\nEnter the field visibility: ");
//                            String visibility = scanner.nextLine();
//                            diagram.classExists(addFieldClassName).addField(addFieldName, addFieldType, visibility);
//                            System.out.println("\nField added.");
//                            returnToMainMenu();
//                            break;
//
//                        case "2":
//                            // remove field logic
//                            System.out.print("\nEnter the class name: ");
//                            String removeFieldClassName = scanner.nextLine();
//                            System.out.print("\nEnter the field name: ");
//                            String removeFieldName = scanner.nextLine();
//                            diagram.classExists(removeFieldClassName).removeField(removeFieldName);
//                            returnToMainMenu();
//                            break;
//
//                        case "3":
//                            // rename field logic
//                            System.out.print("\nEnter the class name: ");
//                            String renameFieldClassName = scanner.nextLine();
//                            System.out.print("\nEnter the old field name: ");
//                            String oldFieldName = scanner.nextLine();
//                            System.out.print("\nEnter the new field name: ");
//                            String newFieldName = scanner.nextLine();
//                            diagram.classExists(renameFieldClassName).renameField(oldFieldName, newFieldName);
//                            returnToMainMenu();
//                            break;
//
//                        case "4":
//                            // add method logic
//                            System.out.print("\nEnter the class name: ");
//                            String addMethodClassName = scanner.nextLine();
//                            System.out.print("\nEnter the method name: ");
//                            String addMethodName = scanner.nextLine();
//                            System.out.print("\nEnter the method type: ");
//                            String addMethodType = scanner.nextLine();
//                            System.out.println("\nEnter the methods parameters (Type name, Type name, ...): ");
//                            String parameters = scanner.nextLine();
//                            // if empty string, then no parameters
//                            ArrayList<String> parametersList;
//                            if (!parameters.isEmpty()) {
//                                // parse parameters into an string array list on the commas
//                                parametersList = new ArrayList<String>(Arrays.asList(parameters.split(", ")));
//                            }
//                            else {
//                                parametersList = new ArrayList<String>();
//                            }
//                            diagram.classExists(addMethodClassName).addMethod(addMethodName, addMethodType, parametersList);
//                            returnToMainMenu();
//                            break;
//
//                        case "5":
//                            // remove method logic
//                            System.out.print("\nEnter the class name: ");
//                            String removeMethodClassName = scanner.nextLine();
//                            diagram.classExists(removeMethodClassName).listMethods();
//                            System.out.print("\nEnter the number of the method you want to remove: ");
//                            String removeMethodNumber = scanner.nextLine();
//                            int num = Integer.parseInt(removeMethodNumber);
//                            // check for valid input
//                            while (num < 0 || num > diagram.classExists(removeMethodClassName).getMethods().size()) {
//                                System.out.println("\nInvalid input. Please try again.");
//                                System.out.print("\nEnter the number of the method you want to remove: ");
//                                removeMethodNumber = String.valueOf(scanner.nextInt());
//                            }
//
//                            diagram.classExists(removeMethodClassName).removeMethod(Integer.parseInt(removeMethodNumber));
//                            // print updated list of methods
//                            diagram.classExists(removeMethodClassName).listMethods();
//                            returnToMainMenu();
//                            break;
//
//                        case "6":
//                            // rename method logic
//                            System.out.print("\nEnter the class name: ");
//                            String renameMethodClassName = scanner.nextLine();
//                            System.out.print("\nEnter the old method name: ");
//                            String oldMethodName = scanner.nextLine();
//                            System.out.print("\nEnter the new method name: ");
//                            String newMethodName = scanner.nextLine();
//                            diagram.classExists(renameMethodClassName).renameMethod(Integer.parseInt(oldMethodName), newMethodName);
//                            returnToMainMenu();
//                            break;
//
//                        case "7":
//                            // help with field and method options
//                            System.out.println("\nHelp with field and method options:");
//                            System.out.println("[1: Add Field] To add a field, select option '1' and provide the class name, field name, and field type.");
//                            System.out.println("[2: Remove Field] To remove a field, select option '2' and provide the class name and field name.");
//                            System.out.println("[3: Rename Field] To rename a field, select option '3' and provide the class name, old field name, and new field name.");
//                            System.out.println("[4: Add Method] To add a method, select option '4' and provide the class name, method name, and method type.");
//                            System.out.println("[5: Remove Method] To remove a method, select option '5' and provide the class name and method name.");
//                            System.out.println("[6: Rename Method] To rename a method, select option '6' and provide the class name, old method name, and new method name.");
//                            returnToMainMenu();
//                            break;
//
//                    }
//                    break;
//
//                case "5":
//                    System.out.println("\nHelp with class operations:");
//                    System.out.println("[1: Add Class] To add a class, select option '1' and provide the class name.");
//                    System.out.println("[2: Delete Class] To delete a class, select option '2' and provide the class name.");
//                    System.out.println("[3: Rename Class] To rename a class, select option '3' and provide the old and new class names.");
//                    System.out.println("[4: Field and Method Options] To access field and methods options, select option '4'.");
//                    break;
//
//                default:
//                    System.out.println("\nInvalid choice. Please try again.");
//
//            }
//
//            }
//        }
//
//    private void displayRelationshipMenu(Scanner scanner) {
//        boolean back = false;
//
//        while (!back) {
//            System.out.println("\n_______Relationship Operations:_______");
//            System.out.println("1: Add Relationship");
//            System.out.println("2: Remove Relationship");
//            System.out.println("3: Help");
//
//            System.out.print("Enter your choice: ");
//            String choice = scanner.nextLine();
//
//            if (choice.equals("r")) {
//                break; // Return to the main menu
//            }
//
//            switch (choice) {
//                case "1":
//                    System.out.print("\nEnter the source class name: ");
//                    String sourceClassName = scanner.nextLine();
//                    System.out.print("\nEnter the destination class name: ");
//                    String destinationClassName = scanner.nextLine();
//                    System.out.print("\nChoose relationship type: \n");
//                    System.out.println("1: Aggregation");
//                    System.out.println("2: Composition");
//                    System.out.println("3: Inheritance");
//                    System.out.println("4: Realization");
//
//                    String relationshipType = scanner.nextLine();
//                    int num = Integer.parseInt(relationshipType);
//                    while (num < 1 || num > 4) {
//                        System.out.println("\nInvalid input. Please try again.");
//                        System.out.print("\nChoose relationship type: ");
//                        System.out.println("1: Aggregation");
//                        System.out.println("2: Composition");
//                        System.out.println("3: Inheritance");
//                        System.out.println("4: Realization");
//                        relationshipType = scanner.nextLine();
//                        num = Integer.parseInt(relationshipType);
//                    }
//                    UMLRelationships.RelationshipType type = UMLRelationships.RelationshipType.values()[num - 1];
//
//                    diagram.addRelationship(diagram.classExists(sourceClassName), diagram.classExists(destinationClassName), type);
//
//                    returnToMainMenu();
//                    break;
//
//                case "2":
//                    // Remove relationship logic
//                    diagram.listRelationships();
//                    System.out.print("\nEnter the number of the relationship you want to remove: ");
//                    String removeRelationshipNumber = scanner.nextLine();
//                    Integer num2 = Integer.parseInt(removeRelationshipNumber);
//                    // check for valid input
//                    diagram.removeRelationship(Integer.parseInt(removeRelationshipNumber));
//                    // print updated list of relationships
//                    diagram.listRelationships();
//                    returnToMainMenu();
//                    break;
//
//                case "3":
//                    System.out.println("\nHelp with relationship operations:");
//                    System.out.println("[1: Add Relationship] To add a relationship, select option '1' and provide source class, destination class, and relationship type.");
//                    System.out.println("[2: Delete Relationship] To delete a relationship, select option '2' and provide source class and destination class.");
//                    break;
//
//                default:
//                    System.out.println("\nInvalid choice. Please try again.");
//            }
//        }
//
//    }
//
//
//    private void displaySaveLoadMenu(Scanner scanner) {
//        boolean back = false;
//        while (!back) {
//            System.out.println("\n______Save/Load Operations:______");
//            System.out.println("1: Save Diagram");
//            System.out.println("2: Load Diagram");
//            System.out.println("3: Help");
//            System.out.println("4: Back to Main Menu");
//
//            System.out.print("Enter your choice: ");
//            String choice = scanner.nextLine();
//
//            if (choice.equals("r")) {
//                break; // Return to the main menu
//            }
//
//            switch(choice)
//            {
//                case "1":
//                    System.out.print("\nEnter the file name: ");
//                    String fileName = scanner.nextLine();
//                    UMLSaveLoad saveLoad = new UMLSaveLoad(diagram);
//                    saveLoad.saveData(fileName);
//                    returnToMainMenu();
//                    break;
//                case "2":
//                    System.out.print("\nEnter the file name: ");
//                    String fileNamed = scanner.nextLine();
//                    UMLSaveLoad load = new UMLSaveLoad(diagram);
//                    load.loadData(fileNamed);
//                    returnToMainMenu();
//                    break;
//                case "3":
//                    System.out.println("\nHelp with save/load operations:");
//                    System.out.println("[1: Save Diagram] To save the diagram, select option '1' and provide the file name.");
//                    System.out.println("[2: Load Diagram] To load the diagram, select option '2' and provide the file name.");
//                    break;
//                case "4":
//                    back = true;
//                    break;
//                default:
//                    System.out.println("\nInvalid choice. Please try again.");
//            }
//        }
//
//    }
//
//    private void displayHelpMenu(Scanner scanner) {
//        System.out.println("\nHelp with UML Diagram Menu:");
//        System.out.println("[1: Class Options] To access class options, select option '1'.");
//        System.out.println("[2: Relationship Options] To access relationship options, select option '2'.");
//        System.out.println("[3: Save/Load Options] To access save/load options, select option '3'.");
//        System.out.println("[4: Help] To access help, select option '4'.");
//        System.out.println("[5: Exit Program] To exit the program, select option '5'.");
//        returnToMainMenu();
//        System.out.println(" ");
//
//    }

}

