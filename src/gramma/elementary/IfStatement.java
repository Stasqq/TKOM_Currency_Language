package gramma.elementary;

import environment.Environment;
import errors.EnvironmentException;
import gramma.NodeType;
import gramma.interfaces.Statement;
import gramma.interfaces.Value;


public class IfStatement implements Statement {

    private Condition condition;

    private CodeBlock ifTrueBlock;

    private CodeBlock ifFalseBlock;

    public IfStatement(Condition condition) { this.condition = condition; }

    @Override
    public StatementOutput execute(Environment environment) throws EnvironmentException {
        environment.createNewLocalRange();

        StatementOutput ret = executeIfStatement(environment);

        environment.deleteRange();
        return ret;
    }

    private StatementOutput executeIfStatement(Environment environment) throws EnvironmentException {
        if (Value.getBoolValue(condition.evaluate(environment))) {
            return ifTrueBlock.execute(environment);
        } else if (ifFalseBlock != null) {
            return ifFalseBlock.execute(environment);
        }

        return new StatementOutput(ReturnStatus.BASIC);
    }

    public void setIfTrueBlock(CodeBlock block) { this.ifTrueBlock = block; }

    public void setIfFalseBlock(CodeBlock block) { this.ifFalseBlock = block; }

    @Override
    public NodeType getType() { return NodeType.IF_LINE; }

    public CodeBlock getIfTrueBlock() { return ifTrueBlock; }

    public CodeBlock getIfFalseBlock() { return ifFalseBlock; }
}
