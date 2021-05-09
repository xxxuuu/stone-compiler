package stone.ast;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import stone.TypeInfo;
import stone.env.Environment;
import stone.env.TypeEnv;
import stone.env.VmEnv;
import stone.exception.StoneException;
import stone.Token;
import stone.exception.TypeException;
import static org.objectweb.asm.Opcodes.*;

/**
 * @author XUQING
 * @date 2020/5/17
 */
public class Name extends ASTLeaf {
    TypeInfo type;

    public Name(Token t) {
        super(t);
    }

    public String name() {
        return this.token().getText();
    }

    @Override
    public void compileToJvm(ClassWriter cw, MethodVisitor mw, VmEnv e) {
        // TODO fun & class & global
        Integer index = e.getIndex(name());
        if(index == null) {
            throw new StoneException("undefined name: " + name(), this);
        }
        if(type.match(TypeInfo.INT)) {
            mw.visitVarInsn(ILOAD, e.getIndex(name()));
        } else if(type.match(TypeInfo.STRING)) {
            mw.visitVarInsn(ALOAD, e.getIndex(name()));
        }
    }

    @Override
    public TypeInfo typeCheck(TypeEnv e) throws TypeException {
        type = e.get(name());
        if(type == null) {
            throw new TypeException("undefined name: " + name(), this);
        }
        return type;
    }

    public TypeInfo typeCheckForAssign(TypeEnv e, TypeInfo typeVal) throws TypeException {
        type = e.get(name());
        if(type == null) {
            this.type = typeVal;
            e.put(name(), typeVal);
            return typeVal;
        }
        // 断言typeVal是type的子类
        typeVal.assertSubtypeOf(type, e, this);
        return type;
    }

    @Override
    public Object eval(Environment e) {
        Object val = e.get(name());
        if(val == null) {
            throw new StoneException("undefined name: " + name(), this);
        }
        return val;
    }
}
