package util;

import java.util.ArrayList;

public class CycleInfo {
  public int cycleNumber;
  public int totalPackages;
  public int spongeBobPackages;
  public int patrickPackages;
  public ArrayList<Address> addresses;

  @Override
  public String toString() {
    return "CycleInfo [cycleNumber=" + cycleNumber + ", totalPackages=" + totalPackages + ", addresses="
        + addresses.size() + " spongeBobPackages=" + spongeBobPackages + ", patrickPackages=" + patrickPackages + "]";
  }
}
