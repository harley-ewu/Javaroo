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

    // Prints out the name of the Attribute
    @Override
    public String toString()
    {
        return this.name;
    }

}