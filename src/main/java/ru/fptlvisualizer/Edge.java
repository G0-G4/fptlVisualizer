package ru.fptlvisualizer;

public record Edge(Vertex a, Vertex b, String name) {

  public Edge(Vertex a, Vertex b) {
    this(a, b, "");
  }

  @Override
  public String toString() {
    return name;
  }
}
