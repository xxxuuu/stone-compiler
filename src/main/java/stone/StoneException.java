package stone;

import stone.ast.ASTree;

/**
 * Token 异常
 * @author XUQING
 * @date 2020/5/15
 */
public class StoneException extends RuntimeException {
    public StoneException(String msg) {
        super(msg);
    }

    public StoneException(String msg, ASTree ast) {
        super(msg + " " + ast.location());
    }
}
