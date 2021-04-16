package stone.ast;

import stone.Environment;
import stone.StoneException;

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

    /**
     * 执行函数
     * @param callerEnv 函数调用语句所处环境，用于计算实参
     * @param value 函数名对应的抽象语法树的eval结果（Function对象）
     * @return
     */
    @Override
    public Object eval(Environment callerEnv, Object value) {
        if(!(value instanceof  Function)) {
            throw new StoneException("bad function", this);
        }
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
}
