import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import command.*;


class MediaFile{};//used for scan and add to playlist
enum CommandType {OPEN_GROUP, PLAY_FILE, PLAY_GROUP};
class Command{ int commandType; String filename; }; 
public class Main 
{
	//List<String> allPlaylists;//just use the names of the playlist to load temporary lists?
	//List<MediaFile> allFiles;//all the files in the database. we need to create MediaFile object
	static String currentCommand = "";
	static String [] menuItems = {"1. Music", "2. Artist", "3. Genre", "4. Playlists"};

	List<MediaFile> currentDisplay;//display numbered and grab by index. shows .ext. getInfo lets you see all file details 
	static DB_Manager dbm;

	public static void main(String[] args) throws InvalidCommandException 
	{		
        //connectDatabase(); open a connection to the databse, check if it is empty.
		//scanFiles();//if database is empty this will automatically be run on the current directory

        //read database: scan through the tables in the database and create a list in memory for all the data 
        //(the groups, the playlists, etc) after this scan, the only time database should be accessed is to add or delete
		//main loop:

		dbm = DB_Manager.getInstance();
		dbm.init();
		displayMenu();
		while(currentCommand.compareTo("exit") != 0)
		{						
			processInput();
		}
        exit();
		
		//while(currentCommand.compareTo("exit") != 0)
		//{						
		//	processInput();
		//}
        //exit();

	}
	
	void scanFiles()
	{
		//scan specified directory and add all media files of specified extension to a local list.		
	}
	static void displayMenu()
	{
		//show all menu options, with switch for submenus 
		/*
		    ***Media Player***
			Select group to open
			music
			artist
			genre
			playlists
			enter ‘help’ for more commands
			>open music -e wmv
			***music (.wmv)****
			deadmouse - strobe
			kaskade - dynasty
			tiesto - Do you feel me
			>help
			***list of commands***
			....
		 */
		System.out.println("**Media Player***");
		for(String s : menuItems)
		{
			System.out.println(s);
		}		
		
		System.out.println("Enter ‘help’ for more commands");

		playAudio("strobe", null);

	}
	void displayCommands()
	{
		//initiated by typing 'help' this displays all commands
		System.out.println("**Displaying all commands***");
	}
	static void processInput()
	{
		//interprets what the user typed in and runs commands if valid
		Scanner reader = new Scanner(System.in);  // Reading from System.in
		System.out.print("$>");
		currentCommand = reader.nextLine();
		//Command c = CommandParser.instance.parse(reader.nextLine());
		if(currentCommand.compareTo("open 1") == 0)//if c.commandType == OPEN_Group then openGroupCommand(c)
		{
			displayList(dbm.getAllMusic());			
		}
		else if(currentCommand.compareTo("open 2") == 0)
		{
			displayList(dbm.getArtists());
		}
		else if(currentCommand.compareTo("open 3") == 0)
		{
			displayList(dbm.getGenres());
		}
		else if(currentCommand.compareTo("open 4") == 0)
		{
			displayList(dbm.getPlaylists());
			displayList(DB_Manager.getInstance().getAllMusic());			
		}
		else if(currentCommand.compareTo("open 2") == 0)
		{
			displayList(DB_Manager.getInstance().getArtists());
		}
		else if(currentCommand.compareTo("open 3") == 0)
		{
			displayList(DB_Manager.getInstance().getGenres());
		}
		else if(currentCommand.compareTo("open 4") == 0)
		{
			displayList(DB_Manager.getInstance().getPlaylists());
		}
		else if(currentCommand.compareTo("home") == 0)
		{
			displayMenu();
		}
			
	}
	static void playCommand()
	{
		
	}static void openFileCommand()
	{
		
	}
	static void openGroupCommand()
	{
		//scan()
		//select from list, add selected to new or default playlist
		//for each item selected, add file to DB. 
		//if playlistName specified, add all to playlist (by looking at database names)
		//>DB.addFile(filename) returns the int id or -1 if failed
		//DB.addToPlaylist(playlistName, int fileID)//this forces users to confirm first the file is in the db
		
	}
	static void displayList(List<String> list)
	{		
		for(String s : list)
		{
			System.out.println(s);
		}
	}
	//===Commands functions
	static void createPlaylist(String playlistName)
	{
		//adds a list to current List<Playlist> and also to the database using specified file objects
		if(dbm.getPlaylistID(playlistName) != -1)
		{
			
		}
		dbm.createPlaylist(playlistName);
	}
	static void deletePlaylist(String playlistName)
	{
		dbm.deletePlaylist(playlistName);
		DB_Manager.getInstance().createPlaylist(playlistName);
	}
	static void addToPlaylist(String playlistName, List<MediaFile> files)
	{
		//this would probably be part of 'add' command run after a directory scan
		int pid = 0;//TODO retrieve playlist id by making sure it exists first
		dbm.addToPlaylist(pid, files);
	}	
	static void addFile(MediaFile file)
	{
		//puts file in database wihtout adding it to any playlist. will be part of "default" playlist
		
	}
	static void playAudio(String fileName, String ext)//TODO should prol play by id ratehr than filename, easier to delete right thing
	{		
		String path = dbm.getPath(fileName);
		if(path == null)
		{
			System.out.println("file not found on disk. Removing from Database...");
			//TODO go into database and remove the file
		}
		else
		{
			System.out.println("playing audio file: " + path + "/" + fileName);
		}
		
		//to be able to say 'play strobe; play strobe.mp3' we'll probably need playAudio to take fileName And extension. 
		//the command parser will need to separate these so that ext is null and dbm will ignore it if it is
		//DB_Manager.getInstance().addToPlaylist(playlistName, files);
	}
	static void parseCommand(String command)
	{
		//self explanatory?
	}
	static void playAudio(String fileName)
	{
		DB_Manager.getInstance().getPath(fileName);
	}
	static void exit()
	{
		//'exit' command closes program
		//do any needed cleanup, database closings..
		System.out.println("Closing program, goodbye!");
		dbm.shutdown();
		DB_Manager.getInstance().shutdown();
	}

}
/*
 * list of commands
open group_name;//opens one of the above groups
open music -e wmv; //filters list of files by extension 
open -p playlist_name;//opens a playlist, displaying all files
play -p playlist_name; //plays all files in that playlist
play -a artist_name; //plays all files by that artist
play -g genre_name
home; shows menu
help;//displays all commands
help command_name; specific documentation for that command
exit; closes the program
*/

