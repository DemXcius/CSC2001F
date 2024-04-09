import java.util.Scanner;
import java.util.Arrays;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimulatorOne {

    /**
 * OptimalTripFormatter class is responsible for formatting trip information for clients and shops.
 */
public static class OptimalTripFormatter {

    /**
     * Comparator for sorting paths from taxi to client based on their starting node.
     */
    private static class TaxiToClientPathComparator implements Comparator<PathAndCost> {
        @Override
        public int compare(PathAndCost obj1, PathAndCost obj2) {
            return obj1.path[0].compareTo(obj2.path[0]);
        }
    }

    /**
     * Comparator for sorting paths from client to shop based on their destination node.
     */
    private static class ClientToShopPathComparator implements Comparator<PathAndCost> {
        @Override
        public int compare(PathAndCost obj1, PathAndCost obj2) {
            return obj1.path[obj1.path.length - 1].compareTo(obj2.path[obj2.path.length - 1]);
        }
    }

    /**
     * Format the trip information for a given client.
     * @param clientNode The client node.
     * @param pathsToClient Array of paths from taxis to the client.
     * @param pathsToShop Array of paths from the client to shops.
     * @return Formatted trip information.
     */
    public static String format(String clientNode, PathAndCost[] pathsToClient, PathAndCost[] pathsToShop) {
        StringBuilder result = new StringBuilder("client " + clientNode + "\n");

        if (pathsToClient.length == 0 || pathsToShop.length == 0) {
            OptimalTripFormatter.appendClientUnhelpable(clientNode, result);
            return result.toString();
        }

        OptimalTripFormatter.appendTaxiResult(pathsToClient, result);
        OptimalTripFormatter.appendShopResult(pathsToShop, result);

        return result.toString().trim();
    }

    /**
     * Append a message for an unhelpable client.
     * @param clientNode The client node.
     * @param result StringBuilder to append the message.
     */
    private static void appendClientUnhelpable(String clientNode, StringBuilder result) {
        result.append("cannot be helped\n");
    }

    /**
     * Append the formatted result for taxi trips.
     * @param pathsToClient Array of paths from taxis to the client.
     * @param result StringBuilder to append the formatted result.
     */
    private static void appendTaxiResult(PathAndCost[] pathsToClient, StringBuilder result) {
        Arrays.sort(pathsToClient, new TaxiToClientPathComparator());
        int i, j;
        String currentTaxi, laterTaxi, path;
        double cost;
        for (i = 0; i < pathsToClient.length;) {
            currentTaxi = first(pathsToClient[i].path);
            for (j = i + 1; j < pathsToClient.length; j++) {
                laterTaxi = first(pathsToClient[j].path);
                if (!currentTaxi.equals(laterTaxi)) {
                    break;
                }
            }
            result.append("taxi " + currentTaxi + "\n");
            if (j - i == 1) {
                // Single optimal route starting at a certain point
                path = String.join(" ", pathsToClient[i].path);
                result.append(path + "\n");
            } else {
                // Multiple optimal routes starting from a certain point
                cost = pathsToClient[i].cost;
                result.append("multiple solutions cost " + Long.toString((long)cost) + "\n");
            }
            i = j;
        }
    }

    /**
     * Append the formatted result for shop trips.
     * @param pathsToShop Array of paths from the client to shops.
     * @param result StringBuilder to append the formatted result.
     */
    private static void appendShopResult(PathAndCost[] pathsToShop, StringBuilder result) {
        Arrays.sort(pathsToShop, new ClientToShopPathComparator());
        int i, j;
        String currentShop, laterShop, path;
        double cost;
        for (i = 0; i < pathsToShop.length;) {
            currentShop = last(pathsToShop[i].path);
            for (j = i + 1; j < pathsToShop.length; j++) {
                laterShop = last(pathsToShop[j].path);
                if (!currentShop.equals(laterShop)) {
                    break;
                }
            }
            result.append("shop " + currentShop + "\n");
            if (j - i == 1) {
                // Single optimal route starting at a certain point
                path = String.join(" ", pathsToShop[i].path);
                result.append(path + "\n");
            } else {
                // Multiple optimal routes starting from a certain point
                cost = pathsToShop[i].cost;
                result.append("multiple solutions cost " + Long.toString((long)cost) + "\n");
            }
            i = j;
        }
    }

    /**
     * Get the first element of an array.
     * @param array The input array.
     * @return The first element of the array.
     */
    private static String first(String[] array) {
        return array[0];
    }

    /**
     * Get the last element of an array.
     * @param array The input array.
     * @return The last element of the array.
     */
    private static String last(String[] array) {
        return array[array.length - 1];
    }

}

/**
 * PathAndCost class represents a path and its associated cost.
 */
public static class PathAndCost {
    public String[] path; // Array representing the path nodes.
    public double cost;   // Cost associated with the path.
    public String destination; // Destination node of the path.
    public boolean isReachable; // Indicates if the destination is reachable.

    /**
     * Default constructor.
     */
    public PathAndCost() {
    }

    /**
     * Constructor with path and cost parameters.
     * @param path Array representing the path nodes.
     * @param cost Cost associated with the path.
     */
    public PathAndCost(String[] path, double cost) {
        this.path = path;
        this.cost = cost;
        this.destination = path[path.length - 1];
        this.isReachable = true;
    }

    /**
     * Constructor for unreachable destinations.
     * @param isReachable Indicates if the destination is reachable (should be false).
     * @param destination Destination node.
     * @throws IllegalArgumentException if used for reachable destinations.
     */
    public PathAndCost(boolean isReachable, String destination) {
        if (!isReachable) {
            throw new IllegalArgumentException("Constructor can only be used for unreachable destinations.");
        }
        this.path = null;
        this.cost = Double.POSITIVE_INFINITY;
        this.destination = destination;
        this.isReachable = false;
    }

    /**
     * Convert PathAndCost object to a string representation.
     * @return String representation of the object.
     */
    public String toString() {
        if (isReachable) {
            return "(Cost is: " + Double.toString(this.cost) + ") " + String.join(" to ", this.path);
        } else {
            return this.destination + " is unreachable";
        }
    }
}

    public static class TripManager {

        // Instance variables
        public Graph graph;
        private List<String> clientNodes;
        private List<String> shopNodes;

        // Constructor
        public TripManager(String graphData) {
            this.clientNodes = new ArrayList<>();
            this.shopNodes = new ArrayList<>();
            this.populateGraph(graphData);
        }

        // Method to populate the graph based on input data
        private void populateGraph(String data) {
            this.graph = new Graph();
            String[] lines = data.split("\n");
            LineInstruction instruction = LineInstruction.SET_NUMBER_OF_NODES;
            int nodes = 0;
            Scanner scanner;
            String line;
            for (int i = 0; i < lines.length; i++) {
                line = lines[i];
                scanner = new Scanner(line);
                switch (instruction) {
                    case SET_NUMBER_OF_NODES:
                        nodes = scanner.nextInt();
                        instruction = LineInstruction.ADD_EDGES;
                        break;
                    case ADD_EDGES:
                        this.addVerticesAndEdges(line);
                        instruction = i == nodes ? LineInstruction.SET_NUMBER_OF_SHOPS : LineInstruction.ADD_EDGES;
                        break;
                    case SET_NUMBER_OF_SHOPS:
                        // I don't need the number of shops
                        instruction = LineInstruction.ADD_SHOPS;
                        break;
                    case ADD_SHOPS:
                        this.addShops(line);
                        instruction = LineInstruction.SET_NUMBER_OF_CLIENTS;
                        break;
                    case SET_NUMBER_OF_CLIENTS:
                        // I don't need the number of shops
                        instruction = LineInstruction.ADD_CLIENTS;
                        break;
                    case ADD_CLIENTS:
                        this.addClients(line);
                        instruction = LineInstruction.END;
                        break;
                    default: // instruction = LineInstruction.END
                        break;
                }
            }
        }

        // Method to add vertices and edges to the graph
        private void addVerticesAndEdges(String data) {
            try (Scanner scanner = new Scanner(data)) {
                String source = scanner.next();
                String destination;
                double cost;
                this.addNode(source);
                while (scanner.hasNext()) {
                    destination = scanner.next();
                    cost = scanner.nextDouble();
                    this.addEdge(source, destination, cost);
                }
            }
        }

        // Method to add a node to the graph
        private void addNode(String node) {
            // if vertex exists, nothing happens
            // if it doesn't, it gets created
            this.graph.getVertex(node);
        }

        // Method to add an edge to the graph
        private void addEdge(String source, String destination, double cost) {
            this.graph.addEdge(source, destination, cost);
        }

        // Method to add shops to the list of shops
        private void addShops(String data) {
            String[] nodes = data.split(" ");
            for (String node : nodes) {
                this.addShop(node);
            }
        }

        // Method to add a shop to the list of shops
        private void addShop(String node) {
            this.shopNodes.add(node);
        }

        // Method to add clients to the list of clients
        private void addClients(String data) {
            String[] nodes = data.split(" ");
            for (String node : nodes) {
                this.addClient(node);
            }
        }

        // Method to add a client to the list of clients
        private void addClient(String node) {
            this.clientNodes.add(node);
        }

        // Method to get optimal paths from start to end node
        public PathAndCost[] getOptimalPaths(String start, String end) {
            List<PathAndCost> optimals = new ArrayList<>();
            PathAndCost optimal = null;
            double minCostFound = Double.MAX_VALUE;

            while (true) {
                String[][] invalidPaths = getPaths(optimals);
                String pathOutput = getOptimalPathOutput(start, end, invalidPaths);
                if (!TripManager.nodeIsReachable(pathOutput)) {
                    break;
                }
                String[] path = TripManager.extractPath(pathOutput);
                double cost = TripManager.extractCost(pathOutput);
                optimal = new PathAndCost(path, cost);
                if (optimal.cost <= minCostFound) {
                    minCostFound = optimal.cost;
                    optimals.add(optimal);
                } else {
                    break;
                }
            }

            if (optimals.isEmpty()) {
                return new PathAndCost[0];
            } else {
                return optimals.toArray(new PathAndCost[optimals.size()]);
            }
        }

        // Method to get paths from previous optimal paths
        private String[][] getPaths(List<PathAndCost> pathAndCosts) {
            String[][] result = new String[pathAndCosts.size()][];
            for (int i = 0; i < pathAndCosts.size(); i++) {
                result[i] = pathAndCosts.get(i).path;
            }
            return result;
        }

        // Method to get optimal path output
        private String getOptimalPathOutput(String start, String end, String[][] invalidPaths) {
            this.graph.dijkstra(start, invalidPaths);
            StringBuilder outputBuilder = new StringBuilder();
            this.graph.printPath(end, outputBuilder);
            return outputBuilder.toString();
        }

        // Method to extract path from path output
        private static String[] extractPath(String pathOutput) {
            int indexOfCloseBrackets = pathOutput.indexOf(')', 0);
            String pathWithTo = pathOutput.trim().substring(indexOfCloseBrackets + 2);
            String[] path = pathWithTo.split(" to ");
            return path;
        }

        // Method to extract cost from path output
        private static double extractCost(String pathOutput) {
            String regex = "\\d+\\.\\d+";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(pathOutput);
            if (matcher.find()) {
                String costString = matcher.group();
                return Double.parseDouble(costString);
            } else {
                return 0.0;
            }
        }

        // Method to check if node is reachable
        private static boolean nodeIsReachable(String pathOutput) {
            return !pathOutput.contains("unreachable");
        }

        // Method to get client nodes
        public String[] clientNodes() {
            return this.clientNodes.toArray(new String[this.clientNodes.size()]);
        }

        // Method to get shop nodes
        public String[] shopNodes() {
            return this.shopNodes.toArray(new String[this.shopNodes.size()]);
        }

        // Method to get optimal paths to shop
        public PathAndCost[] getOptimalPathsToShop(String node) {
            ArrayList<PathAndCost> optimals = new ArrayList<>();
            for (String shopNode : this.shopNodes()) {
                PathAndCost[] optimalsToShop = this.getOptimalPaths(node, shopNode);
                if (optimalsToShop.length == 0) { // no valid paths
                    continue;
                }
                if (!optimals.isEmpty() && optimals.get(0).cost > optimalsToShop[0].cost) {
                    optimals.clear();
                }
                if (optimals.isEmpty() || optimals.get(0).cost == optimalsToShop[0].cost) {
                    for (PathAndCost optimalToShop : optimalsToShop) {
                        optimals.add(optimalToShop);
                    }
                }
            }
            return optimals.toArray(new PathAndCost[optimals.size()]);
        }

        // Method to get optimal paths to client
        public PathAndCost[] getOptimalPathsToClient(String node) {
            ArrayList<PathAndCost> optimals = new ArrayList<>();
            for (String shopNode : this.shopNodes()) {
                PathAndCost[] optimalsToClient = this.getOptimalPaths(shopNode, node);
                if (optimalsToClient.length == 0) { // no valid paths
                    continue;
                }
                if (!optimals.isEmpty() && optimals.get(0).cost > optimalsToClient[0].cost) {
                    optimals.clear();
                }
                if (optimals.isEmpty() || optimals.get(0).cost >= optimalsToClient[0].cost) {
                    for (PathAndCost optimalToClient : optimalsToClient) {
                        optimals.add(optimalToClient);
                    }
                }
            }
            return optimals.toArray(new PathAndCost[optimals.size()]);
        }

    }

    public static enum LineInstruction {
        SET_NUMBER_OF_NODES,
        ADD_EDGES,
        SET_NUMBER_OF_SHOPS,
        ADD_SHOPS,
        SET_NUMBER_OF_CLIENTS,
        ADD_CLIENTS,
        END
    }

    public static void main(String[] args) {
        // Read input from the file
        String input = readFromSystemIn();
        
        // Create a TripManager object with the input data
        TripManager tripManager = new TripManager(input);
        
        // Iterate over client nodes
        for (String clientNode : tripManager.clientNodes()) {
            // Get optimal paths to client and shop for each client node
            PathAndCost[] optimalsToClient = tripManager.getOptimalPathsToClient(clientNode);
            PathAndCost[] optimalsToShop = tripManager.getOptimalPathsToShop(clientNode);
            
            // Format and print the optimal trip information using OptimalTripFormatter
            System.out.println(OptimalTripFormatter.format(clientNode, optimalsToClient, optimalsToShop));
        }
    }
    
    // Method to read input from the standard input
    private static String readFromSystemIn() {
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in); // Create a scanner to read from standard input
        StringBuilder inputBuilder = new StringBuilder(); // StringBuilder to store input lines
        while (scanner.hasNextLine()) { // Continue reading until no more lines are available
            String line = scanner.nextLine(); // Read the next line
            inputBuilder.append(line).append("\n"); // Append the line to the StringBuilder
        }
        return inputBuilder.toString(); // Return the accumulated input as a string
    }

}
