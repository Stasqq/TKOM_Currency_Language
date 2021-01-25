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

public class FunctionTest {
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

    @Test(expected = EnvironmentException.class)
    public void functionWithoutReturn() throws IOException, TokenException, WrongTokenTypeException, EnvironmentException {
        IntNode expectedValue = new IntNode(0);
        initParserLexer("int test() {}");

        Function function = parser.function();
        IntNode given = (IntNode) function.execute(environment, new ArrayList<>());

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test
    public void doubleFunctionReturningInt() throws IOException, TokenException, WrongTokenTypeException, EnvironmentException {
        DoubleNode expectedValue = new DoubleNode(0);
        initParserLexer("double test() {return 0;}");

        Function function = parser.function();
        DoubleNode given = (DoubleNode) function.execute(environment, new ArrayList<>());

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue(), 0);
    }

    @Test
    public void returningFunctionArgument() throws IOException, TokenException, WrongTokenTypeException, EnvironmentException {
        DoubleNode expectedValue = new DoubleNode(5);
        initParserLexer("double test(double d) {return d;}");

        Function function = parser.function();
        DoubleNode given = (DoubleNode) function.execute(environment, new ArrayList<>() {{
            add(expectedValue);
        }});

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue(), 0);
    }

    @Test(expected = EnvironmentException.class)
    public void wrongFunctionArgument() throws IOException, TokenException, WrongTokenTypeException, EnvironmentException {
        initParserLexer("int test(int i) {return i;}");
        Function function = parser.function();
        environment.addFunction(function);
        initParserLexer("test(1.5);");

        FunctionCall functionCall = (FunctionCall) parser.statement();
        functionCall.execute(environment);
    }

    @Test
    public void functionWithCurrencyArgument() throws EnvironmentException, WrongTokenTypeException, IOException, TokenException {
        Currency expectedValue = new Currency(currenciesTypes.get(0), BigDecimal.valueOf(1));
        environment.addVariable("euro", expectedValue);
        initParserLexer("EUR test(EUR e) {return e;}");
        Function function = parser.function();
        environment.addFunction(function);
        initParserLexer("test(euro);");

        FunctionCall functionCall = (FunctionCall) parser.statement();
        Currency given = (Currency) functionCall.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
        assertEquals(expectedValue.getCurrencyType(), given.getCurrencyType());
    }

    @Test
    public void functionCallArgumentIntParameterDouble() throws EnvironmentException, WrongTokenTypeException, IOException, TokenException {
        DoubleNode expectedValue = new DoubleNode(0);
        initParserLexer("double test(double d) {return d;}");
        Function function = parser.function();
        environment.addFunction(function);
        initParserLexer("test(0);");

        FunctionCall functionCall = (FunctionCall) parser.statement();
        DoubleNode given = (DoubleNode) functionCall.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue(), 0);
    }

    @Test(expected = EnvironmentException.class)
    public void wrongArgumentNumber() throws WrongTokenTypeException, IOException, TokenException, EnvironmentException {
        initParserLexer("double test(double d, double c) {return d;}");
        Function function = parser.function();
        environment.addFunction(function);
        initParserLexer("test(0);");

        FunctionCall functionCall = (FunctionCall) parser.statement();
        functionCall.execute(environment);
    }

    @Test(expected = EnvironmentException.class)
    public void notExistingFunctionCall() throws EnvironmentException, WrongTokenTypeException, IOException, TokenException {
        initParserLexer("test(0);");

        FunctionCall functionCall = (FunctionCall) parser.statement();
        functionCall.execute(environment);
    }

    @Test(expected = EnvironmentException.class)
    public void wrongReturnType() throws EnvironmentException, WrongTokenTypeException, IOException, TokenException {
        initParserLexer("int test(double d) {return d;}");
        Function function = parser.function();
        environment.addFunction(function);
        initParserLexer("test(0);");

        FunctionCall functionCall = (FunctionCall) parser.statement();
        functionCall.execute(environment);
    }
}
