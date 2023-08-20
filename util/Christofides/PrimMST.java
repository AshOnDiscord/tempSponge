package util.Christofides;

import java.util.Arrays;

public class PrimMST {

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
