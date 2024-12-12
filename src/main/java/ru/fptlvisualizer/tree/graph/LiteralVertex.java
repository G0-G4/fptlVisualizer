package ru.fptlvisualizer.tree.graph;

public class LiteralVertex extends ExpressionVertex {
  public LiteralVertex(String name) {
    super(name);
    lastOperand = this;
  }

  @Override
  public boolean isRightEndOfOperation() {
    return false;
  }
}
