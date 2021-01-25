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

public class ExpressionTest {
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
        //parser.progress();
    }
    @Test
    public void addIntToInt() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        IntNode expectedValue = new IntNode(2);
        initParserLexer("1 + 1");

        ExpressionNode expressionNode = (ExpressionNode) parser.expression();
        IntNode given = (IntNode) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test
    public void addDoubleToDouble() throws EnvironmentException, WrongTokenTypeException, IOException, TokenException {
        DoubleNode expectedValue = new DoubleNode(3);
        initParserLexer("1.5 + 1.5");

        ExpressionNode expressionNode = (ExpressionNode) parser.expression();
        DoubleNode given = (DoubleNode) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue(), 0);
    }

    @Test
    public void addCurrencyToCurrency() throws EnvironmentException, IOException, TokenException, WrongTokenTypeException {
        environment.addVariable("a", new Currency(currenciesTypes.get(0), BigDecimal.valueOf(1.5)));
        environment.addVariable("b", new Currency(currenciesTypes.get(0), BigDecimal.valueOf(1.5)));
        Currency expectedValue = new Currency(currenciesTypes.get(0), BigDecimal.valueOf(3));
        initParserLexer("a + b");

        ExpressionNode expressionNode = (ExpressionNode) parser.expression();
        Currency given = (Currency) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
        assertEquals(expectedValue.getCurrencyType(), given.getCurrencyType());
    }

    @Test
    public void addDoubleToInt() throws IOException, TokenException, WrongTokenTypeException, EnvironmentException {
        DoubleNode expectedValue = new DoubleNode(4.5);
        initParserLexer("1.5 + 3");

        ExpressionNode expressionNode = (ExpressionNode) parser.expression();
        DoubleNode given = (DoubleNode) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue(), 0);
    }

    @Test
    public void addIntToDouble() throws IOException, TokenException, WrongTokenTypeException, EnvironmentException {
        DoubleNode expectedValue = new DoubleNode(4.5);
        initParserLexer("1 + 3.5");

        ExpressionNode expressionNode = (ExpressionNode) parser.expression();
        DoubleNode given = (DoubleNode) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue(), 0);
    }

    @Test(expected = EnvironmentException.class)
    public void addIntToCurrency() throws EnvironmentException, IOException, TokenException, WrongTokenTypeException {
        environment.addVariable("a", new Currency(currenciesTypes.get(0), BigDecimal.valueOf(1.5)));
        initParserLexer("a + 1");

        ExpressionNode expressionNode = (ExpressionNode) parser.expression();
        expressionNode.evaluate(environment);
    }

    @Test(expected = EnvironmentException.class)
    public void addDoubleToCurrency() throws EnvironmentException, IOException, TokenException, WrongTokenTypeException {
        environment.addVariable("a", new Currency(currenciesTypes.get(0), BigDecimal.valueOf(1.5)));
        initParserLexer("a + 1.5");

        ExpressionNode expressionNode = (ExpressionNode) parser.expression();
        expressionNode.evaluate(environment);
    }

    @Test
    public void subtractIntFromInt() throws IOException, TokenException, WrongTokenTypeException, EnvironmentException {
        IntNode expectedValue = new IntNode(1);
        initParserLexer("3 - 2");

        ExpressionNode expressionNode = (ExpressionNode) parser.expression();
        IntNode given = (IntNode) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test
    public void subtractDoubleFromDouble() throws IOException, TokenException, WrongTokenTypeException, EnvironmentException {
        DoubleNode expectedValue = new DoubleNode(2);
        initParserLexer("3.5 - 1.5");

        ExpressionNode expressionNode = (ExpressionNode) parser.expression();
        DoubleNode given = (DoubleNode) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue(), 0);
    }

    @Test
    public void subtractCurrencyFromCurrency() throws EnvironmentException, IOException, TokenException, WrongTokenTypeException {
        environment.addVariable("a", new Currency(currenciesTypes.get(0), BigDecimal.valueOf(2.5)));
        environment.addVariable("b", new Currency(currenciesTypes.get(0), BigDecimal.valueOf(1.5)));
        Currency expectedValue = new Currency(currenciesTypes.get(0), BigDecimal.valueOf(1));
        initParserLexer("a - b");

        ExpressionNode expressionNode = (ExpressionNode) parser.expression();
        Currency given = (Currency) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
        assertEquals(expectedValue.getCurrencyType(), given.getCurrencyType());
    }

    @Test
    public void subtractDoubleFromInt() throws IOException, TokenException, WrongTokenTypeException, EnvironmentException {
        DoubleNode expectedValue = new DoubleNode(2.5);
        initParserLexer("3.5 - 1");

        ExpressionNode expressionNode = (ExpressionNode) parser.expression();
        DoubleNode given = (DoubleNode) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue(), 0);
    }

    @Test
    public void subtractIntFromDouble() throws IOException, TokenException, WrongTokenTypeException, EnvironmentException {
        DoubleNode expectedValue = new DoubleNode(1.5);
        initParserLexer("3 - 1.5");

        ExpressionNode expressionNode = (ExpressionNode) parser.expression();
        DoubleNode given = (DoubleNode) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue(), 0);
    }

    @Test(expected = EnvironmentException.class)
    public void subtractIntFromCurrency() throws EnvironmentException, IOException, TokenException, WrongTokenTypeException {
        environment.addVariable("a", new Currency(currenciesTypes.get(0), BigDecimal.valueOf(1.5)));
        initParserLexer("a - 1");

        ExpressionNode expressionNode = (ExpressionNode) parser.expression();
        expressionNode.evaluate(environment);
    }

    @Test(expected = EnvironmentException.class)
    public void subtractDoubleFromCurrency() throws EnvironmentException, IOException, TokenException, WrongTokenTypeException {
        environment.addVariable("a", new Currency(currenciesTypes.get(0), BigDecimal.valueOf(1.5)));
        initParserLexer("a - 1.5");

        ExpressionNode expressionNode = (ExpressionNode) parser.expression();
        expressionNode.evaluate(environment);
    }

    @Test
    public void multiplyIntByInt() throws IOException, TokenException, WrongTokenTypeException, EnvironmentException {
        IntNode expectedValue = new IntNode(6);
        initParserLexer("2 * 3");

        ExpressionNode expressionNode = (ExpressionNode) parser.expression();
        IntNode given = (IntNode) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test
    public void multiplyDoubleByDouble() throws IOException, TokenException, WrongTokenTypeException, EnvironmentException {
        DoubleNode expectedValue = new DoubleNode(1.5 * 3.5);
        initParserLexer("1.5 * 3.5");

        ExpressionNode expressionNode = (ExpressionNode) parser.expression();
        DoubleNode given = (DoubleNode) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue(), 0);
    }

    @Test(expected = EnvironmentException.class)
    public void multiplyCurrencyByCurrency() throws EnvironmentException, IOException, TokenException, WrongTokenTypeException {
        environment.addVariable("a", new Currency(currenciesTypes.get(0), BigDecimal.valueOf(1.5)));
        environment.addVariable("b", new Currency(currenciesTypes.get(0), BigDecimal.valueOf(3.5)));
        initParserLexer("a * b");

        ExpressionNode expressionNode = (ExpressionNode) parser.expression();
        expressionNode.evaluate(environment);
    }

    @Test
    public void multiplyDoubleByInt() throws IOException, TokenException, WrongTokenTypeException, EnvironmentException {
        DoubleNode expectedValue = new DoubleNode(1.5 * 3);
        initParserLexer("1.5 * 3");

        ExpressionNode expressionNode = (ExpressionNode) parser.expression();
        DoubleNode given = (DoubleNode) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue(), 0);
    }

    @Test
    public void multiplyIntByDouble() throws IOException, TokenException, WrongTokenTypeException, EnvironmentException {
        DoubleNode expectedValue = new DoubleNode(1 * 3.5);
        initParserLexer("1 * 3.5");

        ExpressionNode expressionNode = (ExpressionNode) parser.expression();
        DoubleNode given = (DoubleNode) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue(), 0);
    }

    @Test
    public void multiplyIntByCurrency() throws EnvironmentException, IOException, TokenException, WrongTokenTypeException {
        environment.addVariable("a", new Currency(currenciesTypes.get(0), BigDecimal.valueOf(1.5)));
        Currency expectedValue = new Currency(currenciesTypes.get(0), BigDecimal.valueOf(3));
        initParserLexer("a * 2");

        ExpressionNode expressionNode = (ExpressionNode) parser.expression();
        Currency given = (Currency) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
        assertEquals(expectedValue.getCurrencyType(), given.getCurrencyType());
    }

    @Test
    public void multiplyDoubleByCurrency() throws EnvironmentException, IOException, TokenException, WrongTokenTypeException {
        environment.addVariable("a", new Currency(currenciesTypes.get(0), BigDecimal.valueOf(2)));
        Currency expectedValue = new Currency(currenciesTypes.get(0), BigDecimal.valueOf(5));
        initParserLexer("a * 2.5");

        ExpressionNode expressionNode = (ExpressionNode) parser.expression();
        Currency given = (Currency) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
        assertEquals(expectedValue.getCurrencyType(), given.getCurrencyType());
    }

    @Test
    public void divideIntByInt() throws IOException, TokenException, WrongTokenTypeException, EnvironmentException {
        IntNode expectedValue = new IntNode(0);
        initParserLexer("2 / 3");

        ExpressionNode expressionNode = (ExpressionNode) parser.expression();
        IntNode given = (IntNode) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test
    public void divideDoubleByDouble() throws IOException, TokenException, WrongTokenTypeException, EnvironmentException {
        DoubleNode expectedValue = new DoubleNode(1.5 / 3.5);
        initParserLexer("1.5 / 3.5");

        ExpressionNode expressionNode = (ExpressionNode) parser.expression();
        DoubleNode given = (DoubleNode) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue(), 0);
    }

    @Test(expected = EnvironmentException.class)
    public void divideCurrencyByCurrency() throws EnvironmentException, IOException, TokenException, WrongTokenTypeException {
        environment.addVariable("a", new Currency(currenciesTypes.get(0), BigDecimal.valueOf(1.5)));
        environment.addVariable("b", new Currency(currenciesTypes.get(0), BigDecimal.valueOf(3.5)));
        initParserLexer("a / b");

        ExpressionNode expressionNode = (ExpressionNode) parser.expression();
        expressionNode.evaluate(environment);
    }

    @Test
    public void divideDoubleByInt() throws IOException, TokenException, WrongTokenTypeException, EnvironmentException {
        DoubleNode expectedValue = new DoubleNode(1.5 / 3);
        initParserLexer("1.5 / 3");

        ExpressionNode expressionNode = (ExpressionNode) parser.expression();
        DoubleNode given = (DoubleNode) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue(), 0);
    }

    @Test
    public void divideIntByDouble() throws IOException, TokenException, WrongTokenTypeException, EnvironmentException {
        DoubleNode expectedValue = new DoubleNode(1 / 3.5);
        initParserLexer("1 / 3.5");

        ExpressionNode expressionNode = (ExpressionNode) parser.expression();
        DoubleNode given = (DoubleNode) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue(), 0);
    }

    @Test
    public void divideCurrencyByInt() throws EnvironmentException, IOException, TokenException, WrongTokenTypeException {
        environment.addVariable("a", new Currency(currenciesTypes.get(0), BigDecimal.valueOf(1.5)));
        Currency expectedValue = new Currency(currenciesTypes.get(0), BigDecimal.valueOf(0.5));
        initParserLexer("a / 3");

        ExpressionNode expressionNode = (ExpressionNode) parser.expression();
        Currency given = (Currency) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
        assertEquals(expectedValue.getCurrencyType(), given.getCurrencyType());
    }

    @Test
    public void divideCurrencyByDouble() throws EnvironmentException, IOException, TokenException, WrongTokenTypeException {
        environment.addVariable("a", new Currency(currenciesTypes.get(0), BigDecimal.valueOf(2.5)));
        Currency expectedValue = new Currency(currenciesTypes.get(0), BigDecimal.valueOf(1));
        initParserLexer("a / 2.5");

        ExpressionNode expressionNode = (ExpressionNode) parser.expression();
        Currency given = (Currency) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
        assertEquals(expectedValue.getCurrencyType(), given.getCurrencyType());
    }

    @Test
    public void correctArithmeticOrder() throws IOException, TokenException, WrongTokenTypeException, EnvironmentException {
        IntNode expectedValue = new IntNode(5);
        initParserLexer("2 / 2 + 2 * 2");

        ExpressionNode expressionNode = (ExpressionNode) parser.expression();
        IntNode given = (IntNode) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test
    public void correctArithmeticOrderWithParenthesis() throws IOException, TokenException, WrongTokenTypeException, EnvironmentException {
        IntNode expectedValue = new IntNode(4);
        initParserLexer("(2 + 2) * 2 / 2");

        ExpressionNode expressionNode = (ExpressionNode) parser.expression();
        IntNode given = (IntNode) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test
    public void expressionWithFunctionCall() throws IOException, TokenException, WrongTokenTypeException, EnvironmentException {
        initParserLexer("int test() { return 2;}");
        parser.progress();
        Function function = parser.function();
        environment.addFunction(function);
        IntNode expectedValue = new IntNode(4);
        initParserLexer("test() + test() * test() / test()");

        ExpressionNode expressionNode = (ExpressionNode) parser.expression();
        IntNode given = (IntNode) expressionNode.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test(expected = EnvironmentException.class)
    public void divideIntByZero() throws IOException, TokenException, WrongTokenTypeException, EnvironmentException {
        initParserLexer("2 / 0");

        ExpressionNode expressionNode = (ExpressionNode) parser.expression();
        expressionNode.evaluate(environment);
    }

    @Test(expected = EnvironmentException.class)
    public void divideDoubleByZero() throws IOException, TokenException, WrongTokenTypeException, EnvironmentException {
        initParserLexer("2.5 / 0");

        ExpressionNode expressionNode = (ExpressionNode) parser.expression();
        expressionNode.evaluate(environment);
    }

    @Test(expected = EnvironmentException.class)
    public void divideDoubleByCurrency() throws IOException, TokenException, WrongTokenTypeException, EnvironmentException {
        environment.addVariable("a", new Currency(currenciesTypes.get(0), BigDecimal.valueOf(2.5)));
        initParserLexer("2.5 / a");

        ExpressionNode expressionNode = (ExpressionNode) parser.expression();
        expressionNode.evaluate(environment);
    }

    @Test(expected = EnvironmentException.class)
    public void divideIntByCurrency() throws IOException, TokenException, WrongTokenTypeException, EnvironmentException {
        environment.addVariable("a", new Currency(currenciesTypes.get(0), BigDecimal.valueOf(2.5)));
        initParserLexer("2 / a");

        ExpressionNode expressionNode = (ExpressionNode) parser.expression();
        expressionNode.evaluate(environment);
    }

    @Test(expected = EnvironmentException.class)
    public void divideCurrencyByZero() throws IOException, TokenException, WrongTokenTypeException, EnvironmentException {
        environment.addVariable("a", new Currency(currenciesTypes.get(0), BigDecimal.valueOf(2.5)));
        initParserLexer("a / 0.");

        ExpressionNode expressionNode = (ExpressionNode) parser.expression();
        expressionNode.evaluate(environment);
    }
}
