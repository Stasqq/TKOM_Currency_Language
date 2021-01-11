package gramma.elementary;

import currency.CurrencyType;
import environment.Environment;
import gramma.NodeType;
import gramma.interfaces.Arithmetic;
import gramma.interfaces.Expression;
import gramma.interfaces.Value;

import java.math.BigDecimal;

public class Currency implements Expression, Arithmetic {

    private CurrencyType currencyType;
    private BigDecimal value;

    public Currency(CurrencyType currencyType, BigDecimal value) {
        this.currencyType = currencyType;
        this.value = value;
    }

    @Override
    public Arithmetic add(Value addValue) {
        return null;
    }

    @Override
    public Arithmetic subtract(Value subValue) {
        return null;
    }

    @Override
    public Arithmetic multiply(Value mulValue) {
        return null;
    }

    @Override
    public Arithmetic divide(Value divValue) {
        return null;
    }

    @Override
    public BoolNode isLess(Value comparisonValue) {
        return null;
    }

    @Override
    public BoolNode isGreater(Value comparisonValue) {
        return null;
    }

    @Override
    public BoolNode isLessOrEqual(Value comparisonValue) {
        return null;
    }

    @Override
    public BoolNode isGreaterOrEqual(Value comparisonValue) {
        return null;
    }

    @Override
    public Value evaluate(Environment environment) {
        return null;
    }

    @Override
    public BoolNode isEqual(Value comparisonValue) {
        return null;
    }

    @Override
    public NodeType getType() { return NodeType.CURRENCY; }

    public CurrencyType getCurrencyType() { return currencyType; }

    public BigDecimal getValue() { return value; }
}
