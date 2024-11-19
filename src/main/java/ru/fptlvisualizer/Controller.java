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
  private Expression expression;
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
    expression = Parser.parse(code);
    System.out.println(expression);
    drawGraph(expression, 500);
  }

  private InOut drawGraph(Expression exp, double lastX) {
    if (exp instanceof Composition composition) {
      var left = drawGraph(composition.getLeft(), lastX);
      var right = drawGraph(composition.getRight(), left.out().x());
      addEdge(left.out(), right.in());
      return new InOut(left.in(), right.out());
    }
    if (exp instanceof Concatenation concatenation) {
      var left = concatenation.getLeft();
      var right = concatenation.getRight();
      var inOutLeft = drawGraph(left, lastX);
      var inOutRight = drawGraph(right, lastX);
      var closeX = Math.max(inOutLeft.out().x(), inOutRight.out().x()) + dx;
      var openX = Math.min(inOutLeft.in().x(), inOutRight.in().x()) - dx;
      var open = getVertex("*", openX);
      var close = getVertex("*", closeX);

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
        panel.setVertexPosition(o, openX, 50);
        panel.setVertexPosition(c, closeX, 50);
        System.out.println("set * open to " + (lastX));
        System.out.println("set * close to " + closeX);
        addEdge(open, inOutLeft.in());
        addEdge(open, inOutRight.in());
        addEdge(inOutLeft.out(), close);
        addEdge(inOutRight.out(), close);
      }
      return new InOut(open, close);
    }
    if (exp instanceof Ternary ternary) {
      var trueBranch = drawGraph(ternary.getTrueBranch(), lastX);
      var falseBranch = drawGraph(ternary.getFalseBranch(), lastX);
      var condition = drawGraph(ternary.getCondition(), lastX);
      var openX = Math.min(Math.min(trueBranch.in().x(), falseBranch.in().x()), condition.in().x()) - dx;
      var closeX = Math.max(Math.max(trueBranch.out().x(), falseBranch.out().x()), condition.out().x()) + dx;
      var open = getVertex("->", openX);
      var close = getVertex("->", closeX);
      var o = g.insertVertex(open);
      var c = g.insertVertex(close);
      panel.updateAndWait();
      panel.setVertexPosition(o, openX, 50);
      panel.setVertexPosition(c, closeX, 50);
      addEdge(open, trueBranch.in());
      addEdge(trueBranch.out(), close);
      addEdge(open, falseBranch.in());
      addEdge(falseBranch.out(), close);
      addEdge(open, condition.in());
      return new InOut(open, close);
    }
    if (exp instanceof Literal literal) {
      double x = lastX + dx;
      var vertex = getVertex(literal.getLiteral(), x);
      var v = g.insertVertex(vertex);
      panel.updateAndWait();
      panel.setVertexPosition(v, x, 50);
      System.out.println("set " + literal.getLiteral() + " to " + x);
      return new InOut(vertex, vertex);
    }
    throw new IllegalStateException("unexpected expression type " + exp.getClass());
  }

  private Vertex getVertex(String name, double x) {
    return new Vertex(name, x, counter++);
  }

  private void addEdge(Vertex a, Vertex b) {
    g.insertEdge(a, b, new Edge(a, b));
  }
}