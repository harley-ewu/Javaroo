package javaroo.cmd;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UMLMethodsTest {

    private UMLMethods umlMethodsUnderTest;

    @BeforeEach
    void setUp() {
        umlMethodsUnderTest = new UMLMethods("name", "returnType", new ArrayList<>(List.of("value")));
    }

    @Test
    void testSetName() {
        // Setup
        // Run the test
        umlMethodsUnderTest.setName("name");

        // Verify the results
    }

    @Test
    void testSetReturnType() {
        // Setup
        // Run the test
        umlMethodsUnderTest.setReturnType("returnType");

        // Verify the results
    }

    @Test
    void testAddParameters() {
        // Setup
        // Run the test
        umlMethodsUnderTest.addParameters("parameter");

        // Verify the results
    }

    @Test
    void testRemoveParameters() {
        // Setup
        // Run the test
        umlMethodsUnderTest.removeParameters("parameter");

        // Verify the results
    }
}
