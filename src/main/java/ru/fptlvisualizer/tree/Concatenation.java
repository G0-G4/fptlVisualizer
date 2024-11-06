package ru.fptlvisualizer.tree;

import java.util.Objects;

public class Concatenation extends Expression {

  Expression left;
  Expression right;

  public Concatenation(Expression left, Expression right) {
    this.left = left;
    this.right = right;
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
    return "(" + left + "*" + right + ")"; // TODO печатать скобочки, только где необходимо
  }
}
