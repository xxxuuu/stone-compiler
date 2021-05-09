package stone.env;

import stone.vm.JvmFunction;
import stone.vm.JvmNativeFunction;

import java.util.HashMap;
import java.util.Map;

/**
 * @author XUQING
 * @date 2021/5/8
 */
public class VmEnv {
    private final Map<String, Integer> indexs = new HashMap<>();
    private final Map<String, JvmFunction> funs = new HashMap<>();
    private final Map<String, JvmNativeFunction> nativeFuns = new HashMap<>();
    public int count = 0;
    public int tempCount = 0;

    private VmEnv outer;

    public VmEnv() {}

    public VmEnv(VmEnv e) {
        this.outer = e;
    }

    public void addVar(String name) {
        if(indexs.containsKey(name)) {
            return;
        }
        indexs.put(name, count++);
    }

    /** 添加操作数 */
    public void addTempVal() {
        this.tempCount++;
    }

    public int varNums() {
        return count;
    }

    public int stackLen() {
        return count + tempCount;
    }

    public void addFun(String name, JvmFunction f) {
        funs.put(name, f);
    }

    public void addNativeFun(String name, JvmNativeFunction f) {
        nativeFuns.put(name, f);
    }

    public boolean hasFun(String name) {
        return getFun(name) != null;
    }

    public boolean hasNativeFun(String name) {
        return nativeFuns.containsKey(name);
    }

    public JvmNativeFunction getNativeFun(String name) {
        return nativeFuns.get(name);
    }

    public JvmFunction getFun(String name) {
        JvmFunction f = funs.get(name);
        if(f != null) {
            return f;
        }
        if(outer != null) {
            return outer.getFun(name);
        }
        return null;
    }

    public Integer getIndex(String name) {
        return indexs.get(name);
    }

    public VmEnv where(String name) {
        if(indexs.get(name) != null) {
            return this;
        }
        if(outer == null) {
            return null;
        }
        return outer.where(name);
    }
}
