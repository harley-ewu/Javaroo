package javaroo.cmd;

import java.util.ArrayList;

public class UMLMethods
{
    private String name;
    private String returnType;
    private ArrayList<String> parameters;

    //constructor that takes name and return type and the the parameters arraylist
    public UMLMethods(String name, String returnType, ArrayList<String> parameters)
    {
        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;
    }

    //Getters and Setters
    public String getName()
    {
        return name;
    }

    public String getReturnType()
    {
        return returnType;
    }

    public ArrayList<String> getParameters()
    {
        return parameters;
    }

    public void setName(String name)
    {
        //check if null or empty and prints out a message then returns
        if(name == null || name.isEmpty())
        {
            System.out.println("Name cannot be null or empty");
            return;
        }
        this.name = name;
    }

    public void setReturnType(String returnType)
    {
        //check if null or empty and prints out a message then returns
        if(returnType == null || returnType.isEmpty())
        {
            System.out.println("Return type cannot be null or empty");
            return;
        }
        this.returnType = returnType;
    }

    //addParameters method that takes a string and adds it to the parameters arraylist
    public void addParameters(String parameter)
    {
        //check for null or empty and prints out a message then returns
        if(parameter == null || parameter.trim().isEmpty())
        {
            System.out.println("Parameter cannot be null or empty");
            return;
        }    

        //check if the parameter already exists and prints out a message then returns
        if(this.parameters.contains(parameter))
        {
            System.out.println("Parameter already exists");
            return;
        }
        else{
            this.parameters.add(parameter);
            System.out.println("Parameter: " + parameter + " added to method: " + this.name);
        }
    }

    //removeParameters method that takes a string and removes it from the parameters arraylist
    public void removeParameters(String parameter)
    {
        //check for null or empty and prints out a message then returns
        if(parameter == null || parameter.isEmpty())
        {
            System.out.println("Parameter cannot be null or empty");
            return;
        }

        if(!this.parameters.contains(parameter))
        {
            System.out.println("Parameter does not exist");
            return;
        }
        else{
            this.parameters.remove(parameter);
            System.out.println("Parameter: " + parameter + " removed from method: " + this.name);
        }
    }


    //toString that returns the method in the format of the UML diagram
    public String toString()
    {
        String method = returnType + " " + name + "(";
        for(int i = 0; i < parameters.size(); i++)
        {
            method += parameters.get(i);
            if(i != parameters.size() - 1)
            {
                method += ", ";
            }
        }
        method += ")";
        return method;
    }
} 