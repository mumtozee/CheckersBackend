package com.ruchekers;

class Piece {
  private final Color color;
  private boolean isKing;

  Piece() {
    color = Color.WHITE;
    isKing = false;
  }

  Piece(Color color, boolean isKing) {
    this.color = color;
    this.isKing = isKing;
  }

  public boolean isKing() {
    return isKing;
  }

  public Color getColor() {
    return color;
  }

  public void makeKing() {
    isKing = true;
  }
}
