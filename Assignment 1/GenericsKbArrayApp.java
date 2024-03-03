import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GenericsKbArrayApp {
    private static int NumLines() {
        int lines = 0;
        String fileName = "GenericsKB-additional.txt";
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
        
    }
}
