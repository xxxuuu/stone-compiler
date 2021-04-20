package stone;

import org.junit.jupiter.api.Test;
import stone.ast.ASTree;

import java.io.StringReader;

/**
 * @author XUQING
 * @date 2021/4/20
 */
public class BenchmarkTest {
    @Test
    public void fibTest() throws ParseException {
        String testCode = "def fib(n) {\n" +
                "    if n < 2 {\n" +
                "        n\n" +
                "    } else {\n" +
                "        fib(n-1) + fib(n-2)\n" +
                "    }\n" +
                "}\n" +
                "t = currentTime()\n" +
                "print(fib(33))\n" +
                "print(currentTime() - t + \" msec\")";
        StringReader reader = new StringReader(testCode);
        Lexer l = new Lexer(reader);
        BasicEnv e = new BasicEnv();
        new Natives().environment(e);
        ClosureParser p = new ClosureParser();

        while(l.peek(0) != Token.EOF) {
            ASTree t = p.parse(l);
            t.eval(e);
        }
    }
}
