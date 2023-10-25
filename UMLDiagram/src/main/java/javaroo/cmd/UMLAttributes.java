package javaroo.cmd;

public class UMLAttributes
{
    // Unique identifier
    private String name;

    // Constructor
    public UMLAttributes(String name)
    {
        this.name = name;
    }

    // Getters and Setters
    public String getName()
    {
        return this.name;
    }

    //Call for the user to change the name of the attribute
    public void setName(String newName)
    {
        this.name = newName;
    }

    /* Adds an attribute to a class
        * @param c - the class to add the attribute to
        * @param name - the name of the attribute
    */
    public static void addAttribute(UMLClass c, String name)
    {
        UMLAttributes attr = new UMLAttributes(name);
        c.getAttributes().add(attr);
        System.out.println("Attribute " + name + " added to class " + c.getName() + " successfully.");
        UMLDiagram.setSaved(false);
    }

    /* Removes an attribute from a class
        * @param c - the class to remove the attribute from
        * @param name - the name of the attribute
    */
   public static void removeAttribute(UMLClass c, String name)
   {
        UMLAttributes attr = c.attributesExists(name);
        if(attr == null)
        {
            System.out.println("Attribute " + name + " does not exist in class " + c.getName() + ".");
        }
        else
        {
            c.getAttributes().remove(attr);
            System.out.println("Attribute " + name + " removed from class " + c.getName() + " successfully.");
        }

   }

    // Prints out the name of the Attribute
    @Override
    public String toString()
    {
        return this.name;
    }

}