package stone.ast;

import stone.TypeInfo;
import stone.env.Environment;
import stone.env.TypeEnv;
import stone.exception.StoneException;
import stone.exception.TypeException;

import java.util.List;

/**
 * 数组引用
 * @author XUQING
 * @date 2021/4/18
 */
public class ArrayRef extends Postfix {
    public ArrayRef(List<ASTree> c) {
        super(c);
    }

    public ASTree index() {
        return child(0);
    }

    @Override
    public String toString() {
        return "[" + index() + "]";
    }

    @Override
    public Object eval(Environment e, Object value) {
        if (value instanceof Object[]) {
            Object index = index().eval(e);
            if (index instanceof Integer) {
                return ((Object[])value)[(Integer)index];
            }
        }

        throw new StoneException("bad array access", this);
    }

    @Override
    public TypeInfo typeCheck(TypeEnv e, TypeInfo target) throws TypeException {
        return null;
    }
}
