package com.ruchekers;

import com.ruchekers.exceptions.AnyErrorException;
import com.ruchekers.exceptions.BusyCellException;
import com.ruchekers.exceptions.InvalidMoveException;
import com.ruchekers.exceptions.WhiteCellException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class GameBoardTest {
  private final static GameBoard board = new GameBoard();

  @BeforeEach
  public void fillTheBoard() {
    board.setTower(1, 1, new PieceTower());
    board.getTower(1, 1).pushPiece(new Piece(Color.WHITE, false));
    board.getTower(1, 1).pushPiece(new Piece(Color.BLACK, false));

    board.setTower(3, 1, new PieceTower());
    board.getTower(3, 1).pushPiece(new Piece(Color.WHITE, false));
    board.getTower(3, 1).pushPiece(new Piece(Color.BLACK, true));

    board.setTower(2, 2, new PieceTower());
    board.getTower(2, 2).pushPiece(new Piece(Color.BLACK, false));
    board.getTower(2, 2).pushPiece(new Piece(Color.WHITE, false));

    board.setTower(7, 1, new PieceTower());
    board.getTower(7, 1).pushPiece(new Piece(Color.BLACK, true));
    board.getTower(7, 1).pushPiece(new Piece(Color.BLACK, false));

    board.setTower(5, 1, new PieceTower());
    board.getTower(5, 1).pushPiece(new Piece(Color.BLACK, true));
    board.getTower(5, 1).pushPiece(new Piece(Color.BLACK, true));
  }

  @AfterEach
  public void cleanTheBoard() {
    for (int i = 1; i < board.getBoardWidth(); ++i) {
      for (int j = 1; j < board.getBoardLength(); ++j) {
        board.setTower(i, j, null);
      }
    }
  }

  @Test
  public void getPossibleVictimsTest() {
    boolean hasVictim = false;
    ArrayList<Pair> victims = board.getPossibleVictims(3, 1);
    if (victims != null) {
      for (Pair pair : victims) {
        if (pair.getFirst() == 2 && pair.getSecond() == 2) {
          hasVictim = true;
          break;
        }
      }
    }
    victims = board.getPossibleVictims(5, 1);
    Assertions.assertThat(hasVictim && victims == null).isEqualTo(true);
  }

  @Test
  public void beatWithTowerTest() throws BusyCellException, WhiteCellException, AnyErrorException, InvalidMoveException {
    board.beatWithTower(3, 1, 1, 3);
    Assertions.assertThat(board.getTower(2, 2).getColor()).isEqualTo(Color.WHITE);
  }

  @Test
  public void moveTowerTest() throws BusyCellException,
          WhiteCellException, AnyErrorException, InvalidMoveException {
    PieceTower tmpTower = board.getTower(7, 1);
    board.moveTower(7, 1, 5, 3);
    Assertions.assertThat(board.getTower(5, 3) == tmpTower).isEqualTo(true);
  }

  @Test
  public void moveTowerThrowsTest() {
    Assertions.assertThatThrownBy(() -> board.moveTower(1, 1, 1, 2))
            .isInstanceOf(WhiteCellException.class);
  }

  @Test
  public void beatWithKingTest() throws BusyCellException, WhiteCellException,
          AnyErrorException, InvalidMoveException {
    board.beatWithTower(1, 1, 3, 3);
    board.beatWithKing(5, 1, 2, 4);
    Assertions.assertThat(board.getTower(3, 3).size()).isEqualTo(2);
  }
}
