import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Optimize {

    public static void main(String[] args) {
        List<String> L = readUsernamesFromFile("mydata.txt");
        List<int[]> weightCombinations = generateWeightCombinations(9, 4);
        int minProbes = Integer.MAX_VALUE;
        int countOfMinProbes = 0;

        for (int[] weights : weightCombinations) {
            LPHashTable hashTable = new LPHashTable(37);
            hashTable.setWeights(weights);
            int totalProbes = 0; // Reset totalProbes for each weight combination
            
            for (String username : L) {
                hashTable.insert(username);
                totalProbes += hashTable.getProbeCount(); // Accumulate total probes
                hashTable.resetProbeCount(); // Reset probe count for next insertion
               
            }

            if (totalProbes < minProbes) {
                minProbes = totalProbes;
                countOfMinProbes = 1;
            } else if (totalProbes == minProbes) {
                countOfMinProbes++;
            }
        }

        System.out.println(minProbes + " " + countOfMinProbes);
    }

    private static List<int[]> generateWeightCombinations(int numWeights, int maxWeight) {
        List<int[]> combinations = new ArrayList<>();
        generateCombinationsHelper(numWeights, maxWeight, new int[numWeights], 0, combinations);
        return combinations;
    }

    private static void generateCombinationsHelper(int numWeights, int maxWeight, int[] currentCombination, int index,
            List<int[]> combinations) {
        if (index == numWeights) {
            combinations.add(currentCombination.clone());
            return;
        }

        for (int weight = 0; weight <= maxWeight; weight++) {
            currentCombination[index] = weight;
            generateCombinationsHelper(numWeights, maxWeight, currentCombination, index + 1, combinations);
        }
    }

    private static List<String> readUsernamesFromFile(String filename) {
        List<String> usernames = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                usernames.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return usernames;
    }
}
