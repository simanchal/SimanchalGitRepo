package com.linkedin.tools;

import java.util.List;
import java.util.PriorityQueue;


public class UnionFind {
  public static void main(String[] args) {
    UnionFind unionFind = new UnionFind();
    List<List<Interval>> schedule = null;

    //PriorityQueue<int[]> px=new PriorityQueue<>((x,y)->schedule.get(x[0]).get(x[1]).start- schedule.get(x[0]).get(x[1]).start);
    PriorityQueue<int[]> pq = new PriorityQueue<>((x, y) -> {
      int xlistPos = x[0];
      int xelePos = x[1];
      int ylistPos = y[0];
      int yelePos = y[1];
      return schedule.get(xlistPos).get(xelePos).start - schedule.get(ylistPos).get(yelePos).start;
    });
  }

  public String lexigraphical(String a, String b, String c) {
    int[] charArr = new int[26];
    for (int i = 0; i < charArr.length; i++) {
      charArr[i] = i;
    }
    for (int i = 0; i < a.length(); i++) {
      int ainx = a.charAt(i) - '0';
      int binx = b.charAt(i) - '0';
      int parent1 = findUnion(charArr, ainx);
      int parent2 = findUnion(charArr, binx);
      if (parent1 < parent2) {
        charArr[parent2] = parent1;
      } else {
        charArr[parent1] = parent2;
      }
    }
    StringBuilder res = new StringBuilder();
    for (int i = 0; i < c.length(); i++) {
      int parent = findUnion(charArr, c.charAt(i) - '0');
      res.append((char) (parent + 'a'));
    }
    return res.toString();
  }

  public int findUnion(int[] charArr, int ch) {
    while (charArr[ch] != ch) {
      ch = charArr[ch];
    }
    return ch;
  }

  static class Interval {
    public int start;
    public int end;

    public Interval() {
    }

    public Interval(int _start, int _end) {
      start = _start;
      end = _end;
    }
  }
}
