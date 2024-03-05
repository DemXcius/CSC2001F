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
     * Searches for a data in the binary search tree.
     * 
     * @param data The data to be searched.
     * @return true if the data is found, false otherwise.
     */
    public boolean search(String data) {
        return searchRec(root, data);
    }

    private boolean searchRec(TreeNode root, String data) {
        if (root == null) {
            return false;
        }

        if (root.data.equals(data)) {
            return true;
        }

        if (data.compareTo(root.data) < 0) {
            return searchRec(root.left, data);
        }

        return searchRec(root.right, data);
    }

    /**
     * Prints the data in the binary search tree in inorder traversal.
     */
    public void inOrder() {
        inOrderRec(root);
    }

    private void inOrderRec(TreeNode root) {
        if (root != null) {
            inOrderRec(root.left);
            System.out.println(root.data);
            inOrderRec(root.right);
        }
    }
}

/**
 * Main class to demonstrate the binary search tree application.
 */
public class GenericsKbBSTApp {
    private static BinarySearchTree bst;

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

            } else if (menuInput.equals("2")) {
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
                System.out.print("Enter the term to search: ");
                String searchTerm = keyboard.nextLine();
                // Search for item in the knowledge base by term
                bst.inOrder(); // Example: Print all statements, replace with search logic

            } else if (menuInput.equals("4")) {
                System.out.print("Enter the term: ");
                String term = keyboard.nextLine();
                System.out.print("Enter the statement to search for: ");
                String searchStatement = keyboard.nextLine();
                // Search for item in the knowledge base by term and sentence
                bst.inOrder(); // Example: Print all statements, replace with search logic
            }
        }

        keyboard.close();
    }
}