package ru.fptlvisualizer;

import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import ru.fptlvisualizer.parser.Parser;
import ru.fptlvisualizer.tree.Composition;
import ru.fptlvisualizer.tree.Concatenation;
import ru.fptlvisualizer.tree.Expression;
import ru.fptlvisualizer.tree.Literal;
import ru.fptlvisualizer.tree.Ternary;

public class Controller {
  private Graph<Vertex, Edge> g;
  private SmartGraphPanel<Vertex, Edge> panel;
  int counter = 0;
  int dx = 50;

  public void setGraph(Graph<Vertex, Edge> g) {
    this.g = g;
  }

  public void setGraphPanel(SmartGraphPanel<Vertex, Edge> panel) {
    this.panel = panel;
  }

  protected void onButtonClick(String code) {
    Expression expression = Parser.parse(code);
    System.out.println(expression);
    drawGraph(expression, 500, 100);
  }

  private InOut drawGraph(Expression exp, double lastX, double lastY) {
    if (exp instanceof Composition composition) {
      var left = drawGraph(composition.getLeft(), lastX, lastY);
      var right = drawGraph(composition.getRight(), left.out().x(), lastY);
      addEdge(left.out(), right.in(), ".");
      return new InOut(left.in(), right.out(), Math.max(left.maxY(), right.maxY()));
    }
    if (exp instanceof Concatenation concatenation) {
      var left = concatenation.getLeft();
      var right = concatenation.getRight();
      var inOutLeft = drawGraph(left, lastX, lastY);
      var inOutRight = drawGraph(right, lastX, inOutLeft.maxY() + dx);
      var closeX = Math.max(inOutLeft.out().x(), inOutRight.out().x()) + dx;
      var openX = Math.min(inOutLeft.in().x(), inOutRight.in().x()) - dx;
      var open = getVertex("*", openX, lastY);
      var close = getVertex("*", closeX, lastY);
      if (left.getType() == Expression.Type.CONCATENATION) {
        open = inOutLeft.in();
        close = inOutLeft.out();
        addEdge(open, inOutRight.in());
        addEdge(inOutRight.out(), close);
      } else if (right.getType() == Expression.Type.CONCATENATION) {
        open = inOutRight.in();
        close = inOutRight.out();
        addEdge(open, inOutLeft.in());
        addEdge(inOutLeft.out(), close);
      } else {
        var o = g.insertVertex(open);
        var c = g.insertVertex(close);
        panel.updateAndWait();
        panel.setVertexPosition(o, openX, lastY);
        panel.setVertexPosition(c, closeX, lastY);
        System.out.println("set * open to " + (lastX));
        System.out.println("set * close to " + closeX);
        addEdge(open, inOutLeft.in());
        addEdge(open, inOutRight.in());
        addEdge(inOutLeft.out(), close);
        addEdge(inOutRight.out(), close);
      }
      return new InOut(open, close, inOutRight.maxY());
    }
    if (exp instanceof Ternary ternary) {
      var trueBranch = drawGraph(ternary.getTrueBranch(), lastX, lastY);
      var condition = drawGraph(ternary.getCondition(), lastX, trueBranch.maxY() + dx);
      var falseBranch = drawGraph(ternary.getFalseBranch(), lastX, condition.maxY() + dx);
      var openX = Math.min(Math.min(trueBranch.in().x(), falseBranch.in().x()), condition.in().x()) - dx;
      var closeX = Math.max(Math.max(trueBranch.out().x(), falseBranch.out().x()), condition.out().x()) + dx;
      var open = getVertex("->", openX, lastY);
      var close = getVertex("->", closeX, lastY);
      var o = g.insertVertex(open);
      var c = g.insertVertex(close);
      panel.updateAndWait();
      panel.setVertexPosition(o, openX, lastY);
      panel.setVertexPosition(c, closeX, lastY);
      addEdge(open, trueBranch.in());
      addEdge(trueBranch.out(), close);
      addEdge(open, falseBranch.in());
      addEdge(falseBranch.out(), close);
      addEdge(open, condition.in());
      return new InOut(open, close, falseBranch.maxY());
    }
    if (exp instanceof Literal literal) {
      double x = lastX + dx;
      var vertex = getVertex(literal.getLiteral(), x, lastY);
      var v = g.insertVertex(vertex);
      panel.updateAndWait();
      panel.setVertexPosition(v, x, lastY);
      System.out.println("set " + literal.getLiteral() + " to " + x + " " + lastY);
      return new InOut(vertex, vertex, lastY);
    }
    throw new IllegalStateException("unexpected expression type " + exp.getClass());
  }

  private Vertex getVertex(String name, double x, double y) {
    return new Vertex(name, x, y, counter++);
  }

  private void addEdge(Vertex a, Vertex b) {
    g.insertEdge(a, b, new Edge(a, b));
  }

  private void addEdge(Vertex a, Vertex b, String name) {
    g.insertEdge(a, b, new Edge(a, b, name));
  }
}