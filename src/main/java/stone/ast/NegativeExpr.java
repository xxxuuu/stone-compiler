package stone.ast;

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
}
