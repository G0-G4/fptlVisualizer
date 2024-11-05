module ru.fptlvisualizer {
  requires javafx.controls;
  requires javafx.fxml;

  requires org.controlsfx.controls;
  requires io.github.eckig.grapheditor.api;
  requires io.github.eckig.grapheditor.core;

  opens ru.fptlvisualizer to javafx.fxml;
  exports ru.fptlvisualizer;
}