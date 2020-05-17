package stone.ast;

import stone.Token;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * AST叶子节点
 * @author XUQING
 * @date 2020/5/16
 */
public class ASTLeaf extends ASTree {
    private static List<ASTree> empty = new ArrayList<>();
    protected Token token;

    public ASTLeaf(Token t) {
        this.token = t;
    }

    @Override
    public ASTree child(int i) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public int numChildren() {
        return 0;
    }

    @Override
    public Iterator<ASTree> children() {
        return empty.iterator();
    }

    @Override
    public String location() {
        return "at line " + this.token.getLineNumber();
    }

    @Override
    public String toString() {
        return this.token.getText();
    }

    public Token token() {
        return this.token;
    }
}
