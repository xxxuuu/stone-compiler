package stone;

/**
 * 单词（token）
 * @author XUQING
 * @date 2020/5/15
 */
public abstract class Token {
    /** end of file */
    public static final Token EOF = new Token(-1){};
    /** end of line */
    public static final String EOL = "\\n";
    private int lineNumber = 0;

    public Token(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    /** 是否为标识符（identifier） */
    public boolean isIdentifier() {
        return false;
    }

    /** 是否为整型字面量（integer literal） */
    public boolean isNumber() {
        return false;
    }

    /** 是否为字符串字面量（string literal）*/
    public boolean isString() {
        return false;
    }

    public int getNumber() {
        throw new StoneException("not number token");
    }

    public String getText() {
        return "";
    }
}
