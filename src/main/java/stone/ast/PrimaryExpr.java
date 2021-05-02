package stone.ast;

import stone.TypeInfo;
import stone.env.Environment;
import stone.env.TypeEnv;
import stone.exception.TypeException;

import java.util.List;

/**
 * 字面量、变量名、函数调用、数组等基本元素和表达式
 * @author XUQING
 * @date 2021/4/14
 */
public class PrimaryExpr extends ASTList {
    public PrimaryExpr(List<ASTree> c) { super(c); }

    public static ASTree create(List<ASTree> c) {
        return c.size() == 1 ? c.get(0) : new PrimaryExpr(c);
    }

    /** 返回字面量、函数、对象名 */
    public ASTree operand() {
        return child(0);
    }

    /** 对于函数调用，返回实参序列；对于对象调用，返回属性、函数名 */
    public Postfix postfix(int nest) {
        return (Postfix)child(numChildren() - nest - 1);
    }

    public boolean hasPostfix(int nest) {
        return numChildren() - nest > 1;
    }

    @Override
    public Object eval(Environment env) {
        return evalSubExpr(env, 0);
    }

    /**
     * 计算子表达式
     * @param env 换
     * @param nest 函数连续调用次数 如 fib(1)(2)(3) 是3次
     * @return
     */
    public Object evalSubExpr(Environment env, int nest) {
        if (hasPostfix(nest)) {
            Object target = evalSubExpr(env, nest + 1);
            return postfix(nest).eval(env, target);
        }
        return operand().eval(env);
    }

    @Override
    public TypeInfo typeCheck(TypeEnv e) throws TypeException {
        return typeCheck(e, 0);
    }

    public TypeInfo typeCheck(TypeEnv e, int nest) throws TypeException {
        if (hasPostfix(nest)) {
            TypeInfo target = typeCheck(e, nest + 1);
            return postfix(nest).typeCheck(e, target);
        }
        return operand().typeCheck(e);
    }
}
