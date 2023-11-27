package javaroo.cmd;

public class AddRelationshipCommand implements Command {
    private UMLDiagram umlDiagram;
    private UMLClass src;
    private UMLClass dest;
    private UMLRelationships.RelationshipType type;

    public AddRelationshipCommand(UMLDiagram umlDiagram, UMLClass src, UMLClass dest, UMLRelationships.RelationshipType type) {
        this.umlDiagram = umlDiagram;
        this.src = src;
        this.dest = dest;
        this.type = type;
    }

    @Override
    public void execute() {
        umlDiagram.addRelationship(src, dest, type);
    }

    @Override
    public void undo() {
        // Check if there are relationships to undo
        if (!umlDiagram.getRelationships().isEmpty()) {
            // Remove the last relationship
            int lastIndex = umlDiagram.getRelationships().size() - 1;
            umlDiagram.removeRelationship(lastIndex);
        } else {
            System.out.println("No relationships to undo.");
        }
    }

}
