package ru.fptlvisualizer;

import com.brunomnsilva.smartgraph.containers.ContentZoomScrollPane;
import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.fptlvisualizer.tree.graph.ExpressionVertex;

public class fptlVisualizer extends Application {
  Controller controller = new Controller();
  @Override
  public void start(Stage stage) {

    Pane root = new VBox();
    TextArea textArea = new TextArea();

    Button buildGraph = new Button("отобразить");

    Digraph<ExpressionVertex, MyEdge> g = new DigraphEdgeList<>();
    controller.setGraph(g);
    SmartPlacementStrategy initialPlacement = new SmartCircularSortedPlacementStrategy();
    SmartGraphPanel<ExpressionVertex, MyEdge> graphView = new SmartGraphPanel<>(g, initialPlacement);
    var pane = new ContentZoomScrollPane(graphView);
    controller.setGraphPanel(graphView);
    controller.setGraphPanelq(graphView);
    graphView.setMinHeight(500);
    graphView.setMinWidth(1024);
    root.getChildren().addAll(textArea, buildGraph, pane);

    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
    graphView.init();

    buildGraph.setOnAction((event) -> {
      controller.onButtonClick(textArea.getText());
      graphView.update();
    });
  }

  public static void main(String[] args) {
    launch();
  }
}