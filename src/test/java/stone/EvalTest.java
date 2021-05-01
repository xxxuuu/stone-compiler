package stone;

import org.junit.jupiter.api.Test;
import stone.ast.ASTree;
import stone.ast.NullStmnt;
import stone.parser.BasicParser;

import java.io.StringReader;

/**
 * @author XUQING
 * @date 2021/4/15
 */
public class EvalTest {
    @Test
    public void evalTest() throws ParseException {
        String testCode = "sum = 0\n" +
                "i = 1\n" +
                "while i <= 10 {\n" +
                "    sum = sum + i\n" +
                "    i = i+1 \n" +
                "}\n" +
                "sum";
        StringReader reader = new StringReader(testCode);
        Lexer l = new Lexer(reader);
        BasicParser parser = new BasicParser();
        BasicEnv e = new BasicEnv();

        while(l.peek(0) != Token.EOF) {
            ASTree tree = parser.parse(l);
            if(!(tree instanceof NullStmnt)) {
                Object result = tree.eval(e);
                System.out.println(tree.toString() + " => " + result);
            }
        }
    }

}
