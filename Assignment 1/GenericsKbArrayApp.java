import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GenericsKbArrayApp {
    public static String fileName = "GenericsKB-additional.txt";

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

    public static void main(String args) {
        
        String menuInput = "";
        String dataInput = "";
        Scanner keyboard = new Scanner(System.in);
        
        String menu ="Choose an action from the menu:\r\n" + //
        "1. Load a knowledge base from a file\r\n" + //
        "2. Add a new statement to the knowledge base\r\n" + //
        "3. Search for an item in the knowledge base by term\r\n" + //
        "4. Search for a item in the knowledge base by term and sentence\r\n" + //
        "5. Quit\r\n" + //
        "Enter your choice";
        System.out.println(menu);
        menuInput = keyboard.nextLine();

        while (!menuInput.equals("5")) {
            if (menuInput == "1"){
                System.out.println("Enter file name: ");
                dataInput = keyboard.nextLine();
                String[] arrFile = PopulateArray(dataInput);
                System.out.println("\n Knowledge base loaded successfully.\n"); 
                System.out.println(menu);    

            }else if (menuInput == "2"){
                System.out.println("Enter the term: ");
                dataInput = keyboard.nextLine();
                String term = dataInput;
                System.out.println("Enter the statement: ");
                dataInput = keyboard.nextLine();
                String statement = dataInput;
                System.out.println("Enter the confidence score: ");
                dataInput = keyboard.nextLine();
                String confidence = dataInput;
            }
        }

    }

}
