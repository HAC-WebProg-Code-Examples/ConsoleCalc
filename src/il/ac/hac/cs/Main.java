package il.ac.hac.cs;

import com.powercalc.Power;
import com.powercalc.PowerCalc;
import il.ac.hac.cs.calc.*;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    record StringAndStatement(String opString, Statement statement) {

        @Override
        public String opString() {
            return opString;
        }

        @Override
        public Statement statement() {
            return statement;
        }
    }

    public static void main(String[] args) {

        PowerCalc.registerDefaults();

        // Instead of 'pow' I want to use the double starlet operator:
        StatementFactory.register("**", Power::new);

        Scanner scanner = new Scanner(System.in);

        ArrayList<StringAndStatement> statements = new ArrayList<>();

        String opString;
        int leftIndex, rightIndex;
        Statement left, right;

        while (true) {

            try {
                printMenu(statements);
                leftIndex = scanner.nextInt();
                if (leftIndex == statements.size() + 1) {
                    break;
                }
                opString = scanner.next();
                rightIndex = scanner.nextInt();
                left = findOrMakeStatement(scanner, statements, leftIndex, "left");
                right = findOrMakeStatement(scanner, statements, rightIndex, "right");
                if (left == null || right == null) {
                    break;
                } else {
                    Statement newStatement = StatementFactory.create(opString, left, right);
                    statements.add(new StringAndStatement(String.format("%s %s %s", left, opString, right), newStatement));
                    System.out.println("Result: " + newStatement);
                }

                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("I can't understand your choice. Please try again.");
            } catch (StatementNotRegisteredException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static Statement findOrMakeStatement(Scanner scanner, ArrayList<StringAndStatement> statements, int index, String operandName) {
        Statement statement = null;
        if (index < statements.size()) {
            statement = statements.get(index).statement();
        } else if (index == statements.size()) {
            statement = readScalar(scanner, operandName);
        }
        return statement;
    }

    private static Statement readScalar(Scanner scanner, String operandName) {
        System.out.printf("Enter number for %s: ", operandName);
        double num = scanner.nextDouble();
        return Scalar.valueOf(num);
    }

    private static void printMenu(ArrayList<StringAndStatement> statements) {
        System.out.println("\nInstructions\nEnter x op y such that x and y are numbers from the following list and op is one of +,-,*,\\,**,root:\n");
        for (int i = 0; i < statements.size(); ++i) {
            System.out.printf("%d. %s%n", i, statements.get(i).opString());
        }
        System.out.println(statements.size() + ". new number");
        System.out.println((statements.size() + 1) + ". quit");
    }
}
