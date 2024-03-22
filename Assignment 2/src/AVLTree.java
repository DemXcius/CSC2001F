/**
 * @author Chris Scheepers
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
public class AVLTree {
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
        if (!isBalanced(root)) {
            root = balance(root);
            // Perform balancing operation if the tree is not balanced after insertion
        }
    }

    /**
     * Balances the AVL tree starting from the given node.
     *
     * @param node The node to start the balancing from.
     * @return The new root of the balanced subtree.
     */
    private AVLNode balance(AVLNode node) {
        if (node == null) {
            return null;
        }

        // Calculate the balance factor for the current node
        int balanceFactor = getBalance(node);

        // Left subtree is heavier
        if (balanceFactor > 1) {
            // Left-Left case: Perform right rotation on the current node
            if (height(node.left.left) >= height(node.left.right)) {
                return rightRotate(node);
            }
            // Left-Right case: Perform left rotation on the left child followed by right
            // rotation on the current node
            else {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }
        }
        // Right subtree is heavier
        else if (balanceFactor < -1) {
            // Right-Right case: Perform left rotation on the current node
            if (height(node.right.right) >= height(node.right.left)) {
                return leftRotate(node);
            }
            // Right-Left case: Perform right rotation on the right child followed by left
            // rotation on the current node
            else {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }
        }
        return node; // Node is already balanced
    }

    private AVLNode insertRec(AVLNode node, String data) {
        if (node == null) {
            insertOpCount++; // Increment insert operation count
            return new AVLNode(data);
        }

        if (data.compareTo(node.data) < 0) {
            insertOpCount++;
            node.left = insertRec(node.left, data);
        } else if (data.compareTo(node.data) > 0) {
            insertOpCount++;
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
        if (node == null) {
            return false;
        }

        String[] parts = node.data.split("\t");
        String termPart = parts[0]; // Extract the term part from the data
        int comparisonResult = term.compareTo(termPart);
        if (comparisonResult == 0) {
            searchOpCount++; // Increment the count for equality comparison
            System.out.println(parts[0]+": "+ parts[1]);
            return true;
        } else if (comparisonResult < 0) {
            searchOpCount++; // Increment the count for less-than comparison
            return searchRec(node.left, term);
        } else {
            searchOpCount++; // Increment the count for greater-than comparison
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

    private boolean isBalanced(AVLNode node) {
        int balanceFactor = getBalance(node);
        return balanceFactor >= -1 && balanceFactor <= 1;
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

    public void resetSearchOpCount() {
        searchOpCount = 0;
    }

    public void resetInsertOpCount() {
        insertOpCount = 0;
    }
}