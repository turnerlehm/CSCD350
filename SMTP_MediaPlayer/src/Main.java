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
	private static DB_Manager dbm;
	private static CommandParser cmd_parse;

	public static void main(String[] args) throws InvalidCommandException 
	{		
		user_interface = ConsoleUI.getInstance();
		file_scanner = SystemScanner.getInstance();
		audio_player = Player.getInstance();
		dbm = DB_Manager.getInstance();
		cmd_parse = CommandParser.getInstance();
		dbm.init();
		file_scanner.setDBM(dbm);
		CommandParser c = CommandParser.getInstance();
		AbstractCommand cmd = c.parseCommand("pause");
		System.out.println(cmd._Command_Type);
		List<Flag> flags = cmd.getFlags();
		for(Flag f: flags)
		{
			System.out.println(f._Flag + " " + f._Parameter);
		}
		CommandExecutionService ces = CommandExecutionService.getInstance();
		user_interface.initInput();
		ces.setCommandParser(cmd_parse);
		ces.setInput(user_interface.getInput());
		ces.setPlayer(audio_player);
		ces.setSysScan(file_scanner);
		user_interface.setScanner(file_scanner);
		user_interface.setExecutor(ces);
		user_interface.initScan();
		System.out.println("--- Initial scan results ---");
		file_scanner.printFileNames();
		file_scanner.printFilePaths();
		user_interface.initMenu();
		user_interface.start();
		while(user_interface.isAlive());
		exit();
	}
	
	static void exit()
	{
		//'exit' command closes program
		//do any needed cleanup, database closings..
		System.out.println("Closing program...");
		System.out.println("Bye!");
		user_interface.interrupt();
		user_interface = null;
		file_scanner = null;
		audio_player = null;
		dbm.shutdown();
		System.exit(0);
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

