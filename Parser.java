
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import util.Address;
import util.CycleInfo;

public class Parser {
  public static CycleInfo parse(File file) {
    try {
      BufferedReader br;
      br = new BufferedReader(new FileReader(file));

      String st;
      CycleInfo cycleInfo = new CycleInfo();
      while ((st = br.readLine()) != null) {
        if (cycleInfo.cycleNumber == 0) {
          cycleInfo.cycleNumber = Integer.parseInt(st);
        } else if (cycleInfo.totalPackages == 0) {
          cycleInfo.totalPackages = Integer.parseInt(st);
        } else if (cycleInfo.addresses == null) {
          cycleInfo.addresses = new ArrayList<Address>();
          cycleInfo.addresses.add(new Address(st));
        } else if (st.contains("SpongeBob") || st.contains("Patrick")) {
          // read number from next line
          boolean isSpongeBob = st.contains("SpongeBob");
          st = br.readLine();
          if (isSpongeBob) {
            cycleInfo.spongeBobPackages = Integer.parseInt(st);
          } else {
            cycleInfo.patrickPackages = Integer.parseInt(st);
          }
        } else {
          cycleInfo.addresses.add(new Address(st));
        }
      }
      br.close();
      return cycleInfo;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
    return null;
  }
}