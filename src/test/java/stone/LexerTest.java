package stone;

import org.junit.jupiter.api.Test;
import stone.exception.ParseException;

import java.io.StringReader;

/**
 * 词法分析器单元测试
 * @author XUQING
 * @date 2020/5/15
 */
class LexerTest {
    @Test
    void read() throws ParseException {
        String testCode = "// lexer text\n"
                        + "test = \"hello world\"\n"
                        + "while i < 10{\n"
                        + "  sum = sum + i\n"
                        + "  i = i + 1\n"
                        + "}\n"
                        + "sum";
        StringReader reader = new StringReader(testCode);
        Lexer l = new Lexer(reader);

        for(Token t; (t = (l.read())) != Token.EOF; ) {
            System.out.println(t.getClass().getName() + " => " + t.getText());
        }
    }
}