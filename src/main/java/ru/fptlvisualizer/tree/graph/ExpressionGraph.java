//package ru.fptlvisualizer.tree.graph;
//
//import com.brunomnsilva.smartgraph.graph.Digraph;
//import com.brunomnsilva.smartgraph.graph.Edge;
//import com.brunomnsilva.smartgraph.graph.InvalidEdgeException;
//import com.brunomnsilva.smartgraph.graph.InvalidVertexException;
//import com.brunomnsilva.smartgraph.graph.Vertex;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.List;
//
//public class ExpressionGraph implements Digraph<ExpressionVertex, ExpressionEdge> {
//
//  private final List<Vertex<ExpressionVertex>> vertices = new ArrayList<>();
////  private final List<Edge<>> edges = new ArrayList<>();
//
//  @Override
//  public int numVertices() {
//    return 0;
//  }
//
//  @Override
//  public int numEdges() {
//    return 0;
//  }
//
//  @Override
//  public Collection<Vertex<ExpressionVertex>> vertices() {
//    return Collections.unmodifiableList(vertices);
//  }
//
//  @Override
//  public Collection<Edge<ExpressionEdge, ExpressionVertex>> edges() {
//    return List.of();
//  }
//
//  @Override
//  public Collection<Edge<ExpressionEdge, ExpressionVertex>> incidentEdges(Vertex<ExpressionVertex> vertex) throws InvalidVertexException {
//    return List.of();
//  }
//
//  @Override
//  public Vertex<ExpressionVertex> opposite(
//      Vertex<ExpressionVertex> vertex,
//      Edge<ExpressionEdge, ExpressionVertex> edge
//  ) throws InvalidVertexException, InvalidEdgeException {
//    return null;
//  }
//
//  @Override
//  public Collection<Edge<ExpressionEdge, ExpressionVertex>> outboundEdges(Vertex<ExpressionVertex> vertex) throws InvalidVertexException {
//    return List.of();
//  }
//
//  @Override
//  public boolean areAdjacent(Vertex<ExpressionVertex> vertex, Vertex<ExpressionVertex> vertex1) throws InvalidVertexException {
//    return false;
//  }
//
//  @Override
//  public Vertex<ExpressionVertex> insertVertex(ExpressionVertex expressionVertex) throws InvalidVertexException {
//    var v = new Vertex<>(expressionVertex);
//    vertices.add(new Vertex<ExpressionVertex>(expressionVertex));
//    return null;
//  }
//
//  @Override
//  public Edge<ExpressionEdge, ExpressionVertex> insertEdge(
//      Vertex<ExpressionVertex> vertex,
//      Vertex<ExpressionVertex> vertex1,
//      ExpressionEdge expressionEdge
//  ) throws InvalidVertexException, InvalidEdgeException {
//    return null;
//  }
//
//  @Override
//  public Edge<ExpressionEdge, ExpressionVertex> insertEdge(
//      ExpressionVertex expressionVertex,
//      ExpressionVertex v1,
//      ExpressionEdge expressionEdge
//  ) throws InvalidVertexException, InvalidEdgeException {
//    return null;
//  }
//
//  @Override
//  public ExpressionVertex removeVertex(Vertex<ExpressionVertex> vertex) throws InvalidVertexException {
//    return null;
//  }
//
//  @Override
//  public ExpressionEdge removeEdge(Edge<ExpressionEdge, ExpressionVertex> edge) throws InvalidEdgeException {
//    return null;
//  }
//
//  @Override
//  public ExpressionVertex replace(Vertex<ExpressionVertex> vertex, ExpressionVertex expressionVertex) throws InvalidVertexException {
//    return null;
//  }
//
//  @Override
//  public ExpressionEdge replace(Edge<ExpressionEdge, ExpressionVertex> edge, ExpressionEdge expressionEdge) throws InvalidEdgeException {
//    return null;
//  }
//}
