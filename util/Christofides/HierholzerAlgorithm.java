package util.Christofides;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class HierholzerAlgorithm {

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