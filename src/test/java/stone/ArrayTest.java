package stone;

import org.junit.jupiter.api.Test;
import stone.ast.ASTree;
import stone.env.BasicEnv;
import stone.env.Natives;
import stone.exception.ParseException;
import stone.parser.ArrayParser;

import java.io.StringReader;

/**
 * @author XUQING
 * @date 2021/4/18
 */
public class ArrayTest {
    @Test
    public void arrayTest() throws ParseException {
        String testCode = "a = [2, 3, 4]\n" +
                "print(a[1])\n" +
                "a[1] = \"three\"\n" +
                "print(\"a[1]: \" + a[1])\n" +
                "b = [[\"one\", 1], [\"two\", 2]]\n" +
                "print(b[1][0] + \": \" + b[1][1])";
        StringReader reader = new StringReader(testCode);
        Lexer l = new Lexer(reader);
        BasicEnv e = new BasicEnv();
        new Natives().environment(e);
        ArrayParser p = new ArrayParser();
        while(l.peek(0) != Token.EOF) {
            ASTree t = p.parse(l);
            t.eval(e);
        }
    }
}
