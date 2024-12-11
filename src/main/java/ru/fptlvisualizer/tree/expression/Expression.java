package ru.fptlvisualizer.tree.expression;


public sealed abstract class Expression permits Composition, Concatenation, Literal, Ternary {

  public enum Type {

    LITERAL(0),
    COMPOSITION(1),
    CONCATENATION(2),
    TERNARY(3),
    EXPRESSION(4);

    private final int order;

    Type(int order) {
      this.order = order;
    }

    public int getOrder() {
      return order;
    }
  }

  public Type getType() {
    return Type.EXPRESSION;
  }
}
