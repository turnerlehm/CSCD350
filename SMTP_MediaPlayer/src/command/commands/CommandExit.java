package command.commands;

import command.*;

public class CommandExit extends AbstractCommand implements Command
{	
	public CommandExit()
	{
		super(COMMAND_TYPE.EXIT);
	}
	
	public void addFlag(Flag flagToAdd) throws InvalidCommandException
	{
		throw new InvalidCommandException("Command Does not Accept Flags");
	}
	
	public static String help()
	{	
		return "COMMAND: exit\nUSAGE: exit\nExit\nEXIT\n\nAll items following keyword will be ignored";
	}
}