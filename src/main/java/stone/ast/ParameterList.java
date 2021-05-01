package stone.ast;

import stone.Environment;

import java.util.List;

/**
 * 函数形参
 * @author XUQING
 * @date 2021/4/15
 */
public class ParameterList extends ASTList {
    public ParameterList(List<ASTree> c) {
        super(c);
    }

    public String name(int i) {
        return ((ASTLeaf)child(i).child(0)).token().getText();
    }

    public TypeTag typeTag(int i) {
        return (TypeTag) child(i).child(1);
    }

    public int size() {
        return numChildren();
    }

    /** 将实参赋值给形参 */
    public void eval(Environment e, int index, Object value) {
        e.putNew(name(index), value);
    }
}
