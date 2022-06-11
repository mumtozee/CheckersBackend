package com.ruchekers;

final class Pair {
  private int first;
  private int second;

  Pair() {
    first = 0;
    second = 0;
  }

  Pair(int first, int second) {
    this.first = first;
    this.second = second;
  }

  public int getFirst() {
    return first;
  }

  public void setFirst(int first) {
    this.first = first;
  }

  public int getSecond() {
    return second;
  }

  public void setSecond(int second) {
    this.second = second;
  }
}
