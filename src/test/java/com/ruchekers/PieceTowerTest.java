package com.ruchekers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

public class PieceTowerTest {
  private static PieceTower tower = new PieceTower();

  @AfterEach
  public void cleanTower() {
    tower = new PieceTower();
  }

  @Test
  public void getContentStringTest() {
    tower.pushPiece(new Piece(Color.WHITE, true));
    tower.pushPiece(new Piece(Color.BLACK, false));
    tower.pushPiece(new Piece(Color.WHITE, false));
    tower.pushPiece(new Piece(Color.BLACK, true));
    Assertions.assertThat(tower.getContentString()).isEqualTo("WbwB");
  }
}
