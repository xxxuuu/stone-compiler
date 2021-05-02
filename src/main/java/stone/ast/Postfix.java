package stone.ast;

import stone.env.Environment;

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
}
