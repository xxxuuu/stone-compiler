package stone.ast;

import stone.env.Environment;

import java.util.Iterator;

/**
 * AST
 * @author XUQING
 * @date 2020/5/16
 */
public abstract class ASTree implements Iterable<ASTree> {
    /** 查询第 i 个子节点 */
    public abstract ASTree child(int i);
    /** 返回子节点个数 */
    public abstract int numChildren();
    /** 返回一个遍历节点的 iterator */
    public abstract Iterator<ASTree> children();
    /** 查询节点在程序内所处位置 */
    public abstract String location();
    /** 执行并计算 */
    public abstract Object eval(Environment e);
    @Override
    public Iterator<ASTree> iterator() {
        return children();
    }

}
