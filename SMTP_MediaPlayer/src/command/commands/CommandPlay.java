package command.commands;

import command.*;

public class CommandPlay extends AbstractCommand implements Command
{	
	public CommandPlay()
	{
		super(COMMAND_TYPE.PLAY);
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