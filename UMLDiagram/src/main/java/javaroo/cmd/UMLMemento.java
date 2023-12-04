package javaroo.cmd;

import javaroo.cmd.UMLClass;
import javaroo.cmd.UMLCommandManager;
import javaroo.cmd.UMLDiagram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class UMLMemento {
    private UMLDiagram state;

    public UMLMemento(UMLDiagram state) {
        // Perform a deep copy of the state
        this.state = new UMLDiagram();
        copyState(state, this.state);
    }

    public UMLDiagram getState() {
        return state;
    }

    private void copyState(UMLDiagram source, UMLDiagram destination) {
        // Deep copy classes
        for (Map.Entry<String, UMLClass> entry : source.getClasses().entrySet()) {
            String className = entry.getKey();
            UMLClass originalClass = entry.getValue();
            UMLClass copiedClass = new UMLClass(originalClass.getName(), originalClass.getX(), originalClass.getY());
            copiedClass.setFields(new ArrayList<>(originalClass.getFields()));
            copiedClass.setMethods(new ArrayList<>(originalClass.getMethods()));
            copiedClass.setWidth(originalClass.getWidth());
            copiedClass.setHeight(originalClass.getHeight());
            destination.getClasses().put(className, copiedClass);
        }

        // Deep copy relationships
        for (UMLRelationships relationship : source.getRelationships()) {
            UMLRelationships copiedRelationship = new UMLRelationships(
                    destination.classExists(relationship.getSource().getName()),
                    destination.classExists(relationship.getDest().getName()),
                    relationship.getType()
            );
            destination.getRelationships().add(copiedRelationship);
        }

        // Copy command manager state
        destination.setCommandManager(new UMLCommandManager());
        destination.getCommandManager().setUndoStack(new Stack<>());
        destination.getCommandManager().setRedoStack(new Stack<>());
    }


}
