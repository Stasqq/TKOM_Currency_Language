package gramma.elementary;

import gramma.interfaces.Value;

public class StatementOutput {
    private Value returnValue;

    private ReturnStatus status;

    public StatementOutput(ReturnStatus status, Value returnValue){
        this.returnValue = returnValue;
        this.status = status;
    }

    public StatementOutput(ReturnStatus status) {
        this.status = status;
        this.returnValue = null;
    }

    public boolean isStatusReturn() { return status == ReturnStatus.RETURN; }

    public ReturnStatus getStatus() { return status; }

    public Value getReturnValue() { return returnValue; }
}
