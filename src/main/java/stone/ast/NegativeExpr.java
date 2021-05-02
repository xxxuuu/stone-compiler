package stone.ast;

import stone.env.Environment;
import stone.exception.StoneException;

import java.util.List;

/**
 * @author XUQING
 * @date 2021/4/14
 */
public class NegativeExpr extends ASTList {
    public NegativeExpr(List<ASTree> c) { super(c); }

    public ASTree operand() { return child(0); }

    @Override
    public String toString() {
        return "-" + operand();
    }

    @Override
    public Object eval(Environment e) {
        Object v = operand().eval(e);
        if(v instanceof Integer) {
            return -(Integer) v;
        }
        throw new StoneException("bad type for -", this);
    }
}
