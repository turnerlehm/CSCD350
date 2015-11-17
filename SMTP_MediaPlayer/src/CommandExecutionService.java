import command.CommandParser;

public class CommandExecutionService {
	private volatile static CommandParser com_parse;
	private volatile static CommandExecutionService instance;
	private volatile static String[] commands = {"open [GROUP_NAME] --> opens media player group by GROUP_NAME",
			"open [GROUP_NUM] --> opens media player group by GROUP_NUM",
			"open [GROUP_NAME] -e [EXTENSION] --> opens group by GROUP_NAME and filters according to EXTENSION",
			"open [GROUP_NUM] -e [EXTENSION] --> opens group by GROUP_NUM and filters according to EXTENSION",
			"open -p [PLAYLIST_NAME] --> opens playlist by name",
			"play -p [PLAYLIST_NAME] --> plays all music in playlist specified by PLAYLIST_NAME",
			"play -a [ARTIST_NAME] --> plays all music associated with ARTIST_NAME",
			"play -g [GENRE] --> play all music filtered by specified GENRE",
			"home --> return to initial menu",
			"help --> display this list of commands",
			"help [COMMAND_NAME] --> detailed help for command specified by COMMAND_NAME"
			};
	
	private CommandExecutionService(){}
	
	public static CommandExecutionService getInstance()
	{
		if(instance == null)
		{
			synchronized(Player.class)
			{
				if(instance == null)
					instance = new CommandExecutionService();
			}
		}
		return instance;
	}
	
	public void execute(String cmd)
	{
		System.out.println("--- COMMAND EXECUTION SERVICE ---");
		System.out.println("ENTERED COMMAND: " + cmd);
	}

}
