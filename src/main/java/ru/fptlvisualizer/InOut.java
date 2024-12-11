package ru.fptlvisualizer;

import ru.fptlvisualizer.tree.graph.ExpressionVertex;

public record InOut (MyVertex in, MyVertex out, double maxY, ExpressionVertex vertex){
}
