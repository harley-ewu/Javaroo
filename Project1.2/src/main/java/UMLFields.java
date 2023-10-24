public class UMLFields
{
    private String name;
    private String type;

    public UMLFields(String name, String type)
    {
        this.name = name;
        this.type = type;
    }

    //Name Getter
    public String getName()
    {
        return this.name;
    }

    //Name Setter
    public void setName(String name)
    {
        this.name = name;
    }

    //Type Getter
    public String getType()
    {
        return this.type;
    }

    //Type Setter
    public void setType(String type)
    {
        this.type = type;
    }

    public String toString()
    {
        return name + " : " + type;
    }

}