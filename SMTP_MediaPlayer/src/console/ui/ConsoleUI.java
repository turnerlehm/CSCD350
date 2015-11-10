package console.ui;

public class ConsoleUI 
{
	private volatile static ConsoleUI instance;
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
	
	private ConsoleUI(){}
	
	public static ConsoleUI getInstance()
	{
		if(instance == null)
		{
			synchronized(ConsoleUI.class)
			{
				if(instance == null)
					instance = new ConsoleUI();
			}
		}
		return instance;
	}
	
	public void initMenu()
	{
		System.out.println("--- Welcome to SMTPlayer v1.something ---");
		System.out.println("Enter group to open (by number or name)");
		System.out.println("1. Music");
		System.out.println("2. Artist");
		System.out.println("3. Genre");
		System.out.println("4. Playlists");
		System.out.println("Type 'help' for more commands");
		System.out.print("$>: ");
	}
	
	public void commands()
	{
		System.out.println("--- Available commands ---");
		for(String command : commands)
			System.out.println(command);
		System.out.println("$>: ");
	}
}
