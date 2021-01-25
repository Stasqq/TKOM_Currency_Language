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
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class WhileStatementTest {
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
    public void returnStatementInWhile() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        StatementOutput expectedValue = new StatementOutput(ReturnStatus.RETURN, new IntNode(2));
        environment.addVariable("a", new IntNode(0));
        initParserLexer("while(a < 3){if(a == 2) return a;a = a + 1;}");
        WhileStatement whileStatement = (WhileStatement) parser.statement();

        StatementOutput given = whileStatement.execute(environment);

        assertEquals(expectedValue.getStatus(), given.getStatus());
        assertEquals(expectedValue.getReturnValue().getType(), given.getReturnValue().getType());
        assertEquals(((IntNode) expectedValue.getReturnValue()).getValue(),
                ((IntNode) given.getReturnValue()).getValue());
    }

    @Test(expected = EnvironmentException.class)
    public void initVariableInWhileRangeAndTryToGetAfter() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        environment.addVariable("a", new IntNode(0));
        initParserLexer("while(a < 1){int b = 1;a = 1;}");
        WhileStatement whileStatement = (WhileStatement) parser.statement();

        whileStatement.execute(environment);
        environment.getVariable("b");

    }

    @Test
    public void neverEnterWhile() throws EnvironmentException, WrongTokenTypeException, IOException, TokenException {
        IntNode expectedValue = new IntNode(0);
        environment.addVariable("a", expectedValue);
        initParserLexer("while(1 < 0){a = 0;}");
        WhileStatement whileStatement = (WhileStatement) parser.statement();

        whileStatement.execute(environment);
        IntNode given = (IntNode) environment.getVariable("a");

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }

    @Test
    public void whileLocalRangeUsingParentRangeVariable() throws EnvironmentException, IOException, TokenException, WrongTokenTypeException {
        IntNode expectedValue = new IntNode(1);
        environment.addVariable("a", new IntNode(0));
        initParserLexer("while(a < 1){a = a + 1;}");
        WhileStatement whileStatement = (WhileStatement) parser.statement();

        whileStatement.execute(environment);
        IntNode given = (IntNode) environment.getVariable("a");

        assertEquals(expectedValue.getType(), given.getType());
        assertEquals(expectedValue.getValue(), given.getValue());
    }
}
