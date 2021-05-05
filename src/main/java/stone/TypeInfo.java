package stone;

import stone.ast.ASTree;
import stone.ast.TypeTag;
import stone.env.TypeEnv;
import stone.exception.TypeException;

import java.util.Arrays;
import java.util.Objects;

/**
 * 类型信息
 * @author XUQING
 * @date 2021/5/2
 */
public class TypeInfo {
    /** Any 类型 */
    public static final TypeInfo ANY = new TypeInfo("Any");
    /** Int 类型 */
    public static final TypeInfo INT = new TypeInfo("Int");
    /** String 类型 */
    public static final TypeInfo STRING = new TypeInfo("String");

    String literal;

    private TypeInfo() {}

    private TypeInfo(String literal) {
        this.literal = literal;
    }

    @Override
    public String toString() {
        return literal;
    }

    public TypeInfo type() {
        return this;
    }

    public boolean match(TypeInfo obj) {
        return type() == obj.type();
    }

    /** 是否是给定类型的子类 */
    public boolean subtypeOf(TypeInfo superType) {
        return superType.type() == type() || superType.type() == ANY;
    }

    public void assertSubtypeOf(TypeInfo type, TypeEnv env, ASTree where) throws TypeException {
        if(type.isUnknownType()) {
            type.toUnknownType().assertSupertypeOf(this, env, where);
            return;
        }
        if (!subtypeOf(type)) {
            throw new TypeException("type mismatch: cannot convert from " + this + " to " + type, where);
        }
    }

    /** 获取与指定类型的共同父类 */
    public TypeInfo union(TypeInfo right, TypeEnv env) {
        if(right.isUnknownType()) {
            return right.union(this, env);
        }
        if(this.match(right)) {
            return type();
        }
        return ANY;
    }

    public boolean isUnknownType() {
        return false;
    }

    public FunctionType toFunctionType() {
        return null;
    }

    public boolean isFunctionType() {
        return false;
    }

    public UnknownType toUnknownType() {
        return null;
    }

    public TypeInfo plus(TypeInfo right, TypeEnv e) {
        if(right.isUnknownType()) {
            return right.plus(this, e);
        }
        if(this.match(INT) && right.match(INT)) {
            return INT;
        }
        if(this.match(STRING) && right.match(STRING)) {
            return STRING;
        }
        return ANY;
    }

    public static TypeInfo get(TypeTag tag) throws TypeException {
        String name = tag.type();
        if(INT.toString().equals(name)) {
            return INT;
        }
        if(STRING.toString().equals(name)) {
            return STRING;
        }
        if(ANY.toString().equals(name)) {
            return ANY;
        }
        if(TypeTag.UNDEF.equals(name)) {
            return new UnknownType();
        }
        throw new TypeException("unknown type" + name, tag);
    }

    public static FunctionType function(TypeInfo ref, TypeInfo... params) {
        return new FunctionType(ref, params);
    }

    public static class UnknownType extends TypeInfo {
        protected TypeInfo type = null;

        public void setType(TypeInfo t) {
            this.type = t;
        }

        public boolean resolved() {
            return type != null;
        }

        @Override
        public TypeInfo type() {
            return type == null ? ANY : type;
        }

        @Override
        public void assertSubtypeOf(TypeInfo t, TypeEnv env, ASTree where) throws TypeException {
            if(resolved()) {
                this.type.assertSubtypeOf(t, env, where);
                return;
            }
            env.addEquation(this, t);
        }

        public void assertSupertypeOf(TypeInfo t, TypeEnv env, ASTree where) throws TypeException {
            if(resolved()) {
                t.assertSubtypeOf(this.type, env, where);
                return;
            }
            env.addEquation(this, t);
        }

        @Override
        public TypeInfo union(TypeInfo right, TypeEnv env) {
            if(resolved()) {
                return this.type.union(right, env);
            }
            env.addEquation(this, right);
            return right;
        }

        @Override
        public TypeInfo plus(TypeInfo right, TypeEnv e) {
            if(resolved()) {
                return type.plus(right, e);
            }
            e.addEquation(this, INT);
            return right.plus(INT, e);
        }

        @Override
        public String toString() {
            return type().toString();
        }

        @Override
        public boolean isUnknownType() {
            return true;
        }

        @Override
        public UnknownType toUnknownType() {
            return this;
        }
    }

    public static class FunctionType extends TypeInfo {
        public TypeInfo returnType;
        public TypeInfo[] parameterTypes;

        FunctionType(TypeInfo ref, TypeInfo... params) {
            this.returnType = ref;
            this.parameterTypes = params;
        }

        @Override
        public boolean isFunctionType() {
            return true;
        }

        @Override
        public FunctionType toFunctionType() {
            return this;
        }

        @Override
        public boolean match(TypeInfo obj) {
            if(!(obj instanceof FunctionType)) {
                return false;
            }
            FunctionType func = (FunctionType) obj;
            if(parameterTypes.length != func.parameterTypes.length) {
                return false;
            }
            for(int i = 0; i < parameterTypes.length; i++) {
                if(!parameterTypes[i].match(func.parameterTypes[i])) {
                    return false;
                }
            }
            return returnType.match(func.returnType);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            if(parameterTypes.length == 0) {
                sb.append("Unit");
            } else {
                for(int i = 0; i < parameterTypes.length; i++) {
                    if(i > 0) {
                        sb.append(" * ");;
                    }
                    sb.append(parameterTypes[i]);
                }
            }
            sb.append(" -> ").append(returnType);
            return sb.toString();
        }
    }

}
