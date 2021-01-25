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

public class AssignStatementTest {
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
    public void assignInt() throws EnvironmentException, IOException, TokenException, WrongTokenTypeException {
        environment.addVariable("a", new IntNode(0));
        IntNode expected = new IntNode(1);
        initParserLexer("a = 1;");

        AssignStatement assignStatement = (AssignStatement) parser.statement();
        assignStatement.execute(environment);

        IntNode given = (IntNode) environment.getVariable("a");

        assertEquals(expected.getValue(), given.getValue());
        assertEquals(expected.getType(), given.getType());
    }

    @Test
    public void assignBool() throws EnvironmentException, IOException, TokenException, WrongTokenTypeException {
        environment.addVariable("a", new BoolNode(true));
        BoolNode expected = new BoolNode(false);
        initParserLexer("a = false;");

        AssignStatement assignStatement = (AssignStatement) parser.statement();
        assignStatement.execute(environment);

        BoolNode given = (BoolNode) environment.getVariable("a");

        assertEquals(expected.getValue(), given.getValue());
        assertEquals(expected.getType(), given.getType());
    }

    @Test(expected = EnvironmentException.class)
    public void assignIntToBool() throws EnvironmentException, IOException, TokenException, WrongTokenTypeException {
        environment.addVariable("a", new BoolNode(true));
        initParserLexer("a = 1;");

        AssignStatement assignStatement = (AssignStatement) parser.statement();
        assignStatement.execute(environment);
    }

    @Test(expected = EnvironmentException.class)
    public void assignBoolToInt() throws EnvironmentException, IOException, TokenException, WrongTokenTypeException {
        environment.addVariable("a", new IntNode(0));
        initParserLexer("a = false;");

        AssignStatement assignStatement = (AssignStatement) parser.statement();
        assignStatement.execute(environment);
    }

    @Test
    public void assignIntToDouble() throws EnvironmentException, IOException, TokenException, WrongTokenTypeException {
        environment.addVariable("a", new DoubleNode(0));
        DoubleNode expected = new DoubleNode(1.0);
        initParserLexer("a = 1;");

        AssignStatement assignStatement = (AssignStatement) parser.statement();
        assignStatement.execute(environment);

        DoubleNode given = (DoubleNode) environment.getVariable("a");

        assertEquals(expected.getValue(), given.getValue(),1);
        assertEquals(expected.getType(), given.getType());
    }

    @Test(expected = EnvironmentException.class)
    public void assignDoubleToInt() throws EnvironmentException, IOException, TokenException, WrongTokenTypeException {
        environment.addVariable("a", new IntNode(0));
        initParserLexer("a = 1.5;");

        AssignStatement assignStatement = (AssignStatement) parser.statement();
        assignStatement.execute(environment);
    }

    @Test
    public void assignCurrency() throws EnvironmentException, IOException, TokenException, WrongTokenTypeException {
        Currency currency = new Currency(currenciesTypes.get(0), BigDecimal.valueOf(0)); //EUR
        environment.addVariable("euro", currency);
        Currency expected = new Currency(currenciesTypes.get(0), BigDecimal.valueOf(1));
        initParserLexer("euro = 1;");

        AssignStatement assignStatement = (AssignStatement) parser.statement();
        assignStatement.execute(environment);

        Currency given = (Currency) environment.getVariable("euro");

        assertEquals(expected.getValue(), given.getValue());
        assertEquals(expected.getType(), given.getType());
    }

    @Test
    public void assignDifferentCurrencyTypes() throws EnvironmentException, IOException, TokenException, WrongTokenTypeException {
        Currency euro = new Currency(currenciesTypes.get(0), BigDecimal.valueOf(0)); //EUR
        environment.addVariable("euro", euro);
        Currency dollars = new Currency(currenciesTypes.get(2), BigDecimal.valueOf(2)); //USD
        environment.addVariable("dollars", dollars);
        Currency expected = new Currency(currenciesTypes.get(0), BigDecimal.valueOf(2*currenciesTypes.get(2).getRatio("EUR")));
        initParserLexer("euro = dollars;");

        AssignStatement assignStatement = (AssignStatement) parser.statement();
        assignStatement.execute(environment);

        Currency given = (Currency) environment.getVariable("euro");

        assertEquals(expected.getValue(), given.getValue());
        assertEquals(expected.getType(), given.getType());
    }

    @Test
    public void assignMultipleDifferentCurrencyTypes() throws EnvironmentException, IOException, TokenException, WrongTokenTypeException {
        Currency euro = new Currency(currenciesTypes.get(0), BigDecimal.valueOf(0)); //EUR
        environment.addVariable("euro", euro);
        Currency dollars = new Currency(currenciesTypes.get(2), BigDecimal.valueOf(2)); //USD
        environment.addVariable("dollars", dollars);
        Currency plns = new Currency(currenciesTypes.get(1), BigDecimal.valueOf(1)); //PLN
        environment.addVariable("plns", plns);
        Currency expected = new Currency(currenciesTypes.get(0), BigDecimal.valueOf(2*currenciesTypes.get(2).getRatio("EUR") +
                1 * currenciesTypes.get(1).getRatio("EUR")));
        initParserLexer("euro = dollars + plns;");

        AssignStatement assignStatement = (AssignStatement) parser.statement();
        assignStatement.execute(environment);

        Currency given = (Currency) environment.getVariable("euro");

        assertEquals(expected.getValue(), given.getValue());
        assertEquals(expected.getType(), given.getType());
    }

    @Test
    public void assignMultipleDifferentCurrencyTypesMinus() throws EnvironmentException, IOException, TokenException, WrongTokenTypeException {
        Currency euro = new Currency(currenciesTypes.get(0), BigDecimal.valueOf(0)); //EUR
        environment.addVariable("euro", euro);
        Currency dollars = new Currency(currenciesTypes.get(2), BigDecimal.valueOf(2)); //USD
        environment.addVariable("dollars", dollars);
        Currency plns = new Currency(currenciesTypes.get(1), BigDecimal.valueOf(1)); //PLN
        environment.addVariable("plns", plns);
        Currency expected = new Currency(currenciesTypes.get(0), BigDecimal.valueOf(2*currenciesTypes.get(2).getRatio("EUR") -
                1 * currenciesTypes.get(1).getRatio("EUR")));
        initParserLexer("euro = dollars - plns;");

        AssignStatement assignStatement = (AssignStatement) parser.statement();
        assignStatement.execute(environment);

        Currency given = (Currency) environment.getVariable("euro");

        assertEquals(expected.getValue(), given.getValue());
        assertEquals(expected.getType(), given.getType());
    }

    @Test
    public void assignIntToCurrency() throws WrongTokenTypeException, IOException, TokenException, EnvironmentException {
        Currency euro = new Currency(currenciesTypes.get(0), BigDecimal.valueOf(0)); //EUR
        environment.addVariable("euro", euro);
        Currency expected = new Currency(currenciesTypes.get(0), BigDecimal.valueOf(1));
        initParserLexer("euro = 1;");

        AssignStatement assignStatement = (AssignStatement) parser.statement();
        assignStatement.execute(environment);

        Currency given = (Currency) environment.getVariable("euro");

        assertEquals(expected.getValue(), given.getValue());
        assertEquals(expected.getType(), given.getType());
    }

    @Test
    public void assignDoubleToCurrency() throws WrongTokenTypeException, IOException, TokenException, EnvironmentException {
        Currency euro = new Currency(currenciesTypes.get(0), BigDecimal.valueOf(0)); //EUR
        environment.addVariable("euro", euro);
        Currency expected = new Currency(currenciesTypes.get(0), BigDecimal.valueOf(1.5));
        initParserLexer("euro = 1.5;");

        AssignStatement assignStatement = (AssignStatement) parser.statement();
        assignStatement.execute(environment);

        Currency given = (Currency) environment.getVariable("euro");

        assertEquals(expected.getValue(), given.getValue());
        assertEquals(expected.getType(), given.getType());
    }
}
