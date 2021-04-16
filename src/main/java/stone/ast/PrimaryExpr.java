package stone.ast;

import stone.Environment;

import java.util.List;

/**
 * 字面量、变量名、函数调用等基本表达式
 * @author XUQING
 * @date 2021/4/14
 */
public class PrimaryExpr extends ASTList {
    public PrimaryExpr(List<ASTree> c) { super(c); }

    public static ASTree create(List<ASTree> c) {
        return c.size() == 1 ? c.get(0) : new PrimaryExpr(c);
    }

    /** 返回字面量、函数名 */
    public ASTree operand() {
        return child(0);
    }

    /** 对于函数调用，返回实参序列 */
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
}
