
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.Map;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

// Used to signal violations of preconditions for
// various shortest path algorithms.
class GraphException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public GraphException(String name) {
        super(name);
    }
}

// Represents an edge in the graph.
class Edge {
    public Vertex dest; // Second vertex in Edge
    public double cost; // Edge cost

    public Edge(Vertex d, double c) {
        dest = d;
        cost = c;
    }
}

// Represents an entry in the priority queue for Dijkstra's algorithm.
class DijkstraPath implements Comparable<DijkstraPath> {
    public Vertex dest; // w
    public double cost; // d(w)

    public DijkstraPath(Vertex d, double c) {
        dest = d;
        cost = c;
    }

    public int compareTo(DijkstraPath rhs) {
        double otherCost = rhs.cost;

        return cost < otherCost ? -1 : cost > otherCost ? 1 : 0;
    }
}

// Represents a vertex in the graph.
class Vertex {
    public String name; // Vertex name
    public List<Edge> adj; // Adjacent vertices
    public double dist; // Cost
    public Vertex prev; // Previous vertex on shortest path
    public int scratch;// Extra variable used in algorithm

    public Vertex(String nm) {
        name = nm;
        adj = new LinkedList<Edge>();
        reset();
    }

    public void reset()
    // { dist = Graph.INFINITY; prev = null; pos = null; scratch = 0; }
    {
        dist = Graph.INFINITY;
        prev = null;
        scratch = 0;
    }

    // public PairingHeap.Position<Path> pos; // Used for dijkstra2 (Chapter 23)
}

// Graph class: evaluate shortest paths.
//
// CONSTRUCTION: with no parameters.
//
// ******************PUBLIC OPERATIONS**********************
// void addEdge( String v, String w, double cvw )
// --> Add additional edge
// void printPath( String w ) --> Print path after alg is run
// void unweighted( String s ) --> Single-source unweighted
// void dijkstra( String s ) --> Single-source weighted
// void negative( String s ) --> Single-source negative weighted
// void acyclic( String s ) --> Single-source acyclic
// ******************ERRORS*********************************
// Some error checking is performed to make sure graph is ok,
// and to make sure graph satisfies properties needed by each
// algorithm. Exceptions are thrown if errors are detected.

public class Graph {
    public static final double INFINITY = Double.MAX_VALUE;
    private Map<String, Vertex> vertexMap = new HashMap<String, Vertex>();

    /**
     * Add a new edge to the graph.
     */
    public void addEdge(String sourceName, String destName, double cost) {
        Vertex v = getVertex(sourceName);
        Vertex w = getVertex(destName);
        v.adj.add(new Edge(w, cost));
    }

    /**
     * Driver routine to handle unreachables and print the total cost.
     * It calls recursive routine to print shortest path to
     * destNode after a shortest path algorithm has run.
     */
    public void printPath(String destName) {
        this.printPath(destName, null);
    }

    /**
     * Driver routine to handle unreachables and output total cost.
     * It calls recursive routine to print shortest path to
     * destNode after a shortest path algorithm has run.
     * 
     * If output is null, the output is printed. Otherwise, the result is appended
     * to output.
     */
    public void printPath(String destName, StringBuilder output) {
        Vertex w = vertexMap.get(destName);
        if (w == null)
            throw new NoSuchElementException("Destination vertex not found");
        else if (w.dist == INFINITY) {
            if (output == null) {
                System.out.println(destName + " is unreachable");
            } else {
                output.append(destName + " is unreachable");
            }
        } else {
            if (output == null) {
                System.out.print("(Cost is: " + w.dist + ") ");
                printPath(w, output);
                System.out.println();
            } else {
                output.append("(Cost is: " + w.dist + ") ");
                printPath(w, output);
                output.append("\n");
            }
        }
    }

    /**
     * If vertexName is not present, add it to vertexMap.
     * In either case, return the Vertex.
     */
    public Vertex getVertex(String vertexName) {
        Vertex v = vertexMap.get(vertexName);
        if (v == null) {
            v = new Vertex(vertexName);
            vertexMap.put(vertexName, v);
        }
        return v;
    }

    /**
     * Recursive routine to print shortest path to dest
     * after running shortest path algorithm. The path
     * is known to exist.
     * 
     * If output is null, the output is printed. Otherwise, the result is appended
     * to output.
     */
    private void printPath(Vertex dest, StringBuilder output) {
        if (dest.prev != null) {
            printPath(dest.prev, output);
            if (output == null) {
                System.out.print(" to ");
            } else {
                output.append(" to ");
            }
        }
        if (output == null) {
            System.out.print(dest.name);
        } else {
            output.append(dest.name);
        }
    }

    /**
     * Initializes the vertex output info prior to running
     * any shortest path algorithm.
     */
    private void clearAll() {
        for (Vertex v : vertexMap.values())
            v.reset();
    }

    /**
     * Single-source unweighted shortest-path algorithm.
     */
    public void unweighted(String startName) {
        clearAll();

        Vertex start = vertexMap.get(startName);
        if (start == null)
            throw new NoSuchElementException("Start vertex not found");

        Queue<Vertex> q = new LinkedList<Vertex>();
        q.add(start);
        start.dist = 0;

        while (!q.isEmpty()) {
            Vertex v = q.remove();

            for (Edge e : v.adj) {
                Vertex w = e.dest;
                if (w.dist == INFINITY) {
                    w.dist = v.dist + 1;
                    w.prev = v;
                    q.add(w);
                }
            }
        }
    }

    /**
     * Single-source weighted shortest-path algorithm. (Dijkstra)
     * using priority queues based on the binary heap.
     */
    public void dijkstra(String startName) {
        dijkstra(startName, null);
    }

    /**
     * Single-source weighted shortest-path algorithm. (Dijkstra)
     * using priority queues based on the binary heap.
     * 
     * Any paths given in invalidPaths is not explored.
     */
    public void dijkstra(String startName, String[][] invalidPaths) {
        if (invalidPaths == null) {
            invalidPaths = new String[0][0];
        }
        String[] formattedInvalidPaths = new String[invalidPaths.length];
        for (int i = 0; i < invalidPaths.length; i++) {
            formattedInvalidPaths[i] = String.join(" to ", invalidPaths[i]);
        }
        PriorityQueue<DijkstraPath> pq = new PriorityQueue<DijkstraPath>();

        Vertex start = vertexMap.get(startName);
        if (start == null)
            throw new NoSuchElementException("Start vertex not found");

        clearAll();
        pq.add(new DijkstraPath(start, 0));
        start.dist = 0;

        int nodesSeen = 0;
        while (!pq.isEmpty() && nodesSeen < vertexMap.size()) {
            DijkstraPath vrec = pq.remove();
            Vertex v = vrec.dest;
            if (v.scratch != 0) // already processed v
                continue;

            v.scratch = 1;
            nodesSeen++;

            for (Edge e : v.adj) {
                Vertex w = e.dest;
                double cvw = e.cost;

                if (cvw < 0)
                    throw new GraphException("Graph has negative edges");

                if (w.dist > v.dist + cvw) {
                    w.dist = v.dist + cvw;
                    w.prev = v;
                    boolean isValid = true;
                    for (String invalidPath : formattedInvalidPaths) {
                        StringBuilder pathBuilder = new StringBuilder();
                        printPath(w, pathBuilder);
                        String path = pathBuilder.toString().trim();
                        if (invalidPath.equals(path)) {
                            isValid = false;
                            w.dist = Double.MAX_VALUE;
                            break;
                        }
                    }
                    if (isValid) {
                        pq.add(new DijkstraPath(w, w.dist));
                    }
                }
            }
        }
    }

    /**
     * Single-source negative-weighted shortest-path algorithm.
     * Bellman-Ford Algorithm
     */
    public void negative(String startName) {
        clearAll();

        Vertex start = vertexMap.get(startName);
        if (start == null)
            throw new NoSuchElementException("Start vertex not found");

        Queue<Vertex> q = new LinkedList<Vertex>();
        q.add(start);
        start.dist = 0;
        start.scratch++;

        while (!q.isEmpty()) {
            Vertex v = q.remove();
            if (v.scratch++ > 2 * vertexMap.size())
                throw new GraphException("Negative cycle detected");

            for (Edge e : v.adj) {
                Vertex w = e.dest;
                double cvw = e.cost;

                if (w.dist > v.dist + cvw) {
                    w.dist = v.dist + cvw;
                    w.prev = v;
                    // Enqueue only if not already on the queue
                    if (w.scratch++ % 2 == 0)
                        q.add(w);
                    else
                        w.scratch--; // undo the enqueue increment
                }
            }
        }
    }

    /**
     * Single-source negative-weighted acyclic-graph shortest-path algorithm.
     */
    public void acyclic(String startName) {
        Vertex start = vertexMap.get(startName);
        if (start == null)
            throw new NoSuchElementException("Start vertex not found");

        clearAll();
        Queue<Vertex> q = new LinkedList<Vertex>();
        start.dist = 0;

        // Compute the indegrees
        Collection<Vertex> vertexSet = vertexMap.values();
        for (Vertex v : vertexSet)
            for (Edge e : v.adj)
                e.dest.scratch++;

        // Enqueue vertices of indegree zero
        for (Vertex v : vertexSet)
            if (v.scratch == 0)
                q.add(v);

        int iterations;
        for (iterations = 0; !q.isEmpty(); iterations++) {
            Vertex v = q.remove();

            for (Edge e : v.adj) {
                Vertex w = e.dest;
                double cvw = e.cost;

                if (--w.scratch == 0)
                    q.add(w);

                if (v.dist == INFINITY)
                    continue;

                if (w.dist > v.dist + cvw) {
                    w.dist = v.dist + cvw;
                    w.prev = v;
                }
            }
        }

        if (iterations != vertexMap.size())
            throw new GraphException("Graph has a cycle!");
    }
}