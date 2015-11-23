package command.commands;

import command.*;

public class CommandSeek extends AbstractCommand
{	
	public CommandSeek()
	{
		super(COMMAND_TYPE.SEEK);
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