public class UMLFields {

    private String name;
    private String type;
    private String visibility;

    public UMLFields(String name, String type, String visibility) {
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

    public String toString() {
        
        if (this.visibility.equals("public")) {
            return "+" + name + " : " + type;
        } else {
            return "-" + name + " : " + type;
        }
    }
}