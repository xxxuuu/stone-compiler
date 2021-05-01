package stone;

import org.junit.jupiter.api.Test;
import stone.ast.ASTree;
import stone.parser.ClassParser;

import java.io.StringReader;

/**
 * @author XUQING
 * @date 2021/4/17
 */
public class ClassTest {
    @Test
    public void classTest() throws ParseException {
        String testCode = "class Position {\n" +
                "    x = y = 0\n" +
                "    def move(nx, ny) {\n" +
                "        x = nx; y = ny\n" +
                "    }\n" +
                "}\n" +
                "class Pos3D extends Position {\n" +
                "    z = 0\n" +
                "    def move(nx, ny, nz) {\n" +
                "        x = nx; y = ny; z = nz\n" +
                "    }\n" +
                "}\n" +
                "p = Pos3D.new\n" +
                "p.move(3, 4, 5)\n" +
                "print(p.x + p.y + p.z)";
        StringReader reader = new StringReader(testCode);
        Lexer l = new Lexer(reader);
        BasicEnv e = new BasicEnv();
        new Natives().environment(e);
        ClassParser p = new ClassParser();

        while(l.peek(0) != Token.EOF) {
            ASTree t = p.parse(l);
            t.eval(e);
        }
    }
}
