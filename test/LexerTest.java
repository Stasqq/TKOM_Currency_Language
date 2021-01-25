import errors.TokenException;
import inputReaders.CurrenciesScanner;
import lexer.Lexer;
import org.junit.Assert;
import org.junit.Test;
import token.TokenType;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class LexerTest {

    @Test
    public void intTest() throws IOException, TokenException {
        Lexer lexer = new Lexer(new StringReader("int"), null);
        Assert.assertEquals(TokenType.INT, lexer.getNextToken().getTokenType());
    }

    @Test
    public void variableInitTest() throws IOException, TokenException {
        ArrayList<String> testList = new ArrayList<>();
        testList.add("EUR");
        Lexer lexer = new Lexer(new StringReader("EUR eur = 1.53;"), testList);
        Assert.assertEquals(TokenType.CURRENCY, lexer.getNextToken().getTokenType());
        Assert.assertEquals(TokenType.ID, lexer.getNextToken().getTokenType());
        Assert.assertEquals(TokenType.ASSIGMENT, lexer.getNextToken().getTokenType());
        Assert.assertEquals(TokenType.NUMBER, lexer.getNextToken().getTokenType());
        Assert.assertEquals(TokenType.SEMICOLON, lexer.getNextToken().getTokenType());
    }

    @Test
    public void boolTest() throws IOException, TokenException {
        Lexer lexer = new Lexer(new StringReader("bool condition = false, second = true"), null);
        Assert.assertEquals(TokenType.BOOL, lexer.getNextToken().getTokenType());
        Assert.assertEquals(TokenType.ID, lexer.getNextToken().getTokenType());
        Assert.assertEquals(TokenType.ASSIGMENT, lexer.getNextToken().getTokenType());
        Assert.assertEquals(TokenType.BOOL_FALSE, lexer.getNextToken().getTokenType());
        Assert.assertEquals(TokenType.COMMA, lexer.getNextToken().getTokenType());
        Assert.assertEquals(TokenType.ID, lexer.getNextToken().getTokenType());
        Assert.assertEquals(TokenType.ASSIGMENT, lexer.getNextToken().getTokenType());
        Assert.assertEquals(TokenType.BOOL_TRUE, lexer.getNextToken().getTokenType());
    }

    @Test
    public void currencyTest() throws IOException, TokenException {
        CurrenciesScanner currenciesScanner = new CurrenciesScanner("inputs/currencies.txt");
        List<String> currenciesNames = currenciesScanner.readCurrenciesNameList();
        Lexer lexer = new Lexer(new StringReader("EUR  PLN  USD  GBP  CHF  RUB"), currenciesNames);
        Assert.assertEquals(TokenType.CURRENCY, lexer.getNextToken().getTokenType());
        Assert.assertEquals(TokenType.CURRENCY, lexer.getNextToken().getTokenType());
        Assert.assertEquals(TokenType.CURRENCY, lexer.getNextToken().getTokenType());
        Assert.assertEquals(TokenType.CURRENCY, lexer.getNextToken().getTokenType());
        Assert.assertEquals(TokenType.CURRENCY, lexer.getNextToken().getTokenType());
        Assert.assertEquals(TokenType.CURRENCY, lexer.getNextToken().getTokenType());
    }

    @Test
    public void operationTest() throws IOException, TokenException {
        ArrayList<String> testList = new ArrayList<>();
        testList.add("EUR");
        Lexer lexer = new Lexer(new StringReader("EUR eur = 15 + x / 2"), testList);
        Assert.assertEquals(TokenType.CURRENCY, lexer.getNextToken().getTokenType());
        Assert.assertEquals(TokenType.ID, lexer.getNextToken().getTokenType());
        Assert.assertEquals(TokenType.ASSIGMENT, lexer.getNextToken().getTokenType());
        Assert.assertEquals(TokenType.NUMBER, lexer.getNextToken().getTokenType());
        Assert.assertEquals(TokenType.PLUS, lexer.getNextToken().getTokenType());
        Assert.assertEquals(TokenType.ID, lexer.getNextToken().getTokenType());
        Assert.assertEquals(TokenType.DIVIDE, lexer.getNextToken().getTokenType());
        Assert.assertEquals(TokenType.NUMBER, lexer.getNextToken().getTokenType());
    }

    @Test
    public void EOFTest() throws IOException, TokenException {
        Lexer lexer = new Lexer(new StringReader(""), null);
        Assert.assertEquals(TokenType.EOF, lexer.getNextToken().getTokenType());
    }

    @Test
    public void functionAssingTest() throws IOException, TokenException {
        ArrayList<String> testList = new ArrayList<>();
        testList.add("EUR");
        Lexer lexer = new Lexer(new StringReader("currency = subCurrency(dollars);"), testList);
        Assert.assertEquals(TokenType.ID, lexer.getNextToken().getTokenType());
        Assert.assertEquals(TokenType.ASSIGMENT, lexer.getNextToken().getTokenType());
        Assert.assertEquals(TokenType.ID, lexer.getNextToken().getTokenType());
        Assert.assertEquals(TokenType.OPEN_ROUND_BRACKET, lexer.getNextToken().getTokenType());
        Assert.assertEquals(TokenType.ID, lexer.getNextToken().getTokenType());
        Assert.assertEquals(TokenType.CLOSE_ROUND_BRACKET, lexer.getNextToken().getTokenType());
        Assert.assertEquals(TokenType.SEMICOLON, lexer.getNextToken().getTokenType());
    }
}
