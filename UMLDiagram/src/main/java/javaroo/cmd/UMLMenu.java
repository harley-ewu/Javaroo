package javaroo.cmd;
import javaroo.cmd.UMLDiagram;
import javaroo.umldiagram.*;

import org.fusesource.jansi.AnsiConsole;
import java.io.IOException;
import java.util.*;

import org.jline.reader.*;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jline.terminal.TerminalBuilder;
import static javafx.application.Application.launch;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import static javaroo.umldiagram.UMLDiagramGUI.myLaunch;
public class UMLMenu {
    private static final Logger logger = LoggerFactory.getLogger(UMLMenu.class);

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
            "  save <fileName>\n" +
            "  load <fileName>\n" +
            "  visualize\n\t-This command opens up the graphical user interface.\n" +
            "  exit";

    public static boolean GView = false;
    public static boolean CView = true;
  
    public static void main(String[] args) {
        UMLMenu menu = new UMLMenu();
        try {
            // Check for console availability
            if (System.console() == null) {
                System.err.println("No console available. Falling back to a simpler interface.");
                // Implement a simpler, non-interactive command processing here if needed
                return;
            }

            // Install AnsiConsole for ANSI support on Windows
            if (System.getProperty("os.name").startsWith("Windows")) {
                AnsiConsole.systemInstall();
            }

            String osName = System.getProperty("os.name").toLowerCase();
            boolean isMacOS = osName.startsWith("mac");

            // Configure the terminal with JNA and Jansi
            Terminal terminal;
            try {
                TerminalBuilder builder = TerminalBuilder.builder()
                        .system(true)
                        .jna(true)
                        .jansi(false)
                        .exec(false)
                        .dumb(false);

                // Set exec to true if the operating system is macOS
                if (isMacOS) {
                    builder.exec(true);
                }

                terminal = builder.build();
            } catch (IOException e) {
                System.err.println("Failed to build the terminal: " + e.getMessage());
                return;
            }
            // Build the line reader with a custom completer
            LineReader lineReader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .completer(new CommandCompleter()) // Replace with your completer
                    .build();

            // Display help menu
            System.out.println(helpMenu);
            String prompt = "Enter command: ";
            String line;

            // Command processing loop
            while (!(line = lineReader.readLine(prompt)).equals("exit")) {
                try {
                    menu.processCommandCLI(line); // Replace with your command processing logic
                } catch (Exception e) {
                    System.err.println("Error processing command: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Uninstall AnsiConsole if it was installed
            if (System.getProperty("os.name").startsWith("Windows")) {
                AnsiConsole.systemUninstall();
            }
        }
    }


//    private static Terminal createTerminal(String type) throws Exception {
//        switch (type) {
//            case "exec":
//                return TerminalBuilder.builder().exec(true).build();
//            case "unix":
//                return TerminalBuilder.builder().unix(true).build();
//            case "win":
//                return TerminalBuilder.builder().win(true).build();
//            case "jna":
//                return TerminalBuilder.builder().jna(true).build();
//            case "jansi":
//                return TerminalBuilder.builder().jansi(true).build();
//            case "false":
//                return TerminalBuilder.builder().dumb(true).build();
//            default:
//                return TerminalBuilder.builder().build();
//        }
//    }

    private static Terminal terminal;

    private static void setupTerminal() {
        try {
            // Create a terminal instance
            Terminal terminal = TerminalBuilder.builder()
                    .system(true)
                    .jna(true)
                    .jansi(false)
                    .exec(false)
                    .dumb(false)
                    .build();

            if (terminal == null) {
                System.err.println("Terminal is not initialized.");
                return;
            }

            // Create a LineReader for the terminal
            LineReader lineReader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .build();

            String prompt = "Enter command: ";
            String line;

            // Read and process lines
            while (!(line = lineReader.readLine(prompt)).equals("exit")) {
                System.out.println("Command entered: " + line);
            }

        } catch (Exception e) {
            System.err.println("Error in command processing: " + e.getMessage());
        }
    }


    private static void processCommands() {
        if (terminal == null) {
            System.err.println("Terminal is not initialized.");
            return;
        }

    }




    private static void cleanup() {
        if (System.getProperty("os.name").startsWith("Windows")) {
            AnsiConsole.systemUninstall();
            System.out.println("AnsiConsole uninstalled.");
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
                undo();
                break;
            case "redo":
                System.out.println("Executing 'redo' command");
                // Add logic for 'redo'
                redo();
                break;
            case "visualize":
                System.out.println("Executing 'visualize' command");
                // Add logic for 'visualize', this is the command to open the GUI
                myLaunch(UMLDiagramGUI.class);
                break;
            case "save":
                //add logic to save the diagram
                String fileName = parts[1];
                UMLSaveLoad saveLoad = new UMLSaveLoad(diagram);
                saveLoad.saveData(fileName);
                break;
            case "load":
                //add logic to load a diagram, load <fileName>
                fileName = parts[1];
                UMLSaveLoad load = new UMLSaveLoad(diagram);
                load.loadData(fileName);
                break;
            case "help":
                // This should print out the help menu showing cmd usage
                System.out.println(helpMenu);
                break;
            default:
                System.out.println("Unknown command: " + action);
        }
    }
  
    private void undo() {
        UMLCommandManager.undo();
        System.out.println("Undo");
    }

    private void redo() {
        UMLCommandManager.redo();
        System.out.println("Redo");
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
                UMLCommandManager.executeCommand(new AddClassCommand(diagram, name));
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
                UMLCommandManager.executeCommand(new RemoveClassCommand(diagram, name));
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
                UMLCommandManager.executeCommand(new RenameClassCommand(diagram, oldName, newName));
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


    
}
