public class UMLRelationships {

    final private UMLClass dest;
    final private UMLClass source;

    public UMLRelationships(final UMLClass source, final UMLClass dest) {
        this.source = source;
        this.dest = dest;
    }

    public void addRelationship(final UMLClass source, final UMLClass dest) {

        if (source == null || dest == null)
            throw new IllegalArgumentException("Cannot find source or destination");

        relationships.add(new UMLRelationships(source, dest));

    }

    
    public void deleteRelationship(final UMLClass source, final UMLClass dest) {
        
        if (!nameList.contains(source) || !nameList.contains(dest))
            throw new IllegalArgumentException("Cannot find source or destination of relationship");
            
        relationships.remove(new UMLRelationships(source, dest));
        
    }


}