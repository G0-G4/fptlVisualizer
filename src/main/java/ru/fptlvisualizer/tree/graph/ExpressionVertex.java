package ru.fptlvisualizer.tree.graph;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class ExpressionVertex {
  public static int ID = 0;
  private String name;
  private double x;
  private double y;
  private final int id;
  private List<ExpressionVertex> children = new ArrayList<>();
  protected ExpressionVertex lastOperand;
  public static Double X = null;
  public static List<ExpressionVertex> expressions = new ArrayList<>();
  private ExpressionVertex rightEndOfOperation;

  public ExpressionVertex(String name) {
    System.out.println("creating vertex " + name + " with x=" + x + ", y=" + y);
    this.id = ID++;
    this.name = name;
    expressions.add(this);
  }

  public ExpressionVertex getLastOperand() {
    if (lastOperand == null) {
      System.out.println();
    }
    return lastOperand;
  }

  public void setLastOperand(ExpressionVertex lastOperand) {
    this.lastOperand = lastOperand;
  }

  public void addChild(ExpressionVertex child) {
    children.add(child);
  }

  public List<ExpressionVertex> getChildren() {
    return Collections.unmodifiableList(children);
  }

  public Optional<ExpressionVertex> getComposed() {
    if (getChildren().size() == 1) {
      var child = getChildren().get(0);
      if (!child.isRightEndOfOperation()) {
        return Optional.of(child);
      }
    }
    return Optional.empty();
  }

  public void setRightEndOfOperation(ExpressionVertex rightEndOfOperation) {
    this.rightEndOfOperation = rightEndOfOperation;
  }

  public ExpressionVertex getRightEndOfOperation() {
    return rightEndOfOperation;
  }

  public abstract boolean isRightEndOfOperation();

  public void setPosition(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public void setX(double x) {
    this.x = x;
  }

  public void setY(double y) {
    this.y = y;
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
