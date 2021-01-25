package gramma;

import currency.CurrencyType;
import environment.Environment;
import errors.CurrenciesFileException;
import errors.EnvironmentException;
import errors.TokenException;
import errors.WrongTokenTypeException;
import gramma.elementary.BoolNode;
import gramma.elementary.Condition;
import gramma.elementary.Currency;
import gramma.elementary.Function;
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

public class ConditionTest {
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
    public void isEqualIntToInt() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        BoolNode expectedValue = new BoolNode(true);
        initParserLexer("5 == 5");
        Condition condition = parser.condition();

        BoolNode given = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test
    public void isEqualDoubleToDouble() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        BoolNode expectedValue = new BoolNode(true);
        initParserLexer("5.5 == 5.5");
        Condition condition = parser.condition();

        BoolNode given = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test
    public void isEqualCurrencyToCurrency() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        environment.addVariable("a", new Currency(currenciesTypes.get(0), BigDecimal.valueOf(5)));
        environment.addVariable("b", new Currency(currenciesTypes.get(0), BigDecimal.valueOf(5)));
        BoolNode expectedValue = new BoolNode(true);
        initParserLexer("a == b");
        Condition condition = parser.condition();

        BoolNode given = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test
    public void isEqualIntToDouble() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        BoolNode expectedValue = new BoolNode(true);
        initParserLexer("5 == 5.");
        Condition condition = parser.condition();

        BoolNode given = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test
    public void isEqualDoubleToInt() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        BoolNode expectedValue = new BoolNode(false);
        initParserLexer("5.5 == 5");
        Condition condition = parser.condition();

        BoolNode given = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test(expected = EnvironmentException.class)
    public void isEqualCurrencyToInt() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        environment.addVariable("a", new Currency(currenciesTypes.get(0), BigDecimal.valueOf(5)));
        initParserLexer("a == 5");
        Condition condition = parser.condition();

        condition.evaluate(environment);
    }

    @Test
    public void isEqualBoolToBool() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        BoolNode expectedValue = new BoolNode(true);
        initParserLexer(" 1 < 2 == 2 < 3");
        Condition condition = parser.condition();

        BoolNode given = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test
    public void isNotEqual() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        BoolNode expectedValue = new BoolNode(true);
        initParserLexer("5 != 4");
        Condition condition = parser.condition();

        BoolNode given = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test
    public void isLessIntThanInt() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        BoolNode expectedValue = new BoolNode(true);
        initParserLexer("4 < 5");
        Condition condition = parser.condition();

        BoolNode given = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test
    public void isLessDoubleThanDouble() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        BoolNode expectedValue = new BoolNode(true);
        initParserLexer("4.5 < 5.5");
        Condition condition = parser.condition();

        BoolNode given = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test
    public void isLessCurrencyThanCurrency() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        environment.addVariable("a", new Currency(currenciesTypes.get(0), BigDecimal.valueOf(4)));
        environment.addVariable("b", new Currency(currenciesTypes.get(0), BigDecimal.valueOf(5)));
        BoolNode expectedValue = new BoolNode(true);
        initParserLexer("a < b");
        Condition condition = parser.condition();

        BoolNode given = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test
    public void isLessIntThanDouble() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        BoolNode expectedValue = new BoolNode(true);
        initParserLexer("4 < 5.");
        Condition condition = parser.condition();

        BoolNode given = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test
    public void isLessDoubleThanInt() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        BoolNode expectedValue = new BoolNode(true);
        initParserLexer("2.5 < 5");
        Condition condition = parser.condition();

        BoolNode given = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test(expected = EnvironmentException.class)
    public void isLessCurrencyThanInt() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        environment.addVariable("a", new Currency(currenciesTypes.get(0), BigDecimal.valueOf(5)));
        initParserLexer("a < 5");
        Condition condition = parser.condition();

        condition.evaluate(environment);
    }

    @Test(expected = EnvironmentException.class)
    public void isLessBoolThanBool() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        initParserLexer(" (1 < 2) < (2 < 3)");
        Condition condition = parser.condition();

        condition.evaluate(environment);
    }

    @Test
    public void isLessOrEqualIntThanInt() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        BoolNode expectedValue = new BoolNode(true);
        initParserLexer("5 <= 5");
        Condition condition = parser.condition();

        BoolNode given = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test
    public void isLessOrEqualDoubleThanDouble() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        BoolNode expectedValue = new BoolNode(true);
        initParserLexer("4.5 <= 5.5");
        Condition condition = parser.condition();

        BoolNode given = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test
    public void isLessOrEqualCurrencyThanCurrency() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        environment.addVariable("a", new Currency(currenciesTypes.get(0), BigDecimal.valueOf(4)));
        environment.addVariable("b", new Currency(currenciesTypes.get(0), BigDecimal.valueOf(5)));
        BoolNode expectedValue = new BoolNode(true);
        initParserLexer("a <= b");
        Condition condition = parser.condition();

        BoolNode given = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test
    public void isLessOrEqualIntThanDouble() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        BoolNode expectedValue = new BoolNode(true);
        initParserLexer("4 <= 4.");
        Condition condition = parser.condition();

        BoolNode given = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test
    public void isLessOrEqualDoubleThanInt() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        BoolNode expectedValue = new BoolNode(true);
        initParserLexer("5. <= 5");
        Condition condition = parser.condition();

        BoolNode given = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test(expected = EnvironmentException.class)
    public void isLessOrEqualCurrencyThanInt() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        environment.addVariable("a", new Currency(currenciesTypes.get(0), BigDecimal.valueOf(5)));
        initParserLexer("a <= 5");
        Condition condition = parser.condition();

        condition.evaluate(environment);
    }

    @Test(expected = EnvironmentException.class)
    public void isLessOrEqualBoolThanBool() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        initParserLexer(" (1 < 2) <= (2 < 3)");
        Condition condition = parser.condition();

        condition.evaluate(environment);
    }

    @Test
    public void isGraterOrEqualIntThanInt() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        BoolNode expectedValue = new BoolNode(false);
        initParserLexer("4 >= 5");
        Condition condition = parser.condition();

        BoolNode given = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test
    public void isGraterOrEqualDoubleThanDouble() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        BoolNode expectedValue = new BoolNode(false);
        initParserLexer("4.5 >= 5.5");
        Condition condition = parser.condition();

        BoolNode given = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test
    public void isGraterOrEqualCurrencyThanCurrency() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        environment.addVariable("a", new Currency(currenciesTypes.get(0), BigDecimal.valueOf(4)));
        environment.addVariable("b", new Currency(currenciesTypes.get(0), BigDecimal.valueOf(5)));
        BoolNode expectedValue = new BoolNode(false);
        initParserLexer("a >= b");
        Condition condition = parser.condition();

        BoolNode given = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test
    public void isGraterOrEqualIntThanDouble() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        BoolNode expectedValue = new BoolNode(false);
        initParserLexer("4 >= 5.");
        Condition condition = parser.condition();

        BoolNode given = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test
    public void isGraterOrEqualDoubleThanInt() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        BoolNode expectedValue = new BoolNode(false);
        initParserLexer("4. >= 5");
        Condition condition = parser.condition();

        BoolNode given = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test(expected = EnvironmentException.class)
    public void isGraterOrEqualCurrencyThanInt() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        environment.addVariable("a", new Currency(currenciesTypes.get(0), BigDecimal.valueOf(5)));
        initParserLexer("a >= 5");
        Condition condition = parser.condition();

        condition.evaluate(environment);
    }

    @Test(expected = EnvironmentException.class)
    public void isGraterOrEqualBoolThanBool() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        initParserLexer(" (1 < 2) >= (2 < 3)");
        Condition condition = parser.condition();

        condition.evaluate(environment);
    }

    @Test
    public void isGraterIntThanInt() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        BoolNode expectedValue = new BoolNode(false);
        initParserLexer("4 > 5");
        Condition condition = parser.condition();

        BoolNode given = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test
    public void isGraterDoubleThanDouble() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        BoolNode expectedValue = new BoolNode(false);
        initParserLexer("4.5 > 5.5");
        Condition condition = parser.condition();

        BoolNode given = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test
    public void isGraterCurrencyThanCurrency() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        environment.addVariable("a", new Currency(currenciesTypes.get(0), BigDecimal.valueOf(4)));
        environment.addVariable("b", new Currency(currenciesTypes.get(0), BigDecimal.valueOf(5)));
        BoolNode expectedValue = new BoolNode(false);
        initParserLexer("a > b");
        Condition condition = parser.condition();

        BoolNode given = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test
    public void isGraterIntThanDouble() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        BoolNode expectedValue = new BoolNode(false);
        initParserLexer("4 > 5.");
        Condition condition = parser.condition();

        BoolNode given = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test
    public void isGraterDoubleThanInt() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        BoolNode expectedValue = new BoolNode(false);
        initParserLexer("4. > 5");
        Condition condition = parser.condition();

        BoolNode given = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test(expected = EnvironmentException.class)
    public void isGraterCurrencyThanInt() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        environment.addVariable("a", new Currency(currenciesTypes.get(0), BigDecimal.valueOf(5)));
        initParserLexer("a > 5");
        Condition condition = parser.condition();

        condition.evaluate(environment);
    }

    @Test(expected = EnvironmentException.class)
    public void isGraterBoolThanBool() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        initParserLexer(" (1 < 2) > (2 < 3)");
        Condition condition = parser.condition();

        condition.evaluate(environment);
    }

    @Test
    public void andMultiple() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        environment.addVariable("a", new Currency(currenciesTypes.get(0), BigDecimal.valueOf(2)));
        BoolNode expectedValue = new BoolNode(true);
        initParserLexer("5 && 5.5 && a");
        Condition condition = parser.condition();

        BoolNode given = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test
    public void orMultiple() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        environment.addVariable("a", new Currency(currenciesTypes.get(0), BigDecimal.valueOf(2)));
        BoolNode expectedValue = new BoolNode(true);
        initParserLexer("5 || 5.5 || a");
        Condition condition = parser.condition();

        BoolNode given = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test
    public void negation() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        environment.addVariable("a", new Currency(currenciesTypes.get(0), BigDecimal.valueOf(2)));
        BoolNode expectedValue = new BoolNode(false);
        initParserLexer("!(5 && 5.5 && a)");
        Condition condition = parser.condition();

        BoolNode given = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test
    public void expressionInCondition() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        environment.addVariable("a", new Currency(currenciesTypes.get(0), BigDecimal.valueOf(2)));
        BoolNode expectedValue = new BoolNode(true);
        initParserLexer("a != a * 2");
        Condition condition = parser.condition();

        BoolNode given = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test
    public void functionCallInCondition() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        initParserLexer("int test(){ return 2;}");
        parser.progress();
        Function function = parser.function();
        environment.addFunction(function);
        BoolNode expectedValue = new BoolNode(true);
        initParserLexer("test() != test() * 2");
        Condition condition = parser.condition();

        BoolNode given = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test
    public void checkCorrectOrder() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        environment.addVariable("a", new Currency(currenciesTypes.get(0), BigDecimal.valueOf(2)));
        BoolNode expectedValue = new BoolNode(true);
        initParserLexer("5 || 0 < 3 && 3 <= 4 || a != a * 2");
        Condition condition = parser.condition();

        BoolNode given = (BoolNode) condition.evaluate(environment);

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }
}
