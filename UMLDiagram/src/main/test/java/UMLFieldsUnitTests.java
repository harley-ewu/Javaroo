import javaroo.cmd.UMLClass;
import javaroo.cmd.UMLDiagram;
//import org.junit.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UMLFieldsUnitTests
{
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
    void testAddFieldName()
    {
        test.addField("test", "int", "public");
        assertEquals(test.getFields().get(0).getName(), "test");
    }

    @Test
    void testAddFieldType()
    {
        test.addField("test", "int", "public");
        assertEquals(test.getFields().get(0).getType(), "int");
    }

    @Test
    void testAddFieldVisibility()
    {
        test.addField("test", "int", "public");
        assertEquals(test.getFields().get(0).getVisibility(), "public");
    }

    @Test
    void testAddFieldToStringPublic()
    {
        test.addField("test", "int", "public");
        assertEquals(test.getFields().get(0).toString(), "+test : int");
    }

    @Test
    void testAddFieldToStringPrivate()
    {
        test.addField("test", "int", "private");
        assertEquals(test.getFields().get(0).toString(), "-test : int");
    }

    @Test
    void testRemoveField()
    {
        test.addField("test", "int", "public");
        test.removeField("test");
        assertEquals(test.getFields().size(), 0);
    }

    @Test
    void testRemoveFieldNotExists()
    {
        test.addField("test", "int", "public");
        test.removeField("test2");
        assertEquals(test.getFields().size(), 1);
    }

    @Test
    void testRenameField()
    {
        test.addField("test", "int", "public");
        test.renameField("test", "test2");
        assertEquals(test.getFields().get(0).getName(), "test2");
    }

    @Test
    void testRenameFieldNotExists()
    {
        test.addField("test", "int", "public");
        test.renameField("test2", "test3");
        assertEquals(test.getFields().get(0).getName(), "test");
    }

    @Test
    void testSameRenameField()
    {
        test.addField("test", "int", "public");
        test.renameField("test", "test");
        assertEquals(test.getFields().get(0).getName(), "test");
    }

    @Test
    void testToStringInvalidVisibility()
    {
        test.addField("test", "int", "invalid");
        assertEquals(test.getFields().size(), 0);
    }

    @Test
    void testAddFieldEmpty()
    {
        test.addField("", "", "");
        assertEquals(test.getFields().size(), 0);
    }
}