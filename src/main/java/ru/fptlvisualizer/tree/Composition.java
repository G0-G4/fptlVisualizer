package ru.fptlvisualizer.tree;

import java.util.Objects;

public class Composition extends Expression {

  private final Expression left;
  private final Expression right;

  public Expression getLeft() {
    return left;
  }

  public Expression getRight() {
    return right;
  }

  public Composition(Expression left, Expression right) {
    this.left = left;
    this.right = right;
  }

  @Override
  public Type getType() {
    return Type.COMPOSITION;
  }

  @Override
  public String toString() {
    String leftRepresentation = left.getType().getOrder() > Type.COMPOSITION.getOrder() ? "(" + left + ")" : left.toString();
    String rightRepresentation = right.getType().getOrder() > Type.COMPOSITION.getOrder() ? "(" + right + ")" : right.toString();
    return leftRepresentation + "." + rightRepresentation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Composition that = (Composition) o;
    return Objects.equals(left, that.left) && Objects.equals(right, that.right);
  }

  @Override
  public int hashCode() {
    return Objects.hash(left, right); // TODO печатать скобочки, только где необходимо;
  }
}
