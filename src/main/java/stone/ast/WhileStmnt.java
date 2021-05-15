package stone.ast;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import stone.Const;
import stone.TypeInfo;
import stone.env.Environment;
import stone.env.TypeEnv;
import stone.env.VmEnv;
import stone.exception.TypeException;

import java.util.List;

import static org.objectweb.asm.Opcodes.*;

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
    public void compileToJvm(ClassWriter cw, MethodVisitor mw, VmEnv e) {
        Label l1 = new Label();
        Label l2 = new Label();

        mw.visitLabel(l1);
        condition().compileToJvm(cw, mw, e);
        mw.visitJumpInsn(IFEQ, l2);
        body().compileToJvm(cw, mw, e);
        mw.visitJumpInsn(GOTO, l1);
        mw.visitLabel(l2);
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
