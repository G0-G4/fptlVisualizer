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
  private Graph<MyVertex, MyEdge> g;
  private SmartGraphPanel<MyVertex, MyEdge> panel;
  int counter = 0;
  int dx = 50;
  MyVertex start = null;

  public void setGraph(Graph<MyVertex, MyEdge> g) {
    this.g = g;
  }

  public void setGraphPanel(SmartGraphPanel<MyVertex, MyEdge> panel) {
    this.panel = panel;
  }

  protected void onButtonClick(String code) {
    Expression expression = Parser.parse(code);
    clearGraph();
    drawGraph(expression, 500, 100);
    System.out.println(g);
    start = g.vertices()
        .stream()
        .min((a, b) -> (int) (a.element().x() - b.element().x()))
        .get().element();
    System.out.println(graphToExpression(start));
  }

  private InOut drawGraph(Expression exp, double lastX, double lastY) {
    // TODO rewrite with custom grapgh implementation to simplify graph to expression conversion
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
//      TODO fix bug when (a*b)*(c*d) there * connects with *
//      if (left.getType() == Expression.Type.CONCATENATION && right.getType() == Expression.Type.CONCATENATION) {
//      }
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
        addEdge(open, inOutLeft.in());
        addEdge(open, inOutRight.in());
        addEdge(inOutLeft.out(), close);
        addEdge(inOutRight.out(), close);
      }
      open.setClosing(close);
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
      open.setClosing(close);
      return new InOut(open, close, falseBranch.maxY());
    }
    if (exp instanceof Literal literal) {
      double x = lastX + dx;
      var vertex = getVertex(literal.getLiteral(), x, lastY);
      var v = g.insertVertex(vertex);
      panel.updateAndWait();
      panel.setVertexPosition(v, x, lastY);
      return new InOut(vertex, vertex, lastY);
    }
    throw new IllegalStateException("unexpected expression type " + exp.getClass());
  }

  private MyVertex getVertex(String name, double x, double y) {
    var vertex = new MyVertex(name, x, y, counter++);
    if (start == null) {
      start = vertex;
    }
    return vertex;
  }

  private void addEdge(MyVertex a, MyVertex b) {
    a.addChild(b);
    g.insertEdge(a, b, new MyEdge(a, b));
  }

  private void addEdge(MyVertex a, MyVertex b, String name) {
    a.addChild(b);
    g.insertEdge(a, b, new MyEdge(a, b, name));
  }

  private Expression graphToExpression(MyVertex curr) {
    if ("*".equals(curr.getName())) {
      var expr =  curr.getChildren().stream()
          .map(this::graphToExpression)
          .reduce(Concatenation::new)
          .get();
      MyVertex opEnd = curr.getClosing();
      if (opEnd != null && !opEnd.getChildren().isEmpty()) {
        var right = graphToExpression(opEnd.getChildren().get(0));
        return  right != null ? new Composition(expr, graphToExpression(opEnd.getChildren().get(0))) : expr;
      }
      return expr;

    } else if ("->".equals(curr.getName())) {
      if (curr.getChildren().isEmpty()) {
        return  null;
      }
      System.out.println(curr);
      var trueBranch = graphToExpression(curr.getChildren().get(0));
      var falseBranch = graphToExpression(curr.getChildren().get(1));
      var condition = graphToExpression(curr.getChildren().get(2));
      var expr = new Ternary(condition, trueBranch, falseBranch);
      MyVertex opEnd = curr.getClosing();
      if (opEnd != null && !opEnd.getChildren().isEmpty()) {
        var right = graphToExpression(opEnd.getChildren().get(0));
        return  right != null ? new Composition(expr, graphToExpression(opEnd.getChildren().get(0))) : expr;
      }
      return expr;
    } else {
      var literal = new Literal(curr.getName());
      MyVertex child = null;
      if (curr.getChildren().size() == 1) {
        child = curr.getChildren().get(0);
      }

      if (child == null || "*".equals(child.getName()) || "->".equals(child.getName())) {
        return literal;
      }
      var right = graphToExpression(child);
      return right != null ? new Composition(literal, graphToExpression(child)) : literal;
    }
  }

  private void clearGraph() {
    for (var vertex : g.vertices()) {
      g.removeVertex(vertex);
    }
  }
}