package stone.ast;

import stone.Environment;
import stone.StoneException;
import stone.Token;

/**
 * @author XUQING
 * @date 2020/5/17
 */
public class Name extends ASTLeaf {
    public Name(Token t) {
        super(t);
    }

    public String name() {
        return this.token().getText();
    }

    @Override
    public Object eval(Environment e) {
        Object val = e.get(name());
        if(val == null) {
            throw new StoneException("undefined name: " + name(), this);
        }
        return val;
    }
}
