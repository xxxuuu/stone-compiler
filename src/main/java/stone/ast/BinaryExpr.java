package stone.ast;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import stone.Const;
import stone.TypeInfo;
import stone.env.Environment;
import stone.env.TypeEnv;
import stone.env.VmEnv;
import stone.exception.StoneException;
import stone.StoneObject;
import stone.exception.TypeException;

import static stone.StoneObject.AccessException;
import static org.objectweb.asm.Opcodes.*;

import java.util.List;

/**
 * 二元表达式
 * @author XUQING
 * @date 2020/5/17
 */
public class BinaryExpr extends ASTList {
    TypeInfo leftType, rightType;

    public BinaryExpr(List<ASTree> list) {
        super(list);
    }

    public ASTree left() {
        return this.child(0);
    }

    public String operator() {
        return ((ASTLeaf) (this.child(1))).token.getText();
    }

    public ASTree right() {
        return this.child(2);
    }

    @Override
    public Object eval(Environment e) {
        String op = operator();
        if("=".equals(op)) {
            Object rvalue = right().eval(e);
            return computeAssign(e, rvalue);
        } else {
            Object lvalue = left().eval(e);
            Object rvalue = right().eval(e);
            return computeOp(lvalue, op, rvalue);
        }
    }

    @Override
    public void compileToJvm(ClassWriter cw, MethodVisitor mw, VmEnv e) {
        String op = operator();
        if("=".equals(op)) {
            // TODO: Array
            ASTree l = left();
            if(l instanceof Name) {
                String name = ((Name) l).name();
                e.addVar(name);
                right().compileToJvm(cw, mw, e);
                mw.visitVarInsn(ISTORE, e.getIndex(name));
            }
            return;
        }
        left().compileToJvm(cw, mw, e);
        right().compileToJvm(cw, mw, e);

        Label label1 = new Label();
        Label label2 = new Label();
        switch (op) {
            case "+":
                mw.visitInsn(IADD);
                break;
            case "-":
                mw.visitInsn(ISUB);
                break;
            case "*":
                mw.visitInsn(IMUL);
                break;
            case "/":
                mw.visitInsn(IDIV);
                break;
            case "%":
                mw.visitInsn(IREM);
                break;
            case "==":
                mw.visitJumpInsn(IF_ICMPEQ, label1);
                mw.visitInsn(ICONST_0);
                mw.visitJumpInsn(GOTO, label2);
                mw.visitLabel(label1);
                mw.visitInsn(ICONST_1);
                mw.visitLabel(label2);
                break;
            case ">":
                mw.visitJumpInsn(IF_ICMPGT, label1);
                mw.visitInsn(ICONST_0);
                mw.visitJumpInsn(GOTO, label2);
                mw.visitLabel(label1);
                mw.visitInsn(ICONST_1);
                mw.visitLabel(label2);
                break;
            case "<":
                mw.visitJumpInsn(IF_ICMPLT, label1);
                mw.visitInsn(ICONST_0);
                mw.visitJumpInsn(GOTO, label2);
                mw.visitLabel(label1);
                mw.visitInsn(ICONST_1);
                mw.visitLabel(label2);
                break;
            case ">=":
                mw.visitJumpInsn(IF_ICMPGE, label1);
                mw.visitInsn(ICONST_0);
                mw.visitJumpInsn(GOTO, label2);
                mw.visitLabel(label1);
                mw.visitInsn(ICONST_1);
                mw.visitLabel(label2);
                break;
            case "<=":
                mw.visitJumpInsn(IF_ICMPLE, label1);
                mw.visitInsn(ICONST_0);
                mw.visitJumpInsn(GOTO, label2);
                mw.visitLabel(label1);
                mw.visitInsn(ICONST_1);
                mw.visitLabel(label2);
                break;
            default:
                throw new StoneException("bad operator", this);
        }
    }

    private Object computeAssign(Environment e, Object rvalue) {
        ASTree l = left();
        if (l instanceof PrimaryExpr) {
            PrimaryExpr p = (PrimaryExpr) l;
            if (p.hasPostfix(0) && p.postfix(0) instanceof ArrayRef) {
                Object a = ((PrimaryExpr) l).evalSubExpr(e, 1);
                if (a instanceof Object[]) {
                    ArrayRef aref = (ArrayRef)p.postfix(0);
                    Object index = aref.index().eval(e);
                    if (index instanceof Integer) {
                        ((Object[])a)[(Integer)index] = rvalue;
                        return rvalue;
                    }
                }
                throw new StoneException("bad array access", this);
            } else if (p.hasPostfix(0) && p.postfix(0) instanceof Dot) {
                Object t = p.evalSubExpr(e, 1);
                if (t instanceof StoneObject) {
                    return setField((StoneObject) t, (Dot) p.postfix(0), rvalue);
                }
            }
        }

        if(l instanceof Name) {
            e.put(((Name)l).name(), rvalue);
            return rvalue;
        }
        throw new StoneException("bad assignment", this);
    }

    private Object computeOp(Object lvalue, String op, Object rvalue) {
        if (lvalue instanceof Integer && rvalue instanceof Integer) {
            return computeNumber((Integer)lvalue, op, (Integer)rvalue);
        }
        if (op.equals("+")) {
            return String.valueOf(lvalue) + String.valueOf(rvalue);
        } else if (op.equals("==")) {
            if (lvalue == null) {
                return rvalue == null ? Const.TRUE : Const.FALSE;
            }
            else {
                return lvalue.equals(rvalue) ? Const.TRUE : Const.FALSE;
            }
        }
        throw new StoneException("bad type", this);
    }

    private Object computeNumber(Integer lvalue, String op, Integer rvalue) {
        int a = lvalue;
        int b = rvalue;
        switch (op) {
            case "+":
                return a+b;
            case "-":
                return a-b;
            case "*":
                return a*b;
            case "/":
                return a/b;
            case "%":
                return a%b;
            case "==":
                return a == b ? Const.TRUE : Const.FALSE;
            case ">":
                return a > b ? Const.TRUE : Const.FALSE;
            case "<":
                return a < b ? Const.TRUE : Const.FALSE;
            case ">=":
                return a >= b ? Const.TRUE : Const.FALSE;
            case "<=":
                return a <= b ? Const.TRUE : Const.FALSE;
            default:
                throw new StoneException("bad operator", this);
        }
    }

    @Override
    public TypeInfo typeCheck(TypeEnv e) throws TypeException {
        String op = operator();
        if("=".equals(op)) {
            return typeCheckForAssign(e);
        } else {
            this.leftType = left().typeCheck(e);
            this.rightType = right().typeCheck(e);
            if("+".equals(op)) {
                return leftType.plus(rightType, e);
            }
            if("==".equals(op)) {
                return TypeInfo.INT;
            }
            leftType.assertSubtypeOf(TypeInfo.INT, e, this);
            rightType.assertSubtypeOf(TypeInfo.INT, e, this);
            return TypeInfo.INT;
        }
    }

    protected TypeInfo typeCheckForAssign(TypeEnv e) throws TypeException {
        rightType = right().typeCheck(e);
        ASTree left = left();
        if(left instanceof Name) {
            return ((Name) left).typeCheckForAssign(e, rightType);
        }
        throw new TypeException("bad assignment", this);
    }

    protected Object setField(StoneObject obj, Dot expr, Object rvalue) {
        String name = expr.name();
        try {
            obj.write(name, rvalue);
            return rvalue;
        } catch (AccessException e) {
            throw new StoneException("bad member access " + location()
                    + ": " + name);
        }
    }
}
