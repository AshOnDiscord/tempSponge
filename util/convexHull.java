package util;

// import util.Point;
import java.util.ArrayList;
import java.util.List;

public class convexHull {
  public static List<double[]> computeConvexHull(List<double[]> points) {
    double[] sp = points.get(0);

    // Find the "leftmost point"
    double[] leftmost = points.get(0);
    for (double[] p : points) {
      if (p[1] < leftmost[1]) {
        leftmost = p;
      }
    }

    List<double[]> path = new ArrayList<>();
    path.add(leftmost);

    while (true) {
      double[] curPoint = path.get(path.size() - 1);
      int selectedIdx = 0;
      double[] selectedPoint = null;

      // find the "most counterclockwise" point
      for (int idx = 0; idx < points.size(); idx++) {
        double[] p = points.get(idx);
        if (selectedPoint == null || orientation(curPoint, p, selectedPoint) == 2) {
          // this point is counterclockwise with respect to the current hull
          // and selected point (e.g. more counterclockwise)
          selectedIdx = idx;
          selectedPoint = p;
        }
      }

      // adding this to the hull so it's no longer available
      points.remove(selectedIdx);

      // back to the furthest left point, formed a cycle, break
      if (selectedPoint == leftmost) {
        break;
      }

      // add to hull
      path.add(selectedPoint);
    }

    while (!points.isEmpty()) {
      double bestRatio = Double.POSITIVE_INFINITY;
      int bestPointIdx = 0;
      int insertIdx = 0;

      for (int freeIdx = 0; freeIdx < points.size(); freeIdx++) {
        double[] freePoint = points.get(freeIdx);
        double bestCost = Double.POSITIVE_INFINITY;
        int bestIdx = 0;

        // for every free point, find the point in the current path
        // that minimizes the cost of adding the point minus the cost of
        // the original segment
        for (int pathIdx = 0; pathIdx < path.size(); pathIdx++) {
          double[] pathPoint = path.get(pathIdx);
          double[] nextPathPoint = path.get((pathIdx + 1) % path.size());

          // the new cost minus the old cost
          double evalCost = pathCost(new double[][] { pathPoint, freePoint, nextPathPoint }) -
              pathCost(new double[][] { pathPoint, nextPathPoint });

          if (evalCost < bestCost) {
            bestCost = evalCost;
            bestIdx = pathIdx;
          }
        }

        // figure out how "much" more expensive this is with respect to the
        // overall length of the segment
        double[] nextPoint = path.get((bestIdx + 1) % path.size());
        double prevCost = pathCost(new double[][] { path.get(bestIdx), nextPoint });
        double newCost = pathCost(new double[][] { path.get(bestIdx), freePoint, nextPoint });
        double ratio = newCost / prevCost;

        if (ratio < bestRatio) {
          bestRatio = ratio;
          bestPointIdx = freeIdx;
          insertIdx = bestIdx + 1;
        }
      }

      double[] nextPoint = points.remove(bestPointIdx);
      path.add(insertIdx, nextPoint);
    }

    // rotate the list so that starting point is back first
    int startIdx = -1;
    for (int i = 0; i < path.size(); i++) {
      if (path.get(i) == sp) {
        startIdx = i;
        break;
      }
    }
    List<double[]> rotatedPath = new ArrayList<>(path.subList(startIdx, path.size()));
    rotatedPath.addAll(path.subList(0, startIdx));

    // go back home
    rotatedPath.add(sp);

    return rotatedPath;
  }

  private static int orientation(double[] p, double[] q, double[] r) {
    double val = (q[1] - p[1]) * (r[0] - q[0]) - (q[0] - p[0]) * (r[1] - q[1]);
    if (val == 0) {
      return 0; // collinear
    }
    return val > 0 ? 1 : 2; // clockwise or counterclockwise
  }

  public static double pathCost(double[][] path) {
    double cost = 0;
    for (int i = 0; i < path.length - 1; i++) {
      cost += distance(path[i], path[i + 1]);
    }
    return cost;
  }

  public static double distance(double[] pt1, double[] pt2) {
    double lng1 = pt1[0];
    double lat1 = pt1[1];
    double lng2 = pt2[0];
    double lat2 = pt2[1];
    if (lat1 == lat2 && lng1 == lng2) {
      return 0;
    }

    double radlat1 = Math.PI * lat1 / 180;
    double radlat2 = Math.PI * lat2 / 180;

    double theta = lng1 - lng2;
    double radtheta = Math.PI * theta / 180;

    double dist = Math.sin(radlat1) * Math.sin(radlat2) +
        Math.cos(radlat1) * Math.cos(radlat2) * Math.cos(radtheta);

    if (dist > 1) {
      dist = 1;
    }
    dist = Math.acos(dist);
    dist = dist * 180 / Math.PI;
    return dist * 60 * 1.1515 * 1.609344;
  }
}
