package javaroo.cmd;

public class RenameClassCommand implements Command {
    private UMLDiagram umlDiagram;
    private String oldName;
    private String newName;

    public RenameClassCommand(UMLDiagram umlDiagram, String oldName, String newName) {
        this.umlDiagram = umlDiagram;
        this.oldName = oldName;
        this.newName = newName;
    }

    @Override
    public void execute() {
        umlDiagram.renameClass(oldName, newName);
    }

    @Override
    public void undo() {
        umlDiagram.renameClass(newName, oldName);
    }
}
