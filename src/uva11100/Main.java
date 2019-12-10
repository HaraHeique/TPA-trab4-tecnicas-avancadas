/* Main.java
* UVa 11100 -- The Trip, 2007
* Autores: David Vilaça, Harã Heique e Larissa Motta
*/

import java.util.Scanner;
import java.util.Arrays;

public class Main {

  private static final Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) {
    int n = scanner.nextInt(), atualBag, amount, outputBagsMax, lastBag;
    Integer[] bags;
    while (n > 0) {
      bags = new Integer[n];
      outputBagsMax = 0;
      amount = 0;
      lastBag = -1;

      // get input and fill bags array
      for (int i = 0; i < n; i++) {
        bags[i] = scanner.nextInt();
      }

      // sort array to scroll in ascending order
      Arrays.sort(bags);
      // printDebug(bags);

      for (int i = 0; i < n; i++) { // for each bag from smallest to largest
        atualBag = bags[i];

        if (lastBag == atualBag) {
          amount++;
        } else {
          outputBagsMax = Math.max(amount, outputBagsMax);
          amount = 1;
        }

        lastBag = atualBag;
      }

      outputBagsMax = Math.max(amount, outputBagsMax);

      output(outputBagsMax, n, bags);

      n = scanner.nextInt();
    }
  }

  private static void output(int outputBagsMax, int n, Integer[] bags) {
    System.out.printf("%d\n", outputBagsMax);
    for (int i = 0; i < outputBagsMax; i++) {
      System.out.printf("%d", bags[i]);
      for (int j = i + outputBagsMax; j < n; j += outputBagsMax) {
        System.out.printf(" %d", bags[j]);
      }
      System.out.printf("\n");
    }
    System.out.printf("\n");
  }

  // private static void printDebug(Integer[] arr) {
  // for (int i = 0; i < arr.length; i++)
  // System.out.printf("%d ", arr[i]);
  // System.out.printf("\n");
  // }

}
