package stone.ast;

import stone.env.Environment;

import java.util.List;

/**
 * @author XUQING
 * @date 2021/5/1
 */
public class VarStmnt extends ASTList {
    public VarStmnt(List<ASTree> list) {
        super(list);
    }

    public String name() {
        return ((ASTLeaf)child(0)).token.getText();
    }

    public TypeTag type() {
        return (TypeTag) child(1);
    }

    public ASTree initializer() {
        return child(2);
    }

    @Override
    public Object eval(Environment e) {
        Object val = initializer().eval(e);
        e.putNew(name(), val);
        return val;
    }

    @Override
    public String toString() {
        return "(var " + name() + " " + type() + " " + initializer() + ")";
    }
}
