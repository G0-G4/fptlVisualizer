package ru.fptlvisualizer.tree;

import java.util.Objects;

public final class Ternary extends Expression {

  private final Expression condition;
  private final Expression trueBranch;
  private final Expression falseBranch;

  public Ternary(Expression condition, Expression trueBranch, Expression falseBranch) {
    this.condition = condition;
    this.trueBranch = trueBranch;
    this.falseBranch = falseBranch;
  }

  public Expression getCondition() {
    return condition;
  }

  public Expression getTrueBranch() {
    return trueBranch;
  }

  public Expression getFalseBranch() {
    return falseBranch;
  }

  @Override
  public Type getType() {
    return Type.TERNARY;
  }

  @Override
  public String toString() {
    String conditionRepresentation = condition.getType() == Type.TERNARY ? "(" + condition + ")" : condition.toString();
    return conditionRepresentation + "->" + trueBranch + "," + falseBranch;
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
    return Objects.equals(condition, ternary.condition) && Objects.equals(trueBranch, ternary.trueBranch) && Objects.equals(
        falseBranch,
        ternary.falseBranch
    );
  }

  @Override
  public int hashCode() {
    return Objects.hash(condition, trueBranch, falseBranch);
  }
}
