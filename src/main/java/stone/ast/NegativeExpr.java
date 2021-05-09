package stone.ast;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import stone.TypeInfo;
import stone.env.Environment;
import stone.env.TypeEnv;
import stone.env.VmEnv;
import stone.exception.StoneException;
import stone.exception.TypeException;
import static org.objectweb.asm.Opcodes.*;

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
    public TypeInfo typeCheck(TypeEnv e) throws TypeException {
        TypeInfo t = operand().typeCheck(e);
        t.assertSubtypeOf(TypeInfo.INT, e, this);
        return TypeInfo.INT;
    }

    @Override
    public void compileToJvm(ClassWriter cw, MethodVisitor mw, VmEnv e) {
        operand().compileToJvm(cw, mw, e);
        mw.visitInsn(INEG);
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
