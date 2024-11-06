package ru.fptlvisualizer.parser;

import java.util.List;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class TokenizerTest {

  @ParameterizedTest
  @MethodSource("tokenizeString")
  public void testTokenize(String expression, List<String> expectedTokens) throws IllegalAccessException {
    var result = Tokenizer.tokenize(expression);

    assertEquals(expectedTokens, result);
  }

  @ParameterizedTest
  @MethodSource("wrongTokens")
  public void testWhenUnexpectedToken(String expression) {
    assertThrows(IllegalArgumentException.class, () -> Tokenizer.tokenize(expression));
  }

  private static Stream<Arguments> tokenizeString() {
    return Stream.of(
        Arguments.of("a*,b.c->e", List.of("a", "*", ",", "b", ".", "c", "->", "e")),
        Arguments.of("**", List.of("*", "*")),
        Arguments.of("  ->   ->", List.of("->", "->")),
        Arguments.of("a -> b, c*d (  )   ", List.of("a", "->", "b", ",", "c", "*", "d", "(", ")")),
        Arguments.of("(Nasa * 0).equal -> (123 * 0)", List.of("(", "Nasa", "*", "0", ")", ".", "equal", "->", "(", "123", "*", "0", ")"))
    );
  }

  private static Stream<Arguments> wrongTokens() {
    return Stream.of(
        Arguments.of("a -< a, b"),
        Arguments.of("afa > afaf"),
        Arguments.of("lsdfm .. ksafj | kdfjh")
    );
  }
}