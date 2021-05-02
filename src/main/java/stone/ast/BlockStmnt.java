package stone.ast;

import stone.TypeInfo;
import stone.env.Environment;
import stone.env.TypeEnv;
import stone.exception.TypeException;

import java.util.List;

/**
 * @author XUQING
 * @date 2021/4/14
 */
public class BlockStmnt extends ASTList {
    TypeInfo type;

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

    @Override
    public TypeInfo typeCheck(TypeEnv e) throws TypeException {
        type = TypeInfo.INT;
        for(ASTree t : this) {
            if(!(t instanceof NullStmnt)) {
                type = t.typeCheck(e);
            }
        }
        return type;
    }
}
