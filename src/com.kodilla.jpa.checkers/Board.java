package com.kodilla.jpa.checkers;

import java.util.ArrayList;
import java.util.List;

class Board {
  private List<BoardRow> rows = new ArrayList<>();
  public FigureColor nextMove = FigureColor.WHITE;

  Board() {
    for (int n = 0; n < 8; n++)
      rows.add(new BoardRow());
  }

  public Figure getFigure(int col, int row) {
    return rows.get(row).getCols().get(col);
  }

  public void setFigure(int col, int row, Figure figure) {
    rows.get(row).getCols().set(col, figure);
  }

  public void populate() {
    for (int col=1; col<8; col=col+2) {
      this.setFigure(col, 0, new Pawn(FigureColor.BLACK));
      this.setFigure(col, 2, new Pawn(FigureColor.BLACK));
      this.setFigure(col, 6, new Pawn(FigureColor.WHITE));
    }
    for (int col=0; col<8; col=col+2) {
      this.setFigure(col, 1, new Pawn(FigureColor.BLACK));
      this.setFigure(col, 7, new Pawn(FigureColor.WHITE));
      this.setFigure(col, 5, new Pawn(FigureColor.WHITE));
    }
  }

  public boolean moveFigure(int currentCol, int currentRow, int targetCol, int targetRow) {

    // common for every figure
    if (checkOutOfBorders(currentCol, currentRow, targetCol, targetRow)) return false;
    if (checkWrongColor(currentCol, currentRow)) return false;
    if (checkNoMove(currentCol, currentRow, targetCol, targetRow)) return false;
    if (checkTargetOccupied(targetCol, targetRow)) return false;

    // we're moving pawn
    if(getFigure(currentCol,currentRow) instanceof Pawn) {
      if(!tryMovePawnWithHit(currentCol, currentRow, targetCol, targetRow)) {
        if (isMovePawnWithoutHitNotPossible(currentCol, currentRow, targetCol, targetRow))
          return false;
        else
          doMove(currentCol, currentRow, targetCol, targetRow);
      }
      CheckBecomeQueen(targetCol, targetRow);
    }

    // we're moving queen
    if(getFigure(currentCol,currentRow) instanceof Queen) {
      if(isMoveQueenNotDiagonal(currentCol, currentRow, targetCol, targetRow))
        return false;
      if (!moveQueen(currentCol, currentRow, targetCol, targetRow))
        return false;
    }

    switchPlayer();
    return true;
  }

  private boolean isMoveQueenNotDiagonal(int currentCol, int currentRow, int targetCol, int targetRow) {
    if(!(currentCol - targetCol == currentRow - targetRow || currentCol - targetCol == targetRow - currentRow ||
            targetCol - currentCol == currentRow - targetRow || targetCol - currentCol == targetRow - currentRow)) {
      System.out.println("Move is not diagonal.\n");
      return true;
    }
    return false;
  }

  private boolean moveQueen(int currentCol, int currentRow, int targetCol, int targetRow) {
    int checkedCol = currentCol;
    int checkedRow = currentRow;
    int lastCheckedCol;
    int lastCheckedRow;
    boolean checkFieldAfterOpponentFigure = false;

    boolean horizontalIncrease = isQueenMoveIncrease(currentCol - targetCol);
    boolean verticalIncrease = isQueenMoveIncrease(currentRow - targetRow);

    while(checkedCol != targetCol) {
      lastCheckedCol = checkedCol;
      lastCheckedRow = checkedRow;
      checkedCol = moveQueenNextIteration(checkedCol, horizontalIncrease);
      checkedRow = moveQueenNextIteration(checkedRow, verticalIncrease);

      if (isQueenMoveBlockedByOurFigure(checkedCol, checkedRow)) return false;
      if(getFigure(checkedCol,checkedRow).getColor() != FigureColor.NONE) {
        if (checkFieldAfterOpponentFigure) {
          System.out.println("There're two figures one after another on your way.\n");
          return false;
        }
        checkFieldAfterOpponentFigure = true;
        continue;
      }
      if(checkFieldAfterOpponentFigure) { // we are on empty field and after opponent's figure so move with hit
        setFigure(lastCheckedCol, lastCheckedRow, new None());
        setFigure(checkedCol, checkedRow, new Queen(getFigure(currentCol, currentRow).getColor()));
        setFigure(currentCol, currentRow, new None());
        return true;
      }
    }
    doMove(currentCol,currentRow,targetCol,targetRow);
    return true;
  }

  private boolean isQueenMoveBlockedByOurFigure(int checkedCol, int checkedRow) {
    if(getFigure(checkedCol,checkedRow).getColor() == nextMove) {
      System.out.println("Another of your figures is on the way.\n");
      return true;
    }
    return false;
  }

  private int moveQueenNextIteration(int checked, boolean increase) {
    if (increase)
      checked++;
    else
      checked--;
    return checked;
  }

  private boolean isQueenMoveIncrease(int difference) {
    boolean increase;
    if (difference > 0)
      increase = false;
    else
      increase = true;
    return increase;
  }

  private void CheckBecomeQueen(int targetCol, int targetRow) {
    if(targetRow == 0 || targetRow == 7)
      setFigure(targetCol, targetRow, new Queen(getFigure(targetCol, targetRow).getColor()));
  }

  public boolean checkGameOver(FigureColor colorToCheck) {
    for (int a = 0; a < 8; a++) {
      for (int b = 0; b < 8; b++) {
        if (rows.get(a).getCols().get(b).getColor() == colorToCheck)
          return false;
      }
    }
    return true;
  }

  private void switchPlayer() {
    if(nextMove == FigureColor.WHITE)
      nextMove = FigureColor.BLACK;
    else
      nextMove = FigureColor.WHITE;
  }

  private void doMove(int currentCol, int currentRow, int targetCol, int targetRow) {
    setFigure(targetCol,targetRow,getFigure(currentCol,currentRow));
    setFigure(currentCol,currentRow,new None());
    System.out.println("Move done.\n");
  }

  private boolean isMovePawnWithoutHitNotPossible(int currentCol, int currentRow, int targetCol, int targetRow) {
    if(!(currentCol+1 == targetCol || currentCol-1 == targetCol)) {
      System.out.println("Move is not diagonal.\n");
      return true;
    }
    if (checkWrongDirection(currentCol, currentRow, targetRow, FigureColor.WHITE, currentRow - 1, "White pawn can move only up.")) return true;
    if (checkWrongDirection(currentCol, currentRow, targetRow, FigureColor.BLACK, currentRow + 1, "Black pawn can move only down.")) return true;
    return false;
  }

  private boolean checkWrongDirection(int currentCol, int currentRow, int targetRow, FigureColor currentColor, int rowToTry, String errorMessage) {
    if (getFigure(currentCol, currentRow).getColor() == currentColor && rowToTry != targetRow) {
      System.out.println(errorMessage);
      return true;
    }
    return false;
  }

  private boolean tryMovePawnWithHit(int currentCol, int currentRow, int targetCol, int targetRow) {
    boolean result = false;
    result = result || tryToMovePawnWithHitInDirection(currentCol, currentRow, targetCol, targetRow, currentCol + 2, currentRow - 2, currentCol + 1, currentRow - 1);
    result = result || tryToMovePawnWithHitInDirection(currentCol, currentRow, targetCol, targetRow, currentCol+2, currentRow+2, currentCol+1, currentRow+1);
    result = result || tryToMovePawnWithHitInDirection(currentCol, currentRow, targetCol, targetRow, currentCol-2, currentRow+2, currentCol-1, currentRow+1);
    result = result || tryToMovePawnWithHitInDirection(currentCol, currentRow, targetCol, targetRow, currentCol-2, currentRow-2, currentCol-1, currentRow-1);
    if(result)
      System.out.println("Hit done.\n");
    return result;
  }

  private boolean tryToMovePawnWithHitInDirection(int currentCol, int currentRow, int targetCol, int targetRow, int possibleCol, int possibleRow, int colToHit, int rowToHit) {
    if (possibleCol == targetCol && possibleRow == targetRow &&
            getFigure(colToHit, rowToHit).getColor() != FigureColor.NONE &&
            getFigure(colToHit, rowToHit).getColor() != getFigure(currentCol, currentRow).getColor()) {
      doMovePawnWithHit(currentCol, currentRow, targetCol, targetRow, colToHit, rowToHit);
      return true;
    }
    return false;
  }

  private void doMovePawnWithHit(int currentCol, int currentRow, int targetCol, int targetRow, int toRemoveCol, int toRemoveRow) {
    setFigure(toRemoveCol, toRemoveRow, new None());
    setFigure(targetCol, targetRow, new Pawn(getFigure(currentCol, currentRow).getColor()));
    setFigure(currentCol, currentRow, new None());
  }

  private boolean checkTargetOccupied(int targetCol, int targetRow) {
    if(getFigure(targetCol,targetRow).getColor() != FigureColor.NONE) {
      System.out.println("Target field is occupied by figure.\n");
      return true;
    }
    return false;
  }

  private boolean checkNoMove(int currentCol, int currentRow, int targetCol, int targetRow) {
    if(currentCol == targetCol && currentRow == targetRow) {
      System.out.println("Current field and target field are the same.\n");
      return true;
    }
    return false;
  }

  private boolean checkWrongColor(int currentCol, int currentRow) {
    if(getFigure(currentCol,currentRow).getColor() == FigureColor.BLACK) {
      System.out.println("You can't move opponent's figure.\n");
      return true;
    }
    else if(getFigure(currentCol,currentRow).getColor() == FigureColor.NONE) {
      System.out.println("There is no figure.\n");
      return true;
    }
    return false;
  }

  private boolean checkOutOfBorders(int currentCol, int currentRow, int targetCol, int targetRow) {
    if(currentCol < 0 || currentCol > 8 || currentRow < 0 || currentRow > 8 || targetCol < 0 || targetCol > 8 || targetRow < 0 || targetRow > 8) {
      System.out.println("There's no such field.\n");
      return true;
    }
    return false;
  }

  @Override
  public String toString() {
    String s = "__|00|01|02|03|04|05|06|07|\n";
    for (int n = 0; n < 8; n++)
      s += n + " " + rows.get(n).toString();
    s += "  |--|--|--|--|--|--|--|--|\n";
    return s;
  }
}
