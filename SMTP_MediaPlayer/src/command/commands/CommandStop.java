package command.commands;

import command.*;

public class CommandStop extends AbstractCommand implements Command
{	
	public CommandStop()
	{
		super(COMMAND_TYPE.STOP);
	}
	
	public void addFlag(Flag flagToAdd) throws InvalidCommandException
	{
		throw new InvalidCommandException("Command Does not Accept Flags");
	}
	
	public static String help()
	{	
		return "";
	}
}