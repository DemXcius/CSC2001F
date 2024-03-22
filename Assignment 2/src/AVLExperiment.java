import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * @author Chris Scheepers
 * Class for conducting experiments with AVL trees.
 */
public class AVLExperiment {
    /** Array of dataset sizes for experiments. */
    public static final int[] datasetSizes = { 3, 10, 33, 100, 333, 1000, 3333, 10000, 33333, 50000 };

    /** Filename for query file. */
    private static final String queryFile = "GenericsKB-queries.txt";

    /**
     * Main method to run the AVL tree experiment.
     * 
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        try {
            FileWriter writer = new FileWriter("experiment_results.txt");

            // Write column headers
            writer.write("Dataset Size\tInsert Min\tInsert Max\tInsert Avg\tSearch Min\tSearch Max\tSearch Avg\n");

            // Read dataset from file
            List<String> dataset = readDatasetFromFile("GenericsKB.txt");

            // Iterate over dataset sizes
            for (int size : datasetSizes) {
                List<Integer> insertOpCounts = new ArrayList<>();
                List<Integer> searchOpCounts = new ArrayList<>();

                // Perform experiment for current dataset size
                for (int i = 0; i < 10; i++) {
                    AVLTree avl = new AVLTree();
                    List<String> subset = generateRandomSubset(size, dataset);

                    // Load data into AVL tree
                    for (String item : subset) {
                        avl.insert(item);
                    }

                    // Perform searches and record operation counts
                    int insertOpCount = avl.getInsertOpCount();
                    int searchOpCount = 0;
                    try {
                        Scanner scanner = new Scanner(new File(queryFile));
                        while (scanner.hasNextLine()) {
                            String query = scanner.nextLine().trim();
                            boolean found = avl.search(query);
                            if (found) {
                                searchOpCount = avl.getSearchOpCount(); // Add current search count
                            }
                            // Reset search operation count for the next iteration
                            avl.resetSearchOpCount();
                        }
                        scanner.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    insertOpCounts.add(insertOpCount);
                    searchOpCounts.add(searchOpCount);
                }

                // Calculate min, max, and average of operation counts
                int minInsertOpCount = insertOpCounts.stream().min(Integer::compareTo).orElse(0);
                int maxInsertOpCount = insertOpCounts.stream().max(Integer::compareTo).orElse(0);
                int avgInsertOpCount = (int) insertOpCounts.stream().mapToInt(Integer::intValue).average().orElse(0);
                int minSearchOpCount = searchOpCounts.stream().min(Integer::compareTo).orElse(0);
                int maxSearchOpCount = searchOpCounts.stream().max(Integer::compareTo).orElse(0);
                int avgSearchOpCount = (int) searchOpCounts.stream().mapToInt(Integer::intValue).average().orElse(0);

                // Write experiment results to file
                writer.write(String.format("%d\t\t%d\t\t%d\t\t%d\t\t%d\t\t%d\t\t%d\n", size, minInsertOpCount,
                        maxInsertOpCount, avgInsertOpCount, minSearchOpCount, maxSearchOpCount, avgSearchOpCount));
                toExcel(size, minInsertOpCount, maxInsertOpCount, avgInsertOpCount, minSearchOpCount, maxSearchOpCount,
                        avgSearchOpCount);
            }

            writer.close();
            System.out.println("Experiment completed. Results written to experiment_results.txt.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Reads dataset from a file.
     * 
     * @param fileName The name of the file containing the dataset.
     * @return The list of strings representing the dataset.
     */
    private static List<String> readDatasetFromFile(String fileName) {
        List<String> dataset = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(fileName));
            while (scanner.hasNextLine()) {
                dataset.add(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return dataset;
    }

    /**
     * Generates a random subset of a dataset.
     * 
     * @param size    The size of the subset.
     * @param dataset The dataset from which to generate the subset.
     * @return The randomly generated subset.
     */
    private static List<String> generateRandomSubset(int size, List<String> dataset) {
        List<String> subset = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int index = random.nextInt(dataset.size());
            subset.add(dataset.get(index));
        }
        return subset;
    }

    /**
     * Writes experiment results to a CSV file.
     * 
     * @param size              The dataset size.
     * @param minInsertOpCount  The minimum insertion operation count.
     * @param maxInsertOpCount  The maximum insertion operation count.
     * @param avgInsertOpCount  The average insertion operation count.
     * @param minSearchOpCount  The minimum search operation count.
     * @param maxSearchOpCount  The maximum search operation count.
     * @param avgSearchOpCount  The average search operation count.
     */
    public static void toExcel(int size, int minInsertOpCount, int maxInsertOpCount, int avgInsertOpCount,
            int minSearchOpCount, int maxSearchOpCount, int avgSearchOpCount) {
        String csvFile = "output.csv";
        try (FileWriter writer = new FileWriter(csvFile, true)) { // Set append flag to true
            if (new

File(csvFile).length() == 0) { // Check if file is empty
                writer.append("Dataset Size,Insert Min,Insert Max,Insert Avg,Search Min,Search Max,Search Avg\n");
            }
            writer.append(size + "," + minInsertOpCount + "," + maxInsertOpCount + "," + avgInsertOpCount + ","
                    + minSearchOpCount + "," + maxSearchOpCount + "," + avgSearchOpCount + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("CSV file updated successfully.");
    }
}