package stone.ast;

import stone.Environment;

import java.util.List;

/**
 * @author XUQING
 * @date 2021/4/14
 */
public class BlockStmnt extends ASTList {
    public BlockStmnt(List<ASTree> c) { super(c); }

    @Override
    public Object eval(Environment e) {
        Object result = 0;
        for (ASTree t: this) {
            if (!(t instanceof NullStmnt)) {
                result = t.eval(e);
            }
        }
        return result;
    }
}
