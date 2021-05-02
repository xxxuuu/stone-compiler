package stone;

import stone.env.Environment;

/**
 * 对象信息
 * @author XUQING
 * @date 2021/4/17
 */
public class StoneObject {
    public static class AccessException extends Exception {}

    protected Environment env;

    public StoneObject(Environment e) { env = e; }

    @Override public String toString() {
        return "<object:" + hashCode() + ">";
    }

    public Object read(String member) throws AccessException {
        return getEnv(member).get(member);
    }

    public void write(String member, Object value) throws AccessException {
        getEnv(member).putNew(member, value);
    }

    protected Environment getEnv(String member) throws AccessException {
        Environment e = env.where(member);
        // 成员存在并且处于对象内的环境 否则会出现p.value的value可能引用全局变量
        if (e != null && e == env) {
            return e;
        }
        throw new AccessException();
    }
}
