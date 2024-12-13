package ru.fptlvisualizer.tree.graph;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.fptlvisualizer.parser.Parser;
import static ru.fptlvisualizer.tree.Convertor.expressionToGraph;
import static ru.fptlvisualizer.tree.Convertor.graphToExpression;

class ConvertorTest {

  @ParameterizedTest
  @MethodSource("validExpressions")
  public void integrationConvertorTest(String input) {
    var expression = Parser.parse(input);
    var graph = expressionToGraph(expression);
    var converted = graphToExpression(graph);
    assertEquals(input.replaceAll("\\s+", ""), converted.toString().replaceAll("\\s+", ""));
  }

  private static List<String> validExpressions() {
    return List.of(
        "abc",
        "a*b",
        "a.b*c.d",
        "a.(b*c).d",
        "a -> c, d",
        "(a * 0).equal -> 1,(a * 1).equal -> 1,((a * 2).sub.Fib * (a * 1).sub.Fib).add",
        "(N * 0).equal -> 0 * 0, (N * 1).sub * ((rand * rand).f * (N * 1).sub.integrate).add",
        "(a1 * 0).equal -> 1, ((a1 * 1).sub.Fact * a1).mul",
        "a.(b*c*d).e.f",
        "a*b*c -> c, d"
    );

  }

}
