package expr_eval;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Evaluator {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.print("\nEnter the expression, or hit return to quit => ");
			String line = sc.nextLine();
			if (line.length() == 0) {
				break;
			}
			Expression expr = new Expression(line);
			expr.buildVariable();

			System.out.print("Enter character values file name, or hit return if no characters => ");
			line = sc.nextLine();
			if (line.length() != 0) {
				Scanner scfile = new Scanner(new File(line));
				expr.loadVariableValues(scfile);

			}
			System.out.println("Value of expression = " + expr.evaluate());
		}
		sc.close();
	}
}