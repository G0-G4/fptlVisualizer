package ru.fptlvisualizer;

public record Vertex (String name, int id){
  @Override
  public String toString() {
    return name;
  }
}
