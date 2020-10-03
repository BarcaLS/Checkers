package com.kodilla.jpa.checkers;

import java.util.Scanner;

class App {

  public static void main(String[] args) {
    Board board = new Board();
    board.populate();
    WelcomeUser();
    while(true) {
      System.out.println(board);
      nextMove(board);
      if(board.checkGameOver(board.nextMove)) {
        System.out.println(board);
        gameOver(board);
      }
    }
  }

  private static void nextMove(Board board) {
    System.out.println("Next move: " + board.nextMove.toString());
    if(board.nextMove == FigureColor.WHITE)
      playerMove(board);
    else
      ComputerMove(board);
  }

  private static void playerMove(Board board) {
    Scanner scanner = new Scanner(System.in);

    System.out.print("Make your move.\ncurrentCol? ");
    int currentCol = scanner.nextInt();
    System.out.print("currentRow? ");
    int currentRow = scanner.nextInt();
    System.out.print("targetCol? ");
    int targetCol = scanner.nextInt();
    System.out.print("targetRow? ");
    int targetRow = scanner.nextInt();
    System.out.print("\n");

    board.moveFigure(currentCol, currentRow, targetCol, targetRow);
  }

  private static void ComputerMove(Board board) {
    System.out.println("Computer moved.\n");
    board.nextMove = FigureColor.WHITE;
  }

  private static void WelcomeUser() {
    System.out.println("Hello, let's play. You're playing with white figures.\n");
  }

  private static void gameOver (Board board) {
    System.out.println(board.nextMove + " lost!");
    System.exit(0);
  }
}
