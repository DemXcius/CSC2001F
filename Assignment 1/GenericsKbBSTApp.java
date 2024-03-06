import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Represents a node in a binary tree.
 */
class TreeNode {
    String data;
    TreeNode left;
    TreeNode right;

    /**
     * Constructs a TreeNode with the given data.
     * 
     * @param data The data to be stored in the node.
     */
    public TreeNode(String data) {
        this.data = data;
        left = right = null;
    }
}

/**
 * Represents a binary search tree.
 */
/**
 * Represents a binary search tree.
 */
class BinarySearchTree {
    TreeNode root;

    /**
     * Constructs an empty binary search tree.
     */
    public BinarySearchTree() {
        root = null;
    }

    /**
     * Inserts a new data into the binary search tree.
     * 
     * @param data The data to be inserted.
     */
    public void insert(String data) {
        root = insertRec(root, data);
    }

    private TreeNode insertRec(TreeNode root, String data) {
        if (root == null) {
            root = new TreeNode(data);
            return root;
        }

        if (data.compareTo(root.data) < 0) {
            root.left = insertRec(root.left, data);
        } else if (data.compareTo(root.data) > 0) {
            root.right = insertRec(root.right, data);
        }

        return root;
    }

    /**
     * Searches for a term in the binary search tree.
     * 
     * @param term The term to be searched.
     * @return true if the term is found, false otherwise.
     */
    public boolean search(String term) {
        return searchRec(root, term);
    }

    private boolean searchRec(TreeNode root, String term) {
        if (root == null) {
            return false;
        }

        if (root.data.startsWith(term)) { // Check if the term matches the start of the data
            System.out.println(root.data); // Print the entire line
            return true;
        }

        if (term.compareTo(root.data) < 0) {
            return searchRec(root.left, term);
        }

        return searchRec(root.right, term);
    }

    /**
     * Prints the terms in the binary search tree in inorder traversal.
     */
    public void inOrder(String term) {
        inOrderRec(root, term);
    }

    private void inOrderRec(TreeNode root, String term) {
        if (root != null) {
            inOrderRec(root.left, term);
            if (root.data.startsWith(term)) {
                System.out.println("\n"+root.data); // Print the entire line
            }
            inOrderRec(root.right, term);
        }
    }
}
/**
 * Main class to demonstrate the binary search tree application.
 */
public class GenericsKbBSTApp {
    private static BinarySearchTree bst;
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
                bst.insert(line); // Assuming each line represents a statement in the knowledge base
            }
            System.out.println("\nKnowledge base loaded successfully.\n");
            knowledgeBaseLoaded = true;
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("\nFile not found: " + fileName + "\n");
        }
    }

    /**
     * Main method to run the binary search tree application.
     * 
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        bst = new BinarySearchTree();
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
                // Add new statement to the binary search tree
                bst.insert(newElement);
                System.out.println("\nStatement for term " + term + " has been updated.\n");

            } else if (menuInput.equals("3")) {
                if (!knowledgeBaseLoaded) {
                    System.out.println("\nKnowledge base has not been loaded yet.\n");
                    continue; // Skip searching
                }
                System.out.print("Enter the term to search: ");
                String searchTerm = keyboard.nextLine();
                // Search for item in the knowledge base by term
                bst.inOrder(searchTerm); // Example: Print all statements, replace with search logic
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
                bst.inOrder(searchStatement); // Example: Print all statements, replace with search logic
            }
        }

        keyboard.close();
    }
}