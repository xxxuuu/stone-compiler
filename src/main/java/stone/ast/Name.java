package stone.ast;

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
}
