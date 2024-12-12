package ru.fptlvisualizer.tree.graph;

public class ConcatenationVertex extends ExpressionVertex {

  public ConcatenationVertex() {
    super("*");
  }

  @Override
  public boolean isRightEndOfOperation() {
    return getChildren().size() <= 1;
  }
}
