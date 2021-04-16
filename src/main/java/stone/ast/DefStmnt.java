package stone.ast;

import stone.Environment;

import java.util.List;

/**
 * def(函数定义)语句
 * @author XUQING
 * @date 2021/4/15
 */
public class DefStmnt extends ASTList {
    public DefStmnt(List<ASTree> c) {
        super(c);
    }

    public String name() {
        return ((ASTLeaf)child(0)).token().getText();
    }

    public ParameterList parameters() {
        return (ParameterList)child(1);
    }

    public BlockStmnt body() {
        return (BlockStmnt)child(2);
    }

    @Override
    public String toString() {
        return "(def " + name() + " " + parameters() + " " + body() + ")";
    }

    @Override
    public Object eval(Environment e) {
        e.putNew(name(), new Function(parameters(), body(), e));
        return name();
    }
}
