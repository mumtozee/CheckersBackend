package com.ruchekers;

import com.ruchekers.exceptions.AnyErrorException;
import com.ruchekers.exceptions.BusyCellException;
import com.ruchekers.exceptions.InvalidMoveException;
import com.ruchekers.exceptions.WhiteCellException;

import java.util.ArrayList;

/**
 * A class containing the main logic of the program. It simulates the real game board.
 */
public final class GameBoard {
  private final int boardWidth = 9;
  private final int boardLength = 9;

  public int getBoardWidth() {
    return boardWidth;
  }

  public int getBoardLength() {
    return boardLength;
  }

  private final PieceTower[] board;

  GameBoard() {
    board = new PieceTower[boardLength * boardWidth + 1];
  }

  public PieceTower getTower(int i, int j) {
    return board[j * boardWidth + i];
  }

  public void setTower(int i, int j, PieceTower newTower) {
    board[j * boardWidth + i] = newTower;
  }

  /**
   * Checks if the given cell is valid. If it is not, throws an exception.
   *
   * @param newI : {@code int} - letter number of the column of the given cell.
   * @param newJ : {@code int} - number of the row of the given cell.
   * @throws BusyCellException  - if the new position is busy
   * @throws WhiteCellException - if the new position is a white cell
   * @throws AnyErrorException  - if any other errors occur
   */
  private void validateNewPos(int newI, int newJ) throws BusyCellException,
          WhiteCellException, AnyErrorException {
    if (newI >= boardWidth || newJ >= boardLength) {
      throw new AnyErrorException("error");
    }
    if (getTower(newI, newJ) != null) {
      throw new BusyCellException("busy cell");
    }
    if (isWhiteCell(newI, newJ)) {
      throw new WhiteCellException("white cell");
    }
  }

  /**
   * Returns all possible towers to be beaten by the towers of the given color.
   *
   * @param color color of the towers that can beat
   * @return a 2d ArrayList of pairs containing the coordinates of towers to be beaten.
   */
  private ArrayList<Pair>[] getAllPossibleVictims(Color color) {
    ArrayList<Pair>[] allVictims = new ArrayList[boardWidth * boardLength];
    for (int i = 1; i < boardWidth; ++i) {
      for (int j = 1; j < boardLength; ++j) {
        if (getTower(i, j) != null && getColor(i, j).equals(color)) {
          allVictims[j * boardWidth + i] = getPossibleVictims(i, j);
        }
      }
    }
    return allVictims;
  }

  /**
   * Defines if other towers of the same color as the given one have possible towers to beat.
   *
   * @param i:{@code          int} letter number of the position of the given tower
   * @param j:{@code          int} row number of the position of the given tower
   * @param allVictims:{@code ArrayList<Pair>[]} all towers that can be beaten
   *                          by the towers of the same color as the given one has
   * @return true if other towers of the same color have possible towers to beat
   * false otherwise
   */
  private boolean haveOthersVictims(int i, int j, ArrayList<Pair>[] allVictims) {
    for (int k = 0; k < boardWidth; ++k) {
      for (int l = 0; l < boardLength; ++l) {
        if (l != j || k != i) {
          if (allVictims[l * boardWidth + k] != null) {
            return true;
          }
        }
      }
    }
    return false;
  }

  private void annihilateLastBeaten() {
    for (int i = 1; i < boardWidth; ++i) {
      for (int j = 1; j < boardLength; ++j) {
        if (getTower(i, j) != null) {
          getTower(i, j).setLastBeatLetterOrder(0);
          getTower(i, j).setLastBeatDigitOrder(0);
        }
      }
    }
  }

  /**
   * Moves the given tower to the new position.
   * Checks if the new position is valid, if it is not, throws an exception.
   *
   * @param oldI:{@code int} letter number of the position of the given tower
   * @param oldJ:{@code int} row number of the position of the given tower
   * @param newI:{@code int} letter number of the new position
   * @param newJ:{@code int} letter number of the new position
   * @throws BusyCellException    - if the new position is busy
   * @throws WhiteCellException   - if the new position is a white cell
   * @throws InvalidMoveException - if the tower piece has possible victims or the other ones do,
   * but the user skips the beating and moves
   * @throws AnyErrorException    - if any other errors occur
   */
  public void moveTower(int oldI, int oldJ, int newI, int newJ) throws BusyCellException,
          WhiteCellException, InvalidMoveException, AnyErrorException {
    validateNewPos(newI, newJ);
    ArrayList<Pair>[] allVictims = getAllPossibleVictims(getColor(oldI, oldJ));
    if (allVictims[oldJ * boardWidth + oldI] != null || haveOthersVictims(oldI, oldJ, allVictims)) {
      throw new InvalidMoveException("invalid move");
    } else {
      Color currColor = getColor(oldI, oldJ);
      if (currColor.equals(Color.WHITE) && newJ == boardLength - 1) {
        board[oldJ * boardWidth + oldI].makeKing();
      } else if (currColor == Color.BLACK && newJ == 1) {
        board[oldJ * boardWidth + oldI].makeKing();
      }
      setTower(newI, newJ, getTower(oldI, oldJ));
      board[oldJ * boardWidth + oldI] = null;
      annihilateLastBeaten();
    }
  }

  /**
   * Moves a king tower and beats one tower between the new position and the given one.
   * Gets all possible victims around the given tower,
   * and finds the one which is between the given position and the new one.
   *
   * @param oldI:{@code int} letter number of the position of the given piece
   * @param oldJ:{@code int} row number of the position of the given piece
   * @param newI:{@code int} letter number of the new position
   * @param newJ:{@code int} letter number of the new position
   * @throws BusyCellException  - if the new position is busy
   * @throws WhiteCellException - if the new position is a white cell
   * @throws AnyErrorException  - if the list of victims is empty or in the given direction
   * there are no towers to beat
   */
  public void beatWithKing(int oldI, int oldJ, int newI, int newJ) throws BusyCellException,
          WhiteCellException, InvalidMoveException, AnyErrorException {
    validateNewPos(newI, newJ);
    ArrayList<Pair> victims = getPossibleVictims(oldI, oldJ);
    PieceTower oldTower = getTower(oldI, oldJ);
    if (victims != null) {
      int maxI = Math.max(oldI, newI);
      int minI = Math.min(oldI, newI);
      int maxJ = Math.max(oldJ, newJ);
      int minJ = Math.min(oldJ, newJ);
      int k = 0;
      for (; k < victims.size(); ++k) {
        if (victims.get(k).getFirst() > minI && victims.get(k).getFirst() < maxI
                && victims.get(k).getSecond() > minJ && victims.get(k).getSecond() < maxJ) {
          break;
        }
      }
      if (k < victims.size()) {
        PieceTower victim = getTower(victims.get(k).getFirst(), victims.get(k).getSecond());
        oldTower.pushPiece(victim.popPiece());
        annihilateLastBeaten();
        oldTower.setLastBeatLetterOrder(victims.get(k).getFirst());
        oldTower.setLastBeatDigitOrder(victims.get(k).getSecond());
        setTower(newI, newJ, oldTower);
        board[oldJ * boardWidth + oldI] = null;
        if (victim.isEmpty()) {
          board[victims.get(k).getSecond() * boardWidth + victims.get(k).getFirst()] = null;
        }
      } else {
        throw new AnyErrorException("error");
      }
    } else {
      throw new AnyErrorException("error");
    }
  }

  /**
   * Beats a tower between the given position and the new position and moves the given tower.
   * Used only for simple towers. The generalisation of this method is the method {@code beatWithKing()}.
   *
   * @param oldI:{@code int} letter number of the position of the given tower
   * @param oldJ:{@code int} row number of the position of the given tower
   * @param newI:{@code int} letter number of the new position
   * @param newJ:{@code int} letter number of the new position
   * @throws BusyCellException  - if the new position is busy
   * @throws WhiteCellException - if the new position is a white cell
   * @throws AnyErrorException  - if the middle cell contains no tower or contains a tower of the same color
   * as the given one
   */
  public void beatWithTower(int oldI, int oldJ, int newI, int newJ) throws BusyCellException,
          WhiteCellException, InvalidMoveException, AnyErrorException {
    validateNewPos(newI, newJ);
    PieceTower oldTower = getTower(oldI, oldJ);
    PieceTower victim = getTower((oldI + newI) / 2, (oldJ + newJ) / 2);
    if (victim == null || victim.getColor().equals(getColor(oldI, oldJ))) {
      throw new AnyErrorException("error");
    }
    oldTower.pushPiece(victim.popPiece());
    annihilateLastBeaten();
    oldTower.setLastBeatLetterOrder((oldI + newI) / 2);
    oldTower.setLastBeatDigitOrder((oldJ + newJ) / 2);
    Color currColor = getColor(oldI, oldJ);
    if (currColor.equals(Color.WHITE) && newJ == boardLength - 1) {
      getTower(oldI, oldJ).makeKing();
    } else if (currColor == Color.BLACK && newJ == 1) {
      getTower(oldI, oldJ).makeKing();
    }
    setTower(newI, newJ, oldTower);
    board[oldJ * boardWidth + oldI] = null;
    if (victim.isEmpty()) {
      board[(oldI + newI) / 2 + (oldJ + newJ) * boardWidth / 2] = null;
    }
  }

  private boolean isWhiteCell(int i, int j) {
    return i % 2 == 0 && j % 2 == 1 || i % 2 == 1 && j % 2 == 0;
  }

  /**
   * A method that returns indices of all towers around the given tower that can be beaten.
   *
   * @param i: Integer - letter order of the given tower
   * @param j: Integer - number of the row of the given tower
   * @return an array-list of pairs: indices of possible towers to be beaten.
   */
  public ArrayList<Pair> getPossibleVictims(int i, int j) {
    ArrayList<Pair> positions = new ArrayList<>();
    PieceTower currTower = getTower(i, j);
    Color oppositeColor = getOppositeColor(currTower.getColor());

    if (isKing(i, j)) {
      for (int k = -1; k < 2; k += 2) {
        for (int l = -1; l < 2; l += 2) {
          for (int m = 1; isCellInBoard(i + m * k, j + m * l); ++m) {
            PieceTower curTower = getTower(i + m * k, j + m * l);
            if (curTower != null) {
              if (getColor(i + m * k, j + m * l).equals(oppositeColor)
                      && (currTower.getLastBeatLetterOrder() != i + m * k
                      || currTower.getLastBeatDigitOrder() != j + m * l)) {
                if (isCellInBoard(i + (m + 1) * k, j + (m + 1) * l)
                        && getTower(i + (m + 1) * k, j + (m + 1) * l) == null) {
                  positions.add(new Pair(i + m * k, j + m * l));
                } else {
                  break;
                }
              } else {
                break;
              }
            }
          }
        }
      }
    } else {
      for (int k = -1; k < 2; k += 2) {
        if (i + k < 1 || i + k >= boardWidth) {
          continue;
        }
        for (int l = -1; l < 2; l += 2) {
          if (j + l < 1 || j + l >= boardLength) {
            continue;
          }
          if (getTower(i + k, j + l) != null) {
            if (getColor(i + k, j + l).equals(oppositeColor)) {
              if (isCellInBoard(i + 2 * k, j + 2 * l) && getTower(i + 2 * k, j + 2 * l) == null
                      && (currTower.getLastBeatLetterOrder() != i + k || currTower.getLastBeatDigitOrder() != j + l)) {
                positions.add(new Pair(i + k, j + l));
              }
            }
          }
        }
      }
    }

    return positions.isEmpty() ? null : positions;
  }

  private boolean isCellInBoard(int i, int j) {
    return i >= 1 && i < boardWidth && j >= 1 && j < boardLength;
  }

  private Color getColor(int i, int j) {
    return board[j * boardWidth + i].getColor();
  }

  private Color getOppositeColor(Color color) {
    return (color.equals(Color.WHITE)) ? Color.BLACK : Color.WHITE;
  }

  private boolean isKing(int i, int j) {
    return board[j * boardWidth + i].isKing();
  }
}
