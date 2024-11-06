package ru.fptlvisualizer.parser;

import ru.fptlvisualizer.tree.Expression;

public class Parser {


  public static void main(String[] args) {
    String code1 = "a * b.c.d * e";
    String code2 = "a * (b.c.d) * e";
    String code3 = "(a * b).c.d * e";

    var polish1 = Polish.convertToPolish(Tokenizer.tokenize(code1));
    var expr1 = Expression.fromPolish(polish1);
    System.out.println(code1 + " -- " + polish1 + " -- " + expr1);

    var polish2 = Polish.convertToPolish(Tokenizer.tokenize(code2));
    var expr2 = Expression.fromPolish(polish2);
    System.out.println(code2 + " -- " + polish2 + " -- " + expr2);

    var polish3 = Polish.convertToPolish(Tokenizer.tokenize(code3));
    var expr3 = Expression.fromPolish(polish3);
    System.out.println(code3 + " -- " + polish3 + " -- " + expr3);

    System.out.println(expr1.equals(expr2));
    System.out.println(expr1.equals(expr3));

  }
}
