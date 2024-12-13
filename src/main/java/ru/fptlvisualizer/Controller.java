package ru.fptlvisualizer;

import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertex;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import javafx.scene.control.TextArea;
import ru.fptlvisualizer.parser.Parser;
import ru.fptlvisualizer.tree.expression.Expression;
import ru.fptlvisualizer.tree.graph.ConcatenationVertex;
import static ru.fptlvisualizer.tree.Convertor.expressionToGraph;
import static ru.fptlvisualizer.tree.Convertor.graphToExpression;
import ru.fptlvisualizer.tree.graph.ExpressionEdge;
import ru.fptlvisualizer.tree.graph.ExpressionVertex;
import ru.fptlvisualizer.tree.graph.LiteralVertex;
import ru.fptlvisualizer.tree.graph.TernaryVertex;

public class Controller {
  private static final double START_X = 500;
  private static final double START_Y = 100;
  private static final int DELTA = 50;

  private enum Operation {
    CONNECT,
    DELETE,
    GENERATE_EXPRESSION
  }

  private final Graph<ExpressionVertex, ExpressionEdge> g;
  private final TextArea expressionInput;
  private final SmartGraphPanel<ExpressionVertex, ExpressionEdge> panel;
  public ExpressionVertex s;
  private Stack<ExpressionVertex> verts = new Stack<>();
  private Operation operation = Operation.CONNECT;

  public Controller(TextArea expressionInput, SmartGraphPanel<ExpressionVertex, ExpressionEdge> panel, Graph<ExpressionVertex, ExpressionEdge> g) {
    this.expressionInput = expressionInput;
    this.panel = panel;
    this.g = g;
    this.panel.setVertexDoubleClickAction(this::performOperation);
  }

  public void performOperation(SmartGraphVertex<ExpressionVertex>  vertex) {
    switch (operation) {
      case DELETE -> {
        vertex.getUnderlyingVertex().element().deleteFromGraph();
        g.removeVertex(vertex.getUnderlyingVertex());
        panel.update();
      }
      case CONNECT -> {
        if (verts.size() < 2) {
          verts.push(vertex.getUnderlyingVertex().element());
        }
        if (verts.size() == 2) {
          var b = verts.pop();
          var a = verts.pop();
          a.addChild(b);
          g.insertEdge(a, b, new ExpressionEdge(a, b));
          panel.update();
        }
      }
      case GENERATE_EXPRESSION -> {
        expressionInput.setText(graphToExpression(vertex.getUnderlyingVertex().element()).toString());
      }
    }
  }

  public void addConcatenation() {
    var open = new ConcatenationVertex();
    var close = new ConcatenationVertex();
    insertOperation(open, close);
  }

  public void addTernary() {
    var open = new TernaryVertex();
    var close = new TernaryVertex();
    insertOperation(open, close);
  }

  private void insertOperation(ExpressionVertex open, ExpressionVertex close) {
    open.setPosition(START_X, START_Y);
    close.setPosition(START_X + DELTA, START_Y);
    open.setRightEndOfOperation(close);
    var o = g.insertVertex(open);
    var c = g.insertVertex(close);
    panel.updateAndWait(); // without it position doesn't update
    panel.setVertexPosition(o, START_X, START_Y);
    panel.setVertexPosition(c, START_X + DELTA, START_Y);
    panel.update();
  }

  public void addLiteral(String name) {
    var vertex = new LiteralVertex(name);
    g.insertVertex(vertex);
    panel.update();
  }

  public void connect() {
    operation = Operation.CONNECT;
  }

  public void delete() {
    operation = Operation.DELETE;
  }

  public void generate() {
    operation = Operation.GENERATE_EXPRESSION;
  }

  protected void onButtonClick(String code) {
    Expression expression = Parser.parse(code);
    clearGraph();
    drawGraph(expressionToGraph(expression));
    panel.update();
  }

  private void drawGraph(ExpressionVertex start) {
    placeVertices(start, START_X, START_Y);
    panel.updateAndWait();
    for (var vertex : g.vertices()) {
      panel.setVertexPosition(vertex, vertex.element().getX(), vertex.element().getY());
    }
    addEdges(start);
    panel.updateAndWait();
  }

  private record maxXY(double maxX, double maxY) {
  }

  private maxXY placeVertices(ExpressionVertex start, double x, double y) {
    var openX = x;
    var openY = y;

    if (start instanceof ConcatenationVertex || start instanceof TernaryVertex) {
      double X = 0;
      x += DELTA;
      boolean firstChild = true;
      for (var vertex : start.getChildren()) {
        var max = placeVertices(vertex, x, firstChild ? y : y + DELTA);
        X = Math.max(X, max.maxX);
        y = Math.max(y, max.maxY);
        firstChild = false;
      }

      var rightEndOfOperation = start.getRightEndOfOperation();

      var closingX = X + DELTA;
      g.insertVertex(start);
      g.insertVertex(rightEndOfOperation);
      start.setPosition(openX, openY);
      rightEndOfOperation.setPosition(closingX, openY);
      var max = rightEndOfOperation.getComposed()
          .map(composed -> placeVertices(composed, closingX + DELTA, openY))
          .orElseGet(() -> new maxXY(closingX, openY));
      return new maxXY(Math.max(closingX, max.maxX), Math.max(y, max.maxY));

    } else {
      var exp = (LiteralVertex) start;
      g.insertVertex(exp);
      exp.setPosition(openX, openY);
      var max = exp.getComposed()
          .map(composed -> placeVertices(composed, openX + DELTA, openY))
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
      for (var child: vertex.getChildren()) {
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
