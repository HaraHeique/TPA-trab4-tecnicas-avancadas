/* Main.java
* UVa 12405 -- Scarecrow
* Autores: David Vilaça, Harã Heique e Larissa Motta
 */

import java.util.Scanner;

public class Main {

  private static final Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) {
    // get number of test cases
    int t = scanner.nextInt();
    // aux variables
    int n, index, answer;
    char c;
    String line;

    // for each case
    for (int numLines = 0; numLines < t; numLines++) {
      // acc answer value of case
      answer = 0;
      // get number of characters
      n = scanner.nextInt();
      // discard \n
      scanner.nextLine();
      // get characters line of case
      line = scanner.nextLine();

      // for each character of line
      index = 0;
      while (index < n) {
        // current character
        c = line.charAt(index);
        if (c == '.') { // if character is '.' then is crop-growing spot
          answer++;
          index += 3;
        } else { // if character is '#' then is infertile region
          index++;
        }
      }
      // output answer of atual case
      System.out.printf("Case %d: %d\n", numLines + 1, answer);
    }
  }
}
