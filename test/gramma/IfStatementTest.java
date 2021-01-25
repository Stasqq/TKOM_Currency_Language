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

public class IfStatementTest {
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
    public void ifStatementWithoutElse() throws IOException, TokenException, WrongTokenTypeException, EnvironmentException {
        StatementOutput expectedValue = new StatementOutput(ReturnStatus.BASIC);
        initParserLexer("if(0 > 0) return -1;");
        IfStatement ifStatement = (IfStatement) parser.statement();

        StatementOutput given = ifStatement.execute(environment);

        assertEquals(expectedValue.getStatus(), given.getStatus());
        assertEquals(expectedValue.getReturnValue(), given.getReturnValue());
    }

    @Test
    public void trueCondition() throws IOException, TokenException, EnvironmentException, WrongTokenTypeException {
        StatementOutput expectedValue = new StatementOutput(ReturnStatus.RETURN, new IntNode(0));
        initParserLexer("if(0 == 0) return 0;else return -1;");
        IfStatement ifStatement = (IfStatement) parser.statement();

        StatementOutput given = ifStatement.execute(environment);

        assertEquals(expectedValue.getStatus(), given.getStatus());
        assertEquals(expectedValue.getReturnValue().getType(), given.getReturnValue().getType());
        assertEquals(((IntNode) expectedValue.getReturnValue()).getValue(),
                ((IntNode) given.getReturnValue()).getValue());
    }

    @Test
    public void falseCondition() throws IOException, TokenException, WrongTokenTypeException, EnvironmentException {
        StatementOutput expectedValue = new StatementOutput(ReturnStatus.RETURN, new IntNode(0));
        initParserLexer("if(0 > 0) return -1;else return 0;");
        IfStatement ifStatement = (IfStatement) parser.statement();

        StatementOutput given = ifStatement.execute(environment);

        assertEquals(expectedValue.getStatus(), given.getStatus());
        assertEquals(expectedValue.getReturnValue().getType(), given.getReturnValue().getType());
        assertEquals(((IntNode) expectedValue.getReturnValue()).getValue(),
                ((IntNode) given.getReturnValue()).getValue());
    }

}
