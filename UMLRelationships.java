public class UMLRelationships {

    final private UMLClass dest;
    final private UMLClass source;
    final private String id;

    public UMLRelationships(final UMLClass source, final UMLClass dest) {
        this.source = source;
        this.dest = dest;
        this.id = source.getName() + dest.getName();
    }

    public void addRelationship(final UMLDiagram diagram, final UMLClass source, final UMLClass dest) {

        if (source == null || dest == null) {
            System.out.println("Sorry but we could not find a valid source or destination for this diagram");
            return;
        }

        diagram.getRelationships().add(new UMLRelationships(source, dest));

    }

    
    public void deleteRelationship(final UMLDiagram diagram, final UMLRelationships rel) {
        
        if (!diagram.getRelationships().contains(source) || !diagram.getRelationships().contains(dest)) {
            System.out.println("Sorry but we could not find a valid source or destination for this diagram");
            return;
        }
            
        diagram.getRelationships().remove(rel);
        
    }


}