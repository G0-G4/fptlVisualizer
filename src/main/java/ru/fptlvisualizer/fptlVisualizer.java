package ru.fptlvisualizer;

import com.brunomnsilva.smartgraph.containers.ContentZoomScrollPane;
import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.fptlvisualizer.tree.graph.ExpressionEdge;
import ru.fptlvisualizer.tree.graph.ExpressionVertex;

public class fptlVisualizer extends Application {
  Controller controller = new Controller();

  @Override
  public void start(Stage stage) {

    Pane root = new VBox();
    TextArea textArea = new TextArea();

    Button buildGraph = new Button("отобразить");

    Digraph<ExpressionVertex, ExpressionEdge> g = new DigraphEdgeList<>();
    controller.setGraph(g);
//    SmartPlacementStrategy initialPlacement = new SmartCircularSortedPlacementStrategy();
    SmartGraphPanel<ExpressionVertex, ExpressionEdge> graphView = new SmartGraphPanel<>(g);
    var pane = new ContentZoomScrollPane(graphView);
    controller.setGraphPanel(graphView);
    controller.setGraphPanelq(graphView);
    graphView.setMinHeight(500);
    graphView.setMinWidth(1024);
//    graphView.setAutomaticLayout(true);
    root.getChildren().addAll(textArea, buildGraph, pane);

    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
    graphView.init();
    System.out.println(System.getProperty("user.dir"));

    buildGraph.setOnAction((event) -> {
      controller.onButtonClick(textArea.getText());
      graphView.update();
//      graphView.setAutomaticLayout(true);
//      graphView.update();
//      graphView.setAutomaticLayout(false);
    });
  }

  public static void main(String[] args) {
    launch();
  }
}