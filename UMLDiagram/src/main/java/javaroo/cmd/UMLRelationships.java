public class UMLRelationships {

    final private UMLClass dest;
    final private UMLClass source;
    final private String id;
    private String type;

    public UMLRelationships(final UMLClass source, final UMLClass dest, final String type) {
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

    public static void addRelationship(final UMLClass source, final UMLClass dest) {

        if (source == null || dest == null) {
            System.out.println("Sorry but we could not find a valid source or destination for this diagram");
            return;
        }

        UMLDiagram.getRelationships().add(new UMLRelationships(source, dest));

    }

    
    public static void deleteRelationship(final UMLDiagram diagram, final UMLRelationships rel) {
        
        if (rel == null) {
            System.out.println("Sorry but we could not find a valid relationship for this diagram");
            return;
        }
            
        diagram.getRelationships().remove(rel);
        
    }


}