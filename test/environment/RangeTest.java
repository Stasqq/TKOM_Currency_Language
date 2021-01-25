package environment;

import errors.EnvironmentException;
import gramma.elementary.DoubleNode;
import gramma.elementary.IntNode;
import gramma.interfaces.Value;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RangeTest {
    private Range range;

    @Before
    public void initRange() { range = new Range(); }

    @Test
    public void getVariable() throws EnvironmentException {
        DoubleNode expectedVariable = new DoubleNode(1.0);
        range.addVariable("a", expectedVariable);

        DoubleNode given = (DoubleNode) range.getVariable("a");

        assertEquals(expectedVariable.getType(), given.getType());
        assertEquals(expectedVariable.getValue(), given.getValue(),1);
    }

    @Test
    public void getNullVariable() {
        Value given = range.getVariable("a");
        assertNull(given);
    }

    @Test
    public void containVariable() {
        assertFalse(range.contains("a"));
    }

    @Test
    public void setVariableWithNewValue() throws EnvironmentException {
        IntNode expectedVariable = new IntNode(1);
        range.addVariable("a", new IntNode(0));

        range.setVariable("a", expectedVariable);
        IntNode given = (IntNode) range.getVariable("a");

        assertEquals(expectedVariable.getType(), given.getType());
        assertEquals(expectedVariable.getValue(), given.getValue());
    }
}
