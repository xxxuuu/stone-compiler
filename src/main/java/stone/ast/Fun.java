package stone.ast;

import stone.Environment;

import java.util.List;

/**
 * AST上的闭包节点
 * @author XUQING
 * @date 2021/4/16
 */
public class Fun extends ASTList {
    public Fun(List<ASTree> list) {
        super(list);
    }

    public ParameterList parameters() {
        return (ParameterList) child(0);
    }

    public BlockStmnt body() {
        return (BlockStmnt) child(1);
    }

    @Override
    public Object eval(Environment e) {
        return new Function(parameters(), body(), e);
    }

    @Override
    public String toString() {
        return "(fun " + parameters() + " " + body() + ")";
    }
}
