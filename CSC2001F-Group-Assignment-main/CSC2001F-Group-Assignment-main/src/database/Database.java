package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a database manager for storing student information.
 */
public class Database {
    private Connection connection;

    /**
     * Constructs a new Database instance and establishes a connection to the SQLite database.
     * @throws SQLException If a database access error occurs.
     */
    public Database() throws SQLException {
        // Establish connection to the SQLite database
        connection = DriverManager.getConnection("jdbc:sqlite:students.db"); // store database in current dir

        // Create the students table if it doesn't exist
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS students (" +
                    "student_number TEXT PRIMARY KEY, " +
                    "name TEXT, " +
                    "date_of_application TEXT, " +
                    "high_school_math_mark INTEGER, " +
                    "approval_status TEXT)");
        }
    }

    /**
     * Populates the students table with mock data if it's empty.
     * @throws SQLException If a database access error occurs.
     */
    public void populateTable() throws SQLException {
        // Check if the students table is empty
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS count FROM students");
            resultSet.next();
            int rowCount = resultSet.getInt("count");
            if (rowCount == 0) {
                // If the table is empty, populate it with mock data
                String[] studentNumbers = { "KHNMUH063", "VTTANS001", "KSHKAV001", "SCHCHR077", "HLDZUH001" };
                String[] names = { "Muhaimin Khan", "Anson Vattakunnel", "Kavya Kaushik", "Chris Scheepers",
                        "Zuhayr Halday" };
                String[] datesOfApplication = { "2022-01-01", "2022-01-02", "2022-01-03", "2022-01-04", "2022-01-05" };
                int[] mathMarks = { 85, 75, 80, 90, 78 };
                String[] approvalStatuses = { "Approved", "Not Approved", "Not Processed", "Not Processed", "Not Processed" };

                try (PreparedStatement insertStatement = connection.prepareStatement(
                        "INSERT INTO students (student_number, name, date_of_application, high_school_math_mark, approval_status) "
                                +
                                "VALUES (?, ?, ?, ?, ?)")) {
                    for (int i = 0; i < studentNumbers.length; i++) {
                        insertStatement.setString(1, studentNumbers[i]);
                        insertStatement.setString(2, names[i]);
                        insertStatement.setString(3, datesOfApplication[i]);
                        insertStatement.setInt(4, mathMarks[i]);
                        insertStatement.setString(5, approvalStatuses[i]);
                        insertStatement.executeUpdate();
                    }
                }
            }
        }
    }

    /**
     * Retrieves the details of a student with the specified student number.
     * @param studentNumber The student number to search for.
     * @return An array containing the student details if found, or null otherwise.
     * @throws SQLException If a database access error occurs.
     */
    public String[] readStudentDetails(String studentNumber) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM students WHERE student_number = ?")) {
            statement.setString(1, studentNumber);
            ResultSet resultSet = statement.executeQuery();
            String[] details = new String[5];
            if (resultSet.next()) {
                details[0] = resultSet.getString("student_number");
                details[1] = resultSet.getString("name");
                details[2] = resultSet.getString("date_of_application");
                details[3] = Integer.toString(resultSet.getInt("high_school_math_mark"));
                details[4] = resultSet.getString("approval_status");
                return details;
            }
            return null;
        }
    }

    /**
     * Retrieves all student numbers from the database.
     * @return An array containing all student numbers.
     * @throws SQLException If a database access error occurs.
     */
    public String[] readAllStudentNumbers() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT student_number FROM students");
            List<String> studentNumbers = new ArrayList<>();
            while (resultSet.next()) {
                studentNumbers.add(resultSet.getString("student_number"));
            }
            return studentNumbers.toArray(new String[0]);
        }
    }

    /**
     * Updates the approval status of a student with the specified student number.
     * @param studentNumber The student number of the student whose approval status is to be updated.
     * @param newStatus The new approval status to set.
     * @throws SQLException If a database access error occurs.
     */
    public void updateApprovalStatus(String studentNumber, String newStatus) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE students SET approval_status = ? WHERE student_number = ?")) {
            statement.setString(1, newStatus);
            statement.setString(2, studentNumber);
            statement.executeUpdate();
        }
    }
/**
     * Closes the connection to the database.
     * @throws SQLException If a database access error occurs.
     */
    public void close() throws SQLException {
        connection.close();
    }
}
