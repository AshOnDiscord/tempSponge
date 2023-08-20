import java.io.File;
import java.nio.Buffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import util.Address;
import util.CycleInfo;
import util.Employee;
import util.Truck;
import util.Christofides.Christofides;

public class Main {
  public static void main(String[] args) {
    CycleInfo cycleInfo = Parser.parse(new File("./cycle0Practice.txt"));
    Truck[] trucks = { new Truck(125, 23), new Truck(125, 23), new Truck(125, 23), new Truck(125, 23) };
    List<ArrayList<Address>> sections = new ArrayList<ArrayList<Address>>();

    for (int i = 0; i < trucks.length; i++) {
      trucks[i].employees[0] = new Employee();
      sections.add(new ArrayList<Address>());
    }
    for (Address address : cycleInfo.addresses) {
      if (address.blockX < 125) {
        if (address.blockY < 23) {
          sections.get(0).add(address);
        } else {
          sections.get(3).add(address);
        }
      } else {
        if (address.blockY < 23) {
          sections.get(1).add(address);
        } else {
          sections.get(2).add(address);
        }
      }
    }

    ArrayList<List<Address>> paths = new ArrayList<List<Address>>();
    for (int i = 0; i < trucks.length; i++) {
      List<Address> path = Christofides.addressWrapper(sections.get(i));
      paths.add(path);
    }

    for (int i = 0; i < trucks.length; i++) {
      for (Address address : paths.get(i)) {
        trucks[i].deliver(address.blockX, address.blockY);
      }
      trucks[i].origin();
    }

    // INFO

    System.out.println(cycleInfo + "\n");

    for (Truck truck : trucks) {
      System.out.print(truck);
      System.out.println(String.format(" | $%,.2f", truck.cost()));
      System.out.print(truck.employees[0]);
      System.out.println(String.format(" | $%,d", truck.employees[0].cost()));
      System.out.println();
    }

    double truckCost = 0.0;
    int employeeCost = 0;
    for (Truck truck : trucks) {
      truckCost += truck.cost();
      employeeCost += truck.employees[0].cost();
    }
    System.out.println(String.format("Total Truck Cost: $%,.2f", truckCost));
    System.out.println(String.format("Total Employee Cost: $%,d", employeeCost));
    System.out.println(String.format("Total Cost: $%,.2f", truckCost + employeeCost));
  }
}