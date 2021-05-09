package stone.ast;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import stone.TypeInfo;
import stone.env.Environment;
import stone.env.TypeEnv;
import stone.env.VmEnv;
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
    public void compileToJvm(ClassWriter cw, MethodVisitor mw, VmEnv e) {
        for(ASTree t: this) {
            if (!(t instanceof NullStmnt)) {
                t.compileToJvm(cw, mw, e);
            }
        }
    }

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
