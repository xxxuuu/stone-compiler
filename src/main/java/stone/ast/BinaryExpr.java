package stone.ast;

import stone.Const;
import stone.Environment;
import stone.StoneException;
import stone.StoneObject;
import static stone.StoneObject.AccessException;

import java.util.List;

/**
 * 二元表达式
 * @author XUQING
 * @date 2020/5/17
 */
public class BinaryExpr extends ASTList {
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

    private Object computeAssign(Environment e, Object rvalue) {
        ASTree l = left();
        if (l instanceof PrimaryExpr) {
            PrimaryExpr p = (PrimaryExpr) l;
            if (p.hasPostfix(0) && p.postfix(0) instanceof Dot) {
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
