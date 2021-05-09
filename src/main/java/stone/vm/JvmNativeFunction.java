package stone.vm;

import org.objectweb.asm.MethodVisitor;
import stone.TypeInfo;
import stone.env.VmEnv;

import java.util.function.Consumer;

/**
 * @author XUQING
 * @date 2021/5/9
 */
public abstract class JvmNativeFunction extends JvmFunction
{
    public JvmNativeFunction(String name) {
        this.name = name;
    }

    public abstract void invoke(MethodVisitor mw, VmEnv e, Consumer<MethodVisitor> loadArgs);
}
