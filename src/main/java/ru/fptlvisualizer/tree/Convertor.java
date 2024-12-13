package ru.fptlvisualizer.tree;

import java.util.ArrayList;
import java.util.List;
import ru.fptlvisualizer.tree.expression.Composition;
import ru.fptlvisualizer.tree.expression.Concatenation;
import ru.fptlvisualizer.tree.expression.Expression;
import ru.fptlvisualizer.tree.expression.Literal;
import ru.fptlvisualizer.tree.expression.Ternary;
import ru.fptlvisualizer.tree.graph.ConcatenationVertex;
import ru.fptlvisualizer.tree.graph.ExpressionVertex;
import ru.fptlvisualizer.tree.graph.LiteralVertex;
import ru.fptlvisualizer.tree.graph.TernaryVertex;

public class Convertor {

  public static ExpressionVertex expressionToGraph(Expression exp) {
    if (exp instanceof Composition composition) {
      var left = expressionToGraph(composition.getLeft());
      var right = expressionToGraph(composition.getRight());
      left.getLastOperand().addChild(right);
      left.setLastOperand(right.getLastOperand());
      return left;
    }
    if (exp instanceof Concatenation concatenation) {
      var left = concatenation.getLeft();
      var right = concatenation.getRight();
      var leftVertex = expressionToGraph(left);
      var rightVertex = expressionToGraph(right);

      ConcatenationVertex openVertex;
      ConcatenationVertex closeVertex;

      if (left.getType() == Expression.Type.CONCATENATION && right.getType() == Expression.Type.CONCATENATION) {
        openVertex = (ConcatenationVertex) leftVertex;
        closeVertex = (ConcatenationVertex) leftVertex.getLastOperand();
        List<ExpressionVertex> children = new ArrayList<>();
        for (var child : rightVertex.getChildren()) {
          openVertex.addChild(child);
          children.add(child);
        }
        children.forEach(child -> child.removeParent(rightVertex));
        List<ExpressionVertex> parents = new ArrayList<>();
        for (var parent : rightVertex.getRightEndOfOperation().getParents()) {
          parent.addChild(closeVertex);
          parents.add(parent);
        }
        parents.forEach(parent -> parent.removeChild(rightVertex.getRightEndOfOperation()));

      } else if (left.getType() == Expression.Type.CONCATENATION) {
        openVertex = (ConcatenationVertex) leftVertex;
        closeVertex = (ConcatenationVertex) leftVertex.getLastOperand();
        openVertex.addChild(rightVertex);
        rightVertex.getLastOperand().addChild(closeVertex);

      } else if (right.getType() == Expression.Type.CONCATENATION) {

        openVertex = (ConcatenationVertex) rightVertex;
        closeVertex = (ConcatenationVertex) rightVertex.getLastOperand();
        openVertex.addChild(leftVertex);
        leftVertex.getLastOperand().addChild(closeVertex);

      } else {

        openVertex = new ConcatenationVertex();
        closeVertex = new ConcatenationVertex();

        openVertex.addChild(leftVertex);
        openVertex.addChild(rightVertex);
        leftVertex.getLastOperand().addChild(closeVertex);
        rightVertex.getLastOperand().addChild(closeVertex);
      }
      // TODO better create separate classes for open vertices
      //  with constructor forcing to set right end of operation to avoid forgetting setting it and to make it easier to distinguish them
      openVertex.setLastOperand(closeVertex);
      openVertex.setRightEndOfOperation(closeVertex);
      return openVertex;
    }

    if (exp instanceof Ternary ternary) {
      var trueBranch = expressionToGraph(ternary.getTrueBranch());
      var condition = expressionToGraph(ternary.getCondition());
      var falseBranch = expressionToGraph(ternary.getFalseBranch());

      var op = new TernaryVertex();
      var cl = new TernaryVertex();
      op.addChild(trueBranch);
      op.addChild(condition);
      op.addChild(falseBranch);
      trueBranch.getLastOperand().addChild(cl);
      condition.getLastOperand().addChild(cl);
      falseBranch.getLastOperand().addChild(cl);
      op.setLastOperand(cl);
      op.setRightEndOfOperation(cl);
      return op;
    }

    if (exp instanceof Literal literal) {
      return new LiteralVertex(literal.getLiteral());
    }
    throw new IllegalStateException("unexpected expression type " + exp.getClass());
  }

  public static Expression graphToExpression(ExpressionVertex curr) {
    Expression left;
    ExpressionVertex end;
    if (curr instanceof ConcatenationVertex) {
      left = curr.getChildren().stream()
          .map(Convertor::graphToExpression)
          .reduce(Concatenation::new)
          .get();
      end = curr.getRightEndOfOperation();

    } else if (curr instanceof TernaryVertex ternary) {
      if (curr.getChildren().isEmpty()) {
        return null;
      }
      var trueBranch = graphToExpression(ternary.getTrueBranch());
      var falseBranch = graphToExpression(ternary.getFalseBranch());
      var condition = graphToExpression(ternary.getCondition());
      left = new Ternary(condition, trueBranch, falseBranch);
      end = curr.getRightEndOfOperation();

    } else {
      left = new Literal(curr.getName());
      end = curr;
    }

    return end.getComposed()
        .map(composed -> (Expression) new Composition(left, graphToExpression(composed)))
        .orElse(left);
  }
}
