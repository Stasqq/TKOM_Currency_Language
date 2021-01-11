package lexer;

import errors.TokenException;
import inputReaders.CodeReader;
import token.Token;
import token.TokenType;
import token.TokenTypeMatcher;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private CodeReader codeReader;
    private String string;
    private Character character;
    private Token token;
    private List<String> currencies;

    public Lexer(Reader reader, List<String> currencies)
    {
        this.codeReader = new CodeReader(reader);
        if(currencies == null)
            this.currencies = new ArrayList<>();
        else
            this.currencies = currencies;
    }

    public Token getToken() { return token; }

    public Token getNextToken() throws IOException, TokenException {
        jumpToFirstCharacter();
        readToken();
        handleToken();
        return token;
    }

    private void jumpToFirstCharacter() throws IOException {
        int c;
        do{
            c = codeReader.getCharacter();
        }while(Character.isWhitespace(c));
        codeReader.undo();
    }

    private void readToken() throws IOException {
        character = codeReader.getCharacter();
        string = "";
        token = new Token(codeReader.getReaderLineNumber(), codeReader.getCharacterPosition());
    }

    private void handleToken() throws IOException, TokenException {
        if(character == CodeReader.EOF_CHAR)
            token.setTokenType(TokenType.EOF);
        else if(Character.isLetter(character))
            parseLetterToken();
        else if(Character.isDigit(character))
            token.setToken(TokenType.NUMBER, getNumber());
        else if(character == '"')
            token.setToken(TokenType.STRING, getString());
        else
            parseSymbolToken();
    }

    private void parseLetterToken() throws IOException {
        String phrase = getPhrase();
        if(TokenTypeMatcher.wordsMatcher.containsKey(phrase))
            token.setToken(TokenTypeMatcher.wordsMatcher.get(phrase), phrase);
        else if(currencies.contains(phrase))
            token.setToken(TokenType.CURRENCY, phrase);
        else
            token.setToken(TokenType.ID, phrase);
    }


    private void parseSymbolToken() throws IOException, TokenException {
        if(TokenTypeMatcher.twoSignMatcher.containsKey(character.toString() + codeReader.checkNextCharacter().toString()))
            token.setTokenType(TokenTypeMatcher.twoSignMatcher.get(character.toString() + codeReader.getCharacter()));
        else if(TokenTypeMatcher.oneSignMatcher.containsKey(character.toString()))
            token.setTokenType(TokenTypeMatcher.oneSignMatcher.get(character.toString()));
        else{
            throw new TokenException(token.getLineNumber(), token.getPositionInLine());
        }
    }

    private String getPhrase() throws IOException {
        do{
            readToString();
        }while(Character.isLetterOrDigit(character));
        codeReader.undo();
        return string;
    }

    private String getNumber() throws IOException, TokenException {
        do{
            readToString();
        }while(Character.isDigit(character) || character == '.');
        codeReader.undo();

        if(isNumberCorrect(string))
            return string;
        else
            throw new TokenException(token.getLineNumber(), token.getPositionInLine());
    }

    private String getString() throws IOException, TokenException {
        do{
            readToString();
        }while((Character.isLetterOrDigit(character) || Character.isWhitespace(character)) && character != '"');
        string += character;

        if(isStringCorrect())
            return string;
        else
            throw new TokenException(token.getLineNumber(), token.getPositionInLine());
    }

    private void readToString() throws IOException {
        string += character;
        character = codeReader.getCharacter();
    }

    private boolean isNumberCorrect(String numberToCheck)
    {
        try
        {
            Double.parseDouble(numberToCheck);
            return true;
        }
        catch(NumberFormatException e)
        {
            return false;
        }
    }

    private boolean isStringCorrect()
    {
        return string.charAt(string.length() - 1) != '"';
    }

}
