package ru.fptlvisualizer.parser;

public class Parserr {

  public static void main(String[] args) {
    var code = Parser.parse("a->b->c,d,e");
    System.out.println(code);
  }
}
