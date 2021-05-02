package stone.ast;

import stone.TypeInfo;
import stone.env.Environment;
import stone.exception.TypeException;

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

    public TypeInfo[] types() throws TypeException {
        int s = size();
        TypeInfo[] result = new TypeInfo[s];
        for(int i = 0; i < s; i++) {
            result[i] = TypeInfo.get(typeTag(i));
        }
        return result;
    }

    /** 将实参赋值给形参 */
    public void eval(Environment e, int index, Object value) {
        e.putNew(name(index), value);
    }
}
