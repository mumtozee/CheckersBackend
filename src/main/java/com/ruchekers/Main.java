package com.ruchekers;

import com.ruchekers.exceptions.AnyErrorException;
import com.ruchekers.exceptions.BusyCellException;
import com.ruchekers.exceptions.InvalidMoveException;
import com.ruchekers.exceptions.WhiteCellException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Main {
  private Main() {

  }

  private static final String POS_LETTERS_CAP = "0ABCDEFGH";
  private static final String POS_LETTERS = "0abcdefgh";
  private static final String DIGITS = "012345678";
  private static final String TOWER_REGEX_STRING = "([a-hA-H][1-8][_][bwBW]+)";
  private static final Pattern TOWER_REGEX = Pattern.compile(TOWER_REGEX_STRING);

  private static int getLetterOrder(String towerString) {
    int letterCapIdx = POS_LETTERS_CAP.indexOf(towerString.charAt(0));
    int letterIdx = POS_LETTERS.indexOf(towerString.charAt(0));
    return (letterCapIdx != -1) ? letterCapIdx : letterIdx;
  }

  private static int getDigitOrder(String towerString) {
    return DIGITS.indexOf(towerString.charAt(1));
  }

  private static void fillBoard(GameBoard board, String coords) {
    Matcher regexMatcher = TOWER_REGEX.matcher(coords);
    while (regexMatcher.find()) {
      String coord = regexMatcher.group();
      PieceTower tower = new PieceTower();
      int towerI = getLetterOrder(coord);
      int towerJ = getDigitOrder(coord);
      for (int i = 3; i < coord.length(); ++i) {
        Piece piece;
        switch (coord.charAt(i)) {
          case 'w':
            piece = new Piece(Color.WHITE, false);
            break;
          case 'W':
            piece = new Piece(Color.WHITE, true);
            break;
          case 'b':
            piece = new Piece(Color.BLACK, false);
            break;
          case 'B':
            piece = new Piece(Color.BLACK, true);
            break;
          default:
            piece = new Piece();
            break;
        }
        tower.pushPiece(piece);
      }
      board.setTower(towerI, towerJ, tower);
    }
  }

  public static void processMove(GameBoard board, String move) throws BusyCellException, WhiteCellException,
          InvalidMoveException, AnyErrorException {
    Pattern moveRegex = Pattern.compile(String.format("(%s[-]%s)", TOWER_REGEX_STRING, TOWER_REGEX_STRING));
    Pattern beatRegex = Pattern.compile(String.format("(%s[:]%s)", TOWER_REGEX_STRING, TOWER_REGEX_STRING));

    Matcher moveMatcher = moveRegex.matcher(move);
    Matcher beatMatcher = beatRegex.matcher(move);

    if (moveMatcher.find()) {
      String pureMove = moveMatcher.group();
      Matcher towerMatcher = TOWER_REGEX.matcher(pureMove);
      towerMatcher.find();
      String oldPos = towerMatcher.group();
      towerMatcher.find();
      String newPos = towerMatcher.group();
      int oldI = getLetterOrder(oldPos);
      int oldJ = getDigitOrder(oldPos);
      int newI = getLetterOrder(newPos);
      int newJ = getDigitOrder(newPos);
      board.moveTower(oldI, oldJ, newI, newJ);
    } else {
      String prev = null;
      String curr = null;
      int lastI = 0;
      int lastJ = 0;
      Matcher towerMatcher = TOWER_REGEX.matcher(move);
      while (towerMatcher.find()) {
        prev = curr;
        curr = towerMatcher.group();
        if (prev == null) {
          continue;
        }
        int oldI = getLetterOrder(prev);
        int oldJ = getDigitOrder(prev);
        int newI = getLetterOrder(curr);
        int newJ = getDigitOrder(curr);
        if (board.getTower(oldI, oldJ).isKing()) {
          board.beatWithKing(oldI, oldJ, newI, newJ);
        } else {
          board.beatWithTower(oldI, oldJ, newI, newJ);
        }
        lastI = newI;
        lastJ = newJ;
      }
      ArrayList<Pair> possibleVictims = board.getPossibleVictims(lastI, lastJ);
      if (possibleVictims != null) {
        throw new InvalidMoveException("invalid move");
      }
    }
  }

  public static void printFigures(GameBoard board, Color color) {
    ArrayList<String> output = new ArrayList<>();
    for (int j = board.getBoardLength() - 1; j >= 1; --j) {
      for (int i = 1; i < board.getBoardWidth(); ++i) {
        PieceTower tower = board.getTower(i, j);
        if (tower != null && tower.getColor().equals(color)) {
          output.add(String.format("%c%d_%s ", POS_LETTERS.charAt(i), j, tower.getContentString()));
        }
      }
    }
    Collections.sort(output);
    for (String s : output) {
      System.out.print(s);
    }
  }

  public static void main(String[] args) {
    try (Scanner in = new Scanner(System.in)) {
      GameBoard board = new GameBoard();
      String whiteCoords = in.nextLine();
      String blackCoords = in.nextLine();
      fillBoard(board, whiteCoords);
      fillBoard(board, blackCoords);

      while (in.hasNextLine()) {
        String moves = in.nextLine();
        if (moves.isEmpty()) {
          break;
        }
        Pattern splitRegex = Pattern.compile("(\\S)+");
        Matcher regexMatcher = splitRegex.matcher(moves);
        regexMatcher.find();
        String whiteMove = regexMatcher.group();
        regexMatcher.find();
        String blackMove = regexMatcher.group();
        try {
          processMove(board, whiteMove);
        } catch (BusyCellException e) {
          System.out.println("busy cell");
          return;
        } catch (WhiteCellException e) {
          System.out.println("white cell");
          return;
        } catch (InvalidMoveException e) {
          System.out.println("invalid move");
          return;
        } catch (AnyErrorException e) {
          System.out.println("error");
          return;
        }

        try {
          processMove(board, blackMove);
        } catch (BusyCellException e) {
          System.out.println("busy cell");
          return;
        } catch (WhiteCellException e) {
          System.out.println("white cell");
          return;
        } catch (InvalidMoveException e) {
          System.out.println("invalid move");
          return;
        } catch (AnyErrorException e) {
          System.out.println("error");
          return;
        }
      }
      printFigures(board, Color.WHITE);
      System.out.println();
      printFigures(board, Color.BLACK);
    }
  }
}
