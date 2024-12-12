package ru.fptlvisualizer;

public record ExpressionEdge(Object a, Object b, String name) {

  public ExpressionEdge(Object a, Object b) {
    this(a, b, "");
  }

  @Override
  public String toString() {
    return name;
  }
}
