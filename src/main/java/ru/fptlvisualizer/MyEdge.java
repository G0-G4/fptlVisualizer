package ru.fptlvisualizer;

public record MyEdge(Object a, Object b, String name) {

  public MyEdge(Object a, Object b) {
    this(a, b, "");
  }

  @Override
  public String toString() {
    return name;
  }
}
