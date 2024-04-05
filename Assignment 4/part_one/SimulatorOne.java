import java.util.*;

class SimulatorOne {
    static int numberOfShops;
    private static Graph graph = new Graph();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Read graph data
        readGraphData(scanner);

        // Read client calls and simulate taxi service
        simulateTaxiService(scanner);

        scanner.close();
    }

    // Method to read graph data
    private static void readGraphData(Scanner scanner) {
        // Read number of nodes
        System.out.println("Enter the number of nodes:");
        int numberOfNodes = Integer.parseInt(scanner.nextLine().trim());
        for (int i = 0; i < numberOfNodes; i++) {
            System.out.println("Enter node data (sourceNode destNode1 weight1 destNode2 weight2 ...):");
            String[] parts = scanner.nextLine().trim().split("\\s+");
            String sourceNode = parts[0];
            for (int j = 1; j < parts.length; j += 2) {
                String destNode = parts[j];
                double weight = Double.parseDouble(parts[j + 1]);
                graph.addEdge(sourceNode, destNode, weight);
            }
        }
    }

    // Method to simulate taxi service
    private static void simulateTaxiService(Scanner scanner) {
        // Read number of shops
        System.out.println("Enter the number of shops:");
        numberOfShops = Integer.parseInt(scanner.nextLine().trim());
        System.out.println("Enter shop node:");
        String[] shopNode = scanner.nextLine().trim().split("\\s+");
        
        // Read number of clients
        System.out.println("Enter number of clients:");
        int numberOfClients = Integer.parseInt(scanner.nextLine().trim());
        System.out.println("Enter client node:");
        String[] clientNode = scanner.nextLine().trim().split("\\s+");
        
        // Process each client call
        for (int i = 0; i < numberOfClients; i++) {
            processClientCall(clientNode[i], shopNode[i]);
        }
    }

    // Method to process a client call
    private static void processClientCall(String clientNode, String shopNode) {
        // Find the nearest taxi for this client
        graph.dijkstra(clientNode);

        // Output results, passing shop node information
        outputResults(clientNode, shopNode);
    }

    // Method to output results
    private static void outputResults(String clientNode, String shopNode) {
        Vertex clientVertex = graph.getVertex(clientNode);
    
        // Output client information
        System.out.println("client " + clientNode);
    
        // Output taxi details
        List<String> taxiDetails = outputTaxiDetails(clientVertex);
    
        // Output shop details
        List<String> shopDetails = outputShopDetails(clientVertex, shopNode);
    
        // Output details only if the client can be helped
        if (!taxiDetails.isEmpty() || !shopDetails.isEmpty() ||numberOfShops ==0) {
            for (String details : taxiDetails) {
                System.out.println(details);
            }
            for (String details : shopDetails) {
                System.out.println(details);
            }
        } else {
            System.out.println("cannot be helped");
        }

    }

    // Method to output taxi details
    private static List<String> outputTaxiDetails(Vertex clientVertex) {
        List<String> taxiDetails = new ArrayList<>();
        Collection<Vertex> allVertices = graph.getVertexMap().values();

        // Output details of taxis with minimum cost
        for (Vertex vertex : allVertices) {
            if (vertex.prev != null) {
                List<String> pathNodes = new ArrayList<>();
                Vertex currentVertex = vertex;
                while (currentVertex != null) {
                    pathNodes.add(currentVertex.name);
                    currentVertex = currentVertex.prev;
                }
                taxiDetails.add("taxi " + vertex.name + "\n" + String.join(" ", pathNodes));
            }
        }

        return taxiDetails;
    }

    // Method to output shop details
    private static List<String> outputShopDetails(Vertex clientVertex, String shopNode) {
        List<String> shopDetails = new ArrayList<>();
        Vertex shopVertex = graph.getVertex(shopNode);
    
        // Check if there is a path from the client to the shop
        if (shopVertex.prev != null) {
            // Find the shortest path from the client to the shop
            List<String> pathNodes = new ArrayList<>();
            Vertex currentVertex = clientVertex;
            while (currentVertex != null) {
                pathNodes.add(currentVertex.name);
                currentVertex = currentVertex.prev;
            }
    
            // Add the path to the shop details
            shopDetails.add("shop " + shopNode + "\n" + String.join(" ", pathNodes) + ' ' + shopNode);
        }
    
        return shopDetails;
    }
    
}
