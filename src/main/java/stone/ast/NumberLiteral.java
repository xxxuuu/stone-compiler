package stone.ast;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import stone.TypeInfo;
import stone.env.Environment;
import stone.Token;
import stone.env.TypeEnv;
import stone.env.VmEnv;
import stone.exception.TypeException;
import static org.objectweb.asm.Opcodes.*;

/**
 * AST上的整型字面量
 * @author XUQING
 * @date 2020/5/17
 */
public class NumberLiteral extends ASTLeaf {
    public NumberLiteral(Token t) {
        super(t);
    }

    public int value() {
        return this.token().getNumber();
    }

    @Override
    public void compileToJvm(ClassWriter cw, MethodVisitor mw, VmEnv e) {
        int val = value();

        e.addTempVal();
        if(val >= -1 && val <= 5) {
            // ICONST_ML -> ICONST_5 是 -2 到 8
            mw.visitInsn(val+3);
        } else if(val >= -128 && val <= 127) {
            mw.visitIntInsn(BIPUSH, val);
        } else if(val >= -32768 && val <= 32767) {
            mw.visitIntInsn(SIPUSH, val);
        } else {
            mw.visitLdcInsn(val);
        }
    }

    @Override
    public TypeInfo typeCheck(TypeEnv e) throws TypeException {
        return TypeInfo.INT;
    }

    @Override
    public Object eval(Environment e) {
        return value();
    }
}
