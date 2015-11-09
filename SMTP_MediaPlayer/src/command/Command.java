package command;

import java.util.List;
import java.util.ArrayList;

public class Command
{
	public final COMMAND_TYPE _Command_Type;
	private List<Flag> _Flags;
	
	public Command(COMMAND_TYPE type)
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
}