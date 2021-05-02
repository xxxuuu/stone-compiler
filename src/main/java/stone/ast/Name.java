package stone.ast;

import stone.TypeInfo;
import stone.env.Environment;
import stone.env.TypeEnv;
import stone.exception.StoneException;
import stone.Token;
import stone.exception.TypeException;

/**
 * @author XUQING
 * @date 2020/5/17
 */
public class Name extends ASTLeaf {
    TypeInfo type;

    public Name(Token t) {
        super(t);
    }

    public String name() {
        return this.token().getText();
    }

    @Override
    public TypeInfo typeCheck(TypeEnv e) throws TypeException {
        type = e.get(name());
        if(type == null) {
            throw new TypeException("undefined name: " + name(), this);
        }
        return type;
    }

    public TypeInfo typeCheckForAssign(TypeEnv e, TypeInfo typeVal) throws TypeException {
        type = e.get(name());
        if(type == null) {
            this.type = typeVal;
            e.put(name(), typeVal);
            return typeVal;
        }
        // 断言typeVal是type的子类
        typeVal.assertSubtypeOf(type, e, this);
        return type;
    }

    @Override
    public Object eval(Environment e) {
        Object val = e.get(name());
        if(val == null) {
            throw new StoneException("undefined name: " + name(), this);
        }
        return val;
    }
}
