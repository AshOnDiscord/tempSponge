package util;

import util.Point;
import java.util.ArrayList;

public class convexHull {
  public static ArrayList<Point> generate(ArrayList<Point> points) {
    Point sp = points.get(0);

    // Find the "left most point"
    Point leftmost = points.get(0);
    for (Point p : points) {
      if (p.x < leftmost.x) {
        leftmost = p;
      }
    }

    ArrayList<Point> path = new ArrayList<Point>();
    path.add(leftmost);

    while (true) {
      Point curPoint = path.get(path.size() - 1);
      int selectedIdx = 0;
      Point selectedPoint = null;

      // find the "most counterclockwise" point
      for (int idx = 0; idx < points.size(); idx++) {
        Point p = points.get(idx);
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
      if (selectedPoint == leftmost || points.size() == 0) {
        break;
      }

      // add to hull
      path.add(selectedPoint);
    }

    while (points.size() > 0) {
      double bestRatio = Double.POSITIVE_INFINITY;
      Integer bestPointIdx = null;
      int insertIdx = 0;

      for (int freeIdx = 0; freeIdx < points.size(); freeIdx++) {
        Point freePoint = points.get(freeIdx);
        // for every free point, find the point in the current path
        // that minimizes the cost of adding the point minus the cost of
        // the original segment
        double bestCost = Double.POSITIVE_INFINITY;
        int bestIdx = 0;
        for (int pathIdx = 0; pathIdx < path.size(); pathIdx++) {
          Point pathPoint = path.get(pathIdx);
          Point nextPathPoint = path.get((pathIdx + 1) % path.size());

          // the new cost minus the old cost
          double evalCost = pathCost(new Point[] { pathPoint, freePoint, nextPathPoint })
              - pathCost(new Point[] { pathPoint, nextPathPoint });

          if (evalCost < bestCost) {
            bestCost = evalCost;
            bestIdx = pathIdx;
          }
        }

        // figure out how "much" more expensive this is with respect to the
        // overall length of the segment
        Point nextPoint = path.get((bestIdx + 1) % path.size());
        double prevCost = pathCost(new Point[] { path.get(bestIdx), nextPoint });
        double newCost = pathCost(new Point[] { path.get(bestIdx), freePoint, nextPoint });
        double ratio = newCost / prevCost;

        if (ratio < bestRatio) {
          bestRatio = ratio;
          bestPointIdx = freeIdx;
          insertIdx = bestIdx + 1;
        }
      }

      Point nextPoint = points.get(bestPointIdx);
      points.remove((int) bestPointIdx);
      path.add(insertIdx, nextPoint);
    }

    // rotate the array so that starting point is back first
    int startIdx = 0;
    for (int i = 0; i < path.size(); i++) {
      if (path.get(i) == sp) {
        startIdx = i;
        break;
      }
    }
    for (int i = 0; i < startIdx; i++) {
      path.add(path.remove(0));
    }

    // go back home
    path.add(sp);

    return path;
  }

  public static double orientation(Point p, Point q, Point r) {
    double val = (q.x - p.x) * (r.y - q.y) - (q.y - p.y) * (r.x - q.x);
    if (val == 0) {
      return 0; // collinear
    }
    return val > 0 ? 1 : 2; // clockwise or counterclockwise
  }

  public static double pathCost(Point[] points) {
    double sum = 0;
    for (int i = 0; i < points.length - 1; i++) {
      sum += distance(points[i], points[i + 1]);
    }
    return sum;
  }

  public static double distance(Point p1, Point p2) {
    if (p1.y == p2.y && p1.x == p2.x) {
      return 0;
    }

    var rad1 = (Math.PI * p1.y) / 180;
    var rad2 = (Math.PI * p2.y) / 180;

    var theta = p1.x - p2.x;
    var radtheta = (Math.PI * theta) / 180;

    var dist = Math.sin(rad1) * Math.sin(rad2) +
        Math.cos(rad1) * Math.cos(rad2) * Math.cos(radtheta);

    if (dist > 1) {
      dist = 1;
    }
    dist = Math.acos(dist);
    dist = (dist * 180) / Math.PI;
    return dist * 60 * 1.1515 * 1.609344;
  }
}