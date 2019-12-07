/* Main.java
* UVa 10684 -- The Jackpot
* Autores: David Vilaça, Harã Heique e Larissa Motta
*/

import java.util.Scanner;

public class Main {

  private static final Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) {
    int n = scanner.nextInt();
    int max, currentValue, aux;

    // length of the sequence
    while (n != 0) {
      max = 0; // to cache the max value
      currentValue = 0;
      for (int i = 0; i < n; i++) { // for each bet of sequence
        aux = scanner.nextInt();
        currentValue = getMax(currentValue + aux, aux); // max value
        max = getMax(max, currentValue); // max value
      }

      output(max);

      n = scanner.nextInt();
    }

  }

  /**
   * get the max value between int value a and int value b
   * 
   * @param a
   * @param b
   * @return
   */
  private static int getMax(int a, int b) {
    if (a >= b)
      return a;
    else
      return b;
  }

  /**
   * output print
   * 
   * @param val
   */
  private static void output(int val) {
    if (val > 0)
      System.out.printf("The maximum winning streak is %d.\n", val);
    else
      System.out.println("Losing streak.");
  }

}
