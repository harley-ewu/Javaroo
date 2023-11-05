import javaroo.cmd.UMLClass;
import javaroo.cmd.UMLDiagram;
//import org.junit.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UMLMethodsUnitTest {

    private UMLDiagram dia;
    private UMLClass test;

    @BeforeEach
    void setUp()
    {
        dia = new UMLDiagram();
        dia.addClass("Test");
        test = dia.classExists("Test");
    }

    @Test
    void testAddMethodName()
    {
        test.addMethod("test", "int", "public");
        assertEquals(test.getMethods().get(0).getName(), "test");
    }

    @Test
    void testAddMethodType()
    {
        test.addMethod("test", "int", "public");
        assertEquals(test.getMethods().get(0).getType(), "int");
    }

    @Test
    void testAddMethodParameters()
    {
        test.addMethod("test", "int", "public");
        assertEquals(test.getMethods().get(0).getParameters(), "");
    }

    @Test
    void testAddMethodtoStringPublic()
    {
        test.addMethod("test", "int", "public");
        assertEquals(test.getMethods().get(0).toString(), "+test() : int");
    }

    @Test
    void testAddMethodtoStringPrivate()
    {
        test.addMethod("test", "int", "private");
        assertEquals(test.getMethods().get(0).toString(), "-test() : int");
    }

    @Test
    void testRemoveMethod()
    {
        test.addMethod("test", "int", "public");
        test.removeMethod("test");
        assertEquals(test.getMethods().size(), 0);
    }

    @Test  
    void testRenameMethod()
    {
        test.addMethod("test", "int", "public");
        test.renameMethod("test", "test2");
        assertEquals(test.getMethods().get(0).getName(), "test2");
    }

    @Test
    void testRenameMethodNotExists()
    {
        test.addMethod("test", "int", "public");
        test.renameMethod("test2", "test3");
        assertEquals(test.getMethods().get(0).getName(), "test");
    }

    @Test
    void testRenameMethodEmptyName()
    {
        test.addMethod("test", "int", "public");
        test.renameMethod("test", "");
        assertEquals(test.getMethods().get(0).getName(), "test");
    }

    @Test
    void testAddMethodEmptyName()
    {
        test.addMethod("", "int", "public");
        assertEquals(test.getMethods().size(), 0);
    }

}
