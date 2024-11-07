package ru.fptlvisualizer.parser;

import java.util.List;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PolishTest {

  @ParameterizedTest
  @MethodSource("expressions")
  public void testPolishWithCorrectExpressions(String expression, String expectedPolish) {
    List<String> tokens = Tokenizer.tokenize(expression);

    String polish = String.join(" ", Polish.convertToPolish(tokens));

    assertEquals(expectedPolish, polish);
  }

  private static Stream<Arguments> expressions() {
    return Stream.of(
        Arguments.of("a.b", "a b ."),
        Arguments.of("a*b", "a b *"),
        Arguments.of("((a.b))", "a b ."),
        Arguments.of("a->b,c", "a b c ->"),
        Arguments.of("a->b->c,d,e", "a b c d -> e ->"), // syntax error in fptl
        Arguments.of("a->(b->c,d),e", "a b c d -> e ->"),
        Arguments.of("a->b,c->d,e", "a b c d e -> ->"),
        Arguments.of("a*b.c*e", "a b c . * e *"),
        Arguments.of("(a*b).(c*e)", "a b * c e * ."),
        Arguments.of("a->d,e*c->f,g", "a d e c * f g -> ->"),
        Arguments.of("(a->d,e)*(c->f,g)", "a d e -> c f g -> *"),
        Arguments.of(
            "(a * 0).equal -> 1,(a * 1).equal -> 1,(((a * 2).sub.Fib * (a * 1).sub.Fib).add)",
            "a 0 * equal . 1 a 1 * equal . 1 a 2 * sub . Fib . a 1 * sub . Fib . * add . -> ->"
        )
    );
  }
}
