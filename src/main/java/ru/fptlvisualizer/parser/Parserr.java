package ru.fptlvisualizer.parser;

public class Parserr {

  public static void main(String[] args) {
    String code = "(a * 0).equal -> 1,(a * 1).equal -> 1,(((a * 2).sub.Fib * (a * 1).sub.Fib).add)";
    System.out.println(Parser.parse(code));
  }
}
