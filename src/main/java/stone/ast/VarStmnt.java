package stone.ast;

import stone.TypeInfo;
import stone.env.Environment;
import stone.env.TypeEnv;
import stone.exception.TypeException;

import java.util.List;

/**
 * @author XUQING
 * @date 2021/5/1
 */
public class VarStmnt extends ASTList {
    protected TypeInfo varType, valueType;

    public VarStmnt(List<ASTree> list) {
        super(list);
    }

    public String name() {
        return ((ASTLeaf)child(0)).token.getText();
    }

    public TypeTag type() {
        return (TypeTag) child(1);
    }

    public ASTree initializer() {
        return child(2);
    }

    @Override
    public Object eval(Environment e) {
        Object val = initializer().eval(e);
        e.putNew(name(), val);
        return val;
    }

    @Override
    public TypeInfo typeCheck(TypeEnv e) throws TypeException {
        if(e.get(name()) != null) {
            throw new TypeException("duplicate variable: " + name(), this);
        }
        this.varType = TypeInfo.get(type());
        e.put(name(), this.varType);
        this.valueType = initializer().typeCheck(e);
        valueType.assertSubtypeOf(valueType, e, this);
        return varType;
    }

    @Override
    public String toString() {
        return "(var " + name() + " " + type() + " " + initializer() + ")";
    }
}
