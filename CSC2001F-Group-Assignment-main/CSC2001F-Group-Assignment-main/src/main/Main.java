package main;

import java.sql.SQLException;

import actions.GetReport;
import actions.ListStudents;
import actions.Quit;
import actions.StudentApproval;
import controlPanel.ControlPanel;
import database.Database;

/**
 * The main class of the program responsible for starting and running the application.
 */
public class Main {

    /**
     * The entry point of the program.
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {

        try {
            Database database = new Database();
            database.populateTable();
            ControlPanel controlPanel = new ControlPanel(
                    new Quit(),
                    new GetReport(database),
                    new ListStudents(database),
                    new StudentApproval(database));
            int choice = 0;
            do {
                System.out.println(controlPanel.getMenu());
                choice = controlPanel.selectAction();
                System.out.print("\n"); // just for nice formatting
                if (choice == -1) {
                    System.out.println("Enter a number from 1 to " + controlPanel.getQuitActionCode());
                } else {
                    controlPanel.runAction(choice);
                }
                System.out.print("\n"); // just for nice formatting
            } while (controlPanel.getIsRunning());
            database.close();
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Something went wrong");
        }
    }

}
