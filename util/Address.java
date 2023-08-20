package util;

public class Address {
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
    // Single letter for the north side of the street
    // Double letter for the south side of the street.
    // it will be stored as a number from 0-19
    try {
      String[] split = address.split(",");
      this.blockX = Integer.parseInt(split[0].substring(0, split[0].length() - 1));
      this.blockY = Integer.parseInt(split[1].substring(0, split[1].length() - 1));
      int tempNumber = (int) split[2].charAt(0) - 65;
      if (split[2].length() == 2) {
        tempNumber += 10;
      }
      this.blockNumber = tempNumber;
    } catch (Exception e) {
      System.out.println("Error parsing address: " + address);
    }
  }
}