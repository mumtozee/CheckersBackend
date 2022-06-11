package com.ruchekers;

import java.util.LinkedList;

/**
 * The main figure on the game board.
 */
class PieceTower {
  private final LinkedList<Piece> tower;
  private int lastBeatLetterOrder;
  private int lastBeatDigitOrder;

  PieceTower() {
    tower = new LinkedList<>();
    lastBeatDigitOrder = 0;
    lastBeatLetterOrder = 0;
  }

  PieceTower(LinkedList<Piece> tower, int i, int j) {
    this.tower = tower;
    this.lastBeatLetterOrder = i;
    this.lastBeatDigitOrder = j;
  }

  public void pushPiece(Piece piece) {
    tower.addLast(piece);
  }

  public Piece popPiece() {
    return tower.pollFirst();
  }

  public void makeKing() {
    tower.getFirst().makeKing();
  }

  public boolean isKing() {
    return tower.getFirst().isKing();
  }

  public Color getColor() {
    return tower.getFirst().getColor();
  }

  public int size() {
    return tower.size();
  }

  public boolean isEmpty() {
    return size() == 0;
  }

  public int getLastBeatLetterOrder() {
    return lastBeatLetterOrder;
  }

  public void setLastBeatLetterOrder(int lastBeatLetterOrder) {
    this.lastBeatLetterOrder = lastBeatLetterOrder;
  }

  public int getLastBeatDigitOrder() {
    return lastBeatDigitOrder;
  }

  public void setLastBeatDigitOrder(int lastBeatDigitOrder) {
    this.lastBeatDigitOrder = lastBeatDigitOrder;
  }

  /**
   * Gets the string representation of the tower.
   * 'w' - simple white piece
   * 'W' - king white piece
   * 'b' - simple black piece
   * 'B' - king black piece
   * for example "WbbBwwB"
   *
   * @return a {@code String}: representation of the contents of the tower
   */
  public String getContentString() {
    StringBuilder contents = new StringBuilder();
    for (Piece piece : tower) {
      if (piece.getColor().equals(Color.WHITE)) {
        contents.append(piece.isKing() ? 'W' : 'w');
      } else if (piece.getColor().equals(Color.BLACK)) {
        contents.append(piece.isKing() ? 'B' : 'b');
      }
    }
    return contents.toString();
  }
}
