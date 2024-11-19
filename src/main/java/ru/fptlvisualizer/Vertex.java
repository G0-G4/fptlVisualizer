package ru.fptlvisualizer;

public record Vertex (String name, double x, int id){
  @Override
  public String toString() {
    return name;
  }
}
