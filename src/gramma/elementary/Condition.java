package gramma.elementary;

import environment.Environment;
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

    @Override
    public Value evaluate(Environment environment) {
        return null;
    }

    public void setOperator(TokenType operator) { this.operator = operator; }

    public void setIsNegated(boolean isNegated) { this.negated = isNegated; }

    public TokenType getOperator() { return operator; }

    @Override
    public NodeType getType() { return NodeType.CONDITION; }

    public void setNegated(boolean negated) { this.negated = negated; }

    public boolean isNegated() { return negated; }
}
