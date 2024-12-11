package ru.fptlvisualizer;

import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.InvalidEdgeException;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import ru.fptlvisualizer.parser.Parser;
import ru.fptlvisualizer.tree.expression.Composition;
import ru.fptlvisualizer.tree.expression.Concatenation;
import ru.fptlvisualizer.tree.expression.Expression;
import ru.fptlvisualizer.tree.expression.Literal;
import ru.fptlvisualizer.tree.expression.Ternary;
import ru.fptlvisualizer.tree.graph.ConcatenationVertex;
import ru.fptlvisualizer.tree.graph.ExpressionVertex;
import ru.fptlvisualizer.tree.graph.LiteralVertex;
import ru.fptlvisualizer.tree.graph.TernaryVertex;

public class Controller {
  private Graph<MyVertex, MyEdge> g;
  private Graph<ExpressionVertex, MyEdge> gg;
  private SmartGraphPanel<MyVertex, MyEdge> panel;
  private SmartGraphPanel<ExpressionVertex, MyEdge> panel1;
  int counter = 0;
  int dx = 50;
  MyVertex start = null;
  ExpressionVertex s = null;

  public void setGraph(Graph<MyVertex, MyEdge> g) {
    this.g = g;
  }

  public void setGraph1(Graph<ExpressionVertex, MyEdge> g) {
    this.gg = g;
  }

  public void setGraphPanel(SmartGraphPanel<MyVertex, MyEdge> panel) {
    this.panel = panel;
  }

  public void setGraphPanelq(SmartGraphPanel<ExpressionVertex, MyEdge> panel) {
    this.panel1 = panel;
  }

  protected void onButtonClick(String code) {
    Expression expression = Parser.parse(code);
    clearGraph();
    var s = convertToGraph(expression, 500, 100).vertex();
    System.out.println(g);
    start = g.vertices()
        .stream()
        .min((a, b) -> (int) (a.element().x() - b.element().x()))
        .get().element();
    System.out.println(graphToExpression(start));
    drawGraph(s);
  }

  private InOut convertToGraph(Expression exp, double lastX, double lastY) {
    // TODO create tree from ExpressionVertex and then convert it to graph. then traverse it and add edges and set position of verticies
    // TODO rewrite with custom grapgh implementation to simplify graph to expression conversion
    if (exp instanceof Composition composition) {
      var left = convertToGraph(composition.getLeft(), lastX, lastY);
      var right = convertToGraph(composition.getRight(), left.out().x(), lastY);
      addEdge(left.out(), right.in(), ".");
      left.vertex().getClosing().addChild(right.vertex());
      left.vertex().setClosing(right.vertex().getClosing());
      return new InOut(left.in(), right.out(), Math.max(left.maxY(), right.maxY()), left.vertex());
    }
    if (exp instanceof Concatenation concatenation) {
      var left = concatenation.getLeft();
      var right = concatenation.getRight();
      var inOutLeft = convertToGraph(left, lastX, lastY);
      var inOutRight = convertToGraph(right, lastX, inOutLeft.maxY() + dx);


      ConcatenationVertex op = null;
      ConcatenationVertex cl = null;

      MyVertex open = null;
      MyVertex close = null;
//      TODO fix bug when (a*b)*(c*d) there * connects with *
//      if (left.getType() == Expression.Type.CONCATENATION && right.getType() == Expression.Type.CONCATENATION) {
//      }
      if (left.getType() == Expression.Type.CONCATENATION) {
        op = (ConcatenationVertex) inOutLeft.vertex();
        cl = (ConcatenationVertex) inOutLeft.vertex().getClosing();
        op.addChild(inOutRight.vertex());
        inOutRight.vertex().getClosing().addChild(cl);

        open = inOutLeft.in();
        close = inOutLeft.out();
        addEdge(open, inOutRight.in());
        addEdge(inOutRight.out(), close);
      } else if (right.getType() == Expression.Type.CONCATENATION) {

        op = (ConcatenationVertex) inOutRight.vertex();
        cl = (ConcatenationVertex) inOutRight.vertex().getClosing();
        op.addChild(inOutLeft.vertex());
        inOutLeft.vertex().getClosing().addChild(cl);

        open = inOutRight.in();
        close = inOutRight.out();
        addEdge(open, inOutLeft.in());
        addEdge(inOutLeft.out(), close);
      } else {
        var closeX = Math.max(inOutLeft.out().x(), inOutRight.out().x()) + dx;
        var openX = Math.min(inOutLeft.in().x(), inOutRight.in().x()) - Integer.divideUnsigned(dx, 2);

        open = getVertex("*", openX, lastY);
        close = getVertex("*", closeX, lastY);

        var o = g.insertVertex(open);
        var c = g.insertVertex(close);
        panel.updateAndWait();
        panel.setVertexPosition(o, openX, lastY);
        panel.setVertexPosition(c, closeX, lastY);
        addEdge(open, inOutLeft.in());
        addEdge(open, inOutRight.in());
        addEdge(inOutLeft.out(), close);
        addEdge(inOutRight.out(), close);

        op = new ConcatenationVertex();
        cl = new ConcatenationVertex();
//        op.setPosition(openX, lastY);
//        cl.setPosition(closeX, lastY);
        op.addChild(inOutLeft.vertex());
        op.addChild(inOutRight.vertex());
        inOutLeft.vertex().getClosing().addChild(cl);
        inOutRight.vertex().getClosing().addChild(cl);
      }
      open.setClosing(close);
      op.setClosing(cl);
      return new InOut(open, close, inOutRight.maxY(), op);
    }
    if (exp instanceof Ternary ternary) {
      var trueBranch = convertToGraph(ternary.getTrueBranch(), lastX, lastY);
      var condition = convertToGraph(ternary.getCondition(), lastX, trueBranch.maxY() + dx);
      var falseBranch = convertToGraph(ternary.getFalseBranch(), lastX, condition.maxY() + dx);
      var openX = Math.min(Math.min(trueBranch.in().x(), falseBranch.in().x()), condition.in().x()) - dx;
      var closeX = Math.max(Math.max(trueBranch.out().x(), falseBranch.out().x()), condition.out().x()) + dx;

      var op = new TernaryVertex(openX, lastY);
      var cl = new TernaryVertex(closeX, lastY);
      op.addChild(trueBranch.vertex());
      trueBranch.vertex().getClosing().addChild(cl);
      op.addChild(falseBranch.vertex());
      falseBranch.vertex().getClosing().addChild(cl);
      op.addChild(condition.vertex());
      op.setClosing(cl);

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
      return new InOut(open, close, falseBranch.maxY(), op);
    }
    if (exp instanceof Literal literal) {
      double x = lastX + dx;
      var vertex = getVertex(literal.getLiteral(), x, lastY);
      var v = g.insertVertex(vertex);
      var vv = new LiteralVertex(literal.getLiteral());
      panel.updateAndWait();
      panel.setVertexPosition(v, x, lastY);
      return new InOut(vertex, vertex, lastY, vv);
    }
    throw new IllegalStateException("unexpected expression type " + exp.getClass());
  }

  private void drawGraph(ExpressionVertex start) {
    gg.insertVertex(start);
    start.setPosition(500, 100);
    draw(start, new HashSet<>(List.of(start)), 500, 100);
    panel1.updateAndWait();
    for (var vertex: gg.vertices()) {
      panel1.setVertexPosition(vertex, vertex.element().getX(), vertex.element().getY());
    }
    panel1.updateAndWait();
  }

  private void draw(ExpressionVertex start, Set<ExpressionVertex> vertices, int x, int y) {
    x += dx;
    for (var child : start.getChildren()) {
      if (!vertices.contains(child)) {
        gg.insertVertex(child);
        child.setPosition(x, y);
//        gg.insertEdge(start, child, new MyEdge(start, child));
        vertices.add(child);
      }
      try{
        gg.insertEdge(start, child, new MyEdge(start, child));
      }
      catch(Exception e){}
      draw(child, vertices, x, y);
      y += start.getChildren().size() > 1 ? dx : 0;
    }
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