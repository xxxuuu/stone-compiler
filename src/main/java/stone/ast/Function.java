package stone.ast;

import stone.Environment;
import stone.NestedEnv;

/**
 * 函数对象
 * 作为环境中函数名存储的对应的值
 * @author XUQING
 * @date 2021/4/15
 */
public class Function {
    protected ParameterList parameters;
    protected BlockStmnt body;
    protected Environment env;

    public Function(ParameterList parameters, BlockStmnt body, Environment env) {
        this.parameters = parameters;
        this.body = body;
        this.env = env;
    }

    public ParameterList parameters() {
        return parameters;
    }

    public BlockStmnt body() {
        return body;
    }

    public Environment makeEnv() {
        return new NestedEnv(env);
    }

    @Override public String toString() {
        return "<fun:" + hashCode() + ">";
    }
}
