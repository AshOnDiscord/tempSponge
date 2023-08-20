package util.Christofides;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MinWeightMatching {

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
