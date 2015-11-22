import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;


public class Main 
{
	//List<String> allPlaylists;//just use the names of the playlist to load temporary lists?
	//List<MediaFile> allFiles;//all the files in the database. we need to create MediaFile object
	static String currentCommand = "";
	static String [] menuItems = {"1. Music", "2. Artist", "3. Genre", "4. Playlists"};	
	List<MediaFile> s;
	
	List<MediaFile> currentDisplay;//display numbered and grab by index. shows .ext. getInfo lets you see all file details 
	static DB_Manager dbm;
	
	public static void main(String[] args) 
	{		
        //connectDatabase(); open a connection to the databse, check if it is empty.
		//scanFiles();//if database is empty this will automatically be run on the current directory

        //read database: scan through the tables in the database and create a list in memory for all the data 
        //(the groups, the playlists, etc) after this scan, the only time database should be accessed is to add or delete
		//main loop:

		dbm = DB_Manager.getInstance();
		dbm.init();		
		displayMenu();		
		//displayTest();
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
	
	static void displayTest()
	{					
		Scanner reader = new Scanner(System.in); 
		reader.nextLine();					//play
		System.out.println("Results for C:/apps/SMTPlayer");
		System.out.println("1. music1.mp3");
		System.out.println("2. music2.mp3");
		System.out.println("3. music3.mp3");
		System.out.println("Add files to playlist? (y/n)");
		reader.nextLine();					//y
		System.out.println("enter new or existing playlist name");
		reader.nextLine();					//coolmusic
		System.out.println("Scanned files added to newly created playlist: coolmusic");		
	}
	void scanFiles()
	{
		//scan specified directory and add all media files of specified extension to a local list.		
	}
	static void displayMenu()
	{
		System.out.println("**Media Player***");
		for(String s : menuItems)
		{
			System.out.println(s);
		}		
		
		System.out.println("Enter ‘help’ for more commands");
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
		}
		else if(currentCommand.compareTo("help") == 0)
		{
			System.out.println("List of Commands:");
			System.out.printf("%s\n%s\n%s\n%s\n", "open", "play", "delete", "add");
		}
		else if(currentCommand.compareTo("help open") == 0)
		{
			System.out.println("<open command>:");
			System.out.println("open: this is a fancy description of how to use the open command");
		}		
	}
	static void displayList(Vector<TempMediaFile> list)
	{		
		for(TempMediaFile t : list)
		{
			System.out.println(t.filename + ", id: " + t.db_id);
		}
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

