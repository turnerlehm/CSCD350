import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import command.*;
import command.commands.AbstractCommand;
 
public class Main 
{
	//List<String> allPlaylists;//just use the names of the playlist to load temporary lists?
	//List<MediaFile> allFiles;//all the files in the database. we need to create MediaFile object
	static String currentCommand = "";
	
	private static ConsoleUI user_interface;
	private static SystemScanner file_scanner;
	private static Player audio_player;
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
		audio_player = Player.getInstance();
		
		CommandParser c = CommandParser.getInstance();
		AbstractCommand cmd = c.parseCommand("play -s stop");
		System.out.println(cmd._Command_Type);
		List<Flag> flags = cmd.getFlags();
		for(Flag f: flags)
		{
			System.out.println(f._Flag + " " + f._Parameter);
		}
		CommandExecutionService ces = CommandExecutionService.getInstance();
		user_interface.initInput();
		user_interface.setScanner(file_scanner);
		user_interface.setExecutor(ces);
		user_interface.initScan();
		System.out.println("--- Initial scan results ---");
		file_scanner.printFileNames();
		file_scanner.printFilePaths();
		user_interface.initMenu();
		user_interface.start();
		audio_player.createPlaylist(file_scanner.getLibraryPaths());
		audio_player.startPlaying();
		audio_player.playPlaylist();
		
		//while(currentCommand.compareTo("exit") != 0)
		//{						
		//	processInput();
		//}
        //exit();
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
	static String parseCommand(String command)
	{
		//self explanatory?
		return "";
	}
	
	static void exit()
	{
		//'exit' command closes program
		//do any needed cleanup, database closings..
		System.out.println("Closing program...");
		System.out.println("Bye!");
		user_interface.stop();
		user_interface = null;
		file_scanner = null;
		audio_player = null;
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

