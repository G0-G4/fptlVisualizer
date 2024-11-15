module ru.fptlvisualizer {
  requires javafx.controls;
  requires javafx.fxml;

  requires org.controlsfx.controls;
  requires smartgraph;

  opens ru.fptlvisualizer to javafx.fxml;
  exports ru.fptlvisualizer;
}