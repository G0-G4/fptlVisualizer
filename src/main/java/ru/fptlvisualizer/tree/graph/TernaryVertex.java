package ru.fptlvisualizer.tree.graph;

public class TernaryVertex extends ExpressionVertex {
  private ExpressionVertex condition;
  private ExpressionVertex trueCondition;
  private ExpressionVertex falseCondition;

  public TernaryVertex(double x, double y) {
    super("->");
  }

  public TernaryVertex setCondition(ExpressionVertex condition) {
    this.condition = condition;
    return this;
  }

  public TernaryVertex setTrueCondition(ExpressionVertex trueCondition) {
    this.trueCondition = trueCondition;
    return this;
  }

  public TernaryVertex setFalseCondition(ExpressionVertex falseCondition) {
    this.falseCondition = falseCondition;
    return this;
  }
}
