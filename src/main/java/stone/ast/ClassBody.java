package stone.ast;

import stone.Environment;

import java.util.List;

/**
 * @author XUQING
 * @date 2021/4/17
 */
public class ClassBody extends ASTList {
    public ClassBody(List<ASTree> list) {
        super(list);
    }

    @Override
    public Object eval(Environment e) {
        for (ASTree t: this) {
            t.eval(e);
        }
        return null;
    }
}
