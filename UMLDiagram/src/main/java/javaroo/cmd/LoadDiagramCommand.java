package javaroo.cmd;

public class LoadDiagramCommand implements Command {
    private UMLSaveLoad saveLoad;
    private String fileName;

    public LoadDiagramCommand(UMLSaveLoad saveLoad, String fileName) {
        this.saveLoad = saveLoad;
        this.fileName = fileName;
    }

    @Override
    public void execute() {
        saveLoad.loadData(fileName);
    }

    @Override
    public void undo() {
        // Undoing loading is not applicable in this case.
    }
}
