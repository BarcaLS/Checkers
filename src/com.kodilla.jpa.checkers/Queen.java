package com.kodilla.jpa.checkers;

class Queen implements Figure {

  private FigureColor color;

  Queen(FigureColor color) {
    this.color = color;
  }

  @Override
  public FigureColor getColor() {
    return color;
  }

  @Override
  public String toString() {
    return getColorSymbol() + "Q";
  }

  private String getColorSymbol() {
    return (color == FigureColor.WHITE) ? "w" : "b";
  }
}
