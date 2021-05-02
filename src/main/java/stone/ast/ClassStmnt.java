package stone.ast;

import stone.ClassInfo;
import stone.env.Environment;

import java.util.List;

/**
 * @author XUQING
 * @date 2021/4/17
 */
public class ClassStmnt extends ASTList {
    public ClassStmnt(List<ASTree> list) {
        super(list);
    }

    @Override
    public Object eval(Environment e) {
        ClassInfo info = new ClassInfo(this, e);
        e.put(name(), info);
        return name();
    }

    public String name() {
        return ((ASTLeaf)child(0)).token().getText();
    }

    public String superClass() {
        if(numChildren() < 3) {
            return null;
        }
        return ((ASTLeaf)child(1)).token.getText();
    }

    public ClassBody body() {
        return (ClassBody) child(numChildren()-1);
    }

    @Override
    public String toString() {
        String parent = superClass();
        if (parent == null) {
            parent = "*";
        }
        return "(class " + name() + " " + parent + " " + body() + ")";
    }
}
