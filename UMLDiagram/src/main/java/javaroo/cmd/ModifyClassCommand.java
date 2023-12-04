package javaroo.cmd;

import java.util.ArrayList;

public class ModifyClassCommand implements Command {
    private UMLDiagram umlDiagram;
    private UMLClass modifiedClass;
    private UMLClass originalClass;

    public ModifyClassCommand(UMLDiagram umlDiagram, UMLClass modifiedClass) {
        this.umlDiagram = umlDiagram;

        // Check if modifiedClass is not null before cloning
        if (modifiedClass != null) {
            // Clone the modifiedClass to create the originalClass
            this.modifiedClass = new UMLClass(modifiedClass.getName());
            this.modifiedClass.setFields(new ArrayList<>(modifiedClass.getFields()));
            this.modifiedClass.setMethods(new ArrayList<>(modifiedClass.getMethods()));

            // Create a new UMLClass for the original state
            this.originalClass = new UMLClass(modifiedClass.getName());
            this.originalClass.setFields(new ArrayList<>(modifiedClass.getFields()));
            this.originalClass.setMethods(new ArrayList<>(modifiedClass.getMethods()));
        } else {
            System.out.println("Error: modifiedClass is null");
        }
    }



    @Override
    public void execute() {
        // No need to execute anything in this case since modifications are done in real-time.
    }

    @Override
    public void undo() {
        umlDiagram.updateClass(originalClass);
    }
}
