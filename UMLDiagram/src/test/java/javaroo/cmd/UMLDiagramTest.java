package javaroo.cmd;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class UMLDiagramTest {

    private UMLDiagram umlDiagramUnderTest;

    @BeforeEach
    void setUp() {
        umlDiagramUnderTest = new UMLDiagram();
    }

    @Test
    void testGetClasses() {
        // Setup
        // Run the test
        final Map<String, UMLClass> result = UMLDiagram.getClasses();

        // Verify the results
    }

    @Test
    void testSetClasses() {
        // Setup
        final Map<String, UMLClass> classes = Map.ofEntries(Map.entry("value", new UMLClass("newName", 0.0, 0.0)));

        // Run the test
        umlDiagramUnderTest.setClasses(classes);

        // Verify the results
    }

    @Test
    void testGetRelationships() {
        // Setup
        // Run the test
        final List<UMLRelationships> result = umlDiagramUnderTest.getRelationships();

        // Verify the results
    }

    @Test
    void testSetRelationships() {
        // Setup
        final List<UMLRelationships> relationships = List.of(
                new UMLRelationships(new UMLClass("newName", 0.0, 0.0), new UMLClass("newName", 0.0, 0.0),
                        UMLRelationships.RelationshipType.AGGREGATION, 0.0, 0.0, 0.0, 0.0));

        // Run the test
        umlDiagramUnderTest.setRelationships(relationships);

        // Verify the results
    }

    @Test
    void testClassExists() {
        // Setup
        // Run the test
        final UMLClass result = umlDiagramUnderTest.classExists("name");

        // Verify the results
    }

    @Test
    void testRemoveClass() {
        // Setup
        // Run the test
        //umlDiagramUnderTest.removeClass("name");

        // Verify the results
    }

    @Test
    void testRenameClass() {
        // Setup
        // Run the test
        umlDiagramUnderTest.renameClass("oldName", "newName");

        // Verify the results
    }

    @Test
    void testRelationshipExists() {
        // Setup
        // Run the test
        final UMLRelationships result = umlDiagramUnderTest.relationshipExists("id");

        // Verify the results
    }

    @Test
    void testAddRelationship() {
        // Setup
        final UMLClass src = new UMLClass("newName", 0.0, 0.0);
        final UMLClass dest = new UMLClass("newName", 0.0, 0.0);

        // Run the test
        umlDiagramUnderTest.addRelationship(src, dest, UMLRelationships.RelationshipType.AGGREGATION);

        // Verify the results
    }

    @Test
    void testRemoveRelationship() {
        // Setup
        // Run the test
        umlDiagramUnderTest.removeRelationship(0);

        // Verify the results
    }

    @Test
    void testListClasses() {
        // Setup
        // Run the test
        umlDiagramUnderTest.listClasses();

        // Verify the results
    }

    @Test
    void testListClassContents() {
        // Setup
        // Run the test
        umlDiagramUnderTest.listClassContents("className");

        // Verify the results
    }

    @Test
    void testListRelationships() {
        // Setup
        // Run the test
        umlDiagramUnderTest.listRelationships();

        // Verify the results
    }
}
