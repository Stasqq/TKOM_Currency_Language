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

public class CodeBlockTest {
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
    public void afterReturnStatementExecutionIsCancelled() throws IOException, TokenException, WrongTokenTypeException, EnvironmentException {
        StatementOutput expectedValue = new StatementOutput(ReturnStatus.RETURN, new IntNode(2));
        initParserLexer("{int a = 0;" +
                "a = a + 2;" +
                "return a;" +
                "a = a + 2;" +
                "return a;}");
        CodeBlock codeBlock = parser.codeBlock();

        StatementOutput given = codeBlock.execute(environment);

        assertEquals(expectedValue.getStatus(), given.getStatus());
        assertEquals(((IntNode) expectedValue.getReturnValue()).getValue(),
                ((IntNode) given.getReturnValue()).getValue());
        assertEquals(expectedValue.getReturnValue().getType(),
                given.getReturnValue().getType());
    }

    @Test
    public void emptyCodeBlock() throws IOException, TokenException, WrongTokenTypeException, EnvironmentException {
        StatementOutput expectedValue = new StatementOutput(ReturnStatus.BASIC);
        initParserLexer("{}");
        CodeBlock codeBlock = parser.codeBlock();

        StatementOutput given = codeBlock.execute(environment);

        assertEquals(expectedValue.getStatus(), given.getStatus());
        assertEquals(expectedValue.getReturnValue(), given.getReturnValue());
    }

    @Test
    public void returnCallAfterInt() throws IOException, TokenException, WrongTokenTypeException, EnvironmentException {
        StatementOutput expectedValue = new StatementOutput(ReturnStatus.RETURN, new IntNode(0));
        initParserLexer("{int a = 0;" +
                "if(a == 0) return a;}");
        CodeBlock codeBlock = parser.codeBlock();

        StatementOutput given = codeBlock.execute(environment);

        assertEquals(expectedValue.getStatus(), given.getStatus());
        assertEquals(((IntNode) expectedValue.getReturnValue()).getValue(),
                ((IntNode) given.getReturnValue()).getValue());
        assertEquals(expectedValue.getReturnValue().getType(),
                given.getReturnValue().getType());
    }
}
