package com.kodilla.jpa.checkers;

class None implements Figure {

  @Override
  public FigureColor getColor() {
    return FigureColor.NONE;
  }

  @Override
  public String toString() {
    return "  ";
  }
}
