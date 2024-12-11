package ru.fptlvisualizer.tree.expression;

import java.util.Objects;

public final class Literal extends Expression {

  private final String literal;

  public Literal(String literal) {
    this.literal = literal;
  }

  public String getLiteral() {
    return literal;
  }

  @Override
  public Type getType() {
    return Type.LITERAL;
  }

  @Override
  public String toString() {
    return literal;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Literal literal1 = (Literal) o;
    return Objects.equals(literal, literal1.literal);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(literal);
  }
}
