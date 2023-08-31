package util;

public class Employee {
  public int time;

  public double cost() {
    // 30 dollars per hour
    // 45 instead if overtime
    double hours = time / 3600.0;
    return 30.0 * hours + 15.0 * Math.max((hours - 8), 0); // max() is to prevent negative overtime
  }

  @Override
  public String toString() {
    return String.format("Employee [hours=%,.1f]", time / 3600.0);
  }
}