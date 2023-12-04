package javaroo.cmd;

public class UMLFields {

    private String name;
    private String type;
    private String visibility;

    public UMLFields(String name, String type, String visibility) {
        if (name.trim().isEmpty() || type.trim().isEmpty() || visibility.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid input");
        }
        if (!visibility.equals("public") && !visibility.equals("private")) {
            throw new IllegalArgumentException("Invalid visibility");
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
        if (this.visibility != null) {
            if (this.visibility.equals("public")) {
                return "+" + name + " : " + type;
            } else {
                return "-" + name + " : " + type;
            }
        } else {
            // Handle the case where visibility is null (e.g., provide a default)
            return "Unknown Visibility" + name + " : " + type;
        }
    }

}