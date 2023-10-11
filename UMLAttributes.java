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

    public void setName(String newName)
    {
        this.name = newName;
    }

    public static void addAttribute(Class c, String name)
    {
        UMLAttributes attr = new UMLAttributes(name);
        c.getAttributes().add(attr);
        System.out.println("Attribute " + name + " added to class " + c.getName() + " successfully.");
    }

    // Prints out the name of the Attribute
    @Override
    public String toString()
    {
        return this.name;
    }

}