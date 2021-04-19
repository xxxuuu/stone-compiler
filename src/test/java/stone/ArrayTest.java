package stone;

import org.junit.jupiter.api.Test;
import stone.ast.ASTree;

import java.io.StringReader;

/**
 * @author XUQING
 * @date 2021/4/18
 */
public class ArrayTest {

    public void printTree(ASTree t, int deep) {
        for(int i = 1; i <= deep; i++) {
            System.out.print("\t");
        }
        System.out.println(t.getClass().getSimpleName() + " => " + t.toString());
        for(ASTree children : t) {
            printTree(children,deep+1);
        }
    }

    @Test
    public void arrayTest() throws ParseException {
        // TODO 数组名和下标之间需要空格才能被解析为ArrayRef
        String testCode = "a = [2, 3, 4]\na [1]\n" +
                "print(a [1])\n" +
                "a[1] = \"three\"\n" +
                "print(\"a[1]: \" + a[1])\n" +
                "b = [[\"one\", 1], [\"two\", 2]]\n" +
                "print(b [1][0] + \": \" + b [1][1])";
        StringReader reader = new StringReader(testCode);
        Lexer l = new Lexer(reader);
        BasicEnv e = new BasicEnv();
        new Natives().environment(e);
        ArrayParser p = new ArrayParser();

        while(l.peek(0) != Token.EOF) {
            ASTree t = p.parse(l);
            printTree(t,0);
            t.eval(e);
        }
    }
}
