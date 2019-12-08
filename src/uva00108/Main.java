/* Main.java
* UVa 00108 -- Maximum Sum
* Autores: David Vilaça, Harã Heique e Larissa Motta
*/

import java.util.Scanner;

public class Main {

  private static final Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) {
    int n, sum[][], square[][], result;
    while (scanner.hasNext()) {
      n = scanner.nextInt();
      sum = new int[n][n]; // array to cache sum
      square = new int[n][n]; // square matrix
      result = 0; // test case result
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
          square[i][j] = scanner.nextInt(); // fill matrix
          if (i == 0 && j == 0) // on begin start result with first element of matrix
            result = square[i][j];

          for (int subI = i; subI > -1; subI--) {
            for (int subJ = j; subJ > -1; subJ--) {

              sum[subI][subJ] = 0; // start sum of sub matrix

              /*
               * SUM ALL SUB MATRIX
               */
              if (subI + 1 <= i && subJ + 1 <= j) {
                sum[subI][subJ] += sum[subI + 1][subJ + 1];
              }

              for (int z = subI; z <= i; z++)
                sum[subI][subJ] += square[z][subJ];

              for (int y = subJ; y <= j; y++)
                sum[subI][subJ] += square[subI][y];

              sum[subI][subJ] -= square[subI][subJ];

              // if result is smaller then exchange
              if (sum[subI][subJ] > result)
                result = sum[subI][subJ];
            }
          }
        }
      }
      System.out.println(result);
      // debugPrint(square);
    }
  }

  // private static void debugPrint(int[][] matrix) {
  // System.out.printf("----------------------------\n");
  // for (int i = 0; i < matrix.length; i++) {
  // for (int j = 0; j < matrix[i].length; j++) {
  // System.out.printf("%d ", matrix[i][j]);
  // }
  // System.out.printf("\n");
  // }
  // System.out.printf("----------------------------\n");
  // }

}
