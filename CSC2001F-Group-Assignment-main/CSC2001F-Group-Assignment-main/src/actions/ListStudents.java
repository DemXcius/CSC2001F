package actions;

import database.Database;

import java.sql.SQLException;

/**
 * Represents an action to print all student numbers from the database.
 */
public class ListStudents implements Action {

	Database database;

	/**
	 * Constructs a new ListStudents action with the specified database.
	 * 
	 * @param database The database to retrieve student numbers from.
	 */
	public ListStudents(Database database) {
		this.database = database;
	}

	/**
	 * Gets the description of the action.
	 * 
	 * @return The description of the action.
	 */
	public String getDescription() {
		return "Print All Student Numbers";
	}

	/**
	 * Runs the action to print all student numbers.
	 */
	public void run() {

		try {
			String[] studentNumbers;
			studentNumbers = database.readAllStudentNumbers();
			if (studentNumbers.length == 0) {
				System.out.println("No applications have been submitted.");
			} else {
				for (String studentNumber : studentNumbers) {
					System.out.println(studentNumber);
				}
			}
		} catch (SQLException e) {
			System.out.println("Something went wrong...");
		}
	}
}
