package stone;

import java.util.HashMap;

/**
 * @author XUQING
 * @date 2021/4/15
 */
public class BasicEnv implements Environment {
    HashMap<String, Object> vals;

    public BasicEnv() {
        vals = new HashMap<>();
    }

    @Override
    public void put(String name, Object value) {
        vals.put(name, value);
    }

    @Override
    public Object get(String name) {
        return vals.get(name);
    }

    @Override
    public void putNew(String name, Object value) {
        this.put(name, value);
    }

    @Override
    public Environment where(String name) {
        if(this.get(name) != null) {
            return this;
        }
        return null;
    }

    @Override
    public void setOuter(Environment e) { }
}
