package stone;

/**
 * 执行环境对象
 * @author XUQING
 * @date 2021/4/15
 */
public interface Environment {
    void put(String name, Object value);
    Object get(String name);
}
