package gramma.elementary;

import environment.Environment;
import errors.EnvironmentException;
import gramma.NodeType;
import gramma.interfaces.Statement;

public class WhileStatement implements Statement {

    private Condition whileCondition;

    private CodeBlock whileBlock;

    @Override
    public StatementOutput execute(Environment environment) throws EnvironmentException {
        StatementOutput output = new StatementOutput(ReturnStatus.BASIC);

        while (((BoolNode) whileCondition.evaluate(environment)).getValue()) {
            environment.createNewLocalRange();
            output = whileBlock.execute(environment);
            if (output.isStatusReturn()) {
                environment.deleteRange();
                return output;
            }
            environment.deleteRange();
        }
        return output;
    }

    @Override
    public NodeType getType() { return NodeType.WHILE_LINE; }

    public void setWhileCondition(Condition condition) { this.whileCondition = condition; }

    public void setWhileBlock(CodeBlock block) { this.whileBlock = block; }

    public CodeBlock getWhileBlock() { return whileBlock; }
}
