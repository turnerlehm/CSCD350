package command.commands;

import command.*;

public class CommandHelp extends AbstractCommand implements Command
{	
	public CommandHelp()
	{
		super(COMMAND_TYPE.HELP);
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