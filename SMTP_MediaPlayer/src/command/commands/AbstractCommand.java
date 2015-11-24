package command.commands;

import command.*;
import java.util.List;
import java.util.ArrayList;

public abstract class AbstractCommand implements Command
{
	public final COMMAND_TYPE _Command_Type;
	private List<Flag> _Flags;
	
	protected AbstractCommand(COMMAND_TYPE type)
	{
		_Command_Type = type;
		_Flags = new ArrayList<Flag>();
	}
	
	public void addFlag(Flag flagToAdd) throws InvalidCommandException
	{
		if(_Flags.contains(flagToAdd))
		{
			throw new InvalidCommandException("Flag has already been set");
		}
		_Flags.add(flagToAdd);
	}
	
	public List<Flag> getFlags()
	{
		return _Flags;
	}
	
	public COMMAND_TYPE getType()
	{
		return _Command_Type;
	}
}