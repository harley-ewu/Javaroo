public class UMLRelationships {

    final private String dest;
    final private String source;

    public UMLRelationships(final String dest, final String source) {
        this.dest = dest;
        this.source = source;
    }

    public UMLRelationships addRelationship(final String dest, final String source) {

        UMLRelationships newRel = new UMLClass(dest, source);

        return newRel;

    }



}