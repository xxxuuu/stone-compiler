package stone.ast;

import stone.env.Environment;
import stone.Function;
import stone.NativeFunction;
import stone.exception.StoneException;

import java.util.List;

/**
 * 函数实参
 * @author XUQING
 * @date 2021/4/15
 */
public class Arguments extends Postfix {
    public Arguments(List<ASTree> c) {
        super(c);
    }

    public int size() {
        return numChildren();
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
        if(value instanceof  Function) {
            return normalEval(callerEnv, value);
        } else if(value instanceof NativeFunction) {
            return nativeEval(callerEnv, value);
        }
        throw new StoneException("bad function", this);
    }
}
