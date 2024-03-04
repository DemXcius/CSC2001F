import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GenericsKbArrayApp {
    private static String[] arrFile;

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

    public static String[] PopulateArray(String fileName) {
        // Code to get arrFile to contain all the lines from the file
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

    public static void SearchByTerm(String searchTerm) {
        if (arrFile == null) {
            System.out.println("Knowledge base has not been loaded yet.");
            return;
        }
        boolean found = false;
        for (String entry : arrFile) {
            if (entry.contains(searchTerm)) {
                String[] parts = entry.split("\t");
                if (parts.length >= 3) {
                    String statement = parts[1];
                    String confidence = parts[2];
                    System.out.println("Statement found: " + statement + " (Confidence score: " + confidence + ")");
                    found = true;
                }
            }
        }
        if (!found) {
            System.out.println("Term not found in the knowledge base.");
        }
    }

    public static void SearchByTermAndStatement(String searchTerm, String searchStatement) {
        if (arrFile == null) {
            System.out.println("Knowledge base has not been loaded yet.");
            return;
        }
        boolean found = false;
        for (String entry : arrFile) {
            if (entry.contains(searchTerm) && entry.contains(searchStatement)) {
                String[] parts = entry.split("\t");
                if (parts.length >= 3) {
                    String statement = parts[1];
                    String confidence = parts[2];
                    System.out.println("The statement was found and has a confidence score of  " + confidence + ".");
                    found = true;
                }
            }
        }
        if (!found) {
            System.out.println("Term and statement not found in the knowledge base.");
        }
    }

    /*
     * public static void ReplaceElement(String searchTerm, String newElement) {
     * for (int i = 0; i < arrFile.length; i++) {
     * String line = arrFile[i];
     * if (line.contains(searchTerm)) {
     * int tabIndex = line.indexOf('\t');
     * if (tabIndex != -1) {
     * // Replace the part of the line until the tab with the new element
     * arrFile[i] = newElement + line.substring(tabIndex);
     * }
     * }
     * }
     * }
     */
    public static void main(String args) {

        String menuInput = "";
        String dataInput = "";
        Scanner keyboard = new Scanner(System.in);

        String menu = "Choose an action from the menu:\r\n" + //
                "1. Load a knowledge base from a file\r\n" + //
                "2. Add a new statement to the knowledge base\r\n" + //
                "3. Search for an item in the knowledge base by term\r\n" + //
                "4. Search for a item in the knowledge base by term and sentence\r\n" + //
                "5. Quit\r\n" + //
                "Enter your choice";
        System.out.println(menu);
        menuInput = keyboard.nextLine();

        while (!menuInput.equals("5")) {
            if (menuInput == "1") {
                System.out.println("Enter file name: ");
                dataInput = keyboard.nextLine();
                String[] arrFile = PopulateArray(dataInput);
                System.out.println("\n Knowledge base loaded successfully.\n");
                System.out.println(menu);

            } else if (menuInput == "2") {
                System.out.println("Enter the term: ");
                dataInput = keyboard.nextLine();
                String term = dataInput;
                System.out.println("Enter the statement: ");
                dataInput = keyboard.nextLine();
                String statement = dataInput;
                System.out.println("Enter the confidence score: ");
                dataInput = keyboard.nextLine();
                String confidence = dataInput;
                String newElement = term + "\t" + statement + "\t" + confidence;
                // Append the new element to the array
                String[] newArrFile = new String[arrFile.length + 1];
                System.arraycopy(arrFile, 0, newArrFile, 0, arrFile.length);
                newArrFile[arrFile.length] = newElement;
                arrFile = newArrFile;
                System.out.println("\nStatement for term " + term + " has been updated.\n");
                System.out.println(menu);
                //Turn Option 2 into a function

            } else if (menuInput == "3") {
                System.out.println("Enter the term to search: ");
                dataInput = keyboard.nextLine();
                SearchByTerm(dataInput);

            } else if (menuInput == "4"){
                System.out.println("Enter the term: ");
                dataInput = keyboard.nextLine();
                String term = dataInput;
                System.out.println("Enter the statement to search for: ");
                dataInput = keyboard.nextLine();
                String statement = dataInput;
                SearchByTermAndStatement(term, statement);

            }
        }

    }

}
