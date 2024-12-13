package ru.fptlvisualizer;

import com.brunomnsilva.smartgraph.containers.ContentZoomScrollPane;
import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.fptlvisualizer.tree.graph.ExpressionEdge;
import ru.fptlvisualizer.tree.graph.ExpressionVertex;

public class fptlVisualizer extends Application {
  private static final int MIN_HEIGHT = 500;
  private static final int MIN_WIDTH = 1024;
  Controller controller;

  @Override
  public void start(Stage stage) {

    Pane root = new VBox();
    TextArea textArea = new TextArea();
    Button buildGraph = new Button("выражение в граф");
    Button composition = new Button("*");
    Button ternary = new Button("->");
    TextField textField = new TextField();
    Button literal = new Button("литерал");
    Button connect = new Button("соединить");
    Button delete = new Button("удалить узел");
    Button buildExpression = new Button("граф в выражение");

    Digraph<ExpressionVertex, ExpressionEdge> g = new DigraphEdgeList<>();
    SmartGraphPanel<ExpressionVertex, ExpressionEdge> graphView = new SmartGraphPanel<>(g);
    var pane = new ContentZoomScrollPane(graphView);
    controller = new Controller(textArea, graphView, g);

    graphView.setMinHeight(MIN_HEIGHT);
    graphView.setMinWidth(MIN_WIDTH);
    root.getChildren().addAll(textArea, buildGraph, composition, ternary, textField, literal, connect, delete, buildExpression, pane);

    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
    graphView.init();

    buildGraph.setOnAction((event) -> controller.onButtonClick(textArea.getText()));
    composition.setOnAction((event -> controller.addConcatenation()));
    ternary.setOnAction((event -> controller.addTernary()));
    literal.setOnAction((event -> controller.addLiteral(textField.getText())));
    connect.setOnAction(event -> controller.connect());
    delete.setOnAction(event -> controller.delete());
    buildExpression.setOnAction(event -> controller.generate());
  }

  public static void main(String[] args) {
    launch();
  }
}