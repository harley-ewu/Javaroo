package javaroo.cmd;

public class SaveDiagramCommand implements Command {
    private UMLSaveLoad saveLoad;
    private String fileName;

    public SaveDiagramCommand(UMLSaveLoad saveLoad, String fileName) {
        this.saveLoad = saveLoad;
        this.fileName = fileName;
    }

    @Override
    public void execute() {
        saveLoad.saveData(fileName);
    }

    @Override
    public void undo() {
        // Undoing saving is not applicable in this case.
    }
}
