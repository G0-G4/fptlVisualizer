package ru.fptlvisualizer.tree.expression;

import java.util.Objects;

public final class Concatenation extends Expression {

  private final Expression left;
  private final Expression right;

  public Concatenation(Expression left, Expression right) {
    this.left = left;
    this.right = right;
  }

  public Expression getLeft() {
    return left;
  }

  public Expression getRight() {
    return right;
  }

  @Override
  public Type getType() {
    return Type.CONCATENATION;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Concatenation that = (Concatenation) o;
    return Objects.equals(left, that.left) && Objects.equals(right, that.right);
  }

  @Override
  public int hashCode() {
    return Objects.hash(left, right);
  }

  @Override
  public String toString() {
    String leftRepresentation = left.getType().getOrder() > Type.CONCATENATION.getOrder() ? "(" + left + ")" : left.toString();
    String rightRepresentation = right.getType().getOrder() > Type.CONCATENATION.getOrder() ? "(" + right + ")" : right.toString();
    return leftRepresentation + "*" + rightRepresentation;
  }
}
