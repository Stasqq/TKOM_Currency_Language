package token;

import java.math.BigDecimal;

public class Token {
    //token info, value
    private TokenType tokenType;
    private String stringValue;
    private BigDecimal decimalValue;

    //token position
    private int lineNumber;
    private int positionInLine;

    public Token(int lineNumber, int positionInLine)
    {
        this.lineNumber = lineNumber;
        this.positionInLine = positionInLine;
    }

    public void setToken(TokenType tokenType, String stringValue)
    {
        this.tokenType = tokenType;
        this.stringValue = stringValue;

        if(tokenType == TokenType.NUMBER)
            this.decimalValue = new BigDecimal(stringValue);
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getPositionInLine() {
        return positionInLine;
    }

    public void setPositionInLine(int positionInLine) {
        this.positionInLine = positionInLine;
    }
}
