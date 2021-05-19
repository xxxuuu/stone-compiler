package stone.util;

import stone.ast.ASTree;

import java.util.stream.IntStream;

/**
 * @author XUQING
 * @date 2021/5/19
 */
public class ASTreeUtils {
    public static void printAst(ASTree t) {
        t.forEach((c) -> {
            printAst(c, 1);
        });
    }
    private static void printAst(ASTree t, int deep) {
        IntStream.range(0, deep-1).forEach((x) -> System.out.print("\t"));
        System.out.println("<" + t.getClass().getSimpleName() + "> " + t);
        t.forEach((c) -> {
            printAst(c, deep+1);
        });
    }
}
