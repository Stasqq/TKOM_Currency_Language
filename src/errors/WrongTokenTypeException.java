package errors;

import token.TokenType;

import java.util.List;

public class WrongTokenTypeException extends Exception {
    public WrongTokenTypeException(int lineNumber, int position, TokenType expected, TokenType given) {
        super("Wrong token type! Line: "+lineNumber+" position: "+position+" Expected type: "+expected+", given: "+given);
    }

    public WrongTokenTypeException(int lineNumber, int position, List<TokenType> expected, TokenType given) {
        super("Wrong token type! Line: "+lineNumber+" position: "+position+" Expected type: "+expected+", given: "+given);
    }

    public WrongTokenTypeException(String exception) { super(exception); }
}
