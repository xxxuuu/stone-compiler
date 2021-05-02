package stone.env;

/**
 * 执行环境对象
 * @author XUQING
 * @date 2021/4/15
 */
public interface Environment {
    /** 设置变量名及其值 */
    void put(String name, Object value);
    /** 获取变量名的值 */
    Object get(String name);
    /** 为当前环境设置变量名 */
    void putNew(String name, Object value);
    /** 查找指定变量名的环境 */
    Environment where(String name);
    /** 设置外部环境作用域 */
    void setOuter(Environment e);
}
