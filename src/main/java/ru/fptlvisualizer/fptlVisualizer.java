package ru.fptlvisualizer;

import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class fptlVisualizer extends Application {
  Controller controller = new Controller();
  @Override
  public void start(Stage stage) throws IOException {

    Pane root = new VBox();
    TextArea textArea = new TextArea();

    Button buildGraph = new Button("отобразить");

    Digraph<Vertex, Edge> g = new DigraphEdgeList<>();
    controller.setGraph(g);
    SmartPlacementStrategy initialPlacement = new SmartCircularSortedPlacementStrategy();
    SmartGraphPanel<Vertex, Edge> graphView = new SmartGraphPanel<>(g, initialPlacement);
    graphView.setMinHeight(100);
    graphView.setAutomaticLayout(true);
    root.getChildren().addAll(textArea, buildGraph, graphView);

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