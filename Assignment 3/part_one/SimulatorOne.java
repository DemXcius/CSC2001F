import java.io.FileReader;
import java.io.IOException;
import java.util.*; 

class SimulatorOne {
    private static Graph graph = new Graph();

    public static void main(String[] args) {
        try {
            FileReader fileReader = new FileReader("input.txt"); // Assuming input file name is input.txt
            Scanner scanner = new Scanner(fileReader);

            // Read graph data
            readGraphData(scanner);

            // Read client calls and simulate taxi service
            simulateTaxiService(scanner);

            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readGraphData(Scanner scanner) {
        int numberOfNodes = Integer.parseInt(scanner.nextLine().trim());
        for (int i = 0; i < numberOfNodes; i++) {
            String[] parts = scanner.nextLine().trim().split("\\s+");
            String sourceNode = parts[0];
            for (int j = 1; j < parts.length; j += 2) {
                String destNode = parts[j];
                double weight = Double.parseDouble(parts[j + 1]);
                graph.addEdge(sourceNode, destNode, weight);
            }
        }

        int numberOfShops = Integer.parseInt(scanner.nextLine().trim());
        for (int i = 0; i < numberOfShops; i++) {
            String shopNode = scanner.nextLine().trim();
            // Add shop nodes to the graph (assuming they are not already present)
            graph.getVertex(shopNode);
        }
    }

    private static void simulateTaxiService(Scanner scanner) {
        int numberOfClients = Integer.parseInt(scanner.nextLine().trim());
        for (int i = 0; i < numberOfClients; i++) {
            String clientNode = scanner.nextLine().trim();
            // Find the nearest taxi and the nearest shop for this client
            processClientCall(clientNode);
        }
    }

    private static void processClientCall(String clientNode) {
        // Find the nearest taxi for this client
        graph.dijkstra(clientNode);

        // Find the nearest shop for this client
        Vertex clientVertex = graph.getVertex(clientNode);
        Vertex nearestShop = findNearestShop(clientVertex);

        // Output results
        System.out.println("client " + clientNode);
        outputTaxiDetails(clientVertex);
        outputShopDetails(nearestShop);
    }

    private static Vertex findNearestShop(Vertex clientVertex) {
        Collection<Vertex> allShops = graph.getVertexMap().values();
        Vertex nearestShop = null;
        double minDistance = Double.MAX_VALUE;

        for (Vertex shop : allShops) {
            if (shop.dist < minDistance) {
                minDistance = shop.dist;
                nearestShop = shop;
            }
        }

        return nearestShop;
    }

    private static void outputTaxiDetails(Vertex clientVertex) {
        List<String> taxiDetails = new ArrayList<>();
        Collection<Vertex> allVertices = graph.getVertexMap().values();

        for (Vertex vertex : allVertices) {
            if (vertex.prev == null || vertex.dist == Graph.INFINITY) {
                continue; // Taxi cannot reach client
            }

            List<String> pathNodes = new ArrayList<>();
            Vertex currentVertex = vertex;
            while (currentVertex != null) {
                pathNodes.add(currentVertex.name);
                currentVertex = currentVertex.prev;
            }
            Collections.reverse(pathNodes);

            taxiDetails.add("taxi " + vertex.name + "\n" + String.join(" ", pathNodes));
        }

        if (taxiDetails.isEmpty()) {
            System.out.println("cannot be helped");
        } else {
            taxiDetails.sort(Comparator.naturalOrder());
            for (String details : taxiDetails) {
                System.out.println(details);
            }
        }
    }

    private static void outputShopDetails(Vertex shop) {
        System.out.println("shop " + shop.name + "\n" + shop.name);
    }
}
