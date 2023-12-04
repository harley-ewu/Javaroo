package javaroo.cmd;

import java.util.Stack;

public class UMLCommandManager {
    private static Stack<Command> undoStack = new Stack<>();
    private static Stack<Command> redoStack = new Stack<>();

    public static void executeCommand(Command command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear(); // Clear redo stack after executing a new command
    }

    public static void undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            redoStack.push(command);
        }
    }

    public static void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            undoStack.push(command);
        }
    }

    public static Command getLastExecutedCommand() {
        return undoStack.isEmpty() ? null : undoStack.peek();
    }

    public static boolean commandExists(Command command) {
        return undoStack.stream().anyMatch(cmd -> cmd.equals(command));
    }

    // Getter and setter for undoStack
    public static Stack<Command> getUndoStack() {
        return undoStack;
    }

    public static void setUndoStack(Stack<Command> stack) {
        undoStack = stack;
    }

    // Getter and setter for redoStack
    public static Stack<Command> getRedoStack() {
        return redoStack;
    }

    public static void setRedoStack(Stack<Command> stack) {
        redoStack = stack;
    }
}
