
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import util.*;
import util.Christofides.Christofides;

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
      CycleInfo cycleInfo = new CycleInfo();
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

      ArrayList<Address> NWSide = new ArrayList<Address>();
      ArrayList<Address> NESide = new ArrayList<Address>();
      ArrayList<Address> SWSide = new ArrayList<Address>();
      ArrayList<Address> SESide = new ArrayList<Address>();
      for (Address a : cycleInfo.addresses) {
        if (a.blockY < 25) {
          if (a.blockX < 125) {
            NWSide.add(a);
          } else {
            NESide.add(a);
          }
        } else {
          if (a.blockX < 125) {
            SWSide.add(a);
          } else {
            SESide.add(a);
          }
        }
      }

      List<Address> NWPath = Christofides.addressWrapper(NWSide);
      List<Address> NEPath = Christofides.addressWrapper(NESide);
      List<Address> SWPath = Christofides.addressWrapper(SWSide);
      List<Address> SEPath = Christofides.addressWrapper(SESide);

      Truck NWTruck = new Truck(125, 22);
      Truck NETruck = new Truck(125, 22);
      Truck SWTruck = new Truck(125, 22);
      Truck SETruck = new Truck(125, 22);
      Employee NWEmployee = new Employee();
      Employee NEEmployee = new Employee();
      Employee SWEmployee = new Employee();
      Employee SEEmployee = new Employee();
      NWTruck.employees[0] = NWEmployee;
      NETruck.employees[0] = NEEmployee;
      SWTruck.employees[0] = SWEmployee;
      SETruck.employees[0] = SEEmployee;

      for (Address a : NWPath) {
        NWTruck.deliver(a.blockX, a.blockY);
      }
      NWTruck.origin();
      for (Address a : NEPath) {
        NETruck.deliver(a.blockX, a.blockY);
      }
      NETruck.origin();
      for (Address a : SWPath) {
        SWTruck.deliver(a.blockX, a.blockY);
      }
      SWTruck.origin();
      for (Address a : SEPath) {
        SETruck.deliver(a.blockX, a.blockY);
      }
      SETruck.origin();

      System.out.println();
      System.out.println(NWPath.size());
      System.out.println(NWTruck);
      System.out.println(NWTruck.cost());
      System.out.println(NWEmployee);
      System.out.println(NWEmployee.cost());
      System.out.println();
      System.out.println(NEPath.size());
      System.out.println(NETruck);
      System.out.println(NETruck.cost());
      System.out.println(NEEmployee);
      System.out.println(NEEmployee.cost());
      System.out.println();
      System.out.println(SWPath.size());
      System.out.println(SWTruck);
      System.out.println(SWTruck.cost());
      System.out.println(SWEmployee);
      System.out.println(SWEmployee.cost());
      System.out.println();
      System.out.println(SEPath.size());
      System.out.println(SETruck);
      System.out.println(SETruck.cost());
      System.out.println(SEEmployee);
      System.out.println(SEEmployee.cost());
      System.out.println();

      DecimalFormat formatter = new DecimalFormat("#,###.00");
      DecimalFormat formatter2 = new DecimalFormat("#,###");

      System.out.println(formatter2.format(NWPath.size() + NEPath.size() + SWPath.size() + SEPath.size()));
      System.out.println(formatter.format(NWTruck.cost() + NETruck.cost() + SWTruck.cost() + SETruck.cost()));
      System.out.println(
          formatter.format((NWTruck.distance / 5000.0) + (NETruck.distance / 5000.0) + (SWTruck.distance / 5000.0)
              + (SETruck.distance / 5000.0)));
      System.out
          .println(formatter2.format(NWEmployee.cost() + NEEmployee.cost() + SWEmployee.cost() + SEEmployee.cost()));
      System.out.println(formatter2.format(NWEmployee.hours + NEEmployee.hours + SWEmployee.hours + SEEmployee.hours));

    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}