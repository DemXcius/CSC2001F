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

        // Get the balance factor of this ancestor node to check whether it became unbalanced
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
        if (node == null) {
            return false;
        }

        String[] parts = node.data.split("\t");
        String termPart = parts[0]; // Extract the term part from the data

        if (termPart.equals(term) || termPart.startsWith(term + " ")) {
            System.out.println(node.data); // Print the entire line
            return true;
        }

        if (term.compareTo(termPart) < 0) {
            return searchRec(node.left, term);
        }

        return searchRec(node.right, term);
    }

    /**
     * Prints the terms in the AVL tree in inorder traversal.
     */
    public void inOrder(String term) {
        boolean found = inOrderRec(root, term);
        if (!found) {
            System.out.println("Term '" + term + "' not found in the knowledge base.");
        }
    }

    private boolean inOrderRec(AVLNode node, String term) {
        boolean found = false;
        if (node != null) {
            found |= inOrderRec(node.left, term);
            String[] parts = node.data.split("\t");
            String termPart = parts[0]; // Extract the term part from the data
            if (termPart.equals(term) || termPart.startsWith(term + " ")) {
                System.out.println(node.data); // Print the entire line
                found = true;
            }
            found |= inOrderRec(node.right, term);
        }
        return found;
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
}

/**
 * Main class to demonstrate the AVL tree application.
 */
public class GenericsKbAVLApp {
    private static AVLTree avl;
    private static boolean knowledgeBaseLoaded = false;

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
                avl.insert(line); // Assuming each line represents a statement in the knowledge base
            }
            System.out.println("\nKnowledge base loaded successfully.\n");
            knowledgeBaseLoaded = true;
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("\nFile not found: " + fileName + "\n");
        }
    }

    /**
     * Main method to run the AVL tree application.
     * 
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        avl = new AVLTree();
        Scanner keyboard = new Scanner(System.in);

        String menuInput = "";

        while (!menuInput.equals("5")) {
            String menu = "Choose an action from the menu:\r\n" + //
                    "1. Load a knowledge base from a file\r\n" + //
                    "2. Add a new statement to the knowledge base\r\n" + //
                    "3. Search for an item in the knowledge base by term\r\n" + //
                    "4. Search for a item in the knowledge base by term and sentence\r\n" + //
                    "5. Quit\r\n\n" + //
                    "Enter your choice: ";
            System.out.print(menu);
            menuInput = keyboard.nextLine().trim();

            if (menuInput.equals("1")) {
                System.out.print("Enter file name: ");
                String dataInput = keyboard.nextLine();
                // Load knowledge base from file (if needed)
                loadKnowledgeBase(dataInput);

            } else if (menuInput.equals("2")) {
                if (!knowledgeBaseLoaded) {
                    System.out.println("\nKnowledge base has not been loaded yet.\n");
                    continue; // Skip adding the new element
                }
                System.out.print("Enter the term: ");
                String term = keyboard.nextLine();
                System.out.print("Enter the statement: ");
                String statement = keyboard.nextLine();
                System.out.print("Enter the confidence score: ");
                String confidence = keyboard.nextLine();
                String newElement = term + "\t" + statement + "\t" + confidence;
                // Add new statement to the AVL tree
                avl.insert(newElement);
                System.out.println("\nStatement for term " + term + " has been updated.\n");

            } else if (menuInput.equals("3")) {
                if (!knowledgeBaseLoaded) {
                    System.out.println("\nKnowledge base has not been loaded yet.\n");
                    continue; // Skip searching
                }
                System.out.print("Enter the term to search: ");
                String searchTerm = keyboard.nextLine();
                System.out.println("");
                // Search for item in the knowledge base by term
                avl.inOrder(searchTerm); // Example: Print all statements, replace with search logic
                System.out.println("");

            } else if (menuInput.equals("4")) {
                if (!knowledgeBaseLoaded) {
                    System.out.println("\nKnowledge base has not been loaded yet.\n");
                    continue; // Skip searching
                }
                System.out.print("Enter the term: ");
                String term = keyboard.nextLine();
                System.out.print("Enter the statement to search for: ");
                String searchStatement = keyboard.nextLine();
                // Search for item in the knowledge base by term and sentence
                // avl.searchByTermAndSentence(term, searchStatement); // Example: Print all statements, replace with
                                                                    // search logic
            }
        }

        keyboard.close();
    }
}