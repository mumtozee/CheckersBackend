package com.ruchekers;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

public class PieceTest {
  private final Piece piece = new Piece(Color.BLACK, false);

  @Test
  public void makeKingTest() {
    piece.makeKing();
    Assertions.assertThat(piece.isKing()).isEqualTo(true);
  }

  @Test
  public void getColorTest() {
    Assertions.assertThat(piece.getColor()).isEqualTo(Color.BLACK);
  }
}
