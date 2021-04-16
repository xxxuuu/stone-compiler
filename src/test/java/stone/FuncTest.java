package stone;

import org.junit.jupiter.api.Test;
import stone.ast.ASTree;

import java.io.StringReader;

/**
 * @author XUQING
 * @date 2021/4/16
 */
public class FuncTest {
    @Test
    public void funcTest() throws ParseException {
        String testCode = "def fib(n) {\n" +
                "    if n < 2 {\n" +
                "        n\n" +
                "    } else {\n" +
                "        fib(n-1) + fib(n-2)\n" +
                "    }\n" +
                "}\n" +
                "fib(10)";
        StringReader reader = new StringReader(testCode);
        Lexer l = new Lexer(reader);
        BasicEnv e = new BasicEnv();
        FuncParser p = new FuncParser();

        while(l.peek(0) != Token.EOF) {
            ASTree t = p.parse(l);
            System.out.println(t.toString() + " => " + t.eval(e));
        }
    }
}
