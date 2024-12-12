package ru.fptlvisualizer.tree.graph;

public class TernaryVertex extends ExpressionVertex {

  public TernaryVertex() {
    super("->");
  }

  @Override
  public boolean isRightEndOfOperation() {
    return getChildren().size() <= 1;
  }

  public ExpressionVertex getTrueBranch() {
    return getChildren().get(0);
  }

  public ExpressionVertex getFalseBranch() {
    return getChildren().get(2);
  }

  public ExpressionVertex getCondition() {
    return getChildren().get(1);
  }
}