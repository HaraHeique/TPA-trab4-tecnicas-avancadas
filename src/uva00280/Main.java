/* Main.java
* UVa 00280 -- Vertex
* Autores: David Vilaça, Harã Heique e Larissa Motta
 */
package uva00280;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class Main {

    private static final Scanner SC = new Scanner(System.in);

    public static void main(String[] args) {
        int nVertices = SC.nextInt();
        ignoreLines(1);

        // Creates a non bidirection graph and populates it
        Graph<Integer> graph = new Graph(false);
        populateGraph(graph);

        // Do the analisys of the graph populated printing all the inacessible vertices
        identifyInacessibleVertices(graph);
    }

    // Define the graphs nodes and edges using the generic class Graph<T>
    private static void populateGraph(Graph<Integer> graph) {
        while (true) {
            String[] groupVertices = SC.nextLine().split(" ");

            // Stops reading when find a zero line
            if (groupVertices.length <= 0 || groupVertices[0].charAt(0) == '0') {
                return;
            }

            // Creates when necessary and connects the vertices
            for (int i = 0; i < groupVertices.length - 1; i++) {
                int destVertex = Integer.parseInt(groupVertices[i + 1]);

                if (destVertex == 0) {
                    break;
                }

                int sourceVertex = Integer.parseInt(groupVertices[i]);
                graph.addEdge(sourceVertex, destVertex);
            }
        }
    }

    // Check and print the vertices inacessible from the one that is in analisys
    private static void identifyInacessibleVertices(Graph<Integer> graph) {
        String[] groupVerticesToCheck = SC.nextLine().split(" ");

        if (groupVerticesToCheck.length <= 1 || groupVerticesToCheck[0].charAt(0) == '0') {
            return;
        }

        int nVerticesToCheck = Integer.parseInt(groupVerticesToCheck[0]);

        // Checking the vertices by the depth first search
        for (int i = 1; i <= nVerticesToCheck; i++) {
            int vertex = Integer.parseInt(groupVerticesToCheck[i]);

            if (graph.hasVertex(vertex)) {
                Set<Integer> acessibleVertices = graph.depthFirstSearch();
                Set<Integer> inacessibleVertices = graph.getAllVertices();

                // Get the inacessible vertices by the difference between the two collections
                if (inacessibleVertices.removeAll(acessibleVertices)) {
                    output(inacessibleVertices);
                }
            }
        }
    }

    private static void output(Set<Integer> elements) {
        StringBuilder builder = new StringBuilder();
        
        for (int el : elements) {
            builder.append(el).append(" ");
        }
        
        System.out.println(builder);
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
        return this.edges.keySet();
    }

    public Set<T> depthFirstSearch() {
        // TODO Lógica de busca em profundidade
        Set<T> dfsElements = new LinkedHashSet();
                
        return dfsElements;
    }

    // Prints the adjancency list of each vertex
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (T v : edges.keySet()) {
            builder.append(v.toString()).append("-> ");
            for (T w : edges.get(v)) {
                builder.append(w.toString()).append("-> ");
            }
            builder.append("null");
            builder.append("\n");
        }

        return builder.toString();
    }
}
