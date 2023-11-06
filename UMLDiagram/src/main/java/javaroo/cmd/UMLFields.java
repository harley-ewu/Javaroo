package javaroo.cmd;

public class UMLFields {

    private String name;
    private String type;
    private String visibility;

    public UMLFields(String name, String type, String visibility) {
        //check for the empty string in paramters or if input contains only spaces
        if(name.trim().isEmpty() || type.trim().isEmpty() || visibility.trim().isEmpty())
        {
            System.out.println("Invalid input");
            return;
        }
        //check for valid visibility
        if(!visibility.equals("public") && !visibility.equals("private"))
        {
            System.out.println("Invalid visibility");
            return;
        }
        this.name = name;
        this.type = type;
        this.visibility = visibility;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        
        if (this.visibility.equals("public")) {
            return "+" + name + " : " + type;
        } else {
            return "-" + name + " : " + type;
        }
    }
}