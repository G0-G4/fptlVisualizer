package ru.fptlvisualizer.tree.graph;

public class LiteralVertex extends ExpressionVertex {
  ExpressionVertex child;
  public LiteralVertex(String name) {
    super(name);
    closing = this;
  }
  
}
