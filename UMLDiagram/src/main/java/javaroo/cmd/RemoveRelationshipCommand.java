package javaroo.cmd;

public class RemoveRelationshipCommand implements Command {
    private UMLDiagram umlDiagram;
    private int index;

    public RemoveRelationshipCommand(UMLDiagram umlDiagram, int index) {
        this.umlDiagram = umlDiagram;
        this.index = index;
    }

    @Override
    public void execute() {
        umlDiagram.removeRelationship(index);
    }

    @Override
    public void undo() {
        // For simplicity, assuming adding the same relationship back
        UMLRelationships relationship = umlDiagram.getRelationships().get(index);
        umlDiagram.addRelationship(relationship.getSource(), relationship.getDest(), relationship.getType());
    }
}
