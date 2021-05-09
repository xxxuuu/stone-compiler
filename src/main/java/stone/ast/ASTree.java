package stone.ast;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import stone.TypeInfo;
import stone.env.Environment;
import stone.env.TypeEnv;
import stone.env.VmEnv;
import stone.exception.TypeException;

import java.util.Iterator;

/**
 * AST
 * @author XUQING
 * @date 2020/5/16
 */
public abstract class ASTree implements Iterable<ASTree> {
    /** 查询第 i 个子节点 */
    public abstract ASTree child(int i);
    /** 返回子节点个数 */
    public abstract int numChildren();
    /** 返回一个遍历节点的 iterator */
    public abstract Iterator<ASTree> children();
    /** 查询节点在程序内所处位置 */
    public abstract String location();
    /** 执行并计算 */
    public abstract Object eval(Environment e);
    /** 类型检查 */
    public abstract TypeInfo typeCheck(TypeEnv e) throws TypeException;
    /** 编译到JVM */
    public abstract void compileToJvm(ClassWriter cw, MethodVisitor mw, VmEnv e);
    @Override
    public Iterator<ASTree> iterator() {
        return children();
    }

}
