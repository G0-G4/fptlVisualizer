package ru.fptlvisualizer;

import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import java.util.HashSet;
import java.util.List;
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
  private Graph<ExpressionVertex, MyEdge> g;
  private SmartGraphPanel<ExpressionVertex, MyEdge> panel;
  int dx = 50;
  public ExpressionVertex s;

  public void setGraph(Graph<ExpressionVertex, MyEdge> g) {
    this.g = g;
  }

  public void setGraphPanel(SmartGraphPanel<ExpressionVertex, MyEdge> panel) {
    this.panel = panel;
  }

  public void setGraphPanelq(SmartGraphPanel<ExpressionVertex, MyEdge> panel) {
    this.panel = panel;
  }

  protected void onButtonClick(String code) {
    Expression expression = Parser.parse(code);
    clearGraph();
    s = convertToGraph(expression);
    System.out.println(g);
    drawGraph(s);
  }

  private ExpressionVertex convertToGraph(Expression exp) {
    // TODO create tree from ExpressionVertex and then convert it to graph. then traverse it and add edges and set position of verticies
    // TODO rewrite with custom grapgh implementation to simplify graph to expression conversion
    if (exp instanceof Composition composition) {
      var left = convertToGraph(composition.getLeft());
      var right = convertToGraph(composition.getRight());
      left.getLastOperand().addChild(right);
      left.setLastOperand(right.getLastOperand());
      return left;
    }
    if (exp instanceof Concatenation concatenation) {
      var left = concatenation.getLeft();
      var right = concatenation.getRight();
      var inOutLeft = convertToGraph(left);
      var inOutRight = convertToGraph(right);

      ConcatenationVertex op;
      ConcatenationVertex cl;


//      TODO fix bug when (a*b)*(c*d) there * connects with *
//      if (left.getType() == Expression.Type.CONCATENATION && right.getType() == Expression.Type.CONCATENATION) {
//      }
      if (left.getType() == Expression.Type.CONCATENATION) {
        op = (ConcatenationVertex) inOutLeft;
        cl = (ConcatenationVertex) inOutLeft.getLastOperand();
        op.addChild(inOutRight);
        inOutRight.getLastOperand().addChild(cl);


      } else if (right.getType() == Expression.Type.CONCATENATION) {

        op = (ConcatenationVertex) inOutRight;
        cl = (ConcatenationVertex) inOutRight.getLastOperand();
        op.addChild(inOutLeft);
        inOutLeft.getLastOperand().addChild(cl);

      } else {


        op = new ConcatenationVertex();
        cl = new ConcatenationVertex();

        op.addChild(inOutLeft);
        op.addChild(inOutRight);
        inOutLeft.getLastOperand().addChild(cl);
        inOutRight.getLastOperand().addChild(cl);
      }
      // TODO better create separate classes for open vertices
      //  with constructor forcing to set right end of operation to avoid forgetting setting it and to make it easier to distinguish them
      op.setLastOperand(cl);
      op.setRightEndOfOperation(cl);
      return op;
    }
    if (exp instanceof Ternary ternary) {
      var trueBranch = convertToGraph(ternary.getTrueBranch());
      var condition = convertToGraph(ternary.getCondition());
      var falseBranch = convertToGraph(ternary.getFalseBranch());

      var op = new TernaryVertex();
      var cl = new TernaryVertex();
      op.addChild(trueBranch);
      op.addChild(condition);
      op.addChild(falseBranch);
      trueBranch.getLastOperand().addChild(cl);
      condition.getLastOperand().addChild(cl);
      falseBranch.getLastOperand().addChild(cl);
      op.setLastOperand(cl);
      op.setRightEndOfOperation(cl);
      return op;
    }
    if (exp instanceof Literal literal) {
      return new LiteralVertex(literal.getLiteral());
    }
    throw new IllegalStateException("unexpected expression type " + exp.getClass());
  }

  private void drawGraph(ExpressionVertex start) {
    draw(start,550, 100);
    panel.updateAndWait();
    for (var vertex : g.vertices()) {
      panel.setVertexPosition(vertex, vertex.element().getX(), vertex.element().getY());
    }
    panel.updateAndWait();
  }

//  private double maxX(ExpressionVertex start, double max) {
//    for (var vertex : start.getChildren()) {
//     max = Math.max(max, maxX(vertex, max));
//    }
//    return max;
//  }

  private void draw(ExpressionVertex start, double x, double y) {
    var openY = y;
    var openX = x;

    if (start instanceof ConcatenationVertex || start instanceof TernaryVertex) {
      x += dx;
      for(var vertex: start.getChildren()) {
        draw(vertex, x, y);
        y += dx;
      }

      var rightEndOfOperation = start.getRightEndOfOperation();
      // TODO takes only first children should take into account transitive children
      double closingX = start.getChildren().stream().map(ExpressionVertex::getX).max(Double::compare).get() + dx;
//      double closingX = maxX(rightEndOfOperation, x) + dx;
      g.insertVertex(start);
      g.insertVertex(rightEndOfOperation);
      start.setPosition(openX, openY);
      rightEndOfOperation.setY(openY);

      if (rightEndOfOperation.getComposed().isPresent()) {
        draw(rightEndOfOperation.getComposed().get(), closingX + dx, openY);
      }
      if (rightEndOfOperation.getChildren().size() == 1) {
        var child = rightEndOfOperation.getChildren().get(0);
        child.setX(Math.max(rightEndOfOperation.getX() + dx, child.getX()));
      }
//    } else if (start instanceof TernaryVertex exp) {

    } else if (start instanceof LiteralVertex exp) {
      g.insertVertex(exp);
      exp.setPosition(x, y);
      if (exp.getComposed().isPresent()) {
        draw(exp.getComposed().get(), x + dx, y);
      }
      else if (exp.getChildren().size() == 1) {
        var rightEnd = exp.getChildren().get(0);
         rightEnd.setX(Math.max( exp.getX() + dx, rightEnd.getX()));
      }
//      exp.getComposed().ifPresent(composed -> draw(composed, vertices, xx, yy));
    }

//    for (var child : start.getChildren()) {
//      if (!vertices.contains(child)) {
//        g.insertVertex(child);
//        child.setPosition(x, y);
//        vertices.add(child);
//      }
//      try { //TODO remove try catch
//        g.insertEdge(start, child, new MyEdge(start, child));
//      } catch (Exception e) {
//      }
//      draw(child, vertices, x + dx, y);
//      y += start.getChildren().size() > 1 ? dx : 0;
//    }
  }

  private Expression graphToExpression(MyVertex curr) {
    if ("*".equals(curr.getName())) {
      var expr = curr.getChildren().stream()
          .map(this::graphToExpression)
          .reduce(Concatenation::new)
          .get();
      MyVertex opEnd = curr.getClosing();
      if (opEnd != null && !opEnd.getChildren().isEmpty()) {
        var right = graphToExpression(opEnd.getChildren().get(0));
        return right != null ? new Composition(expr, graphToExpression(opEnd.getChildren().get(0))) : expr;
      }
      return expr;

    } else if ("->".equals(curr.getName())) {
      if (curr.getChildren().isEmpty()) {
        return null;
      }
      System.out.println(curr);
      var trueBranch = graphToExpression(curr.getChildren().get(0));
      var falseBranch = graphToExpression(curr.getChildren().get(1));
      var condition = graphToExpression(curr.getChildren().get(2));
      var expr = new Ternary(condition, trueBranch, falseBranch);
      MyVertex opEnd = curr.getClosing();
      if (opEnd != null && !opEnd.getChildren().isEmpty()) {
        var right = graphToExpression(opEnd.getChildren().get(0));
        return right != null ? new Composition(expr, graphToExpression(opEnd.getChildren().get(0))) : expr;
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