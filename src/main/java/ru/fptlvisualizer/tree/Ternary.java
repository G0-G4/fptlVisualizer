package ru.fptlvisualizer.tree;

import java.util.Objects;

public class Ternary extends Expression {

  private final Expression condition;
  private final Expression trueCase;
  private final Expression falseCase;

  public Ternary(Expression condition, Expression trueCase, Expression falseCase) {
    this.condition = condition;
    this.trueCase = trueCase;
    this.falseCase = falseCase;
  }

  public Expression getCondition() {
    return condition;
  }

  public Expression getTrueCase() {
    return trueCase;
  }

  public Expression getFalseCase() {
    return falseCase;
  }

  @Override
  public String toString() {
    return condition + " -> " + trueCase + ", " + falseCase;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Ternary ternary = (Ternary) o;
    return Objects.equals(condition, ternary.condition) && Objects.equals(trueCase, ternary.trueCase) && Objects.equals(
        falseCase,
        ternary.falseCase
    );
  }

  @Override
  public int hashCode() {
    return Objects.hash(condition, trueCase, falseCase);
  }
}
