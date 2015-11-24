package command.commands;

import command.*;

public class CommandPrevious extends AbstractCommand implements Command
{	
	public CommandPrevious()
	{
		super(COMMAND_TYPE.PREVIOUS);
	}
	
	public void addFlag(Flag flagToAdd) throws InvalidCommandException
	{
		throw new InvalidCommandException("Command Does not Accept Flags");
	}
	
	public static String help()
	{	
		return "COMMAND: previous\nUSAGE: previous\nPrevious\nPREVIOUS\n\nAll items following keyword will be ignored";
	}
}