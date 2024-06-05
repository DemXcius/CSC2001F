package actions;

/**
 * Represents an action that can be performed.
 */
public interface Action {
    
    /**
     * Gets a description of the action. This will be what the user reads on the menu.
     * 
     * @return The description of the action.
     */
    String getDescription();
    /**
     * Executes the action.
     */
    default void run() {

    };
}
