package util;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Employee {
  public ArrayList<Integer> time = new ArrayList<Integer>();

  public Employee(int maxCycleCount) {
    for (int i = 0; i < maxCycleCount; i++) {
      time.add(0);
    }
  }

  public double cost() {
    // 30 dollars per hour
    // 45 instead if overtime
    double sum = 0;
    for (Integer i : time) {
      double hours = i / 3600.0;
      // return 30.0 * hours + 15.0 * Math.max((hours - 8), 0); // max() is to prevent
      // negative overtime
      sum += 30.0 * hours + 15.0 * Math.max((hours - 8), 0); // max() is to prevent negative overtime
    }
    return sum;
  }

  @Override
  public String toString() {

    return String.format("Employee [hours=%,.1f]",
        time.stream().map(e -> {
          Double a = (e / 3600.0);
          return a.toString();
        }).collect(Collectors.joining(",")));
  }
}