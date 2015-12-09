package command.commands;

import command.*;

public class CommandPlay extends AbstractCommand implements Command
{	
	public FLAG_TYPE[] Allowed_Flags = new FLAG_TYPE[]{FLAG_TYPE.ARTIST, FLAG_TYPE.GENRE, FLAG_TYPE.SONG, FLAG_TYPE.PLAYLIST, FLAG_TYPE.NOFLAG};
	
	public CommandPlay()
	{
		super(COMMAND_TYPE.PLAY);
	}
	
	public void addFlag(Flag flagToAdd) throws InvalidCommandException
	{
		boolean added = false;
		for(int x = 0; x < Allowed_Flags.length; x++)
		{
			if(Allowed_Flags[x].equals(flagToAdd._Flag))
			{
				super.addFlag(flagToAdd);
				added = true;
				break;
			}
		}
		if(!added)
			throw new InvalidCommandException("Invalid Flag");
	}
	
	public static String help()
	{	
		return "";
	}
}