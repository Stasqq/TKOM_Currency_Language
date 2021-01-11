package gramma.elementary;

import environment.Environment;
import gramma.NodeType;
import gramma.interfaces.Statement;

public class WhileStatement implements Statement {

    private Condition whileCondition;

    private CodeBlock whileBlock;

    @Override
    public StatementOutput execute(Environment environment) {
        return null;
    }

    @Override
    public NodeType getType() { return NodeType.WHILE_LINE; }

    public void setWhileCondition(Condition condition) { this.whileCondition = condition; }

    public void setWhileBlock(CodeBlock block) { this.whileBlock = block; }

    public CodeBlock getWhileBlock() { return whileBlock; }
}
