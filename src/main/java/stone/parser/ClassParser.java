package stone.parser;

import stone.Token;
import stone.ast.ClassBody;
import stone.ast.ClassStmnt;
import stone.ast.Dot;

import static stone.parser.Parser.rule;

/**
 * @author XUQING
 * @date 2021/4/17
 */
public class ClassParser extends ClosureParser {
    Parser member = rule().or(def, simple);
    Parser classBody = rule(ClassBody.class).sep("{").option(member)
            .repeat(rule().sep(";", Token.EOL).option(member))
            .sep("}");
    Parser defclass = rule(ClassStmnt.class).sep("class").identifier(reserved)
            .option(rule().sep("extends").identifier(reserved))
            .ast(classBody);

    public ClassParser() {
        postfix.insertChoice(rule(Dot.class).sep(".").identifier(reserved));
        program.insertChoice(defclass);
    }
}
