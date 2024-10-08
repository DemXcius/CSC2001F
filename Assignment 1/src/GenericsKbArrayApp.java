import java.util.Arrays;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * A program to manage a knowledge base using an array.
 */
public class GenericsKbArrayApp {
    private static String[] arrFile; // Array to hold data from file

    /**
     * Counts the number of lines in a file.
     * 
     * @param fileName The name of the file to count lines.
     * @return The number of lines in the file.
     */
    public static int NumLines(String fileName) {
        int lines = 0;
        String file = fileName;
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while (bufferedReader.readLine() != null) {
                lines++;
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
        return lines;
    }

    /**
     * Populates an array with lines from a file.
     * 
     * @param fileName The name of the file to read lines from.
     * @return An array containing the lines from the file.
     */
    public static String[] PopulateArray(String fileName) {
        int numLines = NumLines(fileName);
        String[] arrFile = new String[numLines];
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            int index = 0;
            while ((line = bufferedReader.readLine()) != null) {
                arrFile[index++] = line;
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
        return arrFile;
    }

    /**
     * Searches for an item in the knowledge base by term.
     * 
     * @param searchTerm The term to search for.
     */
    public static void SearchByTerm(String searchTerm) {
        if (arrFile == null) {
            System.out.println("Knowledge base has not been loaded yet.");
            return;
        }
        boolean found = false;
        for (String entry : arrFile) {
            String[] parts = entry.split("\t");
            if (parts.length >= 3 && (parts[0].equals(searchTerm) || 
            parts[0].startsWith(searchTerm + " ") || 
            parts[0].endsWith(" " + searchTerm) || 
            parts[0].contains(" " + searchTerm + " "))) {
                String statement = parts[1];
                String confidence = parts[2];
                System.out.println("\nStatement found for " + parts[0] + ": " + statement + " (Confidence score: " + confidence + ")\n");
                found = true;
            }
        }
        if (!found) {
            System.out.println("Term '" + searchTerm + "' not found in the knowledge base.");
        }
    }

    /**
     * Searches for an item in the knowledge base by term and statement.
     * 
     * @param searchTerm      The term to search for.
     * @param searchStatement The statement to search for.
     */

    public static void SearchByTermAndStatement(String searchTerm, String searchStatement) {
        if (arrFile == null) {
            System.out.println("Knowledge base has not been loaded yet.");
            return;
        }
        boolean found = false;
        for (String entry : arrFile) {
            String[] parts = entry.split("\t");
            if (parts.length >= 3 && parts[0].equals(searchTerm) && parts[1].equals(searchStatement)) {
                String confidence = parts[2];
                System.out.println("\nThe statement was found and has a confidence score of  " + confidence + ".\n");
                found = true;
                break; // No need to continue searching if found
            }
        }
        if (!found) {
            System.out.println("Term and statement: '" +searchTerm +"' and '" + searchStatement+ "' not found in the knowledge base.");
        }
    }

    /**
     * Main method to run the program.
     * 
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {

        String menuInput = "";
        String dataInput = "";
        Scanner keyboard = new Scanner(System.in);

        String menu = "Choose an action from the menu:\r\n" + //
                "1. Load a knowledge base from a file\r\n" + //
                "2. Add a new statement to the knowledge base\r\n" + //
                "3. Search for an item in the knowledge base by term\r\n" + //
                "4. Search for a item in the knowledge base by term and sentence\r\n" + //
                "5. Quit\r\n\n" + //
                "Enter your choice: ";

        while (!menuInput.equals("5")) {
            System.out.print(menu);
            menuInput = keyboard.nextLine().trim();

            if (menuInput.equals("1")) {
                System.out.print("Enter file name: ");
                dataInput = keyboard.nextLine();
                arrFile = PopulateArray(dataInput);
                System.out.println("\n Knowledge base loaded successfully.\n");

            } else if (menuInput.equals("2")) {
                if (arrFile == null) {
                    System.out.println("\nKnowledge base has not been loaded yet.\n");
                    System.out.print(menu);
                    continue; // Skip adding the new element
                }
                System.out.print("Enter the term: ");
                dataInput = keyboard.nextLine();
                String term = dataInput;
                System.out.print("Enter the statement: ");
                dataInput = keyboard.nextLine();
                String statement = dataInput;
                System.out.print("Enter the confidence score: ");
                dataInput = keyboard.nextLine();
                String confidence = dataInput;
                String newElement = term + "\t" + statement + "\t" + confidence;
                // Append the new element to the array
                arrFile = Arrays.copyOf(arrFile, arrFile.length + 1);
                arrFile[arrFile.length - 1] = newElement;
                System.out.println("\nStatement for term " + term + " has been updated.\n");

            } else if (menuInput.equals("3")) {
                if (arrFile == null) {
                    System.out.println("\nKnowledge base has not been loaded yet.\n");
                    System.out.print(menu);
                    continue; // Skip adding the new element
                }
                System.out.print("Enter the term to search: ");
                dataInput = keyboard.nextLine();
                SearchByTerm(dataInput);

            } else if (menuInput.equals("4")) {
                if (arrFile == null) {
                    System.out.println("\nKnowledge base has not been loaded yet.\n");
                    System.out.print(menu);
                    continue; // Skip adding the new element
                }
                System.out.print("Enter the term: ");
                dataInput = keyboard.nextLine();
                String term = dataInput;
                System.out.print("Enter the statement to search for: ");
                dataInput = keyboard.nextLine();
                String statement = dataInput;
                SearchByTermAndStatement(term, statement);
            }
        }
        keyboard.close();
    }
}
