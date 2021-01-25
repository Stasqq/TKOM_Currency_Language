package gramma;

import currency.CurrencyType;
import gramma.elementary.BoolNode;
import gramma.elementary.Currency;
import gramma.elementary.DoubleNode;
import gramma.elementary.IntNode;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;


public class BoolNodeTest {

    @Test
    public void intNodeToBoolNodeTrue() {
        BoolNode boolNode = new BoolNode(new IntNode(1));
        assertTrue(boolNode.getValue());
    }

    @Test
    public void doubleNodeToBoolNodeFalse() {
        BoolNode boolNode = new BoolNode(new DoubleNode(0.0));
        assertFalse(boolNode.getValue());
    }

    @Test
    public void currencyToBoolNodeTrue() {
        BoolNode boolNode = new BoolNode(new Currency(new CurrencyType(" ",null), BigDecimal.valueOf(1)));
        assertTrue(boolNode.getValue());
    }
}
