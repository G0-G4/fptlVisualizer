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

    Digraph<MyVertex, MyEdge> g = new DigraphEdgeList<>();
    Digraph<ExpressionVertex, MyEdge> gg = new DigraphEdgeList<>();
    controller.setGraph(g);
    controller.setGraph1(gg);
    SmartPlacementStrategy initialPlacement = new SmartCircularSortedPlacementStrategy();
    SmartGraphPanel<ExpressionVertex, MyEdge> graphView1 = new SmartGraphPanel<>(gg, initialPlacement);
    SmartGraphPanel<MyVertex, MyEdge> graphView = new SmartGraphPanel<>(g, initialPlacement);
    var pane = new ContentZoomScrollPane(graphView);
    var pane1 = new ContentZoomScrollPane(graphView1);
    controller.setGraphPanel(graphView);
    controller.setGraphPanelq(graphView1);
    graphView.setMinHeight(500);
    graphView.setMinWidth(1024);
    graphView1.setMinHeight(500);
    graphView1.setMinWidth(1024);
//    graphView.setAutomaticLayout(true);
//    graphView1.layout();
//    graphView1.layout();
    root.getChildren().addAll(textArea, buildGraph, pane, pane1);

    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
    graphView.init();
    graphView1.init();

    buildGraph.setOnAction((event) -> {
      controller.onButtonClick(textArea.getText());
      graphView1.update();
    });
  }

  public static void main(String[] args) {
    launch();
  }
}