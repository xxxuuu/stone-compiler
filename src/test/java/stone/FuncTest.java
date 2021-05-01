package stone;

import org.junit.jupiter.api.Test;
import stone.ast.ASTree;
import stone.parser.ClosureParser;
import stone.parser.FuncParser;

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

    @Test
    public void closureTest() throws ParseException {
        String testCode = "def counter(c) {\n" +
                "    fun () {\n" +
                "        c = c+1\n" +
                "    }\n" +
                "}\n" +
                "c1 = counter(0)\n" +
                "c2 = counter(0)\n" +
                "c1()\n" +
                "c1()\n" +
                "c2()";
        StringReader reader = new StringReader(testCode);
        Lexer l = new Lexer(reader);
        BasicEnv e = new BasicEnv();
        ClosureParser p = new ClosureParser();

        while(l.peek(0) != Token.EOF) {
            ASTree t = p.parse(l);
            System.out.println(t.toString() + " => " + t.eval(e));
        }
    }

    @Test
    public void nativeTest() throws ParseException {
        String testCode = "def fib(n) {\n" +
                "    if n < 2 {\n" +
                "        n\n" +
                "    } else {\n" +
                "        fib(n-1) + fib(n-2)\n" +
                "    }\n" +
                "}\n" +
                "t = currentTime()\n" +
                "print(fib(10))\n" +
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
