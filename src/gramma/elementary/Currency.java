package gramma.elementary;

import currency.CurrencyType;
import environment.Environment;
import errors.EnvironmentException;
import gramma.NodeType;
import gramma.interfaces.Arithmetic;
import gramma.interfaces.Expression;
import gramma.interfaces.Value;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Currency implements Expression, Arithmetic {

    private CurrencyType currencyType;
    private BigDecimal value;

    public Currency(CurrencyType currencyType, BigDecimal value) {
        this.currencyType = currencyType;
        this.value = value.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String toString() { return value + " " + currencyType.getCurrencyShortcut(); }

    @Override
    public Arithmetic add(Value addValue) throws EnvironmentException {
        if (!Value.isCurrency(addValue))
            throw new EnvironmentException("Cannot add " + addValue.getType() + " from " + getType());


        return new Currency(currencyType, BigDecimal.valueOf(value.doubleValue() +
                ((Currency)addValue).getValue().doubleValue() * ((Currency)addValue).getCurrencyType().getRatio(currencyType.getCurrencyShortcut())));
    }

    @Override
    public Arithmetic subtract(Value subValue) throws EnvironmentException {
        if (!Value.isCurrency(subValue))
            throw new EnvironmentException("Cannot subtract " + subValue.getType() + " from " + getType());


        return new Currency(currencyType, BigDecimal.valueOf(value.doubleValue() -
                ((Currency)subValue).getValue().doubleValue() * ((Currency)subValue).getCurrencyType().getRatio(currencyType.getCurrencyShortcut())));
    }

    @Override
    public Arithmetic multiply(Value mulValue) throws EnvironmentException {
        switch(mulValue.getType()){
            case INT:
                return new Currency(currencyType, BigDecimal.valueOf(value.doubleValue() * Value.getIntValue(mulValue)) );
            case DOUBLE:
                return new Currency(currencyType, BigDecimal.valueOf(value.doubleValue() * Value.getDoubleValue(mulValue)));
            default:
                throw new EnvironmentException("Cannot multiply " + getType() + " by " + mulValue.getType());
        }
    }

    @Override
    public Arithmetic divide(Value divValue) throws EnvironmentException {
        if (Arithmetic.isZero(divValue)) {
            throw new EnvironmentException("Divide by 0!");
        }

        switch(divValue.getType()){
            case INT:
                return new Currency(currencyType, BigDecimal.valueOf(value.doubleValue() / Value.getIntValue(divValue)) );
            case DOUBLE:
                return new Currency(currencyType, BigDecimal.valueOf(value.doubleValue() / Value.getDoubleValue(divValue)));
            default:
                throw new EnvironmentException("Cannot divide " + getType() + " by " + divValue.getType());
        }
    }

    @Override
    public BoolNode isLess(Value comparisonValue) throws EnvironmentException {
        if (!Value.isCurrency(comparisonValue))
            throw new EnvironmentException("Cannot compare " + comparisonValue.getType() + " to " + getType());

        return new BoolNode((value.doubleValue() * currencyType.getRatio(((Currency)comparisonValue).currencyType.getCurrencyShortcut()))
                < ((Currency)comparisonValue).getValue().doubleValue());
    }

    @Override
    public BoolNode isGreater(Value comparisonValue) throws EnvironmentException {
        if (!Value.isCurrency(comparisonValue))
            throw new EnvironmentException("Cannot compare " + comparisonValue.getType() + " to " + getType());

        return new BoolNode((value.doubleValue() * currencyType.getRatio(((Currency)comparisonValue).currencyType.getCurrencyShortcut()))
                > ((Currency)comparisonValue).getValue().doubleValue());
    }

    @Override
    public BoolNode isLessOrEqual(Value comparisonValue) throws EnvironmentException {
        if (!Value.isCurrency(comparisonValue))
            throw new EnvironmentException("Cannot compare " + comparisonValue.getType() + " to " + getType());

        return new BoolNode((value.doubleValue() * currencyType.getRatio(((Currency)comparisonValue).currencyType.getCurrencyShortcut()))
                <= ((Currency)comparisonValue).getValue().doubleValue());
    }

    @Override
    public BoolNode isGreaterOrEqual(Value comparisonValue) throws EnvironmentException {
        if (!Value.isCurrency(comparisonValue))
            throw new EnvironmentException("Cannot compare " + comparisonValue.getType() + " to " + getType());

        return new BoolNode((value.doubleValue() * currencyType.getRatio(((Currency)comparisonValue).currencyType.getCurrencyShortcut()))
                >= ((Currency)comparisonValue).getValue().doubleValue());
    }

    @Override
    public Value evaluate(Environment environment) throws EnvironmentException {
        return null;
    }

    @Override
    public BoolNode isEqual(Value comparisonValue) throws EnvironmentException {
        if (!Value.isCurrency(comparisonValue))
            throw new EnvironmentException("Cannot compare " + comparisonValue.getType() + " to " + getType());

        return new BoolNode((value.doubleValue() * currencyType.getRatio(((Currency)comparisonValue).currencyType.getCurrencyShortcut()))
                == ((Currency)comparisonValue).getValue().doubleValue());
    }

    @Override
    public NodeType getType() { return NodeType.CURRENCY; }

    public CurrencyType getCurrencyType() { return currencyType; }

    public BigDecimal getValue() { return value; }
}
