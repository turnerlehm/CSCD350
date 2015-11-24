package command.commands;

import command.*;

public class CommandNext extends AbstractCommand
{	
	public CommandNext()
	{
		super(COMMAND_TYPE.NEXT);
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