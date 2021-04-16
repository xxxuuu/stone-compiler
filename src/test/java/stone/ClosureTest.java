package stone;

import org.junit.jupiter.api.Test;
import stone.ast.ASTree;

import java.io.StringReader;

/**
 * @author XUQING
 * @date 2021/4/16
 */
public class ClosureTest {
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
}
