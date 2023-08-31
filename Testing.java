import util.Truck;
import util.Employee;
import util.Address;

class HouseLettering {
  public static int A = 0;
  public static int B = 1;
  public static int C = 2;
  public static int D = 3;
  public static int E = 4;
  public static int F = 5;
  public static int G = 6;
  public static int H = 7;
  public static int I = 8;
  public static int J = 9;
  public static int AA = 10;
  public static int BB = 11;
  public static int CC = 12;
  public static int DD = 13;
  public static int EE = 14;
  public static int FF = 15;
  public static int GG = 16;
  public static int HH = 17;
  public static int II = 18;
  public static int JJ = 19;
}

public class Testing {
  public static void main(String[] args) {
    // THIS IS FIX Truck.deliver() and Truck.driveTo();
    Truck t = new Truck(1, 0);
    Employee e = new Employee();
    t.employees[0] = e;

    // t.deliver(0, 0, HouseLettering.J);

    System.out.println(e.time);
  }
}
