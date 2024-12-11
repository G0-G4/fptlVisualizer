package ru.fptlvisualizer.tree.graph;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ExpressionVertex {
  public static int ID = 0;
  private String name;
  private double x;
  private double y;
  private final int id;
  private List<ExpressionVertex> children = new ArrayList<>();
  protected ExpressionVertex closing;
  public static Double X = null;
  public static ExpressionVertex START = null;
  public static List<ExpressionVertex> expressions = new ArrayList<>();

  public ExpressionVertex(String name) {
    System.out.println("creating vertex " + name + " with x=" + x + ", y=" + y);
    this.id = ID++;
    this.name = name;
    expressions.add(this);
  }

  public ExpressionVertex getClosing() {
    if (closing == null) {
      System.out.println();
    }
    return closing;
  }

  public void setClosing(ExpressionVertex closing) {
    this.closing = closing;
  }

  public void addChild(ExpressionVertex child) {
    children.add(child);
  }

  public List<ExpressionVertex> getChildren() {
    return Collections.unmodifiableList(children);
  }

  public void setPosition(double x, double y) {
    this.x = x;
    this.y = y;
    if (X == null) {
      X = x;
      START = this;
    }
    else if (x <= X) {
      X = x;
      START = this;
    }
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  @Override
  public String toString() {
    return name;
  }
}
