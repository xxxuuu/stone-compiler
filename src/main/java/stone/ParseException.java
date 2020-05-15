package stone;

import java.io.IOException;

/**
 * 词法分析异常
 * @author XUQING
 * @date 2020/5/15
 */
public class ParseException extends Exception {
    private static String location(Token t) {
        if(t == Token.EOF) {
            return "the last line";
        }
        return String.format("\" %s \" at line %d", t.getText(), t.getLineNumber());
    }

    public ParseException(String msg, Token t) {
        super(String.format("syntax error around %s. %s", location(t), msg));
    }

    public ParseException(Token t) {
        this("", t);
    }

    public ParseException(String msg) {
        super(msg);
    }

    public ParseException(IOException e) {
        super(e);
    }
}
