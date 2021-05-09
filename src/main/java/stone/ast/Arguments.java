package stone.ast;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import stone.Function;
import stone.NativeFunction;
import stone.TypeInfo;
import stone.env.Environment;
import stone.env.TypeEnv;
import stone.env.VmEnv;
import stone.exception.StoneException;
import stone.exception.TypeException;
import stone.vm.JvmFunction;
import stone.vm.JvmNativeFunction;
import stone.vm.ToJvm;

import java.util.List;

import static org.objectweb.asm.Opcodes.*;

/**
 * 函数实参
 * @author XUQING
 * @date 2021/4/15
 */
public class Arguments extends Postfix {
    protected TypeInfo[] argTypes;
    protected TypeInfo.FunctionType funcType;

    public Arguments(List<ASTree> c) {
        super(c);
    }

    public int size() {
        return numChildren();
    }

    @Override
    public Object compileToJvm(ClassWriter cw, MethodVisitor mw, VmEnv e, Object target) {
        // 调用函数
        JvmFunction fun = (JvmFunction) target;
        if(e.hasNativeFun(fun.getName())) {
            JvmNativeFunction nf = (JvmNativeFunction) target;
            // 一些像System.out.println的方法 参数入栈要在 GETSTATIC 后
            nf.invoke(mw, e, (methodVisitor) -> {
                for(ASTree t : this) {
                    t.compileToJvm(cw, methodVisitor, e);
                }
            });
        } else {
            // 计算实参并入栈
            for(ASTree t : this) {
                t.compileToJvm(cw, mw, e);
            }
            mw.visitMethodInsn(INVOKESTATIC, ToJvm.mainClsName, fun.getName(), fun.getDescriptor(), false);
        }

        // FRAME的问题暂时通过降低Class文件版本来解决了
        // TODO: 如果函数有返回值并且没有赋值 要POP
        // TODO: 闭包等处理
        return null;
    }

    /** 原生函数执行 */
    private Object nativeEval(Environment callerEnv, Object value) {
        NativeFunction func = (NativeFunction) value;
        int nparams = func.numOfParameters();
        if(size() != nparams) {
            throw new StoneException("bad number of arguments", this);
        }

        Object[] args = new Object[nparams];
        int num = 0;
        // 计算实参表达式
        for(ASTree t : this) {
            args[num++] = t.eval(callerEnv);
        }

        return func.invoke(this, args);
    }

    /** 普通函数执行 */
    private Object normalEval(Environment callerEnv, Object value) {
        Function func = (Function) value;
        ParameterList params = func.parameters();
        if(size() != params.size()) {
            throw new StoneException("bad number of arguments", this);
        }
        // 函数局部作用域环境
        Environment newEnv = func.makeEnv();
        int num = 0;
        // 计算实参 赋值给形参
        for(ASTree t : this) {
            params.eval(newEnv, num++, t.eval(callerEnv));
        }
        // 执行函数
        return func.body().eval(newEnv);
    }

    /**
     * 执行函数
     * @param callerEnv 函数调用语句所处环境，用于计算实参
     * @param value 函数名对应的抽象语法树的eval结果（Function对象）
     * @return
     */
    @Override
    public Object eval(Environment callerEnv, Object value) {
        if(value instanceof Function) {
            return normalEval(callerEnv, value);
        } else if(value instanceof NativeFunction) {
            return nativeEval(callerEnv, value);
        }
        throw new StoneException("bad function", this);
    }

    @Override
    public TypeInfo typeCheck(TypeEnv e, TypeInfo target) throws TypeException {
        if(!(target instanceof TypeInfo.FunctionType)) {
            throw new TypeException("bad function", this);
        }
        this.funcType = (TypeInfo.FunctionType) target;
        TypeInfo[] params = funcType.parameterTypes;
        if(size() != params.length) {
            throw new TypeException("bad number of arguments", this);
        }
        this.argTypes = new TypeInfo[params.length];
        int num = 0;
        for(ASTree t : this) {
            TypeInfo info = argTypes[num] = t.typeCheck(e);
            info.assertSubtypeOf(params[num++], e, this);
        }
        return funcType.returnType;
    }

    @Override
    public TypeInfo typeCheck(TypeEnv e) throws TypeException {
        return null;
    }
}
