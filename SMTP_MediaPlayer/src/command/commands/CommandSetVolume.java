package command.commands;

import command.COMMAND_TYPE;
import command.Flag;
import command.InvalidCommandException;

public class CommandSetVolume  extends AbstractCommand implements Command{

	public CommandSetVolume(){
		super(COMMAND_TYPE.SETVOL);
	}
	
	public void addFlag(Flag flagToAdd) throws InvalidCommandException
	{
		super.addFlag(flagToAdd);
	}

}
