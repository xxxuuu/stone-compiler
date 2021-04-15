package stone.ast;

import stone.Environment;
import stone.Token;

/**
 * @author XUQING
 * @date 2021/4/14
 */
public class StringLiteral extends ASTLeaf {
    public StringLiteral(Token t) { super(t); }

    public String value() { return token().getText(); }

    @Override
    public Object eval(Environment e) {
        return value();
    }
}
