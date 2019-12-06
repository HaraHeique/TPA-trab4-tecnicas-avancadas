/* Main.java
* UVa 10034 -- Freckles
* Autores: David Vilaça, Harã Heique e Larissa Motta
 */
package uva10034;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {

    private static final Scanner SC = new Scanner(System.in);
    private static final String REGEX = " ";

    public static void main(String args[]) {
        int nCases = SC.nextInt();
        ignoreLines(2); // /n input and the blank line

        while (nCases-- > 0) {
            int nFreckles = SC.nextInt();
            ignoreLines(1);

            // Creates a non direction graph of freckles
            Graph<Freckle> frecklesGraph = new Graph(false);
            
            /* Connect the edges of all the vertices connecting all the freckles 
               and calculating the weight by the euclidian distance
            */
            List<Freckle> freckles = identifyAndConnectFreckles(frecklesGraph, nFreckles);
            ignoreLines(1); // Ignore the next blank line
            
            // Finds the minimum cost of ink to connect all the lines
            if (!freckles.isEmpty()) {
                Freckle initialFreckle = freckles.get(0);
                double cost = frecklesGraph.minimumCostConnectAllVertices(initialFreckle);
                
                System.out.printf("%.2f\n\n", cost);
            }
        }
    }

    /* Get all freckles from the current case and creates it populating the graph 
       and then connect to all other freckles inside the graph
    */
    private static List<Freckle> identifyAndConnectFreckles(Graph<Freckle> graph, int qntFreckles) {
        List<Freckle> frecklesAux = new ArrayList();
        
        for (int i = 1; i <= qntFreckles; i++) {
            String[] coordinates = SC.nextLine().split(REGEX);
            double x = Double.parseDouble(coordinates[0]);
            double y = Double.parseDouble(coordinates[1]);

            // Creates a freckle where the i is his identifier then adds into the graph
            Freckle currentFreckle = new Freckle(i, x, y);
            graph.addVertex(currentFreckle);
            
            // Adds into freckles list to make the connections among the other freckles
            frecklesAux.add(currentFreckle);
            
            // Connects the current freckle to all the others already create it
            for (int k = frecklesAux.size() - 2; k >= 0; k--) {
                Freckle destFreckle = frecklesAux.get(k);
                double weight = currentFreckle.euclidianDistance(destFreckle);
                graph.addEdge(currentFreckle, destFreckle, weight);
            }
        }
        
        return frecklesAux;
    }

    // Ignore the number of lines in terminal
    private static void ignoreLines(int numberOfLines) {
        for (int i = 0; i < numberOfLines; i++) {
            SC.nextLine();
        }
    }
}

// Simple representation of the freckles coordinates
class Freckle {

    private int id;
    public double x;
    public double y;

    public Freckle(int identifier, double x, double y) {
        this.id = identifier;
        this.x = x;
        this.y = y;
    }

    public int checkId() {
        return this.id;
    }
    
    public double euclidianDistance(Freckle f) {
        double deltaX = f.x - this.x;
        double deltaY = f.y - this.y;
        
        return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + this.id;
        
        return hash;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null || this == null || this.getClass() != o.getClass()) {
            return false;
        }

        Freckle f = (Freckle) o;

        return this.id == f.id;
    }
    
    @Override
    public String toString() {
        return "" + this.id;
    }
}

// Graph implemented by an adjancency list way
class Graph<T> {

    private final static class Edge<T> {

        private T srcVertex;
        private T destVertex;
        private double weight;

        public Edge(T src, T dest) {
            this.srcVertex = src;
            this.destVertex = dest;
            this.weight = 0d;
        }

        public Edge(T src, T dest, double weight) {
            this.srcVertex = src;
            this.destVertex = dest;
            this.weight = weight;
        }

        public void changeWeight(double weight) {
            this.weight = weight;
        }

        public void changeConnection(T src, T dest) {
            if (src != null) {
                this.srcVertex = src;
            }

            if (dest != null) {
                this.destVertex = dest;
            }
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 53 * hash + Objects.hashCode(this.srcVertex);
            hash = 53 * hash + Objects.hashCode(this.destVertex);

            return hash;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || this == null || this.getClass() != o.getClass()) {
                return false;
            }

            Edge<T> e = (Edge) o;

            return this.srcVertex.equals(e.srcVertex) && this.destVertex.equals(e.destVertex);
        }
    }

    private final HashMap<T, LinkedList<Edge<T>>> edges;
    private final boolean isDirectional;

    public Graph(boolean isDirectional) {
        this.edges = new HashMap();
        this.isDirectional = isDirectional;
    }

    public void addVertex(T v) {
        edges.put(v, new LinkedList());
    }

    public void addEdge(T source, T destination, double weight) {
        if (!edges.containsKey(source)) {
            addVertex(source);
        }

        if (!edges.containsKey(destination)) {
            addVertex(destination);
        }

        Edge<T> srcEdge = new Edge(source, destination, weight);
        this.edges.get(source).add(srcEdge);

        if (!this.isDirectional) {
            Edge<T> destEdge = new Edge(destination, source, weight);
            this.edges.get(destination).add(destEdge);
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

        if (!this.isDirectional) {
            return count / 2;
        }

        return count;
    }

    public boolean hasVertex(T v) {
        return this.edges.containsKey(v);
    }

    public boolean hasEdge(T s, T d) {
        Edge<T> edge = new Edge(s, d);

        return this.edges.get(s).contains(edge);
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
    
    // Returns the minimum sum of weight edge to connect all vertices
    public double minimumCostConnectAllVertices(T InitialVertex) {
        // Uses the dikjstra approach to take the result
        
        int qntVertices = this.numberOfVertices();
        Set<T> vertices = this.edges.keySet();
        
        /* Distance of all vertices where T is the vertex and initialize 
           all of them as infinite
        */
        HashMap<T, Double> distanceMap = new HashMap();
        
        for (T v : vertices) {
            distanceMap.put(v, Double.POSITIVE_INFINITY);
        }
        distanceMap.put(InitialVertex, 0d);
        
        // A Set of object that has been selected as a short path based on edge weight
        HashSet<T> closed = new HashSet();
        
        double totalSum = 0d;
        T analysingVertex = InitialVertex;
        
        while (!closed.contains(analysingVertex)) {
            // Adds into closed wich means there will not be analysed anymore
            totalSum += distanceMap.get(analysingVertex);
            closed.add(analysingVertex);
            
            if (closed.size() == qntVertices) {
                break;
            }
            
            // Update the weight of the vertex adjacent
            for (Edge<T> edgeAdjacent : this.edges.get(analysingVertex)) {
                T adjacentVertex = edgeAdjacent.destVertex;
                
                // Is not necessary to update distance if the vertex is visited
                if (closed.contains(adjacentVertex)) {
                    continue;
                }
                
                double adjcentVertexWeight = distanceMap.get(adjacentVertex);
                
                if (adjcentVertexWeight > edgeAdjacent.weight) {
                    distanceMap.put(adjacentVertex, edgeAdjacent.weight);
                }
            }
            
            // Take the next vertex to analyze based on his weight edge
            double minWeight = Double.POSITIVE_INFINITY;
            for (T adjacentVertex: distanceMap.keySet()) {
                
                // Is not necessary to update distance if the vertex is visited
                if (closed.contains(adjacentVertex)) {
                    continue;
                }
                
                double adjcentVertexWeight = distanceMap.get(adjacentVertex);
                
                if (minWeight > adjcentVertexWeight) {
                    minWeight = adjcentVertexWeight;
                    analysingVertex = adjacentVertex;
                }
            }
        }
        //double totalSum = distanceMap.values().stream().mapToDouble(i -> i).sum();
        
        return totalSum;
    }

    // Returns the adjancency list of each vertex
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (T v : this.edges.keySet()) {
            builder.append(v.toString()).append("-> ");

            for (T w : this.getAdjacencyVertices(v)) {
                builder.append(w.toString()).append("-> ");
            }

            builder.append("null\n");
        }

        return builder.toString();
    }

    private void DFS_execution(T visitedVertex, Set<T> dfsVisitedVertices, boolean addVisitedVertex) {
        if (visitedVertex == null) {
            return;
        }

        // Adds into the set as a visited vertex
        if (addVisitedVertex) {
            dfsVisitedVertices.add(visitedVertex);
        }

        List<T> adjacencyVertices = this.getAdjacencyVertices(visitedVertex);

        for (T nextVisitedVertex : adjacencyVertices) {
            // If the vertex is already in the Set is because is not necessary to call again
            if (dfsVisitedVertices.contains(nextVisitedVertex)) {
                continue;
            }

            DFS_execution(nextVisitedVertex, dfsVisitedVertices, true);
        }
    }

    /* Get all adjacency vertices from the list of edges of the current vertex, 
       wich means all the destination vertices 
     */
    private List<T> getAdjacencyVertices(T vertex) {

        return this.edges.get(vertex).stream().map(e -> e.destVertex).collect(Collectors.toList());
        //.collect(Collectors.toCollection(LinkedList::new));
    }
}
