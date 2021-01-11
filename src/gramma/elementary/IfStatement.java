package gramma.elementary;

import environment.Environment;
import gramma.NodeType;
import gramma.interfaces.Statement;


public class IfStatement implements Statement {

    private Condition condition;

    private CodeBlock ifTrueBlock;

    private CodeBlock ifFalseBlock;

    public IfStatement(Condition condition) { this.condition = condition; }

    @Override
    public StatementOutput execute(Environment environment) {
        return null;
    }

    public void setIfTrueBlock(CodeBlock block) { this.ifTrueBlock = block; }

    public void setIfFalseBlock(CodeBlock block) { this.ifFalseBlock = block; }

    @Override
    public NodeType getType() { return NodeType.IF_LINE; }

    public CodeBlock getIfTrueBlock() { return ifTrueBlock; }

    public CodeBlock getIfFalseBlock() { return ifFalseBlock; }
}
