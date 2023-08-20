package util.Christofides;

import java.util.ArrayList;
import java.util.List;
import util.Address;

public class Christofides {
  public static List<Address> addressWrapper(List<Address> addresses) {
    double[][] points = new double[addresses.size()][2];
    for (int i = 0; i < addresses.size(); i++) {
      points[i][0] = (double) addresses.get(i).blockX;
      points[i][1] = (double) addresses.get(i).blockY;
    }
    // christofides works off a weight(distance) matrix
    // we can just do abs(x1-x2) + abs(y1-y2)
    double[][] graph = new double[addresses.size()][addresses.size()];
    for (int i = 0; i < addresses.size(); i++) {
      for (int j = 0; j < addresses.size(); j++) {
        graph[i][j] = Math.abs(points[i][0] - points[j][0]) + Math.abs(points[i][1] - points[j][1]);
      }
    }
    // List<Integer> order = christofides.christofidesTSP(points);
    List<Integer> order = Christofides.christofidesTSP(graph);
    List<Address> path = new ArrayList<Address>();
    for (int i = 0; i < order.size(); i++) {
      path.add(addresses.get(order.get(i)));
    }
    return path;
  }

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