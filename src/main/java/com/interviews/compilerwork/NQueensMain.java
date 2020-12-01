package com.interviews.compilerwork;

public class NQueensMain {

    public static void main(String[] args) {
        int n = -1;
        if (args.length > 0) {
            try {
                n = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Argument " + args[0] + " must be an integer, i.e., number of queens.");
                System.exit(1);
            }
        }
        else {
            System.out.println("Please provide an integer in command line as the number of queens.");
            System.exit(1);
        }
        final NQueens nqueens = new NQueens(n);
        nqueens.placeQueens();
    }
}
