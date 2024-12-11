//package ru.fptlvisualizer.tree.graph;
//
//import com.brunomnsilva.smartgraph.graph.Edge;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ExpressionEdge implements Edge<ExpressionEdge, ExpressionVertex> {
//  ExpressionVertex a;
//  List<ExpressionVertex> children = new ArrayList<>();
//
//  @Override
//  public ExpressionEdge element() {
//    return this;
//  }
//
//  @Override
//  public ExpressionVertex[] vertices() {
//    return children.toArray(new ExpressionVertex[0]);
//  }
//
//}
