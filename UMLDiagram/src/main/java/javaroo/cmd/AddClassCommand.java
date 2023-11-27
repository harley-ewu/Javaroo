package javaroo.cmd;

import java.util.Objects;
import java.util.Stack;

import javaroo.cmd.UMLCommandManager;

public class AddClassCommand implements Command {
    private UMLDiagram umlDiagram;
    private String className;
    public static Stack<Command> undoStack = new Stack<>();

    public AddClassCommand(UMLDiagram umlDiagram, String className) {
        this.umlDiagram = umlDiagram;
        this.className = className;
    }

    @Override
    public void execute() {
        umlDiagram.addClassInternal(className);
    }

    @Override
    public void undo() {
        umlDiagram.removeClassInternal(className);
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AddClassCommand that = (AddClassCommand) obj;
        return Objects.equals(className, that.className);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className);
    }


    public String getClassName() {
        return className;
    }

    public static boolean commandExists(Command command) {
        return undoStack.contains(command);
    }

}
