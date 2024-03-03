import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GenericsKbArrayApp {
    public static String fileName = "GenericsKB-additional.txt";

    public static int NumLines() {
        int lines = 0;
        try {
            FileReader fileReader = new FileReader(fileName);
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

    public static String[] PopulateArray() {
        // Code to get arrFile to contain all the lines from the file
        int numLines = NumLines();
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
        String[] arrFile = PopulateArray();
        String input = "";
        Scanner keyboard = new Scanner(System.in);
        input = keyboard.nextLine();
        System.out.println("Choose an action from the menu:\r\n" + //
                "1. Load a knowledge base from a file\r\n" + //
                "2. Add a new statement to the knowledge base\r\n" + //
                "3. Search for an item in the knowledge base by term\r\n" + //
                "4. Search for a item in the knowledge base by term and sentence\r\n" + //
                "5. Quit\r\n" + //
                "Enter your choice");

    }

}
