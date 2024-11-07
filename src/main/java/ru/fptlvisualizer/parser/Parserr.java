package ru.fptlvisualizer.parser;

import ru.fptlvisualizer.tree.Literal;

public class Parserr {

  public static void main(String[] args) {
//    String code = "a *";
//    System.out.println(Polish.convertToPolish(Tokenizer.tokenize(code)));
//    System.out.println(Parser.parse(code));
//    var code = Parser.parse("(a.(b*c) -> c,d) -> e, g");
    var code = Parser.parse("a->b->c,d,e");
    System.out.println(code);
  }
}
