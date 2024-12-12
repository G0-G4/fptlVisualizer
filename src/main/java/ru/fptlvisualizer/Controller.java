package ru.fptlvisualizer;

import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import ru.fptlvisualizer.parser.Parser;
import ru.fptlvisualizer.tree.expression.Composition;
import ru.fptlvisualizer.tree.expression.Concatenation;
import ru.fptlvisualizer.tree.expression.Expression;
import ru.fptlvisualizer.tree.expression.Literal;
import ru.fptlvisualizer.tree.expression.Ternary;
import ru.fptlvisualizer.tree.graph.ConcatenationVertex;
import ru.fptlvisualizer.tree.graph.ExpressionEdge;
import ru.fptlvisualizer.tree.graph.ExpressionVertex;
import ru.fptlvisualizer.tree.graph.LiteralVertex;
import ru.fptlvisualizer.tree.graph.TernaryVertex;

public class Controller {
  private Graph<ExpressionVertex, ExpressionEdge> g;
  private SmartGraphPanel<ExpressionVertex, ExpressionEdge> panel;
  int dx = 50;
  public ExpressionVertex s;

  public void setGraph(Graph<ExpressionVertex, ExpressionEdge> g) {
    this.g = g;
  }

  public void setGraphPanel(SmartGraphPanel<ExpressionVertex, ExpressionEdge> panel) {
    this.panel = panel;
  }

  public void setGraphPanelq(SmartGraphPanel<ExpressionVertex, ExpressionEdge> panel) {
    this.panel = panel;
  }

  protected void onButtonClick(String code) {
    Expression expression = Parser.parse(code);
    clearGraph();
    s = convertToGraph(expression);
    System.out.println(g);
    drawGraph(s);
    System.out.println(graphToExpression(s));
  }

  private ExpressionVertex convertToGraph(Expression exp) {
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
      if (left.getType() == Expression.Type.CONCATENATION && right.getType() == Expression.Type.CONCATENATION) {
        op = (ConcatenationVertex) inOutLeft;
        cl = (ConcatenationVertex) inOutLeft.getLastOperand();
        List<ExpressionVertex> children = new ArrayList<>();
        for (var child : inOutRight.getChildren()) {
          op.addChild(child);
          children.add(child);
        }
        children.forEach(child -> child.removeParent(inOutRight));
        List<ExpressionVertex> parents = new ArrayList<>();
        for (var parent : inOutRight.getRightEndOfOperation().getParents()) {
          parent.addChild(cl);
          parents.add(parent);
        }
        parents.forEach(parent -> parent.removeChild(inOutRight.getRightEndOfOperation()));
//        op.addChild(inOutRight);
//        inOutRight.getLastOperand().addChild(cl);
      } else if (left.getType() == Expression.Type.CONCATENATION) {
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
    placeVerticies(start, 550, 100);
    panel.updateAndWait();
    for (var vertex : g.vertices()) {
      panel.setVertexPosition(vertex, vertex.element().getX(), vertex.element().getY());
    }
    addEdges(start);
    panel.updateAndWait();
  }

  private record maxXY(double maxX, double maxY) {
  }

  private maxXY placeVerticies(ExpressionVertex start, double x, double y) {
    var openY = y;
    var openX = x;

    if (start instanceof ConcatenationVertex || start instanceof TernaryVertex) {
      double X = 0;
      x += dx;
      boolean firstChild = true;
      for (var vertex : start.getChildren()) {
        var max = placeVerticies(vertex, x, firstChild ? y : y + dx);
        X = Math.max(X, max.maxX);
        y = Math.max(y, max.maxY);
        firstChild = false;
      }

      var rightEndOfOperation = start.getRightEndOfOperation();

      var closingX = X + dx;
      g.insertVertex(start);
      g.insertVertex(rightEndOfOperation);
      start.setPosition(openX, openY);
      rightEndOfOperation.setPosition(closingX, openY);

      maxXY max = new maxXY(closingX, openY);
      if (rightEndOfOperation.getComposed().isPresent()) {
        max = placeVerticies(rightEndOfOperation.getComposed().get(), closingX + dx, openY);
      }
      return new maxXY(Math.max(closingX, max.maxX), Math.max(y, max.maxY));

    } else {
      var exp = (LiteralVertex) start;
      g.insertVertex(exp);
      exp.setPosition(x, y);
      maxXY max = new maxXY(x, y);
      if (exp.getComposed().isPresent()) {
        max = placeVerticies(exp.getComposed().get(), x + dx, y);
      }
      return new maxXY(Math.max(x, max.maxX), Math.max(y, max.maxY));

    }

  }


  private void addEdges(ExpressionVertex start) {
    Stack<ExpressionVertex> stack = new Stack<>();
    Set<ExpressionEdge> edges = new HashSet<>();
    stack.push(start);
    while (!stack.isEmpty()) {
      var vertex = stack.pop();
      for (var child : vertex.getChildren()) {
        var edge = new ExpressionEdge(vertex, child);
        if (!edges.contains(edge)) {
          g.insertEdge(vertex, child, edge);
          edges.add(edge);
        }
        stack.push(child);
      }
    }
  }

  private Expression graphToExpression(ExpressionVertex curr) {
    if (curr instanceof ConcatenationVertex) {
      var expr = curr.getChildren().stream()
          .map(this::graphToExpression)
          .reduce(Concatenation::new)
          .get();
      ExpressionVertex opEnd = curr.getRightEndOfOperation();
      if (opEnd.getComposed().isPresent()) {
        var right = graphToExpression(opEnd.getComposed().get());
        return right != null ? new Composition(expr, right) : expr;
      }
      return expr;

    } else if (curr instanceof TernaryVertex ternary) {
      if (curr.getChildren().isEmpty()) {
        return null;
      }
      var trueBranch = graphToExpression(ternary.getTrueBranch());
      var falseBranch = graphToExpression(ternary.getFalseBranch());
      var condition = graphToExpression(ternary.getCondition());
      var expr = new Ternary(condition, trueBranch, falseBranch);
      var opEnd = curr.getRightEndOfOperation();
      if (opEnd.getComposed().isPresent()) {
        var right = graphToExpression(opEnd.getComposed().get());
        return right != null ? new Composition(expr, right) : expr;
      }
      return expr;
    } else {
      var literal = new Literal(curr.getName());
      if (curr.getComposed().isPresent()) {
        var child = curr.getComposed().get();
        return new Composition(literal, graphToExpression(child));
      }
      return literal;
    }
  }

  private void clearGraph() {
    for (var vertex : g.vertices()) {
      g.removeVertex(vertex);
    }
  }
}