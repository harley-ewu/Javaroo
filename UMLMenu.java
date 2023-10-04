import java.util.*;
public class UMLMenu
{
    //instance of diagram, which holds UMLDiagram data
    private UMLDiagram diagram;
    //constructor to initialize diagram
    public UMLMenu()
    {
        this.diagram = new UMLDiagram();

    }

    public void displayMenu()
    {
        //initialize scanner for user input
        Scanner scanner = new Scanner(System.in);
        //loop exit condition
        boolean exit = false;

     //display menu - use while loop

        //use switch statements
    }
    //close scanner


    public static void main(String[] args)
    {
        //create instance of UMLMenu
        UMLMenu menu = new UMLMenu();
        //call displayMenu method
        menu.displayMenu();
    }
}//end class
