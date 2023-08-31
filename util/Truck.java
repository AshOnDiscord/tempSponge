package util;

public class Truck {
  public int originX;
  public int originY;
  public int x;
  public int y;
  public Employee[] employees = new Employee[2];
  public int distance;
  public int cycleCount = 0;

  public Truck(int x, int y) {
    // System.out.print("|");
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
    int base = 100000;
    // int base = 10000; // 100k / 10 days = 10k per day, this is so it doesn't
    // overly skew towards
    // // favoring renting
    return base + (5 * miles) + (1000 * (miles / 100));
  }

  public void driveTo(int x, int y) {
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
      // e.time += (xDiff + yDiff) * 30;
      e.time.set(cycleCount, e.time.get(cycleCount) + (xDiff + yDiff) * 30);
    }
    this.distance += xDiff * 1000 + yDiff * 200;

    this.x = x;
    this.y = y;
  }

  public void deliver(Address A, Address B) {
    // System.out.println("Delivering to " + A.blockX + ", " + A.blockY + ", " +
    // A.blockNumber);
    // if going to the next Address(B) after the current one(A) is in the same
    // direction
    // do a full drive rather than a partial drive through the block
    int xDiff = A.blockX - this.x;
    int xDiff2 = 0;
    if (B != null) {
      xDiff = B.blockX - this.x;
    }
    boolean sameDir = false;
    if (xDiff > 0 && xDiff2 > 0) {
      sameDir = true; // going right
    } else if (xDiff < 0 && xDiff2 < 0) {
      sameDir = true; // going left
    }
    this.driveTo(A.blockX, A.blockY);
    // TODO: for full drive, drive the closest side of the same block
    // instead(currently you will always drive the top left)
    if (sameDir && B != null) { // partial driving, if B is null then we do a full drive
      if (xDiff >= 0) {
        System.out.println("ltr");
        // driving in from the left(0,0 is top left)
        //
        // ABCDEFGHIJ
        // 0123456789
        //
        int time = ((A.blockNumber % 10) + 1) * 6; // % 10 to treat a and aa the same, etc,
        for (Employee e : employees) {
          if (e == null) {
            continue;
          }
          // e.time += time + 60; // 1m for the package
          e.time.set(cycleCount, e.time.get(cycleCount) + time + 60);
        }
      } else {
        System.out.println("rtl");
        // driving in from the right
        int time = (11 - ((A.blockNumber % 10) + 1)) * 6;
        for (Employee e : employees) {
          if (e == null) {
            continue;
          }
          // e.time += time + 60; // 1m for the package
          e.time.set(cycleCount, e.time.get(cycleCount) + time + 60);
        }
      }
    } else { // full drive
      for (Employee e : employees) {
        if (e == null) {
          continue;
        }
        // e.time += 30 + 60; // 30s for the drive, 1m for the package
        e.time.set(cycleCount, e.time.get(cycleCount) + 30 + 60);
      }
      this.x += xDiff > 0 ? 1 : -1; // truck ends up on the other side of the block
    }

  }

  public void origin() {
    int xDiff = Math.abs(this.x - originX);
    int yDiff = Math.abs(this.y - originY);
    // 30 seconds per block
    for (Employee e : employees) {
      if (e == null) {
        continue;
      }
      // e.time += (xDiff + yDiff) * 30;
      e.time.set(cycleCount, e.time.get(cycleCount) + (xDiff + yDiff) * 30);
    }
    this.distance += xDiff * 1000 + yDiff * 200;
    this.x = originX;
    this.y = originY;
  }

  @Override
  public String toString() {
    return "Truck [x=" + x + ", y=" + y + ", miles=" + (distance / 5000.0) + "]";
  }

  public static int complexMax(int x, int y) {
    // calculate the max packages that the truck can deliver before overtime
    // 8 hours = 28800 seconds
    // 30 seconds per package
    // 100 packages per trip
    // // the complex is located at 2, 3
    // the truck is located at the origin
    Truck temp = new Truck(0, 0);
    Employee tempE = new Employee(1);
    temp.employees[0] = tempE;
    int travelTime = 0;
    // temp.driveTo(2, 3);
    temp.driveTo(x, y);
    temp.origin();
    // travelTime = tempE.time;
    travelTime = tempE.time.get(0);

    int time = 0;
    int packages = 0;
    while (time + travelTime < 28800) {
      time += travelTime;
      time += 100 * 30;
      packages += 100;
      while (time > 28800) {
        // remove packages until time is less than 8 hours
        time -= 30;
        packages -= 1;
      }
    }
    return packages;
  }
}
