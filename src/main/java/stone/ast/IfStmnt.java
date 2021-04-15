package stone.ast;

import stone.Const;
import stone.Environment;

import java.util.List;

/**
 * @author XUQING
 * @date 2021/4/14
 */
public class IfStmnt extends ASTList {
    public IfStmnt(List<ASTree> c) { super(c); }

    public ASTree condition() { return child(0); }

    public ASTree thenBlock() { return child(1); }

    public ASTree elseBlock() {
        return numChildren() > 2 ? child(2) : null;
    }

    @Override
    public String toString() {
        return "(if " + condition() + " " + thenBlock()
                + " else " + elseBlock() + ")";
    }

    @Override
    public Object eval(Environment e) {
        Object c = condition().eval(e);
        if (c instanceof Integer && ((Integer)c) != Const.FALSE) {
            return thenBlock().eval(e);
        }
        ASTree b = elseBlock();
        return (b == null) ? 0 : b.eval(e);
    }
}