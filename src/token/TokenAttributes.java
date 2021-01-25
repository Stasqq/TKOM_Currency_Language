package token;

import java.util.ArrayList;
import java.util.List;

public final class TokenAttributes {
    public static List<TokenType> valueTypes = new ArrayList<>() {{
        add(TokenType.INT);
        add(TokenType.DOUBLE);
        add(TokenType.BOOL);
        add(TokenType.CURRENCY);
    }};

    public static List<TokenType> lineTypes = new ArrayList<>() {{
        add(TokenType.INT);
        add(TokenType.DOUBLE);
        add(TokenType.BOOL);
        add(TokenType.CURRENCY);
        add(TokenType.IF);
        add(TokenType.WHILE);
        add(TokenType.RETURN);
        add(TokenType.ID);
        add(TokenType.PRINT);
        add(TokenType.AMOUNT);
    }};

    public static List<TokenType> expressionTypes = new ArrayList<>() {{
        add(TokenType.MINUS);
        add(TokenType.NUMBER);
        add(TokenType.OPEN_ROUND_BRACKET);
        add(TokenType.ID);
    }};

    public static List<TokenType> highPriorityOperators = new ArrayList<>() {{
        add(TokenType.MULTIPLY);
        add(TokenType.DIVIDE);
    }};

    public static List<TokenType> lowPriorityOperators = new ArrayList<>() {{
        add(TokenType.PLUS);
        add(TokenType.MINUS);
    }};

    public static List<TokenType> equalityOperators = new ArrayList<>() {{
        add(TokenType.EQUAL);
        add(TokenType.UNEQUAL);
    }};

    public static List<TokenType> relationOperators = new ArrayList<>() {{
        add(TokenType.LESS);
        add(TokenType.LESS_OR_EQUAL);
        add(TokenType.GREATER);
        add(TokenType.GREATER_OR_EQUAL);
    }};
}
