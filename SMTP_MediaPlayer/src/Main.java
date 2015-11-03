import java.sql.*;
import java.util.List;
import java.util.Scanner;

class MediaFile{};

public class Main 
{
	List<String> allPlaylists;//just use the names of the playlist to load temporary lists?
	List<MediaFile> allFiles;//all the files in the database. we need to create MediaFile object
	static String currentCommand = "";

	public static void main(String[] args) 
	{		
        //connectDatabase(); open a connection to the databse, check if it is empty.
		//scanFiles();//if database is empty this will automatically be run on the current directory

        //read database: scan through the tables in the database and create a list in memory for all the data 
        //(the groups, the playlists, etc) after this scan, the only time database should be accessed is to add or delete
		//main loop:
		while(currentCommand.compareTo("exit") != 0)
		{
			System.out.println("current command = " + currentCommand);
			displayUI();
			processInput();
		}
        exit();
	}
	
	void scanFiles()
	{
		//scan specified directory and add all media files of specified extension to a local list.		
	}
	void connectDatabase() throws SQLException
	{
		Connection c;
		 try 
		 {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:media_database.db");
		} catch (ClassNotFoundException e) 
		 {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	static void displayUI()
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
		System.out.println("Enter a number: ");
		currentCommand = reader.nextLine();
	}
	
	//===Commands functions
	static void createPlaylist()
	{
		//adds a list to current List<Playlist> and also to the database using specified file objects
	}
	static void addToPlaylist(String playlistName, MediaFile file)
	{
		//this would probably be part of 'add' command run after a directory scan
	}
	static void parseCommand(String command)
	{
		//self explanatory?
	}
	static void playAudio(MediaFile audioFile)
	{
		
	}
	static void exit()
	{
		//'exit' command closes program
		//do any needed cleanup, database closings..
		System.out.println("Closing program, goodbye!");
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
help;//displays all commands
help command_name; specific documentation for that command
exit; closes the program
*/

