package actions;

import java.sql.SQLException;
import java.util.Scanner;
import database.Database;

/**
 * This class represents an action to update the approval status of a student.
 * @author Chris Scheepers
 */

public class StudentApproval implements Action {

    Database database;

    /**
     * Constructs a StudentApproval object with a specified database.
     *
     * @param database The database to be used for updating approval status.
     */
    public StudentApproval(Database database){
        this.database = database;
    }

    /**
     * Provides a description of the action.
     *
     * @return A description of the action.
     */
    public String getDescription() {
        return "Update the approval status of a student.";
    }

    /**
     * Executes the action to update the approval status of a student.
     */
    public void run(){
        // Prompting user to enter student number
        System.out.print("Enter the student number: ");
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        String studentNumber = scanner.nextLine().toUpperCase();

        // Prompting user to enter new status for the student
        System.out.print("Enter new status for student (1 for Approved, 2 for Not Approved, 3 for Not Processed): ");
        String newStatusInt = scanner.nextLine().trim();
        String newStatus;

        // Converting numeric input to status string
        if (newStatusInt == "1"){
            newStatus = "Approved";
        }
        else if (newStatusInt == "2") {
            newStatus = "Not Approved";
        }
        else if (newStatusInt == "3") {
            newStatus = "Not Processed";
        } else {
            System.out.println("You did not choose an available status.");
            return;
        }

        try {
            // Updating the approval status in the database
            database.updateApprovalStatus(studentNumber, newStatus);
            System.out.println("Status for " + studentNumber + " has been updated!");
        } catch (SQLException e) {
            System.out.println("Something went wrong...");
        }
    }
}
