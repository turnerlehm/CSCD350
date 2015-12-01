import command.CommandParser;
import command.InvalidCommandException;
import command.commands.AbstractCommand;
import javafx.scene.media.MediaPlayer;

import java.io.BufferedReader;
import java.io.IOException;

import command.COMMAND_TYPE;
import command.FLAG_TYPE;
import command.commands.*;

public class CommandExecutionService {
	private volatile static CommandParser com_parse;
	private volatile static Player player;
	private volatile static SystemScanner sys_scan;
	private volatile static BufferedReader in;
	private volatile static CommandExecutionService instance;
	private volatile static String[] commands = new String[100];//placeholder for help command
	private volatile static String[] com_help = new String[100];//placeholder for help command
	
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
	
	public void setCommandParser(CommandParser c)
	{
		if(c != null && com_parse == null)
			this.com_parse = c;
		else
			return;
	}
	
	public void setPlayer(Player p)
	{
		if(p != null && player == null)
			this.player = p;
		else
			return;
	}
	
	public void setSysScan(SystemScanner s)
	{
		if(s != null && this.sys_scan == null)
			this.sys_scan = s;
		else
			return;
	}
	
	public void setInput(BufferedReader input)
	{
		if(input != null && this.in == null)
			this.in = input;
		else
			return;
	}
	
	public void execute(String cmd)
	{
		AbstractCommand command = null;
		try {
			command = com_parse.parseCommand(cmd);
		} catch (InvalidCommandException e) {
			// TODO Auto-generated catch block
			System.err.println("Could not parse command");
			return;
		}
		if(command != null)
		{
			if(command.getType() == COMMAND_TYPE.ADD)
			{
				add(command);
			}
			else if(command.getType() == COMMAND_TYPE.CREATE)
			{
				create(command);
			}
			else if(command.getType() == COMMAND_TYPE.DELETE)
			{
				delete(command);
			}
			else if(command.getType() == COMMAND_TYPE.EXIT)
			{
				System.out.println("Exiting media player...");
				return;
			}
			else if(command.getType() == COMMAND_TYPE.HELP)
			{
				help(command);
			}
			else if(command.getType() == COMMAND_TYPE.NEXT)
			{
				next(command);
			}
			else if(command.getType() == COMMAND_TYPE.OPEN)
			{
				open(command);
			}
			else if(command.getType() == COMMAND_TYPE.PAUSE)
			{
				if(player.getMediaPlayer() != null)
				{
					if(player.getMediaPlayer().getStatus() == MediaPlayer.Status.PLAYING)
						player.getMediaPlayer().pause();
					else
					{
						System.out.println("Song is either already paused, stopped or otherwise not playing");
						return;
					}
				}
				else
				{
					System.out.println("No media found");
					return;
				}
			}
			else if(command.getType() == COMMAND_TYPE.PLAY)
			{
				play(command);
			}
			else if(command.getType() == COMMAND_TYPE.PREVIOUS)
			{
				previous(command);
			}
			else if(command.getType() == COMMAND_TYPE.SCAN)
			{
				if(sys_scan != null && in != null)
					try {
						sys_scan.scan(in);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				else
				{
					System.out.println("System scanner or input for command executor not initialized");
					return;
				}
				return;
			}
			else if(command.getType() == COMMAND_TYPE.SEEK)
			{
				seek(command);
			}
			else if(command.getType() == COMMAND_TYPE.STOP)
			{
				if(player.getMediaPlayer() != null)
				{
					if(player.getMediaPlayer().getStatus() == MediaPlayer.Status.PLAYING)
						player.getMediaPlayer().stop();
					else
					{
						System.out.println("Song is either already paused, stopped or otherwise not playing");
						return;
					}
				}
				else
				{
					System.out.println("No media found");
					return;
				}
			}
		}else
			System.err.println("No command to parse.");
	}
	
	//these methods stubbed out pending consultation with team members
	public void add(AbstractCommand c)
	{
		
	}
	
	public void create(AbstractCommand c)
	{
		
	}
	
	public void delete(AbstractCommand c)
	{
		
	}
	
	public void help(AbstractCommand c)
	{
		
	}
	
	public void next(AbstractCommand c)
	{
		
	}
	
	public void open(AbstractCommand c)
	{
		
	}
	
	public void play(AbstractCommand c)
	{
		
	}
	
	public void previous(AbstractCommand c)
	{
		
	}
	
	public void seek(AbstractCommand c)
	{
		
	}
}
