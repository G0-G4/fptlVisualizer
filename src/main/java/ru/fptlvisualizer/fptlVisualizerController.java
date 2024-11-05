package ru.fptlvisualizer;

import io.github.eckig.grapheditor.Commands;
import io.github.eckig.grapheditor.GraphEditor;
import io.github.eckig.grapheditor.core.DefaultGraphEditor;
import io.github.eckig.grapheditor.core.view.GraphEditorContainer;
import io.github.eckig.grapheditor.model.GConnector;
import io.github.eckig.grapheditor.model.GModel;
import io.github.eckig.grapheditor.model.GNode;
import io.github.eckig.grapheditor.model.GraphFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class fptlVisualizerController {
  @FXML
  public GraphEditorContainer graphEditorContainer;
  @FXML
  private Label welcomeText;
  private final GraphEditor graphEditor = new DefaultGraphEditor();

  public void initialize() {

    final GModel model = GraphFactory.eINSTANCE.createGModel();

    graphEditor.setModel(model);
    addNodes(model);
    graphEditorContainer.setGraphEditor(graphEditor);
  }


  @FXML
  protected void onHelloButtonClick() {
    welcomeText.setText("Welcome to JavaFX Application!");
  }

  private GNode createNode()
  {
    GNode node = GraphFactory.eINSTANCE.createGNode();

    GConnector input = GraphFactory.eINSTANCE.createGConnector();
    GConnector output = GraphFactory.eINSTANCE.createGConnector();

    input.setType("left-input");
    output.setType("right-output");

    node.getConnectors().add(input);
    node.getConnectors().add(output);

    return node;
  }

  private void addNodes(GModel model)
  {
    GNode firstNode = createNode();
    GNode secondNode = createNode();

    firstNode.setX(150);
    firstNode.setY(150);

    secondNode.setX(400);
    secondNode.setY(200);
    secondNode.setWidth(200);
    secondNode.setHeight(150);

    Commands.addNode(model, firstNode);
    Commands.addNode(model, secondNode);
  }
}