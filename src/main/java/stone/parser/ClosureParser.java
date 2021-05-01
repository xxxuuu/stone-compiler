package stone.parser;

import stone.ast.Fun;
import static stone.parser.Parser.rule;

/**
 * @author XUQING
 * @date 2021/4/16
 */
public class ClosureParser extends FuncParser {
    public ClosureParser() {
        primary.insertChoice(rule(Fun.class).sep("fun").ast(paramList).ast(block));
    }
}
