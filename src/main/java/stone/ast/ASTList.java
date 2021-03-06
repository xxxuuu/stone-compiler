package stone.ast;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import stone.TypeInfo;
import stone.env.Environment;
import stone.env.TypeEnv;
import stone.env.VmEnv;
import stone.exception.StoneException;
import stone.exception.TypeException;

import java.util.Iterator;
import java.util.List;

/**
 * AST上的非叶节点
 * @author XUQING
 * @date 2020/5/17
 */
public class ASTList extends ASTree {
    protected List<ASTree> chilren;

    public ASTList(List<ASTree> list) {
        this.chilren = list;
    }

    @Override
    public ASTree child(int i) {
        return this.chilren.get(i);
    }

    @Override
    public int numChildren() {
        return this.chilren.size();
    }

    @Override
    public Iterator<ASTree> children() {
        return this.chilren.iterator();
    }

    @Override
    public String location() {
        for (ASTree t : this.chilren) {
            String s = t.location();
            if(s != null) {
                return s;
            }
        }
        return null;
    }

    @Override
    public Object eval(Environment e) {
        // throw new StoneException("cannot eval: " + toString(), this);
        return null;
    }

    @Override
    public TypeInfo typeCheck(TypeEnv e) throws TypeException {
        return null;
    }

    @Override
    public void compileToJvm(ClassWriter cw, MethodVisitor mw, VmEnv e) {

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        String sep = "";
        for(ASTree t : this.chilren) {
            sb.append(sep);
            sep = " ";
            sb.append(t.toString());
        }
        return sb.append(')').toString();
    }
}
