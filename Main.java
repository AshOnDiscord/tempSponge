import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;

import util.Point;
import util.convexHull;

public class Main {
  public static void main(String[] args) {
    // load points.txt
    ArrayList<Point> points = new ArrayList<Point>();

    BufferedReader br;
    try {
      br = new BufferedReader(new FileReader("./points.txt"));

      String st;

      while ((st = br.readLine()) != null) {
        String[] split = st.split(", ");
        points.add(new Point(Double.parseDouble(split[0]), Double.parseDouble(split[1])));
      }

      br.close();

      ArrayList<Point> path = convexHull.generate(points);

      BufferedWriter bw = new BufferedWriter(new FileWriter("./path.txt"));

      for (Point p : path) {
        bw.write(p.x + " " + p.y + "\n");
      }
      Double pathLength = 0.0;
      for (int i = 0; i < path.size() - 1; i++) {
        pathLength += convexHull.distance(path.get(i), path.get(i + 1));
      }
      bw.write("Length: " + pathLength);

      bw.close();
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }
}
