package stone.parser;

import stone.ast.TypeTag;
import stone.ast.VarStmnt;

import static stone.parser.Parser.rule;

/**
 * @author XUQING
 * @date 2021/5/1
 */
public class TypeParser extends ArrayParser {
    Parser typeTag = rule(TypeTag.class).sep(":").identifier(reserved);
    Parser variable = rule(VarStmnt.class)
            .sep("var").identifier(reserved).maybe(typeTag)
            .sep("=").ast(expr);

    public TypeParser() {
        reserved.add(":");
        param.maybe(typeTag);
        def.reset().sep("def").identifier(reserved).ast(paramList)
                .maybe(typeTag).ast(block);
        statement.insertChoice(variable);
    }
}
