package util;

public class Truck {
  public int originX;
  public int originY;
  public int x;
  public int y;
  public Employee[] employees = new Employee[2];
  public int distance;

  public Truck(int x, int y) {
    this.originX = x;
    this.originY = y;
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

    this.x = x;
    this.y = y;
  }

  public void origin() {
    int xDiff = Math.abs(this.x - originX);
    int yDiff = Math.abs(this.y - originY);
    // 30 seconds per block
    for (Employee e : employees) {
      if (e == null) {
        continue;
      }
      e.hours += (xDiff + yDiff) * 30;
    }
    this.distance += xDiff * 1000 + yDiff * 200;
  }

  @Override
  public String toString() {
    return "Truck [x=" + x + ", y=" + y + ", miles=" + (distance / 5000.0) + "]";
  }
}
