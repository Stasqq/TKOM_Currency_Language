package gramma;

import currency.CurrencyType;
import environment.Environment;
import errors.CurrenciesFileException;
import errors.EnvironmentException;
import gramma.elementary.*;
import inputReaders.CurrenciesScanner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import parser.Parser;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PrintStatementTest {

    private final PrintStream original = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

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

    @Before
    public void changeOutputStream() { System.setOut(new PrintStream(outContent)); }
    @After
    public void restoreSystemStream() { System.setOut(original); }

    @Test
    public void emptyPrint() throws EnvironmentException {
        PrintStatement printStatement = new PrintStatement();
        printStatement.execute(null);
        assertEquals("\r\n",outContent.toString());
    }

    @Test
    public void printInt() throws EnvironmentException {
        PrintStatement printStatement = new PrintStatement();
        printStatement.addArgument(new IntNode(1));
        printStatement.execute(null);
        assertEquals("1\r\n",outContent.toString());
    }

    @Test
    public void printDouble() throws EnvironmentException {
        PrintStatement printStatement = new PrintStatement();
        printStatement.addArgument(new DoubleNode(1.0));
        printStatement.execute(null);
        assertEquals("1.0\r\n",outContent.toString());
    }

    @Test
    public void printString() throws EnvironmentException {
        PrintStatement printStatement = new PrintStatement();
        printStatement.addArgument(new StringNode("example"));
        printStatement.execute(null);
        assertEquals("example\r\n",outContent.toString());
    }

    @Test
    public void multipleArgumentsPrint() throws EnvironmentException {
        PrintStatement printStatement = new PrintStatement();
        printStatement.addArgument(new StringNode("example: "));
        printStatement.addArgument(new IntNode(1));
        printStatement.execute(null);
        assertEquals("example: 1\r\n",outContent.toString());
    }
}
