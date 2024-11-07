package ru.fptlvisualizer.parser;

import java.util.ArrayList;
import java.util.List;

class Tokenizer {

  private enum State {
    literal,
    ternary,
    none
  }

  public static List<String> tokenize(String expression) {
   expression = expression.trim().replaceAll("\\s+", "");

   String token = "";
   State state = State.none;
   List<String> result = new ArrayList<>();

   for (int i = 0; i < expression.length(); i++) {
     char c = expression.charAt(i);
     if (c >= '0' && c <= '9' || c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') {
       if (state != State.literal) {
         state = State.literal;
         if (!token.isEmpty()) {
           result.add(token);
         }
         token = "";
       }
       token += c;
     }
     else if (c == '.' || c == '*' || c == '(' || c == ')' || c == ',') {
       if (state != State.none) {
         state = State.none;
         if (!token.isEmpty()) {
          result.add(token);
         }
         token = "";
       }
       result.add(""+c);
     }
     else if (c == '-') {
       if (state != State.ternary) {
         state = State.ternary;
         if (!token.isEmpty()) {
           result.add(token);
         }
         token = "";
       }
     }
     else if (c == '>') {
       if (state != State.ternary) {
         throw new IllegalArgumentException("unexpected '>' at " + i);
       }
       result.add("->");
       state = State.none;
       token = "";
     }
     else {
       throw new IllegalArgumentException("unexpected character '" + c + "' at " + i);
     }
   }
   if (!token.isEmpty()) {
     result.add(token);
   }
   return result;
  }
}
