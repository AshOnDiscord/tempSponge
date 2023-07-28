import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;

import util.Point;
import util.convexHull;

public class Main {
  public static void main(String[] args) {
    // load points.txt
    List<Point> points = new ArrayList<Point>();

    BufferedReader br;
    try {
      br = new BufferedReader(new FileReader("./points.txt"));

      String st;

      while ((st = br.readLine()) != null) {
        String[] split = st.split(", ");
        points.add(new Point(Double.parseDouble(split[0]), Double.parseDouble(split[1])));
      }

      br.close();

      List<Point> path = convexHull.computeConvexHull(points);

      BufferedWriter bw = new BufferedWriter(new FileWriter("./path.txt"));

      for (Point p : path) {
        bw.write(p.x + ", " + p.y + "\n");
      }
      bw.write("Length: " + convexHull.pathCost(path.toArray(new Point[0])));

      bw.close();
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block6
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }
}
