package stone.env;

import stone.TypeInfo;

import java.util.*;

/**
 * 存储类型信息的环境
 * @author XUQING
 * @date 2021/5/2
 */
public class TypeEnv {
    public static class Equation extends ArrayList<TypeInfo.UnknownType> {

    }

    TypeEnv outer;
    protected List<Equation> equations = new LinkedList<>();
    public Map<String, TypeInfo> types;

    public TypeEnv() {
        this(null);
    }

    public TypeEnv(TypeEnv e) {
        types = new HashMap<>();
        outer = e;
    }

    public void addEquation(TypeInfo.UnknownType t1, TypeInfo t2) {
        if(t2.isUnknownType()) {
            if(t2.toUnknownType().resolved()) {
                t2 = t2.type();
            }
        }
        Equation eq = find(t1);
        if(t2.isUnknownType()) {
            eq.add(t2.toUnknownType());
        } else {
            // 已推论出类型 为等式中其它未知类型设置类型
            for(TypeInfo.UnknownType t : eq) {
                t.setType(t2);
            }
            equations.remove(eq);
        }
    }

    /** 寻找t所在的等式 */
    protected Equation find(TypeInfo.UnknownType t) {
        for(Equation e : equations) {
            if(e.contains(t)) {
                return e;
            }
        }
        Equation e = new Equation();
        e.add(t);
        equations.add(e);
        return e;
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
