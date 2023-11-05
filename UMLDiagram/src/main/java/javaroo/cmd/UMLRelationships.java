package javaroo.cmd;

public class UMLRelationships {

    private UMLClass dest;
    private UMLClass source;
    private String id;

    private double startX; // Starting X-coordinate
    private double startY; // Starting Y-coordinate
    private double endX; // Ending X-coordinate
    private double endY; // Ending Y-coordinate
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

    public UMLRelationships(final UMLClass source, final UMLClass dest, final RelationshipType type, final double startX, final double startY, final double endX, final double endY) {
        // Check each parameter is not null
        if (source == null || dest == null || type == null) {
            System.out.println("Sorry, but we could not find a valid source, destination, or type for this relationship");
            return;
        }
        this.source = source;
        this.dest = dest;
        this.id = source.getName() + dest.getName();
        this.type = type;
        this.startX = 0; // Initialize to default values
        this.startY = 0;
        this.endX = 0;
        this.endY = 0;
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

    public double getStartX() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public double getStartY() {
        return startY;
    }

    public void setStartY(double startY) {
        this.startY = startY;
    }

    public double getEndX() {
        return endX;
    }

    public void setEndX(double endX) {
        this.endX = endX;
    }

    public double getEndY() {
        return endY;
    }

    public void setEndY(double endY) {
        this.endY = endY;
    }
}
