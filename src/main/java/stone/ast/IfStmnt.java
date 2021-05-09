package stone.ast;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import stone.Const;
import stone.TypeInfo;
import stone.env.Environment;
import stone.env.TypeEnv;
import stone.env.VmEnv;
import stone.exception.TypeException;
import org.objectweb.asm.Label;

import java.util.List;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Opcodes.ICONST_1;

/**
 * @author XUQING
 * @date 2021/4/14
 */
public class IfStmnt extends ASTList {
    public IfStmnt(List<ASTree> c) { super(c); }

    public ASTree condition() { return child(0); }

    public ASTree thenBlock() { return child(1); }

    public ASTree elseBlock() {
        return numChildren() > 2 ? child(2) : null;
    }

    @Override
    public String toString() {
        return "(if " + condition() + " " + thenBlock()
                + " else " + elseBlock() + ")";
    }

    @Override
    public void compileToJvm(ClassWriter cw, MethodVisitor mw, VmEnv e) {
        Label label1 = new Label();
        Label label2 = new Label();
        ASTree elseAst = elseBlock();

        condition().compileToJvm(cw, mw, e);
        // 条件为0(false)跳转到else块
        mw.visitJumpInsn(IFEQ, label1);
        thenBlock().compileToJvm(cw, mw, e);
        mw.visitJumpInsn(GOTO, label2);
        mw.visitLabel(label1);
        if(elseAst != null) {
            elseAst.compileToJvm(cw, mw, e);
        }
        mw.visitLabel(label2);
    }

    @Override
    public Object eval(Environment e) {
        Object c = condition().eval(e);
        if (c instanceof Integer && ((Integer)c) != Const.FALSE) {
            return thenBlock().eval(e);
        }
        ASTree b = elseBlock();
        return (b == null) ? 0 : b.eval(e);
    }

    @Override
    public TypeInfo typeCheck(TypeEnv e) throws TypeException {
        TypeInfo condType = condition().typeCheck(e);
        condType.assertSubtypeOf(TypeInfo.INT, e, this);
        TypeInfo thenType = thenBlock().typeCheck(e);
        TypeInfo elseType;
        ASTree elseBlock = elseBlock();
        if(elseBlock == null) {
            elseType = TypeInfo.INT;
        } else {
            elseType = elseBlock.typeCheck(e);
        }
        return thenType.union(elseType, e);
    }
}