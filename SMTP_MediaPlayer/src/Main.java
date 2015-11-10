import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import command.*;
import console.*;
import console.ui.ConsoleUI;

class MediaFile{};//used for scan and add to playlist
enum CommandType {OPEN_GROUP, PLAY_FILE, PLAY_GROUP};
class Command{ int commandType; String filename; }; 
public class Main 
{
	//List<String> allPlaylists;//just use the names of the playlist to load temporary lists?
	//List<MediaFile> allFiles;//all the files in the database. we need to create MediaFile object
	static String currentCommand = "";
	
	private static ConsoleUI user_interface;
	private static SystemScanner file_scanner;
	private static AudioPlayer player;
	private static Vector<String> found_media;
	private static Vector<String> media_paths;

	public static void main(String[] args) throws InvalidCommandException 
	{		
        //connectDatabase(); open a connection to the databse, check if it is empty.
		//scanFiles();//if database is empty this will automatically be run on the current directory

        //read database: scan through the tables in the database and create a list in memory for all the data 
        //(the groups, the playlists, etc) after this scan, the only time database should be accessed is to add or delete
		//main loop:
		
		//We need to seriously refactor this stuff and place all command execution in a separate object
		//the lists should not be static as they will changed CONSTANTLY
		//This is just a complete mess!
		
		user_interface = ConsoleUI.getInstance();
		file_scanner = SystemScanner.getInstance();
		player = AudioPlayer.getInstance();
		
		CommandParser c = CommandParser.getInstance();
		command.Command cmd = c.parseCommand("play -s stop");
		System.out.println(cmd._Command_Type);
		List<Flag> flags = cmd.getFlags();
		for(Flag f: flags)
		{
			System.out.println(f._Flag + " " + f._Parameter);
		}
		displayMenu();
		displayCommands();
		System.out.println("--- Test scan for files ---");
		scanFiles();
		System.out.println("--- absolute paths to found files ---");
		for(String path : file_scanner.getPaths())
			System.out.println(path);
		System.out.println("--- Now playing: Tender Love.mp3 ---");
		player.playAudio("Tender Love.mp3");
		//exit();
		
		
		//while(currentCommand.compareTo("exit") != 0)
		//{						
		//	processInput();
		//}
        //exit();
	}
	
	static void scanFiles()
	{
		file_scanner.scan();
	}
	static void displayMenu()
	{
		//show all menu options, with switch for submenus 
		user_interface.initMenu();
	}
	static void displayCommands()
	{
		user_interface.commands();
	}
	
	
	static void processInput()
	{
		//interprets what the user typed in and runs commands if valid
		Scanner reader = new Scanner(System.in);  // Reading from System.in
		currentCommand = reader.nextLine();
		//Command c = CommandParser.instance.parse(reader.nextLine());
		
		//This code is grrrrrooooooooooosssss and smelly
		//Refactor into a CommandExecutor (Iteration 2)
		//This will deprecate the command methods below and encapsulate them in a Singleton object
		if(currentCommand.compareTo("open 1") == 0)//if c.commandType == OPEN_Group then openGroupCommand(c)
		{
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
		else if(currentCommand.compareTo("help") == 0)
			displayCommands();
		reader.close();	
	}
	static void playCommand()
	{
		
	}static void openFileCommand()
	{
		
	}
	static void openGroupCommand()
	{
		
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
		DB_Manager.getInstance().createPlaylist(playlistName);
	}
	static void addToPlaylist(String playlistName, List<MediaFile> files)
	{
		//this would probably be part of 'add' command run after a directory scan
		DB_Manager.getInstance().addToPlaylist(playlistName, files);
	}
	static String parseCommand(String command)
	{
		//self explanatory?
		return "";
	}
	static void playAudio(String fileName)
	{
		DB_Manager.getInstance().getPath(fileName);
	}
	static void exit()
	{
		//'exit' command closes program
		//do any needed cleanup, database closings..
		System.out.println("Closing program...");
		System.out.println("Bye!");
		user_interface = null;
		file_scanner = null;
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

