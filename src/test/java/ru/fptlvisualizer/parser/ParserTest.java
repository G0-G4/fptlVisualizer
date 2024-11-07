package ru.fptlvisualizer.parser;

import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ParserTest {

  @ParameterizedTest
  @MethodSource("validExpressions")
  public void testStringRepresentationWithValidExpression(String expression, String expected) {
    String result = Parser.parse(expression).toString();

    assertEquals(expected, result);
  }

  private static Stream<Arguments> validExpressions() {
    return Stream.of(
        Arguments.of("abc", "abc"),
        Arguments.of("a.b", "a.b"),
        Arguments.of("a*b", "a*b"),
        Arguments.of("a.b*c.d", "a.b*c.d"),
        Arguments.of("a.(b*c).d", "a.(b*c).d"),
        Arguments.of("a -> c, d", "a->c,d"),
        Arguments.of("(a * 0).equal -> 1,(a * 1).equal -> 1,(((a * 2).sub.Fib * (a * 1).sub.Fib).add)",
            "(a*0).equal->1,(a*1).equal->1,((a*2).sub.Fib*(a*1).sub.Fib).add"),
        Arguments.arguments("(N * 0).equal -> (0 * 0), ((N * 1).sub * ((rand * rand).f * (N * 1).sub.integrate).add)",
            "(N*0).equal->0*0,(N*1).sub*((rand*rand).f*(N*1).sub.integrate).add")
    );
  }
}