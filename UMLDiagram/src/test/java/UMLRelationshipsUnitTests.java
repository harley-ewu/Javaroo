import javaroo.cmd.UMLClass;
import javaroo.cmd.UMLDiagram;
//import org.junit.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UMRelationshipsUnitTests {

    private UMLDiagram dia;
    private UMLClass test;
    private UMLRelationships rel;

    @BeforeEach
    void setUp()
    {
        dia = new UMLDiagram();
        dia.addClass("Test");
        test = dia.classExists("Test");
        rel = new UMLRelationships(test, test, "test");
    }


}
