package stone;

import org.junit.jupiter.api.Test;
import stone.ast.ASTree;
import stone.env.BasicEnv;
import stone.env.Natives;
import stone.env.TypeEnv;
import stone.exception.ParseException;
import stone.exception.TypeException;
import stone.parser.TypeParser;
import stone.vm.ToJvm;

import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * @author XUQING
 * @date 2021/5/8
 */
public class ToJvmTest {
    @Test
    public void compileTest() throws FileNotFoundException, ParseException, TypeException {
        FileReader fileReader = new FileReader(getClass().getResource("/Fib.stone").getFile());
        Lexer l = new Lexer(fileReader);
        TypeParser p = new TypeParser();

        BasicEnv e = new BasicEnv();
        TypeEnv typeEnv = new TypeEnv();

        Natives natives = new Natives();
        natives.environment(e);
        natives.typeEnvironment(typeEnv);

        ToJvm jvm = new ToJvm("Fib");

        while(l.peek(0) != Token.EOF) {
            ASTree t = p.parse(l);
            t.typeCheck(typeEnv);
            jvm.compile(t);
        }

        jvm.write(getClass().getResource("/").getPath());
    }
}
