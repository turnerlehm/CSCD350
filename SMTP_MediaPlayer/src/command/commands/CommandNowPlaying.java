package command.commands;

import command.COMMAND_TYPE;
import command.Flag;
import command.InvalidCommandException;

public class CommandNowPlaying extends AbstractCommand{
	
	public CommandNowPlaying()
	{
		super(COMMAND_TYPE.NOWPLAYING);
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
