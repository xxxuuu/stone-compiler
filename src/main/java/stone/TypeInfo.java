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
    /** 未知类型 */
    public static final TypeInfo UNKNOWN = new TypeInfo();

    String literal;

    private TypeInfo() {
    }

    private TypeInfo(String literal) {
        this.literal = literal;
    }

    @Override
    public String toString() {
        return literal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeInfo typeInfo = (TypeInfo) o;
        return Objects.equals(literal, typeInfo.literal);
    }

    /** 是否是给定类型的子类 */
    public boolean subtypeOf(TypeInfo superType) {
        return superType.equals(ANY) || this.equals(superType);
    }

    public void assertSubtypeOf(TypeInfo type, TypeEnv env, ASTree where) throws TypeException {
        if(!subtypeOf(type)) {
            throw new TypeException("type mismatch: cannot convert from " + this + " to " + type, where);
        }
    }

    /** 获取与指定类型的共同父类 */
    public TypeInfo union(TypeInfo right, TypeEnv env) {
        if(this.equals(right)) {
            return this;
        }
        return ANY;
    }

    public boolean isUnknownType() {
        return this == UNKNOWN;
    }

    public boolean isFunctionType() {
        return false;
    }

    public TypeInfo plus(TypeInfo right, TypeEnv e) {
        if(this.equals(INT) && right.equals(INT)) {
            return INT;
        }
        if(this.equals(STRING) && right.equals(STRING)) {
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
            return UNKNOWN;
        }
        throw new TypeException("unknown type" + name, tag);
    }

    public static FunctionType function(TypeInfo ref, TypeInfo... params) {
        return new FunctionType(ref, params);
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
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            FunctionType that = (FunctionType) o;
            return Objects.equals(returnType, that.returnType) && Arrays.equals(parameterTypes, that.parameterTypes);
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
