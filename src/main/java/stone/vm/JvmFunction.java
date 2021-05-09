package stone.vm;

import stone.TypeInfo;

import java.util.Arrays;

/**
 * @author XUQING
 * @date 2021/5/9
 */
public class JvmFunction {
    protected String name;
    protected String descriptor;
    protected TypeInfo.FunctionType typeInfo;

    public JvmFunction() {}

    public JvmFunction(String name, TypeInfo.FunctionType typeInfo) {
        this.name = name;
        this.typeInfo = typeInfo;
        this.descriptor = ToJvm.makeDesc(
                ToJvm.typeInfo2mDesc(typeInfo.returnType),
                Arrays.stream(typeInfo.parameterTypes)
                        .map(ToJvm::typeInfo2mDesc)
                        .toArray(String[]::new)
        );
    }

    public String getName() {
        return name;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public TypeInfo.FunctionType getTypeInfo() {
        return typeInfo;
    }
}
