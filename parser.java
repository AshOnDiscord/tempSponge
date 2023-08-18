
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import util.Point;
import util.convexHull;
import util.christofides;

public class parser {
  public static void main(String[] args) {
    // read in file
    // parse file
    // store data
    // return data
    BufferedReader br;
    try {
      br = new BufferedReader(new FileReader("cycle0Practice.txt"));

      String st;
      info cycleInfo = new info();
      while ((st = br.readLine()) != null) {
        // System.out.println(st);
        if (cycleInfo.cycleNumber == 0) {
          cycleInfo.cycleNumber = Integer.parseInt(st);
        } else if (cycleInfo.totalPackages == 0) {
          cycleInfo.totalPackages = Integer.parseInt(st);
        } else if (cycleInfo.addresses == null) {
          cycleInfo.addresses = new ArrayList<Address>();
          cycleInfo.addresses.add(new Address(st));
        } else if (st.contains("SpongeBob") || st.contains("Patrick")) {
          // do nothing
          break;
        } else {
          cycleInfo.addresses.add(new Address(st));
        }
      }
      System.out.println(cycleInfo.cycleNumber);
      System.out.println(cycleInfo.totalPackages);

      Truck truck1 = new Truck(0, 0);
      Employee employee1 = new Employee();
      truck1.employees[0] = employee1;

      Truck truck2 = new Truck(0, 0);
      Employee employee2 = new Employee();
      truck2.employees[0] = employee2;

      // for (Address a : cycleInfo.addresses) {
      // System.out.println(a.blockX + " " + a.blockY + " " + a.blockNumber);
      // truck1.deliver(a.blockX, a.blockY);
      // }

      // split into a north and south side
      ArrayList<Address> northSide = new ArrayList<Address>();
      ArrayList<Address> southSide = new ArrayList<Address>();
      for (Address a : cycleInfo.addresses) {
        if (a.blockY < 25) {
          northSide.add(a);
        } else {
          southSide.add(a);
        }
      }

      // // sort north side
      // List<Address> northPath = convexWrapper(northSide);
      // // sort south side
      // List<Address> southPath = convexWrapper(southSide);

      // sort north side
      List<Address> northPath = christofidesWrapper(northSide);
      // sort south side
      List<Address> southPath = christofidesWrapper(southSide);

      // follow paths
      for (Address a : northPath) {
        truck1.deliver(a.blockX, a.blockY);
      }

      for (Address a : southPath) {
        truck2.deliver(a.blockX, a.blockY);
      }

      System.out.println();
      System.out.println(northSide.size());
      System.out.println(truck1);
      System.out.println(truck1.cost());
      System.out.println(employee1);
      System.out.println(employee1.cost());
      System.out.println();
      System.out.println(southSide.size());
      System.out.println(truck2);
      System.out.println(truck2.cost());
      System.out.println(employee2);
      System.out.println(employee2.cost());
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static List<Address> convexWrapper(List<Address> addresses) {
    // convert to points
    List<Point> points = new ArrayList<Point>();
    for (Address a : addresses) {
      points.add(new Point(a.blockX, a.blockY));
    }
    // compute convex hull
    List<Point> hull = convexHull.computeConvexHull(points);
    // convert back to addresses
    List<Address> result = new ArrayList<Address>();
    for (Point p : hull) {
      result.add(new Address((int) p.x, (int) p.y, 0));
    }
    return result;
  }

  public static List<Address> christofidesWrapper(List<Address> addresses) {
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
    List<Integer> order = christofides.christofidesTSP(graph);
    List<Address> path = new ArrayList<Address>();
    for (int i = 0; i < order.size(); i++) {
      path.add(addresses.get(order.get(i)));
    }
    return path;
  }
}

class info {
  public int cycleNumber;
  public int totalPackages;
  public ArrayList<Address> addresses;
}

class Address {
  public int blockX;
  public int blockY;
  public int blockNumber;

  public Address(int blockX, int blockY, int blockNumber) {
    this.blockX = blockX;
    this.blockY = blockY;
    this.blockNumber = blockNumber;
  }

  public Address(String address) {
    // input goes like `${x}s,${y}a,${blocknumber}`
    // block number go like:
    // A,B,C,D,E,F,G,H,I & J for the west side of the street
    // AA,BB,CC,DD,EE,FF,GG,HH,II & JJ for the east side of the street.
    // it will be stored as a number from 0-19

    try {
      String[] split = address.split(",");
      this.blockX = Integer.parseInt(split[0].substring(0, split[0].length() - 1));
      this.blockY = Integer.parseInt(split[1].substring(0, split[1].length() - 1));
      // convert first char to number(a = 0, b = 1, etc)
      int tempNumber = (int) split[2].charAt(0) - 65;
      // if it's on the east side, add 10
      if (split[2].length() == 2) {
        tempNumber += 10;
      }
      this.blockNumber = tempNumber;
    } catch (Exception e) {
      System.out.println("Error parsing address: " + address);
    }
  }
}

class Truck {
  public int x;
  public int y;
  public Employee[] employees = new Employee[2];
  public int distance;

  public Truck(int x, int y) {
    this.x = x;
    this.y = y;
    this.distance = 0;
  }

  public double cost() {
    // base of 100k
    // 5 dollars per mile for gas
    // 1000 dollars per 100 miles for maintenance
    double miles = distance / 5000.0;
    return 100000 + (5 * miles) + (1000 * (miles / 100));
  }

  public void deliver(int x, int y) {
    // calculate distance
    // add distance to miles
    // set x and y
    int xDiff = Math.abs(this.x - x);
    int yDiff = Math.abs(this.y - y);
    // 30 seconds per block
    for (Employee e : employees) {
      if (e == null) {
        continue;
      }
      e.hours += (xDiff + yDiff) * 30;
      e.hours += 60; // 1 minute per package
    }
    this.distance += xDiff * 1000 + yDiff * 200;
  }

  @Override
  public String toString() {
    return "Truck [x=" + x + ", y=" + y + ", miles=" + (distance / 5000.0) + "]";
  }
}

class Employee {
  public int hours;

  public int cost() {
    // 30 dollars per hour
    // 45 instead if overtime
    return 30 * hours + 15 * (hours - 8);
  }

  @Override
  public String toString() {
    return "Employee [hours=" + hours + "]";
  }
}