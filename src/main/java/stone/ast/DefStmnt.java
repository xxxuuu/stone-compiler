package stone.ast;

import stone.TypeInfo;
import stone.env.Environment;
import stone.Function;
import stone.env.TypeEnv;
import stone.exception.TypeException;

import java.util.List;

/**
 * def(函数定义)语句
 * @author XUQING
 * @date 2021/4/15
 */
public class DefStmnt extends ASTList {
    protected TypeInfo.FunctionType funcType;
    protected TypeEnv bodyEnv;

    public DefStmnt(List<ASTree> c) {
        super(c);
    }

    public String name() {
        return ((ASTLeaf)child(0)).token().getText();
    }

    public ParameterList parameters() {
        return (ParameterList)child(1);
    }

    public TypeTag type() {
        return (TypeTag) child(2);
    }

    public BlockStmnt body() {
        return (BlockStmnt)child(3);
    }

    @Override
    public String toString() {
        return "(def " + name() + " " + parameters() + " " + type() + " " + body() + ")";
    }

    @Override
    public Object eval(Environment e) {
        e.putNew(name(), new Function(parameters(), body(), e));
        return name();
    }

    protected void fixUnknown(TypeInfo t) {
        if(t.isUnknownType()) {
            TypeInfo.UnknownType ut = t. toUnknownType();
            if(!ut.resolved()) {
                ut.setType(TypeInfo.ANY);
            }
        }
    }

    @Override
    public TypeInfo typeCheck(TypeEnv e) throws TypeException {
        TypeInfo[] params = parameters().types();
        TypeInfo returnType = TypeInfo.get(type());
        this.funcType = TypeInfo.function(returnType, params);
        TypeInfo oldType = e.put(name(), funcType);
        if(oldType != null) {
            throw new TypeException("function redefinition: " + name(), this);
        }
        this.bodyEnv = new TypeEnv(e);
        for(int i = 0; i < params.length; i++) {
            bodyEnv.put(parameters().name(i), params[i]);
        }
        TypeInfo bodyType = body().typeCheck(bodyEnv);
        bodyType.assertSubtypeOf(returnType, e, this);

        TypeInfo.FunctionType func = funcType.toFunctionType();
        for(TypeInfo t : func.parameterTypes) {
            fixUnknown(t);
        }
        fixUnknown(func.returnType);
        return func;
    }
}
