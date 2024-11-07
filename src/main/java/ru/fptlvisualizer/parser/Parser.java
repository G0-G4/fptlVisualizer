package ru.fptlvisualizer.parser;

import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;
import ru.fptlvisualizer.tree.Composition;
import ru.fptlvisualizer.tree.Concatenation;
import ru.fptlvisualizer.tree.Expression;
import ru.fptlvisualizer.tree.Literal;
import ru.fptlvisualizer.tree.Ternary;

public class Parser {

  public static Expression parse(String expression) {
    List<String> tokens = Tokenizer.tokenize(expression);
    List<String> polish = Polish.convertToPolish(tokens);
    return parse(polish);
  }

  private static Expression parse(List<String> polish) {
    String literal = "^[a-zA-Z0-9]*$";
    Stack<Expression> stack = new Stack<>();
    try {
      for (String term : polish) {
        if (term.matches(literal)) {
          stack.push(new Literal(term));
        } else if ("*".equals(term)) {
          Expression right = stack.pop();
          Expression left = stack.pop();
          stack.push(new Concatenation(left, right));
        } else if (".".equals(term)) {
          Expression right = stack.pop();
          Expression left = stack.pop();
          stack.push(new Composition(left, right));
        } else if ("->".equals(term)) {
          Expression falseBranch = stack.pop();
          Expression trueBranch = stack.pop();
          Expression condition = stack.pop();
          stack.push(new Ternary(condition, trueBranch, falseBranch));
        }
      }
    } catch (EmptyStackException e) {
      throw new IllegalArgumentException("error while converting from polish notation", e);
    }
    assert stack.size() == 1;
    return stack.pop();
  }
}
