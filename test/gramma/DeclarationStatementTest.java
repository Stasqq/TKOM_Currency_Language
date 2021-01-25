package gramma;

import currency.CurrencyType;
import environment.Environment;
import errors.CurrenciesFileException;
import errors.EnvironmentException;
import errors.TokenException;
import errors.WrongTokenTypeException;
import gramma.elementary.*;
import inputReaders.CurrenciesScanner;
import lexer.Lexer;
import org.junit.Before;
import org.junit.Test;
import parser.Parser;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DeclarationStatementTest {
    private Parser parser;
    private Environment environment;
    private List<Function> functions;
    private List<String> currenciesNames;
    private List<CurrencyType> currenciesTypes;

    @Before
    public void initEnvironment() throws CurrenciesFileException, EnvironmentException {
        this.functions = new ArrayList<>() {{
            add(new Function("int", "main"));
        }};
        CurrenciesScanner currenciesScanner = new CurrenciesScanner("inputs/currencies.txt");
        currenciesNames = currenciesScanner.readCurrenciesNameList();
        currenciesTypes = currenciesScanner.readCurrencies();
        environment = new Environment(functions, currenciesTypes);
        environment.createNewRange();
    }

    private void initParserLexer(String inputString) throws IOException, TokenException {
        Lexer lexer = new Lexer(new StringReader(inputString), currenciesNames);
        this.parser = new Parser(lexer);
        parser.progress();
    }

    @Test
    public void declareInt() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        IntNode expectedValue = new IntNode(0);
        initParserLexer("int a = 0;");

        DeclarationStatement initStatement = (DeclarationStatement) parser.statement();
        initStatement.execute(environment);
        IntNode given = (IntNode) environment.getVariable("a");

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test
    public void declareDouble() throws IOException, TokenException, WrongTokenTypeException, EnvironmentException {
        DoubleNode expectedValue = new DoubleNode(0.5);
        initParserLexer("double a = 0.5;");

        DeclarationStatement initStatement = (DeclarationStatement) parser.statement();
        initStatement.execute(environment);
        DoubleNode given = (DoubleNode) environment.getVariable("a");

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue(), 0);
    }

    @Test
    public void declareCurrencyWithInt() throws IOException, TokenException, WrongTokenTypeException, EnvironmentException {
        Currency expectedValue = new Currency(currenciesTypes.get(0), BigDecimal.valueOf(1));
        initParserLexer("EUR a = 1;");

        DeclarationStatement initStatement = (DeclarationStatement) parser.statement();
        initStatement.execute(environment);
        Currency given = (Currency) environment.getVariable("a");

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
        assertEquals(expectedValue.getCurrencyType(), given.getCurrencyType());
    }

    @Test
    public void declareCurrencyWithDouble() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        Currency expectedValue = new Currency(currenciesTypes.get(0), BigDecimal.valueOf(0.5));
        initParserLexer("EUR a = 0.5;");

        DeclarationStatement initStatement = (DeclarationStatement) parser.statement();
        initStatement.execute(environment);
        Currency given = (Currency) environment.getVariable("a");

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
        assertEquals(expectedValue.getCurrencyType(), given.getCurrencyType());
    }

    @Test
    public void declareIntWithoutValue() throws IOException, TokenException, WrongTokenTypeException, EnvironmentException {
        IntNode expectedValue = new IntNode(0);
        initParserLexer("int a;");

        DeclarationStatement initStatement = (DeclarationStatement) parser.statement();
        initStatement.execute(environment);
        IntNode given = (IntNode) environment.getVariable("a");

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test
    public void declareDoubleWithoutValue() throws IOException, TokenException, WrongTokenTypeException, EnvironmentException {
        DoubleNode expectedValue = new DoubleNode(0);
        initParserLexer("double a;");

        DeclarationStatement initStatement = (DeclarationStatement) parser.statement();
        initStatement.execute(environment);
        DoubleNode given = (DoubleNode) environment.getVariable("a");

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue(), 0);
    }

    @Test
    public void declareCurrencyWithoutValue() throws WrongTokenTypeException, IOException, TokenException, EnvironmentException {
        Currency expectedValue = new Currency(currenciesTypes.get(0), BigDecimal.valueOf(0));
        initParserLexer("EUR a;");

        DeclarationStatement initStatement = (DeclarationStatement) parser.statement();
        initStatement.execute(environment);
        Currency given = (Currency) environment.getVariable("a");

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
        assertEquals(expectedValue.getCurrencyType(), given.getCurrencyType());
    }

    @Test
    public void declareDoubleWithInt() throws IOException, TokenException, WrongTokenTypeException, EnvironmentException {
        DoubleNode expectedValue = new DoubleNode(1);
        initParserLexer("double a = 1;");

        DeclarationStatement initStatement = (DeclarationStatement) parser.statement();
        initStatement.execute(environment);
        DoubleNode given = (DoubleNode) environment.getVariable("a");

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue(), 0);
    }

    @Test(expected = EnvironmentException.class)
    public void declareIntWithDouble() throws WrongTokenTypeException, IOException, TokenException, EnvironmentException {
        initParserLexer("int a = 10.5;");

        DeclarationStatement initStatement = (DeclarationStatement) parser.statement();
        initStatement.execute(environment);
    }

    @Test
    public void declareIntWithDifferentIntVariable() throws EnvironmentException, IOException, TokenException, WrongTokenTypeException {
        IntNode expectedValue = new IntNode(1);
        environment.addVariable("b", expectedValue);
        initParserLexer("int a = b;");

        DeclarationStatement initStatement = (DeclarationStatement) parser.statement();
        initStatement.execute(environment);
        IntNode given = (IntNode) environment.getVariable("a");

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test
    public void declareCurrencyWithDifferentCurrencyVariable() throws EnvironmentException, IOException, TokenException, WrongTokenTypeException {
        Currency expectedValue = new Currency(currenciesTypes.get(0), BigDecimal.valueOf(1));
        environment.addVariable("b", expectedValue);
        initParserLexer("EUR a = b;");

        DeclarationStatement initStatement = (DeclarationStatement) parser.statement();
        initStatement.execute(environment);
        Currency given = (Currency) environment.getVariable("a");

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
        assertEquals(expectedValue.getCurrencyType(), given.getCurrencyType());
    }
}
