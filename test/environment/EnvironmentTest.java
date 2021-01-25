package environment;

import currency.CurrencyType;
import errors.CurrenciesFileException;
import errors.EnvironmentException;
import gramma.elementary.Function;
import gramma.elementary.IntNode;
import inputReaders.CurrenciesScanner;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EnvironmentTest {
    private Environment environment;
    private List<Function> functions;
    private List<CurrencyType> currenciesTypes;

    @Before
    public void initEnvironment() throws CurrenciesFileException, EnvironmentException {
        this.functions = new ArrayList<>() {{
           add(new Function("int", "main"));
        }};
        CurrenciesScanner currenciesScanner = new CurrenciesScanner("inputs/currencies.txt");
        currenciesScanner.readCurrenciesNameList();
        currenciesTypes = currenciesScanner.readCurrencies();
        environment = new Environment(functions, currenciesTypes);
        environment.createNewRange();
    }

    @Test
    public void containsCurrenciesTypes() throws EnvironmentException {
        assertTrue(environment.containsCurrencyType("PLN"));
    }

    @Test(expected = EnvironmentException.class)
    public void wrongCurrencyType() throws EnvironmentException {
        environment.containsCurrencyType("wrongname");
    }

    @Test(expected = EnvironmentException.class)
    public void sameNameFunction() throws EnvironmentException {
        List<Function> testFunctions = new ArrayList<>() {{
            add(new Function("int", "main"));
            add(new Function("int", "main"));
        }};
        Environment environment = new Environment(testFunctions, currenciesTypes);
    }

    @Test(expected = EnvironmentException.class)
    public void addExistingVariable() throws EnvironmentException {
        environment.addVariable("a", new IntNode(0));
        environment.addVariable("a", new IntNode(0));
    }

    @Test
    public void getVariable() throws EnvironmentException {
        IntNode expectedVariable = new IntNode(0);
        environment.addVariable("a", expectedVariable);

        IntNode given = (IntNode) environment.getVariable("a");

        assertEquals(expectedVariable.getValue(), given.getValue());
        assertEquals(expectedVariable.getType(), given.getType());
    }

    @Test
    public void getVariableFromParentRange() throws EnvironmentException {
        IntNode expectedVariable = new IntNode(0);
        environment.addVariable("a", expectedVariable);
        environment.createNewLocalRange();

        IntNode given = (IntNode) environment.getVariable("a");

        assertEquals(expectedVariable.getValue(), given.getValue());
        assertEquals(expectedVariable.getType(), given.getType());
    }

    @Test(expected = EnvironmentException.class)
    public void getVariableFromDeletedRange() throws EnvironmentException {
        environment.createNewLocalRange();
        environment.addVariable("a", new IntNode(0));
        environment.deleteRange();

        environment.getVariable("a");
    }

    @Test
    public void setVariableToDifferentValue() throws EnvironmentException {
        environment.addVariable("a", new IntNode(0));
        IntNode expectedVariable = new IntNode(1);

        environment.setVariable("a", expectedVariable);
        IntNode given = (IntNode) environment.getVariable("a");

        assertEquals(expectedVariable.getValue(), given.getValue());
        assertEquals(expectedVariable.getType(), given.getType());
    }

    @Test
    public void setVariableFromParentRangeToDifferentValue() throws EnvironmentException {
        environment.addVariable("a", new IntNode(0));
        IntNode expectedVariable = new IntNode(1);
        environment.createNewLocalRange();

        environment.setVariable("a", expectedVariable);
        IntNode given = (IntNode) environment.getVariable("a");

        assertEquals(expectedVariable.getValue(), given.getValue());
        assertEquals(expectedVariable.getType(), given.getType());
    }

    @Test(expected = EnvironmentException.class)
    public void setNonExistingVariable() throws EnvironmentException {
        environment.setVariable("a", new IntNode(0));
    }
}
