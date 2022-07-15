package com.linkedin.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Aggregator {
  List<Accumulator> _accumulatorList;

  public Aggregator(List<String> exprs) {
    //parse Expression and collecto to accumulators
    _accumulatorList = new ArrayList<>();
  }

  public void aggregate(Map<String, Integer> filedValues) {
    for (String measure : filedValues.keySet()) {
      for (Accumulator accumulator : _accumulatorList) {
        if (accumulator.measure.equals(measure)) {
          accumulator.accumulate(filedValues.get(measure));
        }
      }
    }
  }

  public Object finalResult() {
    //collect all values from accumelators and return as result Value
    for (Accumulator accumulatorcc : _accumulatorList) {
      //accumulatorcc.metri;
      //accumulatorcc.aggrigatedValue;
      //append to result
    }
    return null;
  }

  enum FunctionType {
    SUM, MIN, MAX, UNIQUE
  }

  abstract static class Formula {
    String metric;
    String measure;
    FunctionType funcType;

    public Formula(String metric, String measure, FunctionType funcType) {
      this.metric = metric;
      this.measure = measure;
      this.funcType = funcType;
    }

    public abstract Accumulator getAccumulator();

    public static Formula parse(String metricExpr) {
      // return MaxFunction, MinFunction, SUMFunction, UniqueFunction
      return new SUMFormula("", "", null);
    }
  }

  static class SUMFormula extends Formula {
    public SUMFormula(String metric, String measure, FunctionType funcType) {
      super(metric, measure, funcType);
    }

    public Accumulator getAccumulator() {
      return new Accumulator("", "", new SUMFunction());
    }
  }

  static class Accumulator {
    String metri;
    String measure;
    String name;
    CalculateFunction func;
    double aggrigatedValue;

    public Accumulator(String metri, String measure, CalculateFunction func) {
      this.metri = metri;
      this.measure = measure;
      this.func = func;
    }

    void accumulate(int incomingVal) {
      aggrigatedValue = func.apply(aggrigatedValue, incomingVal);
    }
  }

  interface CalculateFunction {
    public double apply(double val1, double val2);
  }

  static class SUMFunction implements CalculateFunction {
    public double apply(double val1, double val2) {
      return val1 + val2;
    }
  }

  static class MaxFunction implements CalculateFunction {
    public double apply(double val1, double val2) {
      return Math.max(val1, val2);
    }
  }

  static class MinFunction implements CalculateFunction {
    public double apply(double val1, double val2) {
      return Math.min(val1, val2);
    }
  }

  static class UniqueFunction implements CalculateFunction {
    public double apply(double val1, double val2) {
      return 0.0;
    }
  }
}

class DoubleLinkedList {
  Node head;
  Node tail;
  int size = 0;

  public DoubleLinkedList() {
    head = new Node();
    tail = new Node();
    head.next = tail;
    tail.previous = head;
  }

  public void addLast(int val) {
    Node cur = new Node();
    cur.val = val;
    Node curLast = tail.previous;
    curLast.next = cur;
    cur.previous = curLast;
    cur.next = tail;
    tail.previous = cur;
    size++;
  }

  public Node getLast() {
    return tail.previous;
  }

  public void remove(Node node) {
    node.previous.next = node.next;
    node.next.previous = node.previous;
    node.previous = null;
    node.next = null;
    size--;
  }

  static class Node {
    Node previous;
    Node next;
    int val;
  }
}





