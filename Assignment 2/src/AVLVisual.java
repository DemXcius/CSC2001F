import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class AVLVisual {

    public static void visualize(AVLTree tree, String filename) {
        StringBuilder dot = new StringBuilder();
        dot.append("digraph AVLTree {\n");

        // Recursively generate DOT representation of the AVL tree
        buildDot(tree.root, dot);

        dot.append("}");

        // Write DOT representation to file
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(dot.toString());
            System.out.println("DOT file generated: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void buildDot(AVLNode node, StringBuilder dot) {
        if (node != null) {
            if (node.left != null) {
                dot.append("\"").append(node.data).append("\" -> \"").append(node.left.data).append("\";\n");
                buildDot(node.left, dot);
            }
            if (node.right != null) {
                dot.append("\"").append(node.data).append("\" -> \"").append(node.right.data).append("\";\n");
                buildDot(node.right, dot);
            }
        }
    }

    public static void main(String[] args) {
        AVLTree avl = new AVLTree();

        // Load terms from text file and insert into AVL tree
        loadTerms(avl, "queries.txt");

        // Visualize AVL tree
        visualize(avl, "avl_tree.dot");
    }

    private static void loadTerms(AVLTree avl, String fileName) {
        try {
            Scanner scanner = new Scanner(new File(fileName));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // Assuming each line represents a term in the knowledge base
                String[] parts = line.split("\t");
                if (parts.length >= 1) { // Ensure there is at least one part (term)
                    String term = parts[0];
                    avl.insert(term); // Insert the term into the AVL tree
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("\nFile not found: " + fileName + "\n");
        }
    }
}