package javaroo.cmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class UMLDiagram {
    private static Map<String, UMLClass> classes = new HashMap<>();
    private static List<UMLRelationships> relationships = new ArrayList<>();
    private UMLCommandManager commandManager = new UMLCommandManager();

    private Stack<UMLMemento> mementoStack = new Stack<>();
    private Stack<UMLMemento> undoStack = new Stack<>();
    private Stack<UMLMemento> redoStack = new Stack<>();

    public UMLDiagram() {
    }

    public static Map<String, UMLClass> getClasses() {
        return classes;
    }

    public static void setClasses(Map<String, UMLClass> classes) {
        UMLDiagram.classes = classes;
    }

    public static List<UMLRelationships> getRelationships() {
        return relationships;
    }

    public static void setRelationships(List<UMLRelationships> relationships) {
        UMLDiagram.relationships = relationships;
    }

    public void executeCommand(Command command) {
        command.execute();
        undoStack.push(createMemento()); // Save current state for potential undo
        redoStack.clear(); // Clear redo stack after executing a new command
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            UMLMemento undoMemento = undoStack.pop();
            redoStack.push(createMemento()); // Save current state for potential redo

            // Restore the state from the undoMemento
            setMemento(undoMemento);

            // Perform class updates and removals separately
            for (UMLClass originalClass : undoMemento.getState().getClasses().values()) {
                if (classExists(originalClass.getName()) != null) {
                    // If the class exists, update its details
                    updateClass(originalClass);
                } else {
                    // If the class does not exist, remove it
                    removeClassInternal(originalClass.getName());
                }
            }
        }
    }



    public void redo() {
        if (!redoStack.isEmpty()) {
            UMLMemento redoMemento = redoStack.pop();
            undoStack.push(createMemento()); // Save current state for potential undo

            // Restore the state from the redoMemento
            setMemento(redoMemento);
        }
    }

    public void removeClassInternal(String name) {
        UMLClass removedClass = classes.remove(name);
        if (removedClass != null) {
            System.out.println("Class deleted: " + name);
        } else {
            System.out.println("Class '" + name + "' does not exist.");
        }
    }


    public UMLMemento createMemento() {
        return new UMLMemento(this);
    }

    public void setMemento(UMLMemento memento) {
        UMLDiagram newState = memento.getState();

        // Update the current state with the new state
        setClasses(newState.getClasses());
        setRelationships(newState.getRelationships());
        // Other state updates if needed
    }

    public void listClasses() {
        if (classes.isEmpty()) {
            System.out.println("No classes defined.");
        } else {
            for (String className : classes.keySet()) {
                listClassContents(className);
                System.out.println();
            }
        }
    }

    void listClassContents(String className) {
        UMLClass umlClassEntity = classExists(className);
        if (umlClassEntity != null) {
            System.out.println(umlClassEntity.toString());
        } else {
            System.out.println("Class '" + className + "' does not exist.");
        }
    }

    public UMLClass classExists(String name) {
        return classes.get(name);
    }

    public void addClass(String name) {
        if (classExists(name) != null) {
            System.out.println("Sorry, but this class already exists");
            return;
        }

        if (UMLCommandManager.getLastExecutedCommand() instanceof AddClassCommand) {
            System.out.println("Error: Adding class failed due to recursion.");
            return;
        }

        Command addClassCommand = new AddClassCommand(this, name);
        executeCommand(addClassCommand);
    }

    void addClassInternal(String name) {
        String processedName = name.trim();

        if (processedName.isEmpty()) {
            System.out.println("Sorry, but we could not find a valid name for this class");
            return;
        }

        if (classExists(processedName) != null) {
            System.out.println("Sorry, but this class already exists");
            return;
        }

        UMLClass newClass = new UMLClass(processedName);
        classes.put(processedName, newClass);
        System.out.println("Class added: " + processedName);
    }

    public void undoRemoveClass(String name) {
        UMLClass removedClass = classExists(name);
        if (removedClass != null) {
            // Add the removed class back to the classes map
            getClasses().put(name, removedClass);
            System.out.println("Class restored: " + name);
        }
    }


    public void renameClass(String oldName, String newName) {
        if (oldName == null || newName == null) {
            System.out.println("Sorry but we could not find a valid name for this class");
            return;
        }
        if (classExists(oldName) == null) {
            System.out.println("Sorry but this class does not exist");
            return;
        }
        if (classExists(newName) != null) {
            System.out.println("Sorry but this class already exists");
            return;
        }

        UMLClass existingClass = classExists(oldName);
        UMLClass updatedClass = new UMLClass(newName);
        updatedClass.setFields(existingClass.getFields());
        updatedClass.setMethods(existingClass.getMethods());
        updatedClass.setWidth(existingClass.getWidth());
        updatedClass.setHeight(existingClass.getHeight());

        // Update the existing class with the new name
        getClasses().remove(oldName);
        getClasses().put(newName, updatedClass);

        // Inform the user about the successful rename
        System.out.println("Class renamed: " + oldName + " to " + newName);
    }



    public void listRelationships() {
        int index = 0;
        if (relationships.isEmpty()) {
            System.out.println("No relationships defined.");
        } else {
            System.out.println("Relationships:");
            for (UMLRelationships relationship : relationships) {
                System.out.println(index + ":" + relationship.getSource().getName() + " --> " +
                        relationship.getDest().getName() + "::" + relationship.getType());
                index++;
            }
        }
    }

    public void addRelationship(UMLClass src, UMLClass dest, UMLRelationships.RelationshipType type) {
        if (src == null || dest == null || type == null) {
            System.out.println("Invalid source, destination, or relationship type");
            return;
        }

        String relationshipId = src.getName() + dest.getName();
        if (relationshipExists(relationshipId) != null) {
            System.out.println("Relationship already exists");
            return;
        }

        UMLRelationships newRelationship = new UMLRelationships(src, dest, type);
        relationships.add(newRelationship);
        System.out.println("Relationship added: " + relationshipId);
    }


//    public void removeRelationship(int index) {
//        if (index < 0 || index >= relationships.size()) {
//            System.out.println("Sorry but we could not find a valid index for this relationship");
//            return;
//        }
//        Command removeRelationshipCommand = new RemoveRelationshipCommand(this, index);
//        commandManager.executeCommand(removeRelationshipCommand);
//    }

    public void removeRelationship(int index) {
        if (index < 0 || index >= relationships.size()) {
            System.out.println("Invalid index for relationship removal");
            return;
        }

        relationships.remove(index);
    }

    public void updateClass(UMLClass originalClass) {
        if (originalClass == null) {
            System.out.println("Invalid originalClass provided for update.");
            return;
        }

        String className = originalClass.getName();
        UMLClass existingClass = classExists(className);

        if (existingClass != null) {
            existingClass.setFields(new ArrayList<>(originalClass.getFields()));
            existingClass.setMethods(new ArrayList<>(originalClass.getMethods()));
            System.out.println("Class updated: " + className);
        } else {
            System.out.println("Class not found: " + className);
        }
    }

    public UMLCommandManager getCommandManager() {
        return commandManager;
    }

    public void setCommandManager(UMLCommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public UMLRelationships relationshipExists(String id) {
        if (id == null) {
            System.out.println("Sorry but we could not find a valid id for this relationship");
            return null;
        }

        for (UMLRelationships r : relationships) {
            if (r.getId().equals(id)) {
                System.out.println("Found existing relationship: " + id);
                return r;
            }
        }
        return null;
    }


//    public void removeClassNameOnly(String name) {
//        if (name == null) {
//            System.out.println("Sorry but we could not find a valid name for this class");
//            return;
//        }
//        if (classExists(name) == null) {
//            System.out.println("Sorry but this class does not exist");
//            return;
//        }
//
//        // Remove the UMLClass object from the classes map without creating a new command
//        classes.remove(name);
//        System.out.println("Class deleted: " + name);
//    }

}
