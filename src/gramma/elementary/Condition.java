package gramma.elementary;

import environment.Environment;
import errors.EnvironmentException;
import gramma.NodeType;
import gramma.interfaces.Arithmetic;
import gramma.interfaces.Expression;
import gramma.interfaces.Value;
import token.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Condition implements Expression {

    boolean negated = false;

    private TokenType operator;

    private List<Expression> operands;

    public Condition() {
        this.operands = new ArrayList<>();
    }

    public void addOperand(Expression operand) { this.operands.add(operand); }

    public boolean isNegated() { return negated; }

    @Override
    public Value evaluate(Environment environment) throws EnvironmentException {
        Value leftOperand = operands.get(0).evaluate(environment);

        for (int i = 1; i < operands.size(); i++) {
            Value rightOperand = operands.get(i).evaluate(environment);

            leftOperand = executeOperation(operator, leftOperand, rightOperand);
        }

        if (isNegated()) {
            leftOperand = negateOperand(leftOperand);
        }

        return leftOperand;
    }

    private Value negateOperand(Value operand) {
        BoolNode temp = new BoolNode(operand);
        return new BoolNode(!temp.getValue());
    }

    private Value executeOperation(TokenType operator, Value left, Value right) throws EnvironmentException {
        switch (operator) {
            case EQUAL:
                return left.isEqual(right);
            case UNEQUAL:
                return negateOperand(left.isEqual(right));
            case LESS:
                return castToArithmeticType(left).isLess(right);
            case LESS_OR_EQUAL:
                return castToArithmeticType(left).isLessOrEqual(right);
            case GREATER:
                return castToArithmeticType(left).isGreater(right);
            case GREATER_OR_EQUAL:
                return castToArithmeticType(left).isGreaterOrEqual(right);
            case AND:
                return and(left, right);
            case OR:
                return or(left, right);
            default:
                throw new EnvironmentException(operator + "is not valid operation.");
        }
    }

    private Arithmetic castToArithmeticType(Value operand) throws EnvironmentException {
        if(Value.isBool(operand))
            throw new EnvironmentException("Cannot compare " + operand.getType() + " to ArithmeticValue");
        return (Arithmetic) operand;
    }

    private Value and(Value left, Value right) throws EnvironmentException {
        BoolNode leftNode = new BoolNode(left);
        BoolNode rightNode = new BoolNode(right);
        return leftNode.and(rightNode);
    }

    private Value or(Value left, Value right) throws EnvironmentException {
        BoolNode leftNode = new BoolNode(left);
        BoolNode rightNode = new BoolNode(right);
        return leftNode.or(rightNode);
    }

    public void setOperator(TokenType operator) { this.operator = operator; }

    public void setIsNegated(boolean isNegated) { this.negated = isNegated; }

    public TokenType getOperator() { return operator; }

    @Override
    public NodeType getType() { return NodeType.CONDITION; }
}
