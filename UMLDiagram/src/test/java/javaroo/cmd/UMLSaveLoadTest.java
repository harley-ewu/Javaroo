package javaroo.cmd;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.mockito.MockitoAnnotations.openMocks;

class UMLSaveLoadTest {

    @Mock
    private UMLDiagram mockUmlDiagram;

    private UMLSaveLoad umlSaveLoadUnderTest;

    private AutoCloseable mockitoCloseable;

    @BeforeEach
    void setUp() {
        mockitoCloseable = openMocks(this);
        umlSaveLoadUnderTest = new UMLSaveLoad(mockUmlDiagram);
    }

    @AfterEach
    void tearDown() throws Exception {
        mockitoCloseable.close();
    }

    @Test
    void testSaveData() {
        // Setup
        // Run the test
        umlSaveLoadUnderTest.saveData("saveFilePath");

        // Verify the results
    }

    @Test
    void testLoadData() {
        // Setup
        // Run the test
        umlSaveLoadUnderTest.loadData("loadFilePath");

        // Verify the results
    }
}
