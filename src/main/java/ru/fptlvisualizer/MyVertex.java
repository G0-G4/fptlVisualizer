package ru.fptlvisualizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyVertex {
  private final String name;
  private double x;
  private double y;
  private final int id;
  private List<MyVertex> children = new ArrayList<>();
  private MyVertex closing = null;

  public MyVertex(String name, double x, double y, int id) {
    this.name = name;
    this.x = x;
    this.y = y;
    this.id = id;
  }

  public double x() {
    return x;
  }

  public double y() {
    return y;
  }

  public void addChild(MyVertex child) {
    this.children.add(child);
  }

  public List<MyVertex> getChildren() {
    return Collections.unmodifiableList(children);
  }

  public String getName() {
    return name;
  }

  public int getId() {
    return id;
  }

  public void setClosing(MyVertex closing) {
    this.closing = closing;
  }

  public MyVertex getClosing() {
    return closing;
  }

  @Override
  public String toString() {
    return name;
  }
}
