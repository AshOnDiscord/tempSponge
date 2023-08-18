package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class christofides {

  public static List<Integer> christofidesTSP(double[][] graph) {
    int n = graph.length;

    // Step 1: Create minimum spanning tree (MST)
    double[][] mstGraph = PrimMST.primMST(graph);

    // Step 2: Find odd-degree vertices
    List<Integer> oddVertices = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      int degree = 0;
      for (int j = 0; j < n; j++) {
        if (mstGraph[i][j] > 0) {
          degree++;
        }
      }
      if (degree % 2 != 0) {
        oddVertices.add(i);
      }
    }

    // Step 3: Find minimum-weight perfect matching on odd-degree vertices
    List<Edge> matchingEdges = MinWeightMatching.minWeightPerfectMatching(graph, oddVertices);

    // Step 4: Add matching edges to MST
    for (Edge edge : matchingEdges) {
      int u = edge.u;
      int v = edge.v;
      mstGraph[u][v] = graph[u][v];
      mstGraph[v][u] = graph[v][u];
    }

    // Step 5: Find Eulerian circuit in the augmented graph
    List<Integer> eulerianCircuit = HierholzerAlgorithm.findEulerianCircuit(mstGraph);

    // Step 6: Convert Eulerian circuit to Hamiltonian circuit
    List<Integer> hamiltonianCircuit = new ArrayList<>();
    boolean[] visited = new boolean[n];
    for (int node : eulerianCircuit) {
      if (!visited[node]) {
        hamiltonianCircuit.add(node);
        visited[node] = true;
      }
    }

    return hamiltonianCircuit;
  }

  // Example usage
  public static void main(String[] args) {
    double[][] graph = {
        { 0, 29, 20, 21 },
        { 29, 0, 15, 18 },
        { 20, 15, 0, 26 },
        { 21, 18, 26, 0 }
    };

    List<Integer> tspPath = christofidesTSP(graph);
    System.out.println("Approximate TSP Path: " + tspPath);
  }
}

class PrimMST {

  public static double[][] primMST(double[][] graph) {
    int n = graph.length;
    double[][] mst = new double[n][n]; // Minimum Spanning Tree

    boolean[] visited = new boolean[n];
    double[] key = new double[n];
    int[] parent = new int[n];

    Arrays.fill(key, Double.POSITIVE_INFINITY);
    key[0] = 0;
    parent[0] = -1;

    for (int i = 0; i < n - 1; i++) {
      int u = minKeyVertex(key, visited);
      visited[u] = true;

      for (int v = 0; v < n; v++) {
        if (graph[u][v] > 0 && !visited[v] && graph[u][v] < key[v]) {
          parent[v] = u;
          key[v] = graph[u][v];
        }
      }
    }

    for (int i = 1; i < n; i++) {
      mst[i][parent[i]] = mst[parent[i]][i] = graph[i][parent[i]];
    }

    return mst;
  }

  private static int minKeyVertex(double[] key, boolean[] visited) {
    double minKey = Double.POSITIVE_INFINITY;
    int minVertex = -1;

    for (int v = 0; v < key.length; v++) {
      if (!visited[v] && key[v] < minKey) {
        minKey = key[v];
        minVertex = v;
      }
    }

    return minVertex;
  }

  // Example usage
  public static void main(String[] args) {
    double[][] graph = {
        { 0, 29, 20, 21 },
        { 29, 0, 15, 18 },
        { 20, 15, 0, 26 },
        { 21, 18, 26, 0 }
    };

    double[][] mst = primMST(graph);
    for (double[] row : mst) {
      System.out.println(Arrays.toString(row));
    }
  }
}

class MinWeightMatching {

  public static List<Edge> minWeightPerfectMatching(double[][] graph, List<Integer> vertices) {
    List<Edge> matchingEdges = new ArrayList<>();

    for (int u : vertices) {
      if (u % 2 == 0) { // Only consider even-degree vertices for matching
        double minWeight = Double.POSITIVE_INFINITY;
        int minVertex = -1;

        for (int v : vertices) {
          if (v % 2 != 0 && u != v && graph[u][v] < minWeight) {
            minWeight = graph[u][v];
            minVertex = v;
          }
        }

        matchingEdges.add(new Edge(u, minVertex, minWeight));
      }
    }

    return matchingEdges;
  }

  // Example usage
  public static void main(String[] args) {
    double[][] graph = {
        { 0, 3, 5, 7 },
        { 3, 0, 6, 8 },
        { 5, 6, 0, 9 },
        { 7, 8, 9, 0 }
    };

    List<Integer> oddVertices = Arrays.asList(1, 2, 3);
    List<Edge> matchingEdges = minWeightPerfectMatching(graph, oddVertices);
    for (Edge edge : matchingEdges) {
      System.out.println(edge);
    }
  }
}

class HierholzerAlgorithm {

  public static List<Integer> findEulerianCircuit(double[][] graph) {
    List<Integer> circuit = new ArrayList<>();
    int n = graph.length;
    if (n == 0)
      return circuit;

    Stack<Integer> stack = new Stack<>();
    int currentVertex = 0; // Start from vertex 0

    stack.push(currentVertex);

    while (!stack.isEmpty()) {
      if (hasUnvisitedEdges(graph[currentVertex])) {
        stack.push(currentVertex);

        int nextVertex = getNextUnvisitedVertex(graph[currentVertex]);
        graph[currentVertex][nextVertex]--;
        graph[nextVertex][currentVertex]--;
        currentVertex = nextVertex;
      } else {
        circuit.add(currentVertex);
        currentVertex = stack.pop();
      }
    }

    Collections.reverse(circuit);
    return circuit;
  }

  private static boolean hasUnvisitedEdges(double[] edges) {
    for (double edge : edges) {
      if (edge > 0) {
        return true;
      }
    }
    return false;
  }

  private static int getNextUnvisitedVertex(double[] edges) {
    for (int i = 0; i < edges.length; i++) {
      if (edges[i] > 0) {
        return i;
      }
    }
    return -1; // No unvisited vertex found
  }

  // Example usage
  public static void main(String[] args) {
    double[][] graph = {
        { 0, 1, 1, 0 },
        { 1, 0, 1, 1 },
        { 1, 1, 0, 1 },
        { 0, 1, 1, 0 }
    };

    List<Integer> eulerianCircuit = findEulerianCircuit(graph);
    System.out.println("Eulerian Circuit: " + eulerianCircuit);
  }
}

class Edge {
  int u, v;
  double weight;

  Edge(int u, int v, double weight) {
    this.u = u;
    this.v = v;
    this.weight = weight;
  }

  @Override
  public String toString() {
    return u + " - " + v + " (Weight: " + weight + ")";
  }
}
