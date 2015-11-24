package command;

import java.lang.Exception;

public class InvalidCommandException extends Exception
{
	private static final long serialVersionUID = 114419846613096117L;

	public InvalidCommandException()
	{

    }

    public InvalidCommandException(String message)
    {
        super (message);
    }

    public InvalidCommandException(Throwable cause)
    {
        super (cause);
    }

    public InvalidCommandException(String message, Throwable cause)
    {
        super (message, cause);
    }
}