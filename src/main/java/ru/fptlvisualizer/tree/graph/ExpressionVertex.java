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
  // TODO different subclasses can have different numbers of children. Store them in subclasses
  protected List<ExpressionVertex> children = new ArrayList<>();
  private List<ExpressionVertex> parents = new ArrayList<>();
  protected ExpressionVertex lastOperand;
  private ExpressionVertex rightEndOfOperation;

  public ExpressionVertex(String name) {
    this.id = ID++;
    this.name = name;
  }

  public ExpressionVertex getLastOperand() {
    return lastOperand;
  }

  public void setLastOperand(ExpressionVertex lastOperand) {
    this.lastOperand = lastOperand;
  }

  public void addChild(ExpressionVertex child) {
    child.parents.add(this);
    children.add(child);
  }

  public List<ExpressionVertex> getChildren() {
    return Collections.unmodifiableList(children);
  }

  public List<ExpressionVertex> getParents() {
    return Collections.unmodifiableList(parents);
  }

  public void removeChild(ExpressionVertex child) {
    children.remove(child);
    child.parents.remove(this);
  }

  public void removeParent(ExpressionVertex parent) {
    parents.remove(parent);
    parent.children.remove(this);
  }

  public void deleteFromGraph() {
    List<ExpressionVertex> childrenCopy = new ArrayList<>(children);
    List<ExpressionVertex> parentsCopy = new ArrayList<>(parents);
    childrenCopy.forEach(v -> v.removeParent(this));
    parentsCopy.forEach(v -> v.removeChild(this));
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

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return name;
  }
}
