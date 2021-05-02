package stone.ast;

import stone.TypeInfo;
import stone.env.Environment;
import stone.Token;
import stone.env.TypeEnv;
import stone.exception.TypeException;

/**
 * AST上的整型字面量
 * @author XUQING
 * @date 2020/5/17
 */
public class NumberLiteral extends ASTLeaf {
    public NumberLiteral(Token t) {
        super(t);
    }

    public int value() {
        return this.token().getNumber();
    }

    @Override
    public TypeInfo typeCheck(TypeEnv e) throws TypeException {
        return TypeInfo.INT;
    }

    @Override
    public Object eval(Environment e) {
        return value();
    }
}
