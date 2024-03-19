import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Represents a node in an AVL tree.
 */
class AVLNode {
    String data;
    AVLNode left;
    AVLNode right;
    int height;

    /**
     * Constructs an AVLNode with the given data.
     *
     * @param data The data to be stored in the node.
     */
    public AVLNode(String data) {
        this.data = data;
        left = right = null;
        height = 1;
    }
}

/**
 * Represents an AVL tree.
 */
class AVLTree {
    AVLNode root;
    private int searchOpCount = 0;
    private int insertOpCount = 0;

    /**
     * Constructs an empty AVL tree.
     */
    public AVLTree() {
        root = null;
    }

    /**
     * Inserts a new data into the AVL tree.
     *
     * @param data The data to be inserted.
     */
    public void insert(String data) {
        root = insertRec(root, data);
    }

    private AVLNode insertRec(AVLNode node, String data) {
        if (node == null) {
            insertOpCount++; // Increment insert operation count
            return new AVLNode(data);
        }

        if (data.compareTo(node.data) < 0) {
            node.left = insertRec(node.left, data);
        } else if (data.compareTo(node.data) > 0) {
            node.right = insertRec(node.right, data);
        } else {
            // Duplicate data not allowed
            return node;
        }

        // Update height of this ancestor node
        node.height = 1 + Math.max(height(node.left), height(node.right));

        // Get the balance factor of this ancestor node to check whether it became
        // unbalanced
        int balance = getBalance(node);

        // If this node becomes unbalanced, then there are 4 cases

        // Left Left Case
        if (balance > 1 && data.compareTo(node.left.data) < 0) {
            return rightRotate(node);
        }

        // Right Right Case
        if (balance < -1 && data.compareTo(node.right.data) > 0) {
            return leftRotate(node);
        }

        // Left Right Case
        if (balance > 1 && data.compareTo(node.left.data) > 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right Left Case
        if (balance < -1 && data.compareTo(node.right.data) < 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    /**
     * Searches for a term in the AVL tree.
     *
     * @param term The term to be searched.
     * @return true if the term is found, false otherwise.
     */
    public boolean search(String term) {
        return searchRec(root, term);
    }

    private boolean searchRec(AVLNode node, String term) {
        searchOpCount++;
        if (node == null) {
            return false;
        }

        String[] parts = node.data.split("\t");
        String termPart = parts[0]; // Extract the term part from the data

        if (termPart.equals(term) || termPart.startsWith(term + " ")) {
            // Term found
            return true;
        } else if (term.compareTo(termPart) < 0) {
            // Search left subtree
            return searchRec(node.left, term);
        } else {
            // Search right subtree
            return searchRec(node.right, term);
        }
    }

    // Utility functions

    /**
     * Calculates the height of a node.
     *
     * @param node The node to calculate the height for.
     * @return The height of the node.
     */
    private int height(AVLNode node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    /**
     * Calculates the balance factor of a node.
     *
     * @param node The node to calculate the balance factor for.
     * @return The balance factor of the node.
     */
    private int getBalance(AVLNode node) {
        if (node == null) {
            return 0;
        }
        return height(node.left) - height(node.right);
    }

    /**
     * Performs a right rotation on a node.
     *
     * @param y The node to perform the rotation on.
     * @return The new root node after rotation.
     */
    private AVLNode rightRotate(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;

        // Perform rotation
        x.right = y;
        y.left = T2;

        // Update heights
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        // Return new root
        return x;
    }

    /**
     * Performs a left rotation on a node.
     *
     * @param x The node to perform the rotation on.
     * @return The new root node after rotation.
     */
    private AVLNode leftRotate(AVLNode x) {
        AVLNode y = x.right;
        AVLNode T2 = y.left;

        // Perform rotation
        y.left = x;
        x.right = T2;

        // Update heights
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        // Return new root
        return y;
    }

    // Getters for operation counts
    public int getSearchOpCount() {
        return searchOpCount;
    }

    public int getInsertOpCount() {
        return insertOpCount;
    }
}

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