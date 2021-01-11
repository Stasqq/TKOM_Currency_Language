package token;

import java.util.HashMap;
import java.util.Map;

public final class TokenTypeMatcher {


    public static Map<String, TokenType> oneSignMatcher = new HashMap<>(){
        {
            put("(", TokenType.OPEN_ROUND_BRACKET);
            put(")", TokenType.CLOSE_ROUND_BRACKET);
            put("[", TokenType.OPEN_SQUARE_BRACKET);
            put("]", TokenType.CLOSE_SQUARE_BRACKET);
            put("{", TokenType.OPEN_BUCKLE);
            put("}", TokenType.CLOSE_BUCKLE);
            put("=", TokenType.ASSIGMENT);
            put("+", TokenType.PLUS);
            put("-", TokenType.MINUS);
            put("*", TokenType.MULTIPLY);
            put("/", TokenType.DIVIDE);
            put("<", TokenType.LESS);
            put(">", TokenType.GREATER);
            put(",", TokenType.COMMA);
            put(".", TokenType.DOT);
            put(";", TokenType.SEMICOLON);
            put("!", TokenType.NOT);
        }
    };


    public static Map<String, TokenType> twoSignMatcher = new HashMap<>(){
        {
            put("==", TokenType.EQUAL);
            put("!=", TokenType.UNEQUAL);
            put("||", TokenType.OR);
            put("&&", TokenType.AND);
            put("<=", TokenType.LESS_OR_EQUAL);
            put(">=", TokenType.GREATER_OR_EQUAL);
        }
    };

    public static Map<String, TokenType> wordsMatcher = new HashMap<>(){
        {
            put("if", TokenType.IF);
            put("else", TokenType.ELSE);
            put("while", TokenType.WHILE);
            put("return", TokenType.RETURN);
            put("int", TokenType.INT);
            put("double", TokenType.DOUBLE);
            put("bool", TokenType.BOOL);
            put("true", TokenType.BOOL_TRUE);
            put("false", TokenType.BOOL_FALSE);
            put("print", TokenType.PRINT);
            put("amount", TokenType.AMOUNT);
        }
    };
}
