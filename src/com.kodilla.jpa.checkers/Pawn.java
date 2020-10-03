package com.kodilla.jpa.checkers;

class Pawn implements Figure {

  private FigureColor color;

  Pawn(FigureColor color) {
    this.color = color;
  }

  @Override
  public FigureColor getColor() {
    return color;
  }

  @Override
  public String toString() {
    return getColorSymbol() + "P";
  }

  private String getColorSymbol() {
    return (color == FigureColor.WHITE) ? "w" : "b";
  }
}
