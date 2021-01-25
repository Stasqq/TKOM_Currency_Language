package gramma.elementary;

import environment.Environment;
import errors.EnvironmentException;
import gramma.NodeType;
import gramma.interfaces.Expression;
import gramma.interfaces.Statement;
import gramma.interfaces.Value;

import java.math.BigDecimal;

public class DeclarationStatement extends Signature implements Statement {

    private Expression assign;

    public DeclarationStatement(String returnType, String id) {
        super(returnType, id);
    }

    private Value getDefaultValue(Environment environment) throws EnvironmentException {
        if (isReturnType(NodeType.INT)) {
            return new IntNode(0);
        } else if (isReturnType(NodeType.DOUBLE)) {
            return new DoubleNode(0);
        } else if(isReturnType(NodeType.BOOL)) {
            return new BoolNode(true);
        }else if (environment.containsCurrencyType(getReturnType())) {
            return new Currency(environment.getCurrencyType(getReturnType()), BigDecimal.ZERO);
        }

        throw new EnvironmentException("Unexpected data type: " + getReturnType());
    }

    private Value initializeVariable(Environment environment, Value assign) throws EnvironmentException {
        if (isReturnType(assign.getType())) {
            return assign;
        } else if (isReturnType(NodeType.DOUBLE) && Value.isInt(assign)) {
            return new DoubleNode(Value.getIntValue(assign));
        } else if (environment.containsCurrencyType(getReturnType()) && Value.isCurrency(assign)) {
            return new Currency(environment.getCurrencyType(getReturnType()),
                    BigDecimal.valueOf(((Currency)assign).getValue().doubleValue() * ((Currency)assign).getCurrencyType().getRatio(getReturnType())));
        } else if(environment.containsCurrencyType(getReturnType()) && Value.isInt(assign)) {
            return new Currency(environment.getCurrencyType(getReturnType()), BigDecimal.valueOf(((IntNode)assign).getValue()));
        } else if(environment.containsCurrencyType(getReturnType()) && Value.isDouble(assign)) {
            return new Currency(environment.getCurrencyType(getReturnType()), BigDecimal.valueOf(((DoubleNode)assign).getValue()));
        } else {
            throw new EnvironmentException("Cannot assign " + assign.getType() + " to " + getReturnType().toUpperCase());
        }
    }

    @Override
    public StatementOutput execute(Environment environment) throws EnvironmentException {
        if (assign == null) {
            environment.addVariable(getId(), getDefaultValue(environment));
            return new StatementOutput(ReturnStatus.BASIC);
        }

        Value assign = this.assign.evaluate(environment);
        environment.addVariable(getId(), initializeVariable(environment, assign));

        return new StatementOutput(ReturnStatus.BASIC);
    }

    public void setAssign(Expression expression) { this.assign = expression; }

    @Override
    public NodeType getType() { return NodeType.DECLARATION_LINE; }
}
