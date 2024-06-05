package actions;

import java.sql.SQLException;
import java.util.Scanner;

import database.Database;

/**
 * Represents an action to generate a report for a student.
 */
public class GetReport implements Action {

    Database database;
    
    /**
     * Constructs a new GetReport action with the specified database.
     * @param database The database to retrieve student details from.
     */
    public GetReport(Database database) {
        this.database = database;
    }

    /**
     * Gets the description of the action.
     * @return The description of the action.
     */
    public String getDescription() {
        return "Generate report for a student";
    }

    /**
     * Runs the action to generate a report for a student.
     */
    public void run() {
        System.out.print("Enter the student number: ");
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        String studentNumber = scanner.nextLine().toUpperCase().trim();
        try {
            String[] details = database.readStudentDetails(studentNumber);
            if (details == null) {
                System.out.println("No student with that student number found.");
            } else {
                System.out.println("Student Number: " + details[0]);
                System.out.println("Name: " + details[1]);
                System.out.println("Date of application: " + details[2]);
                System.out.println("High school math mark: " + details[3]);
                System.out.println("Approval Status: " + details[4]);
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong...");
        }
    }

}
