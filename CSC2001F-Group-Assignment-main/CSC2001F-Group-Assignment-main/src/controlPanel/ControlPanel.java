package controlPanel;
import java.util.ArrayList;
import java.util.Scanner;

import actions.Action;
import actions.Quit;

/**
 * Represents a control panel for managing actions in a program.
 */
public class ControlPanel {
    private ArrayList<Action> actions = new ArrayList<Action>();
    private boolean isRunning = true;

    /**
     * Constructs a ControlPanel with the given actions.
     * 
     * @param quitAction The QuitAction to add to the control panel.
     * @param actions    Additional actions to add to the control panel.
     */
    public ControlPanel(Quit quitAction, Action... actions) {
        this.addActions(actions);
        this.addActions(quitAction);
    }

    /**
     * Adds one or more actions to the control panel.
     * 
     * @param actions The actions to add.
     */
    public void addActions(Action... actions) {
        for (Action action : actions) {
            this.actions.add(action);
        }
    }

    /**
     * Generates the menu of actions available in the control panel.
     * 
     * @return The menu string.
     */
    public String getMenu() {
        String menu = "Choose an action from the menu:\n";
        for (int i = 0; i < this.actions.size(); i++) {
            menu += String.format("%d. %s\n", i + 1, this.actions.get(i).getDescription());
        }
        menu = menu.strip();
        return menu;
    }

    /**
     * Prompts the user to select an action and returns the selected action code.
     * 
     * @return The selected action code.
     */
    public int selectAction() {
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your choice: ");
        String rawChoice = scanner.nextLine();
        try {
            int choice = Integer.parseInt(rawChoice);
            if (choice >= 1 && choice <= this.actions.size()) {
                return choice;
            } else {
                return -1;
            }
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Executes the action corresponding to the given action code.
     * 
     * @param actionCode The action code.
     */
    public void runAction(int actionCode) {
        if (!this.isActionCode(actionCode)) {
            return;
        }
        if (actionCode == this.getQuitActionCode()) {
            this.isRunning = false;
        }
        // -1 because actionCode is 1-indexed while
        // actions is 0-indexed
        this.actions.get(actionCode - 1).run();
    }

    /**
     * Checks if the control panel is running.
     * 
     * @return true if the control panel is running, otherwise false.
     */
    public boolean getIsRunning() {
        return this.isRunning;
    }

    /**
     * Retrieves the action code corresponding to the quit action.
     * 
     * @return The action code of the quit action.
     */
    public int getQuitActionCode() {
        return this.actions.size();
    }

    /**
     * Checks if the given action code is valid.
     * 
     * @param actionCode The action code to check.
     * @return true if the action code is valid, otherwise false.
     */
    private boolean isActionCode(int actionCode) {
        return actionCode >= 1 && actionCode <= this.actions.size();
    }

}