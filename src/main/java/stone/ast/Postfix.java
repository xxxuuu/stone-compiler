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
 * EBNF: postfix :=  "[" expr "]" | "." IDENTIFIER | "(" [args] ")"
 * @author XUQING
 * @date 2021/4/15
 */
public abstract class Postfix extends ASTList {
    public Postfix(List<ASTree> c) {
        super(c);
    }

    /**
     * 执行（函数）
     * @param e 环境
     * @param value 函数名对应的抽象语法树的eval结果（Function对象）
     * @return
     */
    public abstract Object eval(Environment e, Object value);

    /**
     * 类型检查
     * @param e 类型环境信息
     * @param target 前置类型 如函数调用时为函数类型
     * @return
     * @throws  TypeException 类型检查错误
     */
    public abstract TypeInfo typeCheck(TypeEnv e, TypeInfo target) throws TypeException;
    /** 编译到JVM */
    public abstract Object compileToJvm(ClassWriter cw, MethodVisitor mw, VmEnv e, Object target);
}
