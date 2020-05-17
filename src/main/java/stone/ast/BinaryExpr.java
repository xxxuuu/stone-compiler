package stone.ast;

import java.util.List;

/**
 * 二元表达式
 * @author XUQING
 * @date 2020/5/17
 */
public class BinaryExpr extends ASTList {
    public BinaryExpr(List<ASTree> list) {
        super(list);
    }

    public ASTree left() {
        return this.child(0);
    }

    public String operator() {
        return ((ASTLeaf) (this.child(1))).token.getText();
    }

    public ASTree right() {
        return this.child(2);
    }

}
