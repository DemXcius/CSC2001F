import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class AVLExperiment {
    private static final int[] datasetSizes = {10, 100, 1000, 10000, 100000};
    private static final int numQueries = 16; // Number of queries
    private static final String queryFile = "queries.txt"; // File containing queries

    public static void main(String[] args) {
        List<Integer> insertOpCounts = new ArrayList<>();
        List<Integer> searchOpCounts = new ArrayList<>();

        for (int size : datasetSizes) {
            List<String> subset = generateRandomSubset(size);
            int totalInsertOpCount = 0;
            int totalSearchOpCount = 0;

            for (int i = 0; i < numQueries; i++) {
                AVLTree avl = new AVLTree();
                totalInsertOpCount += loadAndInsertData(avl, subset);
                totalSearchOpCount += searchQueries(avl, queryFile);
            }

            int avgInsertOpCount = totalInsertOpCount / numQueries;
            int avgSearchOpCount = totalSearchOpCount / numQueries;

            insertOpCounts.add(avgInsertOpCount);
            searchOpCounts.add(avgSearchOpCount);
        }

        // Calculate min, max, and average of insert and search operation counts
        int minInsertOpCount = insertOpCounts.stream().min(Integer::compareTo).orElse(0);
        int maxInsertOpCount = insertOpCounts.stream().max(Integer::compareTo).orElse(0);
        int avgInsertOpCount = (int) insertOpCounts.stream().mapToInt(Integer::intValue).average().orElse(0);
        
        int minSearchOpCount = searchOpCounts.stream().min(Integer::compareTo).orElse(0);
        int maxSearchOpCount = searchOpCounts.stream().max(Integer::compareTo).orElse(0);
        int avgSearchOpCount = (int) searchOpCounts.stream().mapToInt(Integer::intValue).average().orElse(0);

         try (FileWriter writer = new FileWriter("operation_counts.txt")) {
            writer.write("Insert Operations:\n");
            writer.write("Minimum: " + minInsertOpCount + "\n");
            writer.write("Maximum: " + maxInsertOpCount + "\n");
            writer.write("Average: " + avgInsertOpCount + "\n");

            writer.write("\nSearch Operations:\n");
            writer.write("Minimum: " + minSearchOpCount + "\n");
            writer.write("Maximum: " + maxSearchOpCount + "\n");
            writer.write("Average: " + avgSearchOpCount + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Plotting code here
        // You can use libraries like JFreeChart or JavaFX for plotting the data
    }

    private static List<String> generateRandomSubset(int size) {
        List<String> subset = new ArrayList<>();
        List<String> dataset = readDatasetFromFile("dataset.txt");
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int index = random.nextInt(dataset.size());
            subset.add(dataset.get(index));
        }
        return subset;
    }

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

    private static int loadAndInsertData(AVLTree avl, List<String> data) {
        int insertOpCount = 0;
        for (String item : data) {
            avl.insert(item);
            insertOpCount += avl.getInsertOpCount();
        }
        return insertOpCount;
    }

    private static int searchQueries(AVLTree avl, String queryFile) {
        int searchOpCount = 0;
        try {
            Scanner scanner = new Scanner(new File(queryFile));
            while (scanner.hasNextLine()) {
                String query = scanner.nextLine().trim();
                avl.search(query);
                searchOpCount += avl.getSearchOpCount();
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return searchOpCount;
    }
}

