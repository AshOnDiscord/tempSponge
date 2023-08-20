package util.Christofides;

public class Edge {
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
