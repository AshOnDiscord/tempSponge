import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import util.Address;
import util.CycleInfo;
import util.Employee;
import util.Truck;
import util.Christofides.Christofides;

public class Main {
  public static void main(String[] args) {
    ArrayList<CycleInfo> cycleInfos = new ArrayList<>();
    // cycleInfos.add(Parser.parse(new File("./cycle0Practice.txt")));
    // use the same cycle again so we can verify that the results are the same
    // cycleInfos.add(Parser.parse(new File("./cycle0Practice.txt")));
    cycleInfos.add(Parser.parse(new File("./cycle1.txt")));
    cycleInfos.add(Parser.parse(new File("./cycle2.txt")));
    cycleInfos.add(Parser.parse(new File("./cycle3.txt")));
    cycleInfos.add(Parser.parse(new File("./cycle4.txt")));
    cycleInfos.add(Parser.parse(new File("./cycle5.txt")));
    cycleInfos.add(Parser.parse(new File("./cycle6.txt")));
    cycleInfos.add(Parser.parse(new File("./cycle7.txt")));
    cycleInfos.add(Parser.parse(new File("./cycle8.txt")));
    cycleInfos.add(Parser.parse(new File("./cycle9.txt")));
    cycleInfos.add(Parser.parse(new File("./cycle10.txt")));

    ArrayList<Truck> trucks = new ArrayList<Truck>();
    List<ArrayList<Object>> spongeBob = new ArrayList<ArrayList<Object>>();
    List<ArrayList<Object>> patrick = new ArrayList<ArrayList<Object>>();
    int spongeBobMax = Truck.complexMax(2, 3);
    int patrickMax = Truck.complexMax(149, 33);

    for (int t = 0; t < cycleInfos.size(); t++) {
      CycleInfo cycleInfo = cycleInfos.get(t);

      List<ArrayList<Address>> sections = new ArrayList<ArrayList<Address>>();

      // create trucks for the complexes
      int unallocatedSpongeBobPackages = cycleInfo.spongeBobPackages;
      int truckC = 0;
      while (unallocatedSpongeBobPackages > 0) {
        truckC++;
        Truck truck;
        Employee employee;
        if (truckC > spongeBob.size()) {
          truck = new Truck(0, 0);
          employee = new Employee(cycleInfos.size());
          truck.employees[0] = employee;
        } else {
          truck = (Truck) spongeBob.get(truckC - 1).get(0);
          employee = (Employee) spongeBob.get(truckC - 1).get(1);
        }
        int packages = Math.min(spongeBobMax, unallocatedSpongeBobPackages);
        // deliver to the complex
        truck.driveTo(2, 3);
        // employee.time += 30 * packages;
        employee.time.set(t, employee.time.get(t) + 30 * packages);
        truck.origin();

        // add the truck to the list
        if (truckC > spongeBob.size()) {
          spongeBob.add(new ArrayList<Object>(Arrays.asList(truck, employee, packages)));
        } else {
          spongeBob.get(truckC - 1).set(2, packages + (int) spongeBob.get(truckC - 1).get(2));
        }
        unallocatedSpongeBobPackages -= spongeBobMax;
      }
      int unallocatedPatrickPackages = cycleInfo.patrickPackages;
      truckC = 0;
      while (unallocatedPatrickPackages > 0) {
        truckC++;
        Truck truck;
        Employee employee;
        if (truckC > patrick.size()) {
          truck = new Truck(0, 0);
          employee = new Employee(cycleInfos.size());
          truck.employees[0] = employee;
        } else {
          truck = (Truck) patrick.get(truckC - 1).get(0);
          employee = (Employee) patrick.get(truckC - 1).get(1);
        }
        int packages = Math.min(patrickMax, unallocatedPatrickPackages);
        // deliver to the complex
        truck.driveTo(149, 33);
        // employee.time += 30 * packages;
        employee.time.set(t, employee.time.get(t) + 30 * packages);
        truck.origin();

        // add the truck to the list
        if (truckC > patrick.size()) {
          patrick.add(new ArrayList<Object>(Arrays.asList(truck, employee, packages)));
        } else {
          patrick.get(truckC - 1).set(2, packages + (int) patrick.get(truckC - 1).get(2));
        }
        unallocatedPatrickPackages -= patrickMax;
      }

      // create squares starting from 0,0 expand by 5 in each
      // direction until simulate is > 28800(8 hours)
      // then push the section to sections
      // then repeat until all addresses are in sections
      Point lastMin = new Point(0, -1);
      Point lastMax = new Point(0, 0);
      Point currentMin = new Point(0, 0);
      Point currentMax = new Point(0, 0);

      outer: for (;;) {
        // create a new section
        // we move ltr, ttb
        // the +1 is to avoid overlap
        currentMin.x = lastMax.x + 1;
        // if we had just moved to the next row then revert the +1
        if (currentMin.x == 1 || currentMin.x == 250) {
          currentMin.x = 0;
        }
        currentMax = new Point(currentMin.x, currentMin.y + 24);
        for (;;) {
          // expand
          currentMax.x += 5;
          // simulate
          // System.out.println(String.format("Simulating %s %s", currentMin,
          // currentMax));
          int time = simulate(cycleInfo.addresses, currentMin, currentMax, new Point(0, 0));
          // if we are over 8 hours then remove the last expansion(-5)
          // if we are over the bounds then set the max to the bounds
          if (time > 28800) {
            currentMax.x -= 5;
            currentMax.y -= 5;
            if (currentMax.x > 249) {
              currentMax.x = 249;
            }
            if (currentMax.y > 50) {
              currentMax.y = 50;
            }
          }
          if (currentMax.x > 249) {
            currentMax.x = 249;
          }
          if (currentMax.y > 50) {
            currentMax.y = 50;
          }
          if (time > 28800 || currentMax.x == 249 || currentMax.y == 50) {
            // add the section to sections
            if (time == 0 && currentMax.x == 249 && currentMax.y == 50) {
              break outer;
            }
            ArrayList<Address> section = new ArrayList<Address>();
            for (Address address : cycleInfo.addresses) {
              if (address.blockX >= currentMin.x && address.blockX <= currentMax.x && address.blockY >= currentMin.y
                  && address.blockY <= currentMax.y) {
                section.add(address);
              }
            }
            sections.add(section);
            // update lastMin and lastMax
            lastMin = currentMin.copy();
            lastMax = currentMax.copy();
            // check if we needed to go to the next row
            if (currentMax.x >= 249) {
              currentMin = new Point(-1, currentMax.y + 1);
              currentMax = currentMin.copy();
            }
            break;
          }
        }
        // check if we are done
        if ((currentMax.y == 50 && currentMax.x == 249) || currentMin.y == 50 || currentMax.y > 50) {
          break;
        }
      }
      // System.out.println(sections.size());
      for (ArrayList<Address> section : sections) {
        // System.out.print(section.size());
        Point min = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
        Point max = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);
        for (Address address : section) {
          if (address.blockX < min.x) {
            min.x = address.blockX;
          }
          if (address.blockY < min.y) {
            min.y = address.blockY;
          }
          if (address.blockX > max.x) {
            max.x = address.blockX;
          }
          if (address.blockY > max.y) {
            max.y = address.blockY;
          }
        }
        // System.out
        // .println(
        // String.format(" | %s %s | %,.2f", min, max, simulate(section, min, max, new
        // Point(0, 0)) / 3600.0));
      }

      // create a truck and employee for each section
      truckC = 0;
      for (int i = 0; i < sections.size(); i++) {
        truckC++;
        Truck truck;
        Employee employee;
        if (truckC > trucks.size()) {
          truck = new Truck(0, 0);
          employee = new Employee(cycleInfos.size());
          truck.employees[0] = employee;
          trucks.add(truck);
        } else {
          truck = trucks.get(truckC - 1);
          employee = truck.employees[0];
        }
      }

      ArrayList<List<Address>> paths = new ArrayList<List<Address>>();
      // for (int i = 0; i < trucks.size(); i++) {
      // List<Address> path = Christofides.addressWrapper(sections.get(i));
      // paths.add(path);
      // }
      // switch to sections.size()
      for (int i = 0; i < sections.size(); i++) {
        List<Address> path = Christofides.addressWrapper(sections.get(i));
        paths.add(path);
      }

      // for (int i = 0; i < trucks.size(); i++) {
      // // for (Address address : paths.get(i)) {
      // for (int j = 0; j < paths.get(i).size(); j++) {
      // Address address = paths.get(i).get(j);
      // Address nextAddress = j + 1 < paths.get(i).size() ? paths.get(i).get(j + 1) :
      // null;
      // trucks.get(i).deliver(address, nextAddress);
      // }
      // trucks.get(i).origin();
      // }

      // use paths.size() instead of trucks.size() because we may have
      // created more trucks than we need from previous cycles
      for (int i = 0; i < paths.size(); i++) {
        for (int j = 0; j < paths.get(i).size(); j++) {
          Address address = paths.get(i).get(j);
          Address nextAddress = j + 1 < paths.get(i).size() ? paths.get(i).get(j + 1) : null;
          trucks.get(i).deliver(address, nextAddress);
        }
        trucks.get(i).origin();
      }

      // INFO

      System.out.println(cycleInfo);

      // for (Truck truck : trucks) {
      // System.out.print(truck);
      // System.out.println(String.format(" | $%,.2f", truck.cost()));
      // System.out.print(truck.employees[0]);
      // System.out.println(String.format(" | $%,.2f", truck.employees[0].cost()));
      // System.out.println();
      // }
      // System.out.println("SpongeBob");
      // for (ArrayList<Object> truck : spongeBob) {
      // System.out.print(truck.get(0));
      // System.out.println(String.format(" | $%,.2f", ((Truck)
      // truck.get(0)).cost()));
      // System.out.print(truck.get(1));
      // System.out.println(String.format(" | $%,.2f", ((Employee)
      // truck.get(1)).cost()));
      // System.out.println();
      // }
      // System.out.println("Patrick");
      // for (ArrayList<Object> truck : patrick) {
      // System.out.print(truck.get(0));
      // System.out.println(String.format(" | $%,.2f", ((Truck)
      // truck.get(0)).cost()));
      // System.out.print(truck.get(1));
      // System.out.println(String.format(" | $%,.2f", ((Employee)
      // truck.get(1)).cost()));
      // System.out.println();
      // }

      double truckCost = 0.0;
      double employeeCost = 0;
      for (Truck truck : trucks) {
        truckCost += truck.cost();
        employeeCost += truck.employees[0].cost();
      }
      for (ArrayList<Object> truck : spongeBob) {
        truckCost += ((Truck) truck.get(0)).cost();
        employeeCost += ((Employee) truck.get(1)).cost();
      }
      for (ArrayList<Object> truck : patrick) {
        truckCost += ((Truck) truck.get(0)).cost();
        employeeCost += ((Employee) truck.get(1)).cost();
      }
      System.out.println(String.format("Total Truck Cost: $%,.2f(%s trucks)", truckCost, trucks.size()));
      System.out.println(String.format("Total Employee Cost: $%,.2f", employeeCost));
      System.out.println(String.format("Total Cost: $%,.2f", truckCost + employeeCost));
      System.out.println();
      for (Truck truck : trucks) {
        // increase the cycle count
        truck.cycleCount++;
        for (Employee employee : truck.employees) {
          if (employee == null) {
            continue;
          }
        }
      }
    }
  }

  public static int simulate(ArrayList<Address> addresses, Point min, Point max, Point origin) {
    // take in a section
    // create a truck
    // create an employee
    // create a path
    // simulate the path
    // return the hours
    Address[] section = Arrays.stream(addresses.toArray(new Address[0]))
        .filter(a -> a.blockX >= min.x && a.blockX <= max.x && a.blockY >= min.y && a.blockY <= max.y)
        .toArray(Address[]::new);
    if (section.length == 0) {
      return 0;
    }
    Truck truck = new Truck(origin.x, origin.y);
    Employee employee = new Employee(1);
    truck.employees[0] = employee;
    // for (Address address : section) {
    // System.out.println(address);
    // }
    List<Address> path = Christofides.addressWrapper(Arrays.asList(section));
    // for (Address address : path) {
    for (int i = 0; i < path.size(); i++) {
      Address address = path.get(i);
      Address nextAddress = i + 1 < path.size() ? path.get(i + 1) : null;
      truck.deliver(address, nextAddress);
    }
    truck.origin();
    // return employee.time; // in seconds, 28800 is 8 hours or the non overtime
    // limit
    return employee.time.get(0);
  }
}

class Point {
  public int x;
  public int y;

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Point copy() {
    return new Point(x, y);
  }

  @Override
  public String toString() {
    return String.format("(%d, %d)", x, y);
  }
}