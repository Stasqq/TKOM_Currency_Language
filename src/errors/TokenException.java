package errors;

public class TokenException extends Exception{
    public TokenException(int line, int position)
    {
        super("Wrong token, in line - " + line + " , at position - "+position);
    }
}
