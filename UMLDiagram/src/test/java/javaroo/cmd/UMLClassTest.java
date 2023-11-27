package javaroo.cmd;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UMLClassTest {

    private UMLClass umlClassUnderTest;

    @BeforeEach
    void setUp() {
        umlClassUnderTest = new UMLClass("name", 0.0, 0.0);
    }


    @Test
    void testAddClassWithCoordinates() {
        // Setup
        // Run the test
        UMLClass.addClassWithCoordinates("name", 0.0, 0.0);

        // Verify the results
    }

    @Test
    void testFieldExists() {
        // Setup
        // Run the test
        final UMLFields result = umlClassUnderTest.fieldExists("name");

        // Verify the results
    }

    @Test
    void testAddField() {
        // Setup
        // Run the test
        //umlClassUnderTest.addField("name", "type", "visibility");

        // Verify the results
    }

    @Test
    void testRemoveField() {
        // Setup
        // Run the test
        umlClassUnderTest.removeField("name");

        // Verify the results
    }

    @Test
    void testRenameField() {
        // Setup
        // Run the test
        umlClassUnderTest.renameField("oldName", "name");

        // Verify the results
    }

    @Test
    void testMethodExists() {
        // Setup
        // Run the test
        final UMLMethods result = umlClassUnderTest.methodExists("name", new ArrayList<>(List.of("value")));

        // Verify the results
    }

    @Test
    void testAddMethod() {
        // Setup
        // Run the test
        umlClassUnderTest.addMethod("name", "returnType", new ArrayList<>(List.of("value")));

        // Verify the results
    }

    @Test
    void testRemoveMethod() {
        // Setup
        // Run the test
        umlClassUnderTest.removeMethod(0);

        // Verify the results
    }

    @Test
    void testRenameMethod() {
        // Setup
        // Run the test
        umlClassUnderTest.renameMethod(0, "name");

        // Verify the results
    }

    @Test
    void testListMethods() {
        // Setup
        // Run the test
        umlClassUnderTest.listMethods();

        // Verify the results
    }


    @Test
    void testSetPosition() {
        // Setup
        // Run the test
        umlClassUnderTest.setPosition(0.0, 0.0);

        // Verify the results
    }
}
