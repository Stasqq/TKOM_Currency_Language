package gramma.elementary;

import gramma.interfaces.Value;

public class StatementOutput {
    private Value returnValue;

    private ReturnStatus status;

    public StatementOutput(ReturnStatus status) {
        this.status = status;
        this.returnValue = null;
    }
}
