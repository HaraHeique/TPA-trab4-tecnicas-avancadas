/* Main.java
* UVa 00280 -- Vertex
* Autores: David Vilaça, Harã Heique e Larissa Motta
 */

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;

public class Main {

    private static final Scanner SC = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            int nVertices = SC.nextInt();
            ignoreLines(1);
            
            // Stops when find the 0 number of vertices
            if (nVertices == 0) {
                break;
            }

            // Creates a non bidirection graph and populates it
            Graph<Integer> graph = new Graph(false);
            populateGraph(graph, nVertices);

            // Do the analisys of the graph populated printing all the inacessible vertices
            identifyInacessibleVertices(graph);
        }
    }

    // Define the graphs nodes and edges using the generic class Graph<T>
    private static void populateGraph(Graph<Integer> graph, int qntVertices) {
        
        // First of all create the nodes/vertices before connect with the edges
        for (int i = 1; i <= qntVertices; i++) {
            graph.addVertex(i);
        }
        
        // Connect the edges
        while (true) {
            String[] groupVertices = SC.nextLine().split(" ");

            // Stops reading when find a zero line
            if (groupVertices.length <= 0 || groupVertices[0].charAt(0) == '0') {
                return;
            }

            // Takes the initial vertex
            int initialVertex = Integer.parseInt(groupVertices[0]);

            // Connects the vertices with the initial vertex
            for (int i = 1; i < groupVertices.length; i++) {
                int destVertex = Integer.parseInt(groupVertices[i]);

                if (destVertex == 0) {
                    break;
                }

                graph.addEdge(initialVertex, destVertex);
            }
        }
    }

    // Check and print the vertices inacessible from the one that is in analisys
    private static void identifyInacessibleVertices(Graph<Integer> graph) {
        String[] groupVerticesToCheck = SC.nextLine().split(" ");
        int nVerticesToCheck = Integer.parseInt(groupVerticesToCheck[0]);

        // Checking the vertices by the depth first search
        for (int i = 1; i <= nVerticesToCheck; i++) {
            int vertex = Integer.parseInt(groupVerticesToCheck[i]);
            
            // Get all the acessible vertices, but doesn't count the initial one
            Set<Integer> acessibleVertices = graph.depthFirstSearch(vertex, false);
            Set<Integer> inacessibleVertices = graph.getAllVertices();

            // Get the inacessible vertices by the difference between the two collections
            inacessibleVertices.removeAll(acessibleVertices);
            output(inacessibleVertices);
        }
    }

    private static void output(Set<Integer> elements) {
        StringBuilder builder = new StringBuilder();

        // Getting the count of inacessible vertices
        builder.append(elements.size());

        // Getting the inacessible vertices
        for (int el : elements) {
            builder.append(" ").append(el);
        }
        
        builder.append("\n");
        System.out.print(builder);
    }

    // Ignore the number of lines in terminal
    private static void ignoreLines(int numberOfLines) {
        for (int i = 0; i < numberOfLines; i++) {
            SC.nextLine();
        }
    }
}

// Graph implemented by an adjancency list way
class Graph<T> {

    private final HashMap<T, LinkedList<T>> edges;
    private final boolean bidirectional;

    public Graph(boolean hasBidirection) {
        this.edges = new HashMap();
        this.bidirectional = hasBidirection;
    }

    public void addVertex(T s) {
        edges.put(s, new LinkedList());
    }

    public void addEdge(T source, T destination) {
        if (!edges.containsKey(source)) {
            addVertex(source);
        }

        if (!edges.containsKey(destination)) {
            addVertex(destination);
        }

        edges.get(source).add(destination);

        if (this.bidirectional == true) {
            edges.get(destination).add(source);
        }
    }

    public int numberOfVertices() {
        return this.edges.size();
    }

    public int numberOfEdges() {
        int count = 0;

        for (T v : edges.keySet()) {
            count += edges.get(v).size();
        }

        if (this.bidirectional == true) {
            return count / 2;
        }

        return count;
    }

    public boolean hasVertex(T s) {
        return this.edges.containsKey(s);
    }

    public boolean hasEdge(T s, T d) {
        return this.edges.get(s).contains(d);
    }

    public Set<T> getAllVertices() {
        return new LinkedHashSet(this.edges.keySet());
    }

    public Set<T> depthFirstSearch(T vertex, boolean countInitialVertex) {
        Set<T> dfsVisitedElements = new LinkedHashSet();
        
        // Calls the function to get all visited vertices without counting the first one
        if (this.hasVertex(vertex)) {
            DFS_execution(vertex, dfsVisitedElements, countInitialVertex);
        }

        return dfsVisitedElements;
    }

    private void DFS_execution(T visitedVertex, Set<T> dfsVisitedVertices, boolean addVisitedVertex) {
        if (visitedVertex == null) {
            return;
        }

        // Adds into the set as a visited vertex
        if (addVisitedVertex) { dfsVisitedVertices.add(visitedVertex); }

        LinkedList<T> adjacencyVertices = this.edges.get(visitedVertex);
        for (T nextVisitedVertex : adjacencyVertices) {

            // If the vertex is already in the Set is because is not necessary to call again
            if (dfsVisitedVertices.contains(nextVisitedVertex)) {
                continue;
            }

            DFS_execution(nextVisitedVertex, dfsVisitedVertices, true);
        }
    }

    // Returns the adjancency list of each vertex
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (T v : edges.keySet()) {
            builder.append(v.toString()).append("-> ");

            for (T w : edges.get(v)) {
                builder.append(w.toString()).append("-> ");
            }

            builder.append("null\n");
        }

        return builder.toString();
    }
}
