public class UMLAttributes
{
    private String name;

    public UMLAttributes(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }


    @Override
    public String toString()
    {
        return this.name;
    }

}