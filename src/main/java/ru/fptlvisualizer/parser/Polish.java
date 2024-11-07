package ru.fptlvisualizer.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

class Polish {

  public static Map<String, Integer> order = Map.of(
      ".", 0,
      "*", 1,
      "->", 2
  );

  public static boolean isOperation(String token) {
    return order.containsKey(token);
  }

  public static boolean isOperationBefore(String o1, String o2) {
    return (!"->".equals(o1) || !"->".equals(o2)) && order.get(o1) <= order.get(o2);
  }

  public static List<String> convertToPolish(List<String> expression) {
    String literal = "^[a-zA-Z0-9]*$";
    Stack<String> stack = new Stack<>();
    List<String> result = new ArrayList<>();

    for (String token: expression) {
      if (token.matches(literal)) {
        result.add(token);
      }
      else if ("(".equals(token)) {
        stack.push("(");
      }
      else if (")".equals(token)) {
        while (!"(".equals(stack.peek())) {
          result.add(stack.pop());
        }
        stack.pop();
      }
      else if (",".equals(token)) {
        while (!"->".equals(stack.peek())) {
          result.add(stack.pop());
        }
      }
      else if (".".equals(token) || "*".equals(token) || "->".equals(token)) {
        while (!stack.isEmpty() && isOperation(stack.peek()) && isOperationBefore(stack.peek(), token)) {
          result.add(stack.pop());
        }
        stack.push(token);
      }
    }
    while(!stack.isEmpty()) {
      if (!isOperation(stack.peek())) {
        throw new IllegalArgumentException();
      }
      result.add(stack.pop());
    }
    return result;
  }
}
