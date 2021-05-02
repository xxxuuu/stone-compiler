package stone.ast;

import stone.Const;
import stone.TypeInfo;
import stone.env.Environment;
import stone.env.TypeEnv;
import stone.exception.TypeException;

import java.util.List;

/**
 * @author XUQING
 * @date 2021/4/14
 */
public class WhileStmnt extends ASTList {
    public WhileStmnt(List<ASTree> c) { super(c); }

    public ASTree condition() { return child(0); }

    public ASTree body() { return child(1); }

    @Override
    public String toString() {
        return "(while " + condition() + " " + body() + ")";
    }

    @Override
    public Object eval(Environment e) {
        Object result = 0;
        for (;;) {
            Object c = condition().eval(e);
            if (c instanceof Integer && ((Integer)c) == Const.FALSE) {
                return result;
            }
            result = body().eval(e);
        }
    }

    @Override
    public TypeInfo typeCheck(TypeEnv e) throws TypeException {
        TypeInfo condType = condition().typeCheck(e);
        condType.assertSubtypeOf(TypeInfo.INT, e, this);
        TypeInfo bodyType = body().typeCheck(e);
        return bodyType.union(condType, e);
    }
}
