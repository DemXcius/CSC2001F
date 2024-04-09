import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SimulatorOne simulates a taxi service system for clients and shops. It provides functionality
 * to process client requests, find optimal paths, and generate results based on the obtained paths.
 * 
 * @author Chris Scheepers
 * 
 */
public class SimulatorOne {

    /**
     * SimulaterTaxiService simulates taxi service operations.
     */
    public static class SimulaterTaxiService {

        /**
         * Comparator for sorting taxi paths based on starting node.
         */
        private static class TaxiToClientPath implements Comparator<tripPathAndCost> {
            @Override
            public int compare(tripPathAndCost a, tripPathAndCost b) {
                return a.pathNode[0].compareTo(b.pathNode[0]);
            }
        }

        /**
         * Comparator for sorting client paths based on ending node.
         */
        private static class ClientToShopPath implements Comparator<tripPathAndCost> {
            @Override
            public int compare(tripPathAndCost a, tripPathAndCost b) {
                return a.pathNode[a.pathNode.length - 1].compareTo(b.pathNode[b.pathNode.length - 1]);
            }
        }

        /**
         * Formats the output based on client node, paths to client, and paths to shop.
         * 
         * @param clientNode    The client node.
         * @param pathsToClient Paths to the client.
         * @param pathsToShop   Paths to the shop.
         * @return The formatted output string.
         */
        public static String format(String clientNode, tripPathAndCost[] pathsToClient, tripPathAndCost[] pathsToShop) {
            StringBuilder result = new StringBuilder("client " + clientNode + "\n");

            if (pathsToClient.length == 0 || pathsToShop.length == 0) {
                error(clientNode, result);
                return result.toString();
            }

            TaxiResult(pathsToClient, result);
            ShopResult(pathsToShop, result);

            return result.toString().trim();
        }

        private static void error(String clientNode, StringBuilder result) {
            result.append("cannot be helped\n");
        }

        /**
         * Generates result for paths to clients.
         *
         * @param pathsToClient An array of tripPathAndCost objects containing paths and
         *                      associated costs to clients.
         * @param result        StringBuilder to store the generated result.
         */
        private static void TaxiResult(tripPathAndCost[] pathsToClient, StringBuilder result) {
            Arrays.sort(pathsToClient, new TaxiToClientPath());
            int i, j;
            String currentTaxi, laterTaxi, path;
            double cost;
            for (i = 0; i < pathsToClient.length;) {
                currentTaxi = first(pathsToClient[i].pathNode);
                for (j = i + 1; j < pathsToClient.length; j++) {
                    laterTaxi = first(pathsToClient[j].pathNode);
                    if (!currentTaxi.equals(laterTaxi)) {
                        break;
                    }
                }
                result.append("taxi " + currentTaxi + "\n");
                if (j - i == 1) {
                    path = String.join(" ", pathsToClient[i].pathNode);
                    result.append(path + "\n");
                } else {
                    cost = pathsToClient[i].cost;
                    result.append("multiple solutions cost " + Long.toString((long) cost) + "\n");
                }
                i = j;
            }
        }

        /**
         * Generates result for paths to shops.
         *
         * @param pathsToShop An array of tripPathAndCost objects containing paths and
         *                    associated costs to shops.
         * @param result      StringBuilder to store the generated result.
         */
        private static void ShopResult(tripPathAndCost[] pathsToShop, StringBuilder result) {
            Arrays.sort(pathsToShop, new ClientToShopPath());
            int i, j;
            String currentShop, nextShop, path;
            double cost;
            for (i = 0; i < pathsToShop.length;) {
                currentShop = last(pathsToShop[i].pathNode);
                for (j = i + 1; j < pathsToShop.length; j++) {
                    nextShop = last(pathsToShop[j].pathNode);
                    if (!currentShop.equals(nextShop)) {
                        break;
                    }
                }
                result.append("shop " + currentShop + "\n");
                if (j - i == 1) {
                    path = String.join(" ", pathsToShop[i].pathNode);
                    result.append(path + "\n");
                } else {
                    cost = pathsToShop[i].cost;
                    result.append("multiple solutions cost " + Long.toString((long) cost) + "\n");
                }
                i = j;
            }
        }

        /**
         * Retrieves the first element of an array.
         *
         * @param array The input array.
         * @return The first element of the array.
         */
        private static String first(String[] array) {
            return array[0];
        }

        /**
         * Retrieves the last element of an array.
         *
         * @param array The input array.
         * @return The last element of the array.
         */
        private static String last(String[] array) {
            return array[array.length - 1];
        }

    }

    /**
     * Represents a trip path and its cost.
     */
    public static class tripPathAndCost {
        public String[] pathNode;
        public double cost;
        public String destinationNode;
        public boolean valid;

        public tripPathAndCost() {
        }

        /**
         * Constructor for a valid path.
         * 
         * @param path The path nodes.
         * @param cost The cost of the path.
         */
        public tripPathAndCost(String[] path, double cost) {
            this.pathNode = path;
            this.cost = cost;
            this.destinationNode = path[path.length - 1];
            this.valid = true;
        }

        /**
         * Constructor for unreachable destination nodes.
         * 
         * @param valid           Indicates if the destination is reachable.
         * @param destinationNode The destination node.
         */
        public tripPathAndCost(boolean valid, String destinationNode) {
            if (!valid) {
                throw new IllegalArgumentException("Constructor can only be used for unreachable destinationNodes.");
            }
            this.pathNode = null;
            this.cost = Double.POSITIVE_INFINITY;
            this.destinationNode = destinationNode;
            this.valid = false;
        }

        /**
         * Generates a string representation of the trip path and cost.
         * 
         * @return The string representation.
         */
        public String toString() {
            if (valid) {
                return "(Cost is: " + Double.toString(this.cost) + ") " + String.join(" to ", this.pathNode);
            } else {
                return this.destinationNode + " is unreachable";
            }
        }
    }

    /**
     * Class for processing client calls and managing trip details.
     */
    public static class processClientCall {
        public Graph graph;
        private List<String> clientNodes;
        private List<String> shopNodes;

        /**
         * Constructor for processing client calls.
         * 
         * @param graphData The input data containing graph information.
         */
        public processClientCall(String graphData) {
            this.clientNodes = new ArrayList<>();
            this.shopNodes = new ArrayList<>();
            this.populateGraph(graphData);
        }

        /**
         * Populates the graph based on the provided data.
         *
         * @param data The input data containing information about nodes, edges, shops,
         *             and clients.
         */
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
                        instruction = LineInstruction.ADD_SHOPS;
                        break;
                    case ADD_SHOPS:
                        this.addShops(line);
                        instruction = LineInstruction.SET_NUMBER_OF_CLIENTS;
                        break;
                    case SET_NUMBER_OF_CLIENTS:
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

        /**
         * Adds vertices and edges to the graph based on the provided data.
         *
         * @param data The input data containing information about vertices and edges.
         */
        private void addVerticesAndEdges(String data) {
            try (Scanner scanner = new Scanner(data)) {
                String source = scanner.next();
                String destinationNode;
                double cost;
                this.addNode(source);
                while (scanner.hasNext()) {
                    destinationNode = scanner.next();
                    cost = scanner.nextDouble();
                    this.addEdge(source, destinationNode, cost);
                }
            }
        }

        /**
         * Adds a node to the graph.
         *
         * @param node The node to be added.
         */
        private void addNode(String node) {
            this.graph.getVertex(node);
        }

        /**
         * Adds an edge to the graph.
         *
         * @param source          The source node of the edge.
         * @param destinationNode The destination node of the edge.
         * @param cost            The cost associated with traversing the edge.
         */
        private void addEdge(String source, String destinationNode, double cost) {
            this.graph.addEdge(source, destinationNode, cost);
        }

        /**
         * Adds shops to the list of shop nodes.
         *
         * @param data The input data containing shop nodes.
         */
        private void addShops(String data) {
            String[] nodes = data.split("\\s+");
            for (String node : nodes) {
                this.addShop(node);
            }
        }

        /**
         * Adds a shop node to the list of shop nodes.
         *
         * @param node The shop node to be added.
         */
        private void addShop(String node) {
            this.shopNodes.add(node);
        }

        /**
         * Adds clients to the list of client nodes.
         *
         * @param data The input data containing client nodes.
         */
        private void addClients(String data) {
            String[] nodes = data.split(" ");
            for (String node : nodes) {
                this.addClient(node);
            }
        }

        /**
         * Adds a client node to the list of client nodes.
         *
         * @param node The client node to be added.
         */
        private void addClient(String node) {
            this.clientNodes.add(node);
        }

        /**
         * Finds Dijkstra paths and costs from a start node to an end node.
         *
         * @param start The starting node.
         * @param end   The destination node.
         * @return An array of tripPathAndCost objects containing paths and associated
         *         costs.
         */
        public tripPathAndCost[] getDijkstraPaths(String start, String end) {
            List<tripPathAndCost> optimals = new ArrayList<>();
            tripPathAndCost optimal = null;
            double minCostFound = Double.MAX_VALUE;

            while (true) {
                String[][] invalidPaths = getPaths(optimals);
                String pathOutput = getPathOutput(start, end, invalidPaths);
                if (!processClientCall.Valid(pathOutput)) {
                    break;
                }
                String[] path = processClientCall.outputPath(pathOutput);
                double cost = processClientCall.outputCost(pathOutput);
                optimal = new tripPathAndCost(path, cost);
                if (optimal.cost <= minCostFound) {
                    minCostFound = optimal.cost;
                    optimals.add(optimal);
                } else {
                    break;
                }
            }

            if (optimals.isEmpty()) {
                return new tripPathAndCost[0];
            } else {
                return optimals.toArray(new tripPathAndCost[optimals.size()]);
            }
        }

        /**
         * Retrieves paths from a list of tripPathAndCost objects.
         *
         * @param tripPathAndCosts The list of tripPathAndCost objects.
         * @return A 2D array containing paths.
         */
        private String[][] getPaths(List<tripPathAndCost> tripPathAndCosts) {
            String[][] result = new String[tripPathAndCosts.size()][];
            for (int i = 0; i < tripPathAndCosts.size(); i++) {
                result[i] = tripPathAndCosts.get(i).pathNode;
            }
            return result;
        }

        /**
         * Retrieves path output based on start and end nodes and invalid paths.
         *
         * @param start        The starting node.
         * @param end          The destination node.
         * @param invalidPaths 2D array containing invalid paths.
         * @return The path output as a string.
         */
        private String getPathOutput(String start, String end, String[][] invalidPaths) {
            this.graph.dijkstra(start, invalidPaths);
            StringBuilder outputBuilder = new StringBuilder();
            this.graph.printPath(end, outputBuilder);
            return outputBuilder.toString();
        }

        /**
         * Extracts the output path from the full path output string.
         *
         * @param pathOutput The full path output string.
         * @return An array containing the path nodes.
         */
        private static String[] outputPath(String pathOutput) {
            int index = pathOutput.indexOf(')', 0);
            String pathWithTo = pathOutput.trim().substring(index + 2);
            String[] path = pathWithTo.split(" to ");
            return path;
        }

        /**
         * Outputs the cost from the full path output string.
         *
         * @param pathOutput The full path output string.
         * @return The extracted cost.
         */
        private static double outputCost(String pathOutput) {
            String regex = "\\d+\\.\\d+";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(pathOutput);
            if (matcher.find()) {
                String cost = matcher.group();
                return Double.parseDouble(cost);
            } else {
                return 0;
            }
        }

        /**
         * Checks if the path output is valid.
         *
         * @param pathOutput The path output to be validated.
         * @return True if the path output is valid, false otherwise.
         */
        private static boolean Valid(String pathOutput) {
            return !pathOutput.contains("unreachable");
        }

        /**
         * Retrieves client nodes.
         *
         * @return An array containing client nodes.
         */
        public String[] clientNodes() {
            return this.clientNodes.toArray(new String[this.clientNodes.size()]);
        }

        /**
         * Retrieves shop nodes.
         *
         * @return An array containing shop nodes.
         */
        public String[] shopNodes() {
            return this.shopNodes.toArray(new String[this.shopNodes.size()]);
        }

        /**
         * Retrieves paths to a shop from a given node.
         *
         * @param node The starting node.
         * @return An array of tripPathAndCost objects containing paths and associated
         *         costs.
         */
        public tripPathAndCost[] getPathsToShop(String node) {
            ArrayList<tripPathAndCost> paths = new ArrayList<>();
            for (String shopNode : this.shopNodes()) {
                tripPathAndCost[] pathsToShop = this.getDijkstraPaths(node, shopNode);
                if (pathsToShop.length == 0) {
                    continue;
                }
                if (!paths.isEmpty() && paths.get(0).cost > pathsToShop[0].cost) {
                    paths.clear();
                }
                if (paths.isEmpty() || paths.get(0).cost == pathsToShop[0].cost) {
                    for (tripPathAndCost pathToShop : pathsToShop) {
                        paths.add(pathToShop);
                    }
                }
            }
            return paths.toArray(new tripPathAndCost[paths.size()]);
        }

        /**
         * Retrieves paths to a client from a given node.
         *
         * @param node The starting node.
         * @return An array of tripPathAndCost objects containing paths and associated
         *         costs.
         */
        public tripPathAndCost[] PathsToClient(String node) {
            ArrayList<tripPathAndCost> paths = new ArrayList<>();
            for (String shopNode : this.shopNodes()) {
                tripPathAndCost[] pathsToClient = this.getDijkstraPaths(shopNode, node);
                if (pathsToClient.length == 0) {
                    continue;
                }
                if (!paths.isEmpty() && paths.get(0).cost > pathsToClient[0].cost) {
                    paths.clear();
                }
                if (paths.isEmpty() || paths.get(0).cost >= pathsToClient[0].cost) {
                    for (tripPathAndCost pathToClient : pathsToClient) {
                        paths.add(pathToClient);
                    }
                }
            }
            return paths.toArray(new tripPathAndCost[paths.size()]);
        }

    }

    /**
     * Enum representing different line instructions during the graph population
     * process.
     * These instructions guide the parsing of input data for populating the graph.
     */
    public static enum LineInstruction {
        // Indicates setting the number of nodes in the graph.
        SET_NUMBER_OF_NODES,

        // Indicates adding edges between nodes in the graph.
        ADD_EDGES,

        // Indicates setting the number of shops in the graph.
        SET_NUMBER_OF_SHOPS,

        // Indicates adding shop nodes to the graph.
        ADD_SHOPS,

        // Indicates setting the number of clients in the graph.
        SET_NUMBER_OF_CLIENTS,

        // Indicates adding client nodes to the graph.
        ADD_CLIENTS,

        // Indicates the end of the graph population process.
        END
    }

    /**
     * Main method to run the simulation.
     * 
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        String input = systemInput();
        processClientCall taxiService = new processClientCall(input);
        for (String clientNode : taxiService.clientNodes()) {
            tripPathAndCost[] pathsToClient = taxiService.PathsToClient(clientNode);
            tripPathAndCost[] pathsToShop = taxiService.getPathsToShop(clientNode);
            System.out.println(SimulaterTaxiService.format(clientNode, pathsToClient, pathsToShop));
        }
    }

    // Reads input from System.in
    private static String systemInput() {
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        StringBuilder input = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            input.append(line).append("\n");
        }
        return input.toString();
    }

}