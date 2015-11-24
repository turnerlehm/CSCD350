package command.commands;

import java.util.List;
import command.*;

public interface Command
{		
	public abstract void addFlag(Flag flagToAdd) throws InvalidCommandException;
	public abstract List<Flag> getFlags();
	public abstract COMMAND_TYPE getType();
}