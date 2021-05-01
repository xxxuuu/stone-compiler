package stone.ast;

import java.util.List;

/**
 * @author XUQING
 * @date 2021/5/1
 */
public class TypeTag extends ASTList {
    public static final String UNDEF = "<Undef>";

    public TypeTag(List<ASTree> list) {
        super(list);
    }

    public String type() {
        if(numChildren() > 0) {
            return ((ASTLeaf)child(0)).token.getText();
        }
        return UNDEF;
    }

    @Override
    public String toString() {
        return ":" + type();
    }
}
