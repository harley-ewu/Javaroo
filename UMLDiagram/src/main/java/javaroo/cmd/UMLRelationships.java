package javaroo.cmd;

public class UMLRelationships {

    private UMLClass dest;
    private UMLClass source;
    private String id;
    RelationshipType type;

    public enum RelationshipType {
        AGGREGATION, COMPOSITION, INHERITANCE, REALIZATION
    }

    public UMLRelationships(final UMLClass source, final UMLClass dest, final RelationshipType type) {
        //check each parameter is not null
        if (source == null || dest == null || type == null) {
            System.out.println("Sorry but we could not find a valid source, destination or type for this relationship");
            return;
        }
        this.source = source;
        this.dest = dest;
        this.id = source.getName() + dest.getName();
        this.type = type;
    }

    public UMLClass getDest() {
        return dest;
    }

    public UMLClass getSource() {
        return source;
    }

    public String getId() {
        return id;
    }

    public RelationshipType getType() {
        return type;
    }
}