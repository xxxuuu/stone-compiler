package stone.ast;

import stone.TypeInfo;
import stone.env.Environment;
import stone.env.TypeEnv;
import stone.exception.StoneException;
import stone.Token;
import stone.exception.TypeException;

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
    public Object eval(Environment e) {
        throw new StoneException("cannot eval: " + toString(), this);
    }

    @Override
    public TypeInfo typeCheck(TypeEnv e) throws TypeException {
        return null;
    }

    @Override
    public String toString() {
        return this.token.getText();
    }

    public Token token() {
        return this.token;
    }
}
