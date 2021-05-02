package stone.ast;

import stone.TypeInfo;
import stone.env.Environment;
import stone.Token;
import stone.env.TypeEnv;
import stone.exception.TypeException;

/**
 * @author XUQING
 * @date 2021/4/14
 */
public class StringLiteral extends ASTLeaf {
    public StringLiteral(Token t) { super(t); }

    public String value() { return token().getText(); }

    @Override
    public TypeInfo typeCheck(TypeEnv e) throws TypeException {
        return TypeInfo.STRING;
    }

    @Override
    public Object eval(Environment e) {
        return value();
    }
}
