/* Main.java
* UVa 00872 -- Ordering
* Autores: David Vilaça, Harã Heique e Larissa Motta
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {

    private static final Scanner SC = new Scanner(System.in);
    private static final String REGEX = " ";

    public static void main(String[] args) throws Exception {
        int nCases = SC.nextInt();
        ignoreLines(2);

        for (int i = 0; i < nCases; i++) {
            // Populates the graph and reads the variables and constraints
            Graph<String> graph = new Graph(false);
            String[] constraints = readVariablesAndConstraints(graph);

            // Get all the permutations from the graph
            List<ArrayList<String>> permutations = graph.permutations(constraints);
            output(permutations);

            if (i < nCases - 1) {
                System.out.println();
            }
        }
    }

    // Reads the variable and constraints of the given input and returns the constraints
    private static String[] readVariablesAndConstraints(Graph<String> graph) throws Exception {
        String[] variables = SC.nextLine().split(REGEX);
        String[] constraints = SC.nextLine().split(REGEX);

        // Reads the empty line
        if (SC.hasNextLine()) {
            SC.nextLine();
        }
        
        // Validating the rules in the given problem description
        if (variables.length < 2 || variables.length > 20) {
            throw new Exception("The numbers of variables must be greater than or "
                    + "equal to 2 and less than or equal to 20.");
        }

        if (constraints.length < 1 || constraints.length > 50) {
            throw new Exception("The numbers of constraints must be greater than or "
                    + "equal to 1 and less than or equal to 50.");
        }

        // Populates the graph with the variables as vertices
        graph.addVertex(variables);

        return constraints;
    }

    // Ignore the number of lines in terminal
    private static void ignoreLines(int numberOfLines) {
        for (int i = 0; i < numberOfLines; i++) {
            SC.nextLine();
        }
    }

    private static void output(List<ArrayList<String>> permutations) {
        if (permutations.isEmpty()) {
            System.out.println("NO");
        }

        List<String> lstStrPerm = new ArrayList();

        // Converting the List of string to a List of strings
        for (List<String> perm : permutations) {
            lstStrPerm.add(perm.stream().map(String::valueOf).collect(Collectors.joining(REGEX)));
        }

        // After this sort the list of permutations and then print them out
        Collections.sort(lstStrPerm);
        for (int i = 0; i < lstStrPerm.size(); i++) {
            System.out.println(lstStrPerm.get(i));
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
        this.edges.put(v, new LinkedList());
    }

    public void addVertex(T[] vs) {
        for (T v : vs) {
            this.edges.put(v, new LinkedList());
        }
    }

    public void addEdge(T source, T destination) {
        if (this.hasEdge(source, destination)) {
            return;
        }

        if (!this.edges.containsKey(source)) {
            this.addVertex(source);
        }

        if (!this.edges.containsKey(destination)) {
            this.addVertex(destination);
        }

        this.edges.get(source).add(destination);

        if (!this.isDirectional == true) {
            this.edges.get(destination).add(source);
        }
    }

    public int numberOfVertices() {
        return this.edges.size();
    }

    public int numberOfEdges() {
        int count = 0;

        for (T v : this.edges.keySet()) {
            count += this.edges.get(v).size();
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

    public List<ArrayList<T>> permutations(String constraints[]) throws Exception {
        ArrayList<T> vertices = new ArrayList(this.edges.keySet());
        List<ArrayList<T>> result = doPermutation(vertices);
        
        // Filter the permutations by the constraint given
        this.filterBasedOnConstraints(result, constraints);
        if (result.size() > 300) {
            throw new Exception("The numbers of orderings consistent with the "
                                + "constraints must be less than or equal to 300.");
        }
        
        return result;
    }

    private List<ArrayList<T>> doPermutation(ArrayList<T> lstPermute) {
        List<ArrayList<T>> structurePermute = new ArrayList();

        // Check the bases of the collection (zero or one element)
        if (lstPermute.isEmpty()) {
            return structurePermute;
        }

        if (lstPermute.size() == 1) {
            structurePermute.add(lstPermute);
            return structurePermute;
        }

        // Check for more than one element
        for (int i = 0; i < lstPermute.size(); i++) {
            T firstElementPermute = lstPermute.get(i);

            // Retrieve all the list except the first element of permutation
            ArrayList<T> lstRetrieve = (ArrayList<T>) lstPermute.clone();
            lstRetrieve.remove(i);

            // Generating all permutations from the retrieve list
            for (ArrayList<T> lstCombination : this.doPermutation(lstRetrieve)) {
                // firstElementPermute is always the first element of the current permutation
                lstCombination.add(0, firstElementPermute);
                structurePermute.add(lstCombination);
            }
        }

        return structurePermute;
    }

    private void filterBasedOnConstraints(List<ArrayList<T>> permutations, String[] constraints) {
        if (constraints.length == 0) {
            return;
        }

        /* For each constraint check if filter the permutations wich is not 
           following the rules of the constraint
         */
        List<ArrayList<T>> removePermutations = new ArrayList();
        for (String constraint : constraints) {
            String var1 = String.valueOf(constraint.charAt(0));
            String var2 = String.valueOf(constraint.charAt(2));

            for (ArrayList<T> lstPermut : permutations) {

                // Get the index of the variables inside the list of permutations
                int indexVar1 = lstPermut.indexOf(var1);
                int indexVar2 = lstPermut.indexOf(var2);

                if (indexVar1 == -1 || indexVar2 == -1) {
                    continue;
                }

                /* Always var1 < var2, so if index of var1 > index of var2 adds 
                   adds inside the removePermutations list
                 */
                if (indexVar1 > indexVar2) {
                    removePermutations.add(lstPermut);
                }
            }
        }

        // Remove all failed permutations according to the constraints given
        permutations.removeAll(removePermutations);
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
}
