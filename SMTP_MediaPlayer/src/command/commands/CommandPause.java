package command.commands;

import command.*;

public class CommandPause extends AbstractCommand implements Command
{	
	public CommandPause()
	{
		super(COMMAND_TYPE.PAUSE);
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