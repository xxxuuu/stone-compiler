package stone;

import stone.ast.ASTree;
import stone.env.BasicEnv;
import stone.env.Natives;
import stone.env.TypeEnv;
import stone.exception.ParseException;
import stone.exception.TypeException;
import stone.parser.TypeParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.util.Arrays;

/**
 * @author XUQING
 * @date 2021/5/6
 */
public class FileRunner {

    public static void runForFile(String filePath) throws FileNotFoundException, ParseException, TypeException {
        FileReader fileReader = new FileReader(filePath);
        Lexer l = new Lexer(fileReader);
        TypeParser p = new TypeParser();

        BasicEnv e = new BasicEnv();
        TypeEnv typeEnv = new TypeEnv();

        Natives natives = new Natives();
        natives.environment(e);
        natives.typeEnvironment(typeEnv);

        while(l.peek(0) != Token.EOF) {
            ASTree t = p.parse(l);
            t.typeCheck(typeEnv);
            t.eval(e);
        }
    }


    public static void main(String[] args) throws FileNotFoundException, ParseException, TypeException {
        if(args.length != 1) {
            System.out.println("arguments wrong, argument only one and it is source-code file path");
            return;
        }
        String filePath = args[0];
        runForFile(filePath);
    }
}
