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

    public static void main(String args) {
        int numLines = NumLines();
        String[] arrFile = new String[numLines];
        // Code to get arrFile to contain all the lines from the file
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
        
    }

}
