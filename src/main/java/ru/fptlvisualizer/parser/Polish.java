package ru.fptlvisualizer.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Polish {

//  ., *, -> - порядок операций
// TODO добавить тернарный оператор

  public static Map<String, Integer> order = Map.of(
      ".", 0,
      "*", 1,
      "->", 2
  );

  public static boolean isOperation(String token) {
    return order.containsKey(token);
  }

  public static List<String> convertToPolish(List<String> expression) {
    String literal = "^[a-zA-Z0-9]*$";
    Stack<String> stack = new Stack<>();
    List<String> result = new ArrayList<>();

    for (String token: expression) {
      if (token.matches(literal)) {
        result.add(token);
      } else if ("(".equals(token)) {
        stack.push("(");
      } else if (")".equals(token)) {
        while (!"(".equals(stack.peek())) {
          result.add(stack.pop());
        }
        stack.pop();
      }
      else if (".".equals(token) || "*".equals(token)) {
        while (!stack.isEmpty() && isOperation(stack.peek()) && order.get(stack.peek()) <= order.get(token) ) {
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
