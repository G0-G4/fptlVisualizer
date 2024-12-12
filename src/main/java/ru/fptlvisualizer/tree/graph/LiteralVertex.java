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

//  public Optional<ExpressionVertex> getComposed() {
//    if (getChildren().size() == 1) {
//      var child = getChildren().get(0);
//      if (!child.isRightEndOfOperation()) {
//        return Optional.of(child);
//      }
//    }
//    return Optional.empty();
//  }
}
