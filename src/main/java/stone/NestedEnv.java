package stone;

import java.util.HashMap;

/**
 * 嵌套作用域环境
 * @author XUQING
 * @date 2021/4/15
 */
public class NestedEnv implements Environment {
    protected HashMap<String,Object> values;
    /** 外层作用域环境 */
    protected Environment outer;

    public NestedEnv() { this(null); }

    public NestedEnv(Environment e) {
        values = new HashMap<>();
        outer = e;
    }

    @Override
    public void setOuter(Environment e) { outer = e; }

    @Override
    public Object get(String name) {
        Object v = values.get(name);
        if (v == null && outer != null) {
            return outer.get(name);
        }
        return v;
    }

    @Override
    public void putNew(String name, Object value) {
        values.put(name, value);
    }

    @Override
    public void put(String name, Object value) {
        // 为变量赋值，如果未定义默认是当前作用域
        Environment e = where(name);
        if (e == null) {
            e = this;
        }
        e.putNew(name, value);
    }

    @Override
    public Environment where(String name) {
        // 当前环境
        if (values.get(name) != null) {
            return this;
        }
        if (outer == null) {
            return null;
        }
        // 在外部环境中查找
        return outer.where(name);
    }
}
