package stone;

import stone.exception.ParseException;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 词法分析器
 * @author XUQING
 * @date 2020/5/15
 */
public class Lexer {
    /** 匹配注释、整型字面量、字符串字面量、标识符、运算符的模式 */
    public static String regexPat = "\\s*((//.*)|([0-9]+)|(\"(\\\\\"|\\\\\\\\|\\\\n|[^\"])*\")"
                                + "|[A-Z_a-z][A-Z_a-z0-9]*|==|<=|>=|&&|\\|\\||\\p{Punct})?";
    private Pattern pattern = Pattern.compile(regexPat);
    /** token 暂存队列 */
    private List<Token> queue = new ArrayList<>();
    private boolean hasMore;
    private LineNumberReader reader;

    public Lexer(Reader r) {
        this.hasMore = true;
        reader = new LineNumberReader(r);
    }

    /** 读取 Token */
    public Token read() throws ParseException {
        if(fillQueue(0)) {
            return queue.remove(0);
        }
        return Token.EOF;
    }

    /** 预读 Token */
    public Token peek(int i) throws ParseException {
        if(fillQueue(i)) {
            return queue.get(i);
        }
        return Token.EOF;
    }

    /** 读取每一行并进行词法分析 */
    protected void readLine() throws ParseException {
        String line;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            throw new ParseException(e);
        }

        if(line == null) {
            this.hasMore = false;
            return;
        }

        int lineNo = reader.getLineNumber();
        Matcher matcher = pattern.matcher(line);
        matcher.useTransparentBounds(true).useAnchoringBounds(false);
        int pos = 0;
        int endPos = line.length();
        while(pos < endPos) {
            matcher.region(pos,endPos);
            if(matcher.lookingAt()) {
                this.addToken(lineNo, matcher);
                pos = matcher.end();
            } else {
                throw new ParseException("bad token at line " + lineNo);
            }
        }
        queue.add(new IdToken(lineNo, Token.EOL));
    }

    /**
     * 分析 Token 类型并添加进队列
     * @param lineNo
     * @param matcher
     */
    protected void addToken(int lineNo, Matcher matcher) {
        String m = matcher.group(1);
        // 不是空格
        if(m != null) {
            // 不是注释
            if(matcher.group(2) == null) {
                Token token;
                // 整型字面量
                if(matcher.group(3) != null) {
                    token = new NumToken(lineNo, Integer.parseInt(m));
                } else if(matcher.group(4) != null) {   // 字符串字面量
                    token = new StrToken(lineNo, this.toStringLiteral(m));
                } else {    // 标识符或运算符字面量
                    token = new IdToken(lineNo, m);
                }
                queue.add(token);
            }
        }
    }

    /**
     * 填充 Token 队列
     * @param i
     * @return
     * @throws ParseException
     */
    private boolean fillQueue(int i) throws ParseException {
        while(i >= queue.size()) {
            if(hasMore) {
                this.readLine();
            } else {
                return false;
            }
        }
        return true;
    }

    /** 处理转义 */
    protected String toStringLiteral(String s) {
        StringBuilder sb = new StringBuilder();
        int len = s.length() -1;
        for(int i = 1; i < len; i++) {
            char c = s.charAt(i);
            if(c == '\\' && i + 1 < len) {
                int c2 = s.charAt(i+1);
                if(c2 == '"' || c2 == '\\') {
                    c = s.charAt(++i);
                } else if (c2 == 'n') {
                    i++;
                    c = '\n';
                }
            }
            sb.append(c);
        }
        return sb.toString();
    }

    /** 整型 Token */
    protected static class NumToken extends Token {
        private int value;

        public NumToken(int lineNumber, int value) {
            super(lineNumber);
            this.value = value;
        }

        @Override
        public boolean isNumber() {
            return true;
        }

        @Override
        public int getNumber() {
            return this.value;
        }

        @Override
        public String getText() {
            return Integer.toString(value);
        }
    }

    /** 字符串 Token */
    protected static class StrToken extends Token {
        private String literal;
        public StrToken(int lineNumber, String str) {
            super(lineNumber);
            this.literal = str;
        }

        @Override
        public boolean isString() {
            return true;
        }

        @Override
        public String getText() {
            return this.literal;
        }
    }

    /** 标识符 Token */
    protected static class IdToken extends Token {
        private String text;

        public IdToken(int lineNumber, String id) {
            super(lineNumber);
            this.text = id;
        }

        @Override
        public boolean isIdentifier() {
            return true;
        }

        @Override
        public String getText() {
            return this.text;
        }
    }
}
