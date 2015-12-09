package command.commands;

import command.COMMAND_TYPE;
import command.Flag;
import command.InvalidCommandException;

public class CommandInfo extends AbstractCommand{
	public CommandInfo()
	{
		super(COMMAND_TYPE.INFO);
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
