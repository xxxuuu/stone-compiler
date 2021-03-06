package stone.ast;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import stone.*;
import stone.env.Environment;
import stone.env.NestedEnv;
import stone.env.TypeEnv;
import stone.env.VmEnv;
import stone.exception.StoneException;
import stone.exception.TypeException;

import static stone.StoneObject.AccessException;

import java.util.List;

/**
 * @author XUQING
 * @date 2021/4/17
 */
public class Dot extends Postfix {
    public Dot(List<ASTree> c) {
        super(c);
    }

    public String name() {
        return ((ASTLeaf)child(0)).token().getText();
    }

    @Override
    public String toString() {
        return "." + name();
    }

    private void initObject(ClassInfo ci, Environment env) {
        if (ci.superClass() != null) {
            initObject(ci.superClass(), env);
        }
        ci.body().eval(env);
    }

    @Override
    public Object eval(Environment e, Object value) {
        String member = name();
        if (value instanceof ClassInfo) {
            if ("new".equals(member)) {
                ClassInfo ci = (ClassInfo)value;
                NestedEnv env = new NestedEnv(ci.environment());
                StoneObject so = new StoneObject(env);
                env.putNew("this", so);
                initObject(ci, env);
                return so;
            }
        } else if (value instanceof StoneObject) {
            try {
                return ((StoneObject)value).read(member);
            } catch (AccessException ex) {}
        }
        throw new StoneException("bad member access: " + member, this);
    }

    @Override
    public TypeInfo typeCheck(TypeEnv e, TypeInfo target) throws TypeException {
        return null;
    }

    @Override
    public Object compileToJvm(ClassWriter cw, MethodVisitor mw, VmEnv e, Object target) {
        return null;
    }
}
