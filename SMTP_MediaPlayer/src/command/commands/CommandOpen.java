package command.commands;

import command.*;
public class CommandOpen extends AbstractCommand implements Command
{	
	public CommandOpen()
	{
		super(COMMAND_TYPE.OPEN);
	}
	
	public void addFlag(Flag flagToAdd) throws InvalidCommandException
	{
		super.addFlag(flagToAdd);
	}
	
	public static String help()
	{	
		return "";
	}
}