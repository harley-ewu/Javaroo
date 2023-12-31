package javaroo.cmd;
import javafx.application.Application;
import javafx.application.Platform;
import javaroo.umldiagram.*;
import org.fusesource.jansi.AnsiConsole;
import java.io.IOException;
import java.util.*;
import org.jline.reader.*;
import org.jline.terminal.Terminal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jline.terminal.TerminalBuilder;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
public class UMLMenu {
    private static final Logger logger = LoggerFactory.getLogger(UMLMenu.class);

    private UMLDiagram diagram;

    private UMLView umlView; // Add a field for UMLView

    public UMLMenu(UMLView umlView) {
        this.diagram = new UMLDiagram();
        this.umlView = umlView; // Initialize UMLView
    }



    private static final List<String> CMDS = Arrays.asList("add class ", "add field ", "add method ", "add relationship ", "add parameter ",
            "delete class ", "delete field ", "delete method ", "delete relationship ", "delete parameter ",
    "rename class ", "rename field ", "rename method ", "list all",
    "list class ", "list relationships", "undo", "redo", "visualize", "save", "load ", "help", "exit");

    private static final String helpMenu = "Available Commands:\n" +
            "Add Commands:\n" +
            "  add class <ClassName>\n" +
            "  add field <class> <visibility> <type> <name>\n" +
            "  add method <class> <returnType> <methodName> <param1Type> <param1Name> etc.\n" +
            "  add relationship <sourceClass> <destinationClass> <type>\n" +
            "  add parameter <class>\n\n" +

            "Delete Commands:\n" +
            "  delete class <ClassName>\n" +
            "  delete field <class> <fieldName>\n" +
            "  delete method <class>\n" +
            "  delete relationship <sourceClass> <destinationClass>\n" +
            "  delete parameter <class>\n\n" +

            "Rename Commands:\n" +
            "  rename class <OldClassName> <NewClassName>\n" +
            "  rename field <class> <old> <new>\n" +
            "  rename method <class>\n" +

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
        new Thread(() -> Application.launch(UMLDiagramGUI.class)).start();
        UMLDiagramGUI.waitForInitialization();

        // Obtain UMLController from UMLDiagramGUI
        UMLController umlController = UMLDiagramGUI.getController(); // Implement this method in UMLDiagramGUI

        // Use the UMLView from the controller
        UMLView umlView = umlController.getUMLView(); // Implement getUMLView in UMLController

        // Pass the UMLView instance to UMLMenu
        UMLMenu menu = new UMLMenu(umlView);
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
                UMLDiagramGUI.getInstance().showGUI(true);
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
            case "exit":
                System.exit(0);
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
        Platform.runLater(() -> {
            if (umlView != null) {
                umlView.refresh();
            } else {
                System.out.println("Error: UMLView is null.");
            }
        });
        System.out.println("Undo");
    }

    private void redo() {
        UMLCommandManager.redo();
        Platform.runLater(() -> {
            if (umlView != null) {
                umlView.refresh();
            } else {
                System.out.println("Error: UMLView is null.");
            }
        });
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
                Platform.runLater(() -> {
                    if (umlView != null) {
                        umlView.refresh();
                    } else {
                        System.out.println("Error: UMLView is null.");
                    }
                });
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
                        Platform.runLater(() -> {
                            if (umlView != null) {
                                umlView.refresh();
                            } else {
                                System.out.println("Error: UMLView is null.");
                            }
                        });
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
                        Platform.runLater(() -> {
                            if (umlView != null) {
                                umlView.refresh();
                            } else {
                                System.out.println("Error: UMLView is null.");
                            }
                        });
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
                    Platform.runLater(() -> {
                        if (umlView != null) {
                            umlView.refresh();
                        } else {
                            System.out.println("Error: UMLView is null.");
                        }
                    });
                }
                else {
                    System.out.println("Please use format: add relationship <class> <class> <type>");
                }
                break;
            case "parameter":
                // Add logic for adding a parameter
                if(parts.length == 3)
                {
                    if(diagram.classExists(name) != null)
                    {
                        diagram.listClassContents(name);
                        System.out.println("Choose the method you want to remove from the above list and re-enter the command in this format: add parameter <class> <numInList> <paramType> <paramName>");
                    }
                    else {
                        System.out.println("Class " + name + " doesn't exist.");
                    }
                }
                else
                {
                    UMLClass cls = diagram.classExists(name);
                    if(cls != null)
                    {
                        int index = Integer.parseInt(parts[3]);
                        UMLMethods method = cls.getMethods().get(index - 1);
                        method.addParameters(parts[4] + " " + parts[5]);
                    }
                    else {
                        System.out.println("Class " + name + " doesn't exist.");
                    }
                }
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
                Platform.runLater(() -> {
                    if (umlView != null) {
                        umlView.updateCanvasRemoveClass(name);
                        umlView.refresh();
                    } else {
                        System.out.println("Error: UMLView is null.");
                    }
                });
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
                        Platform.runLater(() -> {
                            if (umlView != null) {
                                umlView.refresh();
                            } else {
                                System.out.println("Error: UMLView is null.");
                            }
                        });
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
                if(parts.length == 3)
                {
                    if(diagram.classExists(name) != null)
                    {
                        diagram.listClassContents(name);
                        System.out.println("Choose the method you want to remove from the above list and re-enter the command in this format: delete method <class> <numInList> ");
                    }
                    else {
                        System.out.println("Class " + name + " doesn't exist.");
                    }
                }
                else
                {
                    UMLClass cls = diagram.classExists(name);
                    if(cls != null)
                    {
                        int index = Integer.parseInt(parts[3]);
                        cls.removeMethod(index - 1);
                        Platform.runLater(() -> {
                            if (umlView != null) {
                                umlView.refresh();
                            } else {
                                System.out.println("Error: UMLView is null.");
                            }
                        });
                    }
                    else {
                        System.out.println("Class " + name + " doesn't exist.");
                    }
                }
                break;
            case "relationship":
                // Add logic for deleting relationships
                UMLRelationships rel = diagram.relationshipExists(name + parts[3]);
                if(rel != null)
                {
                    int index = UMLDiagram.getRelationships().indexOf(rel);
                    diagram.removeRelationship(index);
                    Platform.runLater(() -> {
                        if (umlView != null) {
                            umlView.refresh();
                        } else {
                            System.out.println("Error: UMLView is null.");
                        }
                    });
                    System.out.println("Relationship removed.");
                }
                else {
                    System.out.println("There is no relationship between " + name + " and " + parts[3]);
                }
                break;
            case "parameter":
                // Add logic for deleting a parameter
                if(parts.length == 3)
                {
                    if(diagram.classExists(name) != null)
                    {
                        diagram.listClassContents(name);
                        System.out.println("Choose the method you want to remove from the above list and re-enter the command in this format: delete parameter <class> <numInList> <paramType> <paramName>");
                    }
                    else {
                        System.out.println("Class " + name + " doesn't exist.");
                    }
                }
                else
                {
                    UMLClass cls = diagram.classExists(name);
                    if(cls != null)
                    {
                        int index = Integer.parseInt(parts[3]);
                        UMLMethods method = cls.getMethods().get(index - 1);
                        method.removeParameters(parts[4] + " " + parts[5]);
                    }
                    else {
                        System.out.println("Class " + name + " doesn't exist.");
                    }
                }
                break;
            default:
                System.out.println("Unknown 'delete' subcommand: " + type);
        }
    }


    private void processRenameCommand(String[] parts) {
        if (parts.length < 3) {
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
                Platform.runLater(() -> {
                    if (umlView != null) {
                        umlView.refresh();
                    } else {
                        System.out.println("Error: UMLView is null.");
                    }
                });
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
                        Platform.runLater(() -> {
                            if (umlView != null) {
                                umlView.refresh();
                            } else {
                                System.out.println("Error: UMLView is null.");
                            }
                        });
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
                if(diagram.classExists(oldName) != null)
                {
                    diagram.listClassContents(oldName);
                    System.out.println("Choose the method you want to remove from the above list and re-enter the command in this format: delete method <class> <numInList> ");
                }
                else {
                    System.out.println("Class " + oldName + " doesn't exist.");
                }
                if(parts.length > 4)
                {
                    String className = parts[2];
                    int index = Integer.parseInt(parts[3]);
                    newName = parts[4];
                    UMLClass cls = diagram.classExists(className);
                    if(cls != null)
                    {
                        cls.renameMethod(index - 1, newName);
                        Platform.runLater(() -> {
                            if (umlView != null) {
                                umlView.refresh();
                            } else {
                                System.out.println("Error: UMLView is null.");
                            }
                        });
                    }
                    else {
                        System.out.println("Class " + className + " doesn't exist.");
                    }
                }
                else {
                    System.out.println("Please " +
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
