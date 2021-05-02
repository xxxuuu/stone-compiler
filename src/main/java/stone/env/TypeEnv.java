package stone.env;

import stone.TypeInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * 存储类型信息的环境
 * @author XUQING
 * @date 2021/5/2
 */
public class TypeEnv {
    TypeEnv outer;
    public Map<String, TypeInfo> types;

    public TypeEnv() {
        this(null);
    }

    public TypeEnv(TypeEnv e) {
        types = new HashMap<>();
        outer = e;
    }

    public TypeInfo get(String name) {
        var v = types.get(name);
        if (v == null && outer != null) {
            return outer.get(name);
        }
        return v;
    }

    public TypeInfo putNew(String name, TypeInfo value) {
        TypeInfo oldValue = types.get(name);
        types.put(name, value);
        return oldValue;
    }

    public TypeInfo put(String name, TypeInfo value) {
        // 为变量赋值，如果未定义默认是当前作用域
        TypeEnv e = where(name);
        if (e == null) {
            e = this;
        }
        return e.putNew(name, value);
    }

    public TypeEnv where(String name) {
        // 当前环境
        if (types.get(name) != null) {
            return this;
        }
        if (outer == null) {
            return null;
        }
        // 在外部环境中查找
        return outer.where(name);
    }
}
