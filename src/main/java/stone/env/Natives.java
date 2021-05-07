package stone.env;

import stone.NativeFunction;
import stone.TypeInfo;
import stone.exception.StoneException;

import javax.swing.*;
import java.lang.reflect.Method;

/**
 * @author XUQING
 * @date 2021/4/16
 */
public class Natives {
    public Environment environment(Environment env) {
        appendNatives(env);
        return env;
    }

    public TypeEnv typeEnvironment(TypeEnv env) {
        appendNativesType(env);
        return env;
    }

    protected void appendNatives(Environment env) {
        append(env, "print", Natives.class, "print", Object.class);
        append(env, "read", Natives.class, "read");
        append(env, "length", Natives.class, "length", String.class);
        append(env, "toInt", Natives.class, "toInt", Object.class);
        append(env, "currentTime", Natives.class, "currentTime");
    }

    protected void appendNativesType(TypeEnv env) {
        appendType(env, "print",  TypeInfo.function(TypeInfo.INT, TypeInfo.ANY));
        appendType(env, "read", TypeInfo.function(TypeInfo.STRING));
        appendType(env, "length", TypeInfo.function(TypeInfo.INT, TypeInfo.STRING));
        appendType(env, "toInt", TypeInfo.function(TypeInfo.INT, TypeInfo.ANY));
        appendType(env, "currentTime", TypeInfo.function(TypeInfo.INT));
    }

    protected void append(Environment env, String name, Class<?> clazz,
                          String methodName, Class<?> ... params) {
        Method m;
        try {
            m = clazz.getMethod(methodName, params);
        } catch (Exception e) {
            throw new StoneException("cannot find a native function: "
                    + methodName);
        }
        env.put(name, new NativeFunction(methodName, m));
    }

    protected void appendType(TypeEnv env, String name, TypeInfo.FunctionType type) {
        env.put(name, type);
    }

    // native methods
    public static int print(Object obj) {
        System.out.println(obj.toString());
        return 0;
    }

    public static String read() {
        return JOptionPane.showInputDialog(null);
    }

    public static int length(String s) { return s.length(); }

    public static int toInt(Object value) {
        if (value instanceof String) {
            return Integer.parseInt((String) value);
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        throw new NumberFormatException(value.toString());
    }

    private static long startTime = System.currentTimeMillis();

    public static int currentTime() {
        return (int)(System.currentTimeMillis() - startTime);
    }
}
