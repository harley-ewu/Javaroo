package javaroo.cmd;

import java.util.Objects;
import java.util.Stack;

public class RemoveClassCommand implements Command {
    private UMLDiagram umlDiagram;
    private String className;
    private Stack<Command> undoStack;

    public RemoveClassCommand(UMLDiagram umlDiagram, String className) {
        this.umlDiagram = umlDiagram;
        this.className = className;
        this.undoStack = undoStack;
    }

    @Override
    public void execute() {
        umlDiagram.removeClassInternal(className);
    }

    @Override
    public void undo() {
        umlDiagram.undoRemoveClass(className);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RemoveClassCommand that = (RemoveClassCommand) obj;
        return Objects.equals(className, that.className);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className);
    }

    public String getClassName() {
        return className;
    }
}
