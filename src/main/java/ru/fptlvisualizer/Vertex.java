package ru.fptlvisualizer;

public record Vertex (String name, double x, double y, int id){
  @Override
  public String toString() {
    return name;
  }
}
