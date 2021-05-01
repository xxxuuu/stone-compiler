package stone.ast;

import stone.Environment;
import stone.Function;

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
    public Object eval(Environment e) {
        e.putNew(name(), new Function(parameters(), body(), e));
        return name();
    }
}
