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
 * @author XUQING
 * @date 2021/4/14
 */
public class StringLiteral extends ASTLeaf {
    public StringLiteral(Token t) { super(t); }

    public String value() { return token().getText(); }

    @Override
    public void compileToJvm(ClassWriter cw, MethodVisitor mw, VmEnv e) {
        String val = value();
        mw.visitLdcInsn(val);
    }

    @Override
    public TypeInfo typeCheck(TypeEnv e) throws TypeException {
        return TypeInfo.STRING;
    }

    @Override
    public Object eval(Environment e) {
        return value();
    }
}
