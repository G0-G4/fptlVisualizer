package ru.fptlvisualizer;

public record MyEdge(MyVertex a, MyVertex b, String name) {

  public MyEdge(MyVertex a, MyVertex b) {
    this(a, b, "");
  }

  @Override
  public String toString() {
    return name;
  }
}
