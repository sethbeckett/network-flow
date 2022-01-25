import java.io.File;
import java.util.Scanner;
public class Graph {
    int vertexCt;  // Number of vertices in the graph.
    int[][] capacity;  // Adjacency  matrix
    String graphName;  //The file from which the graph was created.
    int[][] residual; // Residual Adjacency Matrix
    boolean[] visited; // Array to keep track of which vertices have been visited
    int[] pred; // Array to keep track of each vertex's predecessor

    public Graph(String filename) {
        this.vertexCt = 0;
        this.graphName = filename;
        makeGraph();
    }

    public int getVertexCt() {
        return vertexCt;
    }
    public boolean addEdge(int source, int destination, int cap) {
        // System.out.println("addEdge " + source + "->" + destination + "(" + cap +")");
        if (source < 0 || source >= vertexCt) return false;
        if (destination < 0 || destination >= vertexCt) return false;
        capacity[source][destination] = cap;
        residual[source][destination] = cap; //creates first state of residual graph
        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nThe Graph " + graphName + " \n");
        for (int i = 0; i < vertexCt; i++) {
            for (int j = 0; j < vertexCt; j++) {
                sb.append(String.format("%5d", capacity[i][j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public void makeGraph() {
        try {
            graphName = graphName;
            Scanner reader = new Scanner(new File(graphName));

            vertexCt = reader.nextInt();
            capacity = new int[vertexCt][vertexCt];
            residual = new int[vertexCt][vertexCt];
            visited = new boolean[vertexCt];
            pred = new int[vertexCt];

            for (int i = 0; i < vertexCt; i++) {
                visited[i] = false;
                for (int j = 0; j < vertexCt; j++) {
                    capacity[i][j] = 0;
                    residual[i][j] = 0;
                }
            }
            while (reader.hasNextInt()) {
                int v1 = reader.nextInt();
                int v2 = reader.nextInt();
                int cap = reader.nextInt();
                if (!addEdge(v1, v2, cap))
                    throw new Exception();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * makes visited array completely false
     */
    public void clearVisited() {
        for (int i = 0; i < vertexCt; i++) {
            visited[i] = false;
        }
    }

    /**
     * Finds the different paths, prints them, and prints final paths with their flow
     */
    public void maxFlow() {

        System.out.println("Paths found in order");
        while (hasAugmentingPath(0, vertexCt - 1)) {
            int pathFlow = updateResidual(); // loops through edges in path to find minCap along path, then updates the residual matrix (including backwards paths) based on minCap
            printPath(pathFlow); // prints the path and its flow
        }

        System.out.println("\nFinal paths and flows");
        printFinalFlows(); // prints all of the final paths and their flows
    }

    /**
     * prints the end result of each path's flow
     */
    private void printFinalFlows() {
        for (int v = 0; v < vertexCt; v++) {
            for (int w = 0; w < vertexCt; w++) {
                if ((capacity[v][w] > 0) && (residual[w][v] != 0)) {
                    System.out.println("Flow " + v + "->" + w + " (" + residual[w][v] + ")" );
                }
            }
        }
    }

    /**
     * prints the current path determined by the predecessors of the target/sink
     * @param flow the amount of flow found along that path
     */
    private void printPath(int flow) {
        int curVertex = vertexCt - 1;
        String path = "";
        while (curVertex > 0) {
            path = curVertex + " " + path;
            curVertex = pred[curVertex];
        }
        path = "Path 0 " + path + "(flow " + flow + ")";
        System.out.println(path); //might have to use formatted string for path
    }

    /**
     * updates the residual graph based on the current predecessor array
     * @return the flow along the most recent path found
     */
    private int updateResidual() {
        int curVertex = vertexCt - 1; // start vertex is the sink
        int minCap = residual[pred[curVertex]][curVertex]; // starting minimum capacity is the minimum capacity at the last edge of path

        while (curVertex != 0) {
            int prevVertex = pred[curVertex];
            if (residual[prevVertex][curVertex] < minCap) {
                minCap = residual[prevVertex][curVertex];
            }
            curVertex = prevVertex;
        }

        curVertex = vertexCt - 1; // reset current vertex to sink
        while (curVertex != 0) {
            int prevVertex = pred[curVertex];
            residual[prevVertex][curVertex] -= minCap; // flow is going along edge
            residual[curVertex][prevVertex] += minCap; // update backward flow potential
            curVertex = prevVertex;
        }

        return minCap;
    }

    /**
     * updates the adjacency matrix with path flows
     * @param source the source vertex
     * @param target the sink vertex
     * @return true if there is a path from s to t in the residual matrix, else false
     */
    private boolean hasAugmentingPath(int source, int target) {
        Queue<Integer> queue = new Queue<Integer>();
        queue.enqueue(source);
        clearVisited();
        visited[source] = true;

        while (!queue.isEmpty() && !visited[target]) {
            int v = queue.dequeue();

            for (int w = 0; w < vertexCt; w++) // goes through all potential edges from v
                if (residual[v][w] > 0 && !visited[w]) { // makes sure there is potential flow and the vertex isn't redundant
                    pred[w] = v;   // Remembers Path
                    visited[w] = true;
                    queue.enqueue(w);
                    if(w==target) return true;
                }
            }
        return false;
    }

    public static void main(String[] args) {
        Graph graph0 = new Graph("match0.txt");
        System.out.println(graph0.toString());
        graph0.maxFlow();
        Graph graph1 = new Graph("match1.txt");
        System.out.println(graph1.toString());
        graph1.maxFlow();
        Graph graph2 = new Graph("match2.txt");
        System.out.println(graph2.toString());
        graph2.maxFlow();
        Graph graph3 = new Graph("match3.txt");
        System.out.println(graph3.toString());
        graph3.maxFlow();
    }
}