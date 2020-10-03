package com.kodilla.jpa.checkers;

import java.util.ArrayList;
import java.util.List;

class BoardRow {
  private List<Figure> cols = new ArrayList<>();

  BoardRow() {
    Object o;

    for (int n = 0; n < 8; n++)
      cols.add(new None());
  }

  List<Figure> getCols() {
    return cols;
  }

  @Override
  public String toString() {
    String s = "|";
    for (int n = 0; n < 8; n++)
      s += cols.get(n).toString() + "|";
    s += "\n";
    return s;
  }
}
