package stone.ast;

import stone.env.Environment;

import java.util.List;

/**
 * 数组字面量
 * @author XUQING
 * @date 2021/4/18
 */
public class ArrayLiteral extends ASTList {
    public ArrayLiteral(List<ASTree> list) {
        super(list);
    }

    public int size() {
        return numChildren();
    }

    @Override
    public Object eval(Environment e) {
        int s = numChildren();
        Object[] res = new Object[s];
        int i = 0;
        for (ASTree t: this) {
            res[i++] = t.eval(e);
        }
        return res;
    }
}
