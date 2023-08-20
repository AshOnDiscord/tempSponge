package util;

public class Employee {
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