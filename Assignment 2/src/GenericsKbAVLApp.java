import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Represents a node in an AVL tree.
 */

/**
 * Main class to demonstrate the AVL tree application.
 */
public class GenericsKbAVLApp {
    private static AVLTree avl;

    /**
     * Main method to run the AVL tree application.
     * 
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        avl = new AVLTree();

        // Prompt user for knowledge base filename
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the filename of the knowledge base: ");
        String knowledgeBaseFile = scanner.nextLine();

        // Prompt user for query filename
        System.out.print("Enter the filename of the query file: ");
        String queryFile = scanner.nextLine();

        scanner.close();

        // Load knowledge base from file
        loadKnowledgeBase(knowledgeBaseFile);

        // Perform searches based on queries
        searchQueries(queryFile);

        // Print operation counts
        System.out.println("Search Operations: " + avl.getSearchOpCount());
        System.out.println("Insert Operations: " + avl.getInsertOpCount());
    }

    /**
     * Loads the knowledge base from the specified file.
     * 
     * @param fileName The name of the file containing the knowledge base.
     */
    private static void loadKnowledgeBase(String fileName) {
        try {
            Scanner fileScanner = new Scanner(new File(fileName));
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                // Assuming each line represents a statement in the knowledge base
                String[] parts = line.split("\t");
                if (parts.length == 3) { // Ensure there are three parts (term, sentence, confidence score)
                    String term = parts[0];
                    avl.insert(line);
                }
            }
            System.out.println("\nKnowledge base loaded successfully.\n");
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("\nFile not found: " + fileName + "\n");
        }
    }

    /**
     * Perform searches based on queries from the specified file.
     * 
     * @param fileName The name of the file containing the queries.
     */
    private static void searchQueries(String fileName) {
        try {
            Scanner fileScanner = new Scanner(new File(fileName));
            while (fileScanner.hasNextLine()) {
                String query = fileScanner.nextLine().trim();
                System.out.println("Query: " + query);
                boolean found = avl.search(query); // Search for the query term
                if (!found) {
                    System.out.println("Term not found: " + query);
                }
                System.out.println();
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("\nFile not found: " + fileName + "\n");
        }
    }

}