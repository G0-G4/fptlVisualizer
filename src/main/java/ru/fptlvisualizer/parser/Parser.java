package ru.fptlvisualizer.parser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class Parser {
  public static void main(String[] args) throws ParseException {
    String code = """
        a=b->c,d->e,f;
        """;
    InputStream stream = new ByteArrayInputStream(code.getBytes());
    FptlParser parser = new FptlParser(stream);
    ru.fptlvisualizer.parser.SimpleNode n = parser.Function();
    PrintingVisitor visitor = new PrintingVisitor();
    visitor.visit(n, null);
  }
}
