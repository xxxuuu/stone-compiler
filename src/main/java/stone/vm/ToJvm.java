package stone.vm;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import stone.TypeInfo;
import stone.ast.ASTree;
import stone.env.VmEnv;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.function.Consumer;

import static org.objectweb.asm.Opcodes.*;

/**
 * 转换为Java字节码文件
 * @author XUQING
 * @date 2021/5/7
 */
public class ToJvm {
    private ClassWriter cw;
    private MethodVisitor main;
    private VmEnv e;

    public static final String STRING_DESCRIPTOR = "Ljava/lang/String;";
    public static final String OBJECT_CLS_NAME = "java/lang/Object";
    public static final String OBJECT_DESCRIPTOR = "Ljava/lang/Object;";
    public static final String ARRAY_DESCRIPTOR = "[";
    public static final String VOID = "V";
    public static final String VOID_DESC = "()V";
    public static String mainClsName;

    public ToJvm() {
        this("StoneRuntime");
    }

    public ToJvm(String mainClsName) {
        ToJvm.mainClsName = mainClsName;
        // 生成默认的类和构造方法
        this.cw = makeClass(mainClsName, null);

        this.main = cw.visitMethod(
                ACC_PUBLIC + ACC_STATIC,
                "main",
                makeDesc(VOID, ARRAY_DESCRIPTOR + STRING_DESCRIPTOR),
                null,
                null);

        e = new VmEnv();
        addNativeFun(e);
    }

    public static void addNativeFun(VmEnv e) {
        e.addNativeFun("print", new JvmNativeFunction("print") {
            @Override
            public void invoke(MethodVisitor mw, VmEnv e, Consumer<MethodVisitor> loadArgs) {
                mw.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                loadArgs.accept(mw);
                mw.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
                // 有个long 在操作数栈中占两个空间
                e.addTempVal(); e.addTempVal();
            }
        });
        e.addNativeFun("currentTime", new JvmNativeFunction("currentTime") {
            @Override
            public void invoke(MethodVisitor mw, VmEnv e,  Consumer<MethodVisitor> loadArgs) {
                mw.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
                mw.visitLdcInsn(3600000L);
                mw.visitInsn(LREM);
                mw.visitInsn(L2I);
            }
        });
    }

    public static String typeInfo2mDesc(TypeInfo t) {
        if(t.match(TypeInfo.INT)) {
            return "I";
        }
        if(t.match(TypeInfo.STRING)) {
            return STRING_DESCRIPTOR;
        }
        return OBJECT_DESCRIPTOR;
    }

    public static String makeDesc(String returns, String... params) {
        StringBuilder sb = new StringBuilder("(");
        for(String p : params) {
            sb.append(p);
        }
        sb.append(")").append(returns);
        return sb.toString();
    }

    private ClassWriter makeClass(String clsName, String superClsName) {
        String superCls = superClsName == null ? OBJECT_CLS_NAME : superClsName;

        ClassWriter c = new ClassWriter(0);
        c.visit(V1_5, ACC_PUBLIC, clsName, null, superCls, null);

        MethodVisitor construct = c.visitMethod(ACC_PUBLIC, "<init>", VOID_DESC, null, null);
        // 相当于 return this.super()
        construct.visitVarInsn(ALOAD, 0);
        construct.visitMethodInsn(INVOKESPECIAL, superCls, "<init>", VOID_DESC, false);
        construct.visitInsn(RETURN);
        construct.visitMaxs(1, 1);
        construct.visitEnd();

        return c;
    }

    public void compile(ASTree tree) {
        tree.compileToJvm(this.cw, this.main, e);
    }

    public void write(String path) {
        this.main.visitInsn(RETURN);
        // main有个参数 所以 +1
        this.main.visitMaxs(this.e.stackLen()+1, this.e.varNums()+1);
        this.main.visitEnd();
        this.cw.visitEnd();

        try(FileOutputStream out = new FileOutputStream(path + "/" + mainClsName + ".class")) {
            out.write(this.cw.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
