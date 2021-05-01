package stone.parser;

import stone.ast.*;

import static stone.parser.Parser.rule;

/**
 * @author XUQING
 * @date 2021/4/18
 */
public class ArrayParser extends ClassParser {
    Parser elements = rule(ArrayLiteral.class)
            .ast(expr).repeat(rule().sep(",").ast(expr));

    public ArrayParser() {
        reserved.add("]");
        primary.insertChoice(rule().sep("[").maybe(elements).sep("]"));
        postfix.insertChoice(rule(ArrayRef.class).sep("[").ast(expr).sep("]"));
    }
}
