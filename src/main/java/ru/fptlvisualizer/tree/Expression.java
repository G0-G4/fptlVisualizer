package ru.fptlvisualizer.tree;

import java.util.List;
import java.util.Stack;

public class Expression {
  public static Expression fromPolish(List<String> polish) {
    String literal = "^[a-zA-Z0-9]*$";
    Stack<Expression> stack = new Stack<>();
    for (String term: polish) {
     if (term.matches(literal)) {
       stack.push(new Literal(term));
     }
     else if ("*".equals(term)) {
       Expression right = stack.pop();
       Expression left = stack.pop();
       stack.push(new Concatenation(left, right));
     }
     else if (".".equals(term)) {
       Expression right = stack.pop();
       Expression left = stack.pop();
       stack.push(new Composition(left, right));
     }
    }
    assert stack.size() == 1;
    return stack.pop();
  }
}
