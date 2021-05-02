package stone.ast;

import stone.env.Environment;
import stone.Token;

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
    public Object eval(Environment e) {
        return value();
    }
}
