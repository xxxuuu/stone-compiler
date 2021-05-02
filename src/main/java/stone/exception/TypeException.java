package stone.exception;

import stone.ast.ASTree;

/**
 * 类型异常
 * @author XUQING
 * @date 2021/5/2
 */
public class TypeException extends Exception {
    public TypeException(String msg, ASTree t) {
        super(msg + " " + t.location());
    }
}
