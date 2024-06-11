package actions;

/**
 * Represents an action to quit the program.
 */
public class Quit implements Action {

    /**
     * Gets the description of the quit action.
     * 
     * @return The description of the quit action.
     */
    public String getDescription() {
        return "Quit";
    }

    /**
     * Executes the quit action.
     * 
     * This method is intentionally doesn't do anything because the control panel
     * recognizes this as a special action and handles quitting in the ControlPanel
     * class.
     */
    public void run() {
        System.out.println("Quitting...");
    }

}
