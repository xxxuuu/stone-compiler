package stone.ast;

import stone.Environment;
import stone.StoneException;

import java.lang.reflect.Method;

/**
 * @author XUQING
 * @date 2021/4/16
 */
public class NativeFunction {
    private Method method;
    private String name;
    private int numParams;

    public NativeFunction(String n, Method m) {
        this.name = n;
        this.method = m;
        this.numParams = m.getParameterCount();
    }

    @Override
    public String toString() {
        return "<native:" + hashCode() + ">";
    }

    public int numOfParameters() {
        return this.numParams;
    }

    public Object invoke(ASTree tree, Object... args) {
        try {
            // 调用静态方法
            return method.invoke(null, args);
        } catch (Exception e) {
            throw new StoneException("bad native function call: " + name, tree);
        }
    }
}
