public class UMLRelationships {

    final private UMLClass dest;
    final private UMLClass source;

    public UMLRelationships(final UMLClass source, final UMLClass dest) {
        this.source = source;
        this.dest = dest;
    }

    public UMLRelationships addRelationship(final UMLClass source, final UMLClass dest) {

        if (source == null || dest == null)
            throw new IllegalArgumentException("Cannot find destination or source or relationship");

        UMLRelationships newRel = new UMLClass(source, dest);

        return newRel;

    }

    // 
    public void deleteRelationship(UMLClass dest, UMLClass source) {

        
        
    }



}