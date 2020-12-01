package com.interviews.compilerwork;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

//
// Finds all the solutions for N queens problem, ensuring no any three queens are collinear.
// Prints each solution, representing each queen by a (row, col) pair, row and col values being in the range [1..n]
// The solutions are printed one by one, instead of returning all the solution at once. This makes the program more
// scalable for large n values. However this mixes logic of presentation, i.e., printing solution, with model/control.
// I believe a the presentation logic could be separated from model/control using functional programming techniques,
// and "generators" which would enable us to create iterator on returned solutions, and the iterated through solutions
// and print them one by one.

public class NQueens {
    private static final String NO_SOLUTION = "No solution exists.";
    private final int n;
    // The following array will represent the (row, col) positions of N queens found as solution
    // It will contain the rows of the queens, and index of each array element is the column of that queen
    // Better to use ArrayList instead of List as interface type. We need to work with random-access data-structure in some
    // places. Other implementations of List such as LinkedList won't work.
    private final ArrayList<Integer> rowsOfQueensSofar;
    private int nSolutions = 0;

    NQueens(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException(n + " is an invalid value for dimension of chess board");
        }
        this.n = n;
        rowsOfQueensSofar = new ArrayList<Integer>(n);
    }

    void placeQueens() {
        placeRemainingQueens();
        System.out.println(this.nSolutions + " solution(s) found for " + n + " Queens problem, excluding any 3 collinear.");
    }

    private void printChessBoard() {
        if (rowsOfQueensSofar.size() == 0) {
            System.out.println(NO_SOLUTION);
            return;
        }
        for (int col = 0; col < rowsOfQueensSofar.size(); ++col ) {
            System.out.print("(" + (rowsOfQueensSofar.get(col) + 1) + ", " + (col + 1) + ") ");
        }
        System.out.println();
    }

    // Each board square is represented by a pair (row, col), row and col having values in the range [0..n-1]
    // However the final positions of queens are printed in the form (row, col) pairs, in the range [1..n]
    // The algorithm starts positioning queens for each column from column 0 to n-1.
    // This is done using a recursive algorithm.
    // However it can be changed to an iterative algorithm instead, using a stack data-structure.
    // The iterative version would be more scalable. i.e., less prone to stack-overflow exception for very large n values.
    // It could be implemented as a modified version of a DFS graph algorithm.
    private void placeRemainingQueens() {
        if (rowsOfQueensSofar.size() == n) {
            printChessBoard();
            ++nSolutions;
            return;
        }

        final int colNewQueen = rowsOfQueensSofar.size();
        for (int rowNewQueen = 0; rowNewQueen < n; ++rowNewQueen) {
            if (!isAttacked(rowNewQueen, colNewQueen) &&
                    !isAny3Collinear(rowNewQueen, colNewQueen)) {
                // Could use Stack (Deque) to use push and pop instead of add and remove
                rowsOfQueensSofar.add(rowNewQueen);
                placeRemainingQueens();
                // Remove last element
                // Watch out not to call List.remove(Object) overload. This could happen
                // if you passed Integer instead of int to remove.
                rowsOfQueensSofar.remove(rowsOfQueensSofar.size() - 1);
            }

        }


    }

    // I tested this function for 8x8, and it does not seem very precise as one can argue the answer
    // (3, 1) (5, 2) (8, 3) (4, 4) (1, 5) (7, 6) (2, 7) (6, 8)
    // has collinear queens visually, although mathematically they are not.
    // I tried also the method of zero value of triangle area of three points, i.e,, using determinant,
    // the answers were not so precise either.
    private boolean isAny3Collinear(int rowNewQueen, int colNewQueen) {
        if (rowsOfQueensSofar.size() < 2) {
            return false;
        }
        int colPrevQueen = 0;
        final Set<Double> slopes = new HashSet<>();
        for (int rowPrevQueen : rowsOfQueensSofar) {
            double slope = (colPrevQueen - colNewQueen) / (double) (rowPrevQueen - rowNewQueen);

            if (slopes.contains(slope)) {
                return true;
            }
            slopes.add(slope);
            ++colPrevQueen;
        }
        return false;
    }


    private boolean isAttacked(int rowNewQueen, int colNewQueen) {
        for (int colPrevQueen = 0; colPrevQueen < rowsOfQueensSofar.size(); ++colPrevQueen ) {
            final int rowPrevQueen = rowsOfQueensSofar.get(colPrevQueen);
            if (isAttackedHorizontally(rowNewQueen, rowPrevQueen) ||
                    isAttackedDiagonally(rowNewQueen, colNewQueen, rowPrevQueen, colPrevQueen)) {
                return true;
            }

            // No need to do vertical attack test, as we put only one queen at each column.
        }
        return false;
    }

    private boolean isAttackedHorizontally(int rowNewQueen, int rowPrevQueen) {
        return (rowNewQueen == rowPrevQueen);
    }

    private boolean isAttackedDiagonally(int rowAttackee, int colAttackee, int rowAttacker, int colAttacker) {
        final int deltaRow = rowAttackee - rowAttacker;
        final int deltaCol = colAttackee - colAttacker;
        if ((deltaRow == deltaCol) || (deltaRow == -deltaCol)) {
            return true;
        }
        return false;
    }
}