package gramma.elementary;

import environment.Environment;
import errors.EnvironmentException;
import gramma.NodeType;
import gramma.interfaces.Expression;
import gramma.interfaces.Statement;
import gramma.interfaces.Value;

import java.math.BigDecimal;

public class AssignStatement implements Statement {

    private String id;

    private Expression assign;

    public AssignStatement(String id, Expression assign) {
        this.id = id;
        this.assign = assign;
    }

    @Override
    public StatementOutput execute(Environment environment) throws EnvironmentException {
        Value value = environment.getVariable(id);

        Value assign = this.assign.evaluate(environment);

        if (Value.isCurrency(assign) && Value.isCurrency(value)) {
            environment.setVariable(id, new Currency(((Currency) value).getCurrencyType(),
                    BigDecimal.valueOf(((Currency)assign).getValue().doubleValue() *
                            ((Currency)assign).getCurrencyType().getRatio(((Currency) value).getCurrencyType().getCurrencyShortcut()))));
        } else if(Value.isCurrency(value) && Value.isInt(assign)) {
            environment.setVariable(id, new Currency(((Currency)value).getCurrencyType(), BigDecimal.valueOf(((IntNode)assign).getValue())));
        } else if(Value.isCurrency(value) && Value.isDouble(assign)) {
            environment.setVariable(id, new Currency(((Currency)value).getCurrencyType(), BigDecimal.valueOf(((DoubleNode)assign).getValue())));
        } else if (assign.getType() == value.getType()) {
            environment.setVariable(id, assign);
        } else if (Value.isDouble(value) && Value.isInt(assign)) {
            environment.setVariable(id, new DoubleNode(((IntNode) assign).getValue()));
        } else {
            throw new EnvironmentException("Cannot assign " + assign.getType() + " to " + value.getType());
        }

        return new StatementOutput(ReturnStatus.BASIC);
    }

    @Override
    public NodeType getType() { return NodeType.ASSIGN_LINE; }

    public String getId() { return id; }
}
