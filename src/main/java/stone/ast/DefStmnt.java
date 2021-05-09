package stone.ast;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import stone.Function;
import stone.TypeInfo;
import stone.env.Environment;
import stone.env.TypeEnv;
import stone.env.VmEnv;
import stone.exception.TypeException;
import stone.vm.JvmFunction;
import stone.vm.ToJvm;

import java.util.Arrays;
import java.util.List;

import static org.objectweb.asm.Opcodes.*;

/**
 * def(函数定义)语句
 * @author XUQING
 * @date 2021/4/15
 */
public class DefStmnt extends ASTList {
    protected TypeInfo.FunctionType funcType;
    protected TypeEnv bodyEnv;

    public DefStmnt(List<ASTree> c) {
        super(c);
    }

    public String name() {
        return ((ASTLeaf)child(0)).token().getText();
    }

    public ParameterList parameters() {
        return (ParameterList)child(1);
    }

    public TypeTag type() {
        return (TypeTag) child(2);
    }

    public BlockStmnt body() {
        return (BlockStmnt)child(3);
    }

    @Override
    public String toString() {
        return "(def " + name() + " " + parameters() + " " + type() + " " + body() + ")";
    }

    @Override
    public void compileToJvm(ClassWriter cw, MethodVisitor mw, VmEnv e) {
        JvmFunction f = new JvmFunction(name(), funcType);
        e.addFun(name(), f);

        // 创建一个新方法
        MethodVisitor newMw = cw.visitMethod(
                ACC_PUBLIC + ACC_STATIC,
                name(),
                f.getDescriptor(),
                null,
                null);

        // 添加局部变量到环境
        VmEnv newEnv = new VmEnv(e);
        for(int i = 0; i < parameters().size(); i++) {
            newEnv.addVar(parameters().name(i));
        }

        body().compileToJvm(cw, newMw, newEnv);

        // 添加返回指令
        if(funcType.returnType.match(TypeInfo.INT)) {
            newMw.visitInsn(IRETURN);
        } else if(funcType.returnType.match(TypeInfo.STRING)) {
            newMw.visitInsn(ARETURN);
        } else {
            newMw.visitInsn(RETURN);
        }

        // TODO 栈帧计算好像有问题 (应该没问题了)
        newMw.visitMaxs(newEnv.stackLen(), newEnv.varNums());
        newMw.visitEnd();
    }

    @Override
    public Object eval(Environment e) {
        e.putNew(name(), new Function(parameters(), body(), e));
        return name();
    }

    protected void fixUnknown(TypeInfo t) {
        if(t.isUnknownType()) {
            TypeInfo.UnknownType ut = t.toUnknownType();
            if(!ut.resolved()) {
                ut.setType(TypeInfo.ANY);
            }
        }
    }

    @Override
    public TypeInfo typeCheck(TypeEnv e) throws TypeException {
        TypeInfo[] params = parameters().types();
        TypeInfo returnType = TypeInfo.get(type());
        this.funcType = TypeInfo.function(returnType, params);
        TypeInfo oldType = e.put(name(), funcType);
        if(oldType != null) {
            throw new TypeException("function redefinition: " + name(), this);
        }
        this.bodyEnv = new TypeEnv(e);
        for(int i = 0; i < params.length; i++) {
            bodyEnv.put(parameters().name(i), params[i]);
        }
        TypeInfo bodyType = body().typeCheck(bodyEnv);
        bodyType.assertSubtypeOf(returnType, e, this);

        // 为参数和返回值进行类型推论
        TypeInfo.FunctionType func = funcType.toFunctionType();
        for(TypeInfo t : func.parameterTypes) {
            fixUnknown(t);
        }
        fixUnknown(func.returnType);
        return func;
    }
}
