package ru.fptlvisualizer.tree;

import java.util.Objects;

public class Composition extends Expression {

  public Expression left;
  public Expression right;

  public Composition(Expression left, Expression right) {
    this.left = left;
    this.right = right;
  }

  @Override
  public String toString() {
    return "(" + left + "." + right + ")";
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
