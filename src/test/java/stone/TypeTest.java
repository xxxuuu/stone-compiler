package stone;

import org.junit.jupiter.api.Test;
import stone.ast.ASTree;
import stone.parser.ArrayParser;
import stone.parser.TypeParser;

import java.io.StringReader;
import java.lang.reflect.Type;

/**
 * @author XUQING
 * @date 2021/5/1
 */
public class TypeTest {
    @Test
    public void arrayTest() throws ParseException {
        String testCode = "def inc(n: Int): Int { n + 1 }\n"
                + "var i: Int = 1\n"
                + "print(inc(i))";
        StringReader reader = new StringReader(testCode);
        Lexer l = new Lexer(reader);
        BasicEnv e = new BasicEnv();
        new Natives().environment(e);
        TypeParser p = new TypeParser();
        while(l.peek(0) != Token.EOF) {
            ASTree t = p.parse(l);
            t.eval(e);
        }
    }
}
