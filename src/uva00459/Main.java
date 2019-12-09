/* Main.java
* UVa 00459 -- Graph Connectivity
* Autores: David Vilaça, Harã Heique e Larissa Motta
 */

package uva00459;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {

    private static final Scanner SC = new Scanner(System.in);

    public static void main(String[] args) {
        int nCases = SC.nextInt();
        ignoreLines(2);
        
        for (int i = 0; i < nCases; i++) {
            char largestNode = SC.nextLine().charAt(0);

            // Populates and connect the graph vertices
            Graph<Character> graph = new Graph(false);
            populateGraph(graph, largestNode);
            
            // Count the number of subgraphs
            int numberOfSubGraphs = graph.numberOfSubGraphsConnected();
            System.out.println(numberOfSubGraphs);
            
            // print the number of subgraphs
            if(i > 0){
                System.out.print('\n');
            }
            System.out.println(numberOfSubGraphs);
        }
    }

    // Define the graphs nodes and edges using the generic class Graph<T>
    private static void populateGraph(Graph<Character> graph, char largestLetter) {
        
        /* First of all create the nodes/vertices before connect with the edges 
           getting from the first letter (A) to the end letter (largest letter in input)
        */
        graph.addVertex(generateAlphabet('A', largestLetter));
        
        // Connect the edges
        while (true) {
            String edgesConnection = SC.nextLine();

            if (edgesConnection.isEmpty()) {
                break;
            }

            // Connect them
            graph.addEdge(edgesConnection.charAt(0), edgesConnection.charAt(1));
        }
    }
    
    private static List<Character> generateAlphabet(char start, char end) {
        List<Character> lstChars = new LinkedList();
        
        for (char c = start; c <= end; c++) {
            lstChars.add(c);
        }
        
        return lstChars;
    }

    // Ignore the number of lines in terminal
    private static void ignoreLines(int numberOfLines) {
        for (int i = 0; i < numberOfLines; i++) {
            SC.nextLine();
        }
    }
}

// Graph implemented by an adjancency list where T is a node/vertex
class Graph<T> {

    private final HashMap<T, LinkedList<T>> edges;
    private final boolean isDirectional;

    public Graph(boolean isDirecional) {
        this.edges = new HashMap();
        this.isDirectional = isDirecional;
    }

    public void addVertex(T v) {
        edges.put(v, new LinkedList());
    }
    
    public void addVertex(List<T> vs) {
        for (T v : vs) {
            edges.put(v, new LinkedList());
        }
    }

    public void addEdge(T source, T destination) {
        if (!edges.containsKey(source)) {
            addVertex(source);
        }

        if (!edges.containsKey(destination)) {
            addVertex(destination);
        }

        edges.get(source).add(destination);

        if (!this.isDirectional == true) {
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

        if (!this.isDirectional == true) {
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
    
    public int numberOfSubGraphsConnected() {
        List<Set<T>> subGraphs = new LinkedList();
        
        for (T vertex : this.getAllVertices()) {
            
            // Check if the vertex is in some subgraph
            boolean exists = false;
            for (Set<T> subgraph : subGraphs) {
                if (subgraph.contains(vertex)) {
                    exists = true;
                    break;
                }
            }
            
            // If exists pass to the next vertex to check
            if (exists) {
                continue;
            }
            
            // Uses the DFS to captures the subgraphs from the current graph
            Set<T> visitedVertices = this.depthFirstSearch(vertex, true);
            subGraphs.add(visitedVertices);
        }
        
        return subGraphs.size();
    }
    
    // Returns the adjancency list of each vertex
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        edges.keySet().stream().map((v) -> {
            builder.append(v.toString()).append("-> ");
            return v;
        }).map((v) -> {
            edges.get(v).forEach((w) -> {
                builder.append(w.toString()).append("-> ");
            });
            return v;
        }).forEachOrdered((_item) -> {
            builder.append("null\n");
        });

        return builder.toString();
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
}
