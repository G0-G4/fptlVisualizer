package ru.fptlvisualizer.parser;

public class PrintingVisitor implements ru.fptlvisualizer.parser.FptlParserVisitor {

  @Override
  public Object visit(ru.fptlvisualizer.parser.SimpleNode node, Object data) {
    System.out.println("node");
    node.childrenAccept(this, data);
    return null;
  }

  @Override
  public Object visit(ru.fptlvisualizer.parser.ASTFunction node, Object data) {
    System.out.println("function");
    node.childrenAccept(this, data);
    return null;
  }

  @Override
  public Object visit(ru.fptlvisualizer.parser.ASTComposition node, Object data) {
    System.out.println("composition");
    node.childrenAccept(this, data);
    return null;
  }

  @Override
  public Object visit(ru.fptlvisualizer.parser.ASTConcatenation node, Object data) {
    System.out.println("concatenation");
    node.childrenAccept(this, data);
    return null;
  }

  @Override
  public Object visit(ru.fptlvisualizer.parser.ASTTerm node, Object data) {
    System.out.println("term");
    node.childrenAccept(this, data);
    return null;
  }

  @Override
  public Object visit(ru.fptlvisualizer.parser.ASTIdentifier node, Object data) {
    System.out.println("identifier " + node.jjtGetValue());
    node.childrenAccept(this, data);
    return null;
  }
}
