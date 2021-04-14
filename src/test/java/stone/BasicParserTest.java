package stone;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import stone.ast.ASTree;

import java.io.StringReader;

/**
 * @author XUQING
 * @date 2021/4/14
 */
public class BasicParserTest {
    @Test
    public void parserTest() throws ParseException {
        String testCode = "// parser test\n" +
                "even = 0\n" +
                "odd = 0\n" +
                "i = 1\n" +
                "while i < 10 {\n" +
                "    if i % 2 == 0 {   // even number?\n" +
                "        even = even + i\n" +
                "    } else {\n" +
                "        odd = odd + i\n" +
                "    }\n" +
                "    i = i + 1\n" +
                "}\n" +
                "even + odd";
        StringReader reader = new StringReader(testCode);
        Lexer l = new Lexer(reader);
        BasicParser parser = new BasicParser();

        while(l.peek(0) != Token.EOF) {
            ASTree tree = parser.parse(l);
            System.out.println(tree.toString());
        }
    }
}
