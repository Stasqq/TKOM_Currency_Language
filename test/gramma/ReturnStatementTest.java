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

public class ReturnStatementTest {
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
    public void returnInt() throws WrongTokenTypeException, IOException, TokenException, EnvironmentException {
        StatementOutput expectedCall = new StatementOutput(ReturnStatus.RETURN, new IntNode(10));
        initParserLexer("return 10;");
        ReturnStatement returnStatement = (ReturnStatement) parser.statement();

        StatementOutput given = returnStatement.execute(environment);

        assertEquals(expectedCall.getStatus(), given.getStatus());
        assertEquals(expectedCall.getReturnValue().getType(), given.getReturnValue().getType());
        assertEquals(((IntNode) expectedCall.getReturnValue()).getValue(),
                ((IntNode) given.getReturnValue()).getValue());
    }

    @Test
    public void returnDouble() throws WrongTokenTypeException, IOException, TokenException, EnvironmentException {
        StatementOutput expectedCall = new StatementOutput(ReturnStatus.RETURN, new DoubleNode(10.5));
        initParserLexer("return 10.5;");
        ReturnStatement returnStatement = (ReturnStatement) parser.statement();

        StatementOutput given = returnStatement.execute(environment);

        assertEquals(expectedCall.getStatus(), given.getStatus());
        assertEquals(expectedCall.getReturnValue().getType(), given.getReturnValue().getType());
        assertEquals(((DoubleNode) expectedCall.getReturnValue()).getValue(),
                ((DoubleNode) given.getReturnValue()).getValue(), 0);
    }

    @Test
    public void returnBool() throws WrongTokenTypeException, IOException, TokenException, EnvironmentException {
        StatementOutput expectedCall = new StatementOutput(ReturnStatus.RETURN, new BoolNode(true));
        initParserLexer("return true;");
        ReturnStatement returnStatement = (ReturnStatement) parser.statement();

        StatementOutput given = returnStatement.execute(environment);

        assertEquals(expectedCall.getStatus(), given.getStatus());
        assertEquals(expectedCall.getReturnValue().getType(), given.getReturnValue().getType());
        assertEquals(((BoolNode) expectedCall.getReturnValue()).getValue(),
                ((BoolNode) given.getReturnValue()).getValue());
    }

    @Test
    public void returnCurrencyVariable() throws EnvironmentException, IOException, TokenException, WrongTokenTypeException {
        Currency currency = new Currency(new CurrencyType("EUR", null),BigDecimal.valueOf(1));
        StatementOutput expectedCall = new StatementOutput(ReturnStatus.RETURN, currency);
        environment.addVariable("a", currency);
        initParserLexer("return a;");
        ReturnStatement returnStatement = (ReturnStatement) parser.statement();

        StatementOutput given = returnStatement.execute(environment);

        assertEquals(expectedCall.getStatus(), given.getStatus());
        assertEquals(expectedCall.getReturnValue().getType(), given.getReturnValue().getType());
        assertEquals(((Currency) expectedCall.getReturnValue()).getValue(),
                ((Currency) given.getReturnValue()).getValue());
    }
}
