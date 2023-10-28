package javaroo.cmd;

public class UMLRelationships {

    final private UMLClass dest;
    final private UMLClass source;
    final private String id;

    public UMLRelationships(final UMLClass source, final UMLClass dest) {
        this.source = source;
        this.dest = dest;
        this.id = source.getName() + dest.getName();
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
        UMLDiagram.setSaved(false);

    }

    
    public static void deleteRelationship(final UMLDiagram diagram, final UMLRelationships rel) {
        
        if (rel == null) {
            System.out.println("Sorry but we could not find a valid relationship for this diagram");
            return;
        }
            
        diagram.getRelationships().remove(rel);
        UMLDiagram.setSaved(false);
        
    }


}