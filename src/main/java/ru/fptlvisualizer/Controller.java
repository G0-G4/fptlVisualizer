package ru.fptlvisualizer;

import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import ru.fptlvisualizer.parser.Parser;
import ru.fptlvisualizer.tree.expression.Expression;
import ru.fptlvisualizer.tree.graph.ConcatenationVertex;
import static ru.fptlvisualizer.tree.graph.Convertor.expressionToGraph;
import static ru.fptlvisualizer.tree.graph.Convertor.graphToExpression;
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
    s = expressionToGraph(expression);
    System.out.println(g);
    drawGraph(s);
    System.out.println(graphToExpression(s));
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
    var openX = x;
    var openY = y;

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
      var max = rightEndOfOperation.getComposed()
          .map(composed -> placeVerticies(composed, closingX + dx, openY))
          .orElseGet(() -> new maxXY(closingX, openY));
      return new maxXY(Math.max(closingX, max.maxX), Math.max(y, max.maxY));

    } else {
      var exp = (LiteralVertex) start;
      g.insertVertex(exp);
      exp.setPosition(openX, openY);
      var max = exp.getComposed()
          .map(composed -> placeVerticies(composed, openX + dx, openY))
          .orElseGet(() -> new maxXY(openX, openY));
      return new maxXY(Math.max(openX, max.maxX), Math.max(openY, max.maxY));

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

  private void clearGraph() {
    for (var vertex : g.vertices()) {
      g.removeVertex(vertex);
    }
  }
}