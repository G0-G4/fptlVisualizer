package ru.fptlvisualizer;

import com.brunomnsilva.smartgraph.graph.Graph;
import ru.fptlvisualizer.parser.Parser;
import ru.fptlvisualizer.tree.Composition;
import ru.fptlvisualizer.tree.Concatenation;
import ru.fptlvisualizer.tree.Expression;
import ru.fptlvisualizer.tree.Literal;

public class Controller {
  private Expression expression;
  private Graph<Vertex, Edge> g;
  int counter = 0;

  public void setGraph(Graph<Vertex, Edge> g) {
    this.g = g;
  }

  protected void onButtonClick(String code) {
    expression = Parser.parse(code);
    System.out.println(expression);
    drawGraph(expression);
  }

  private InOut drawGraph(Expression exp) {
    if (exp instanceof Composition composition) {

    }
    if (exp instanceof Concatenation concatenation) {
      var left = concatenation.getLeft();
      var right = concatenation.getRight();
      var inOutLeft = drawGraph(left);
      var inOutRight = drawGraph(right);
      var open = getVertex("*");
      var close = getVertex("*");
      if (left.getType() == Expression.Type.CONCATENATION) {
        open = inOutLeft.in();
        close = inOutLeft.out();
        addEdge(open, inOutRight.in());
        addEdge(inOutRight.out(), close);
      }
      else if (right.getType() == Expression.Type.CONCATENATION) {
        open = inOutRight.in();
        close = inOutRight.out();
        addEdge(open, inOutLeft.in());
        addEdge(inOutLeft.out(), close);
      }
      else {
        g.insertVertex(open);
        g.insertVertex(close);
        addEdge(open, inOutLeft.in());
        addEdge(open, inOutRight.in());
        addEdge(inOutLeft.out(), close);
        addEdge(inOutRight.out(), close);
      }
      return new InOut(open, close);
    }
    if (exp.getType() == Expression.Type.TERNARY) {

    }
    if (exp instanceof Literal literal) {
      var vertex = new Vertex(literal.getLiteral(), counter++);
      g.insertVertex(vertex);
      return new InOut(vertex, vertex);
    }
    return null;
  }

  private Vertex getVertex(String name) {
    var v = new Vertex(name, counter++);
    return v;
  }

  private void addEdge(Vertex a, Vertex b) {
    g.insertEdge(a, b, new Edge(a, b));
  }
}