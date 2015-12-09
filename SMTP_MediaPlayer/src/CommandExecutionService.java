import command.CommandParser;
import command.InvalidCommandException;
import command.commands.AbstractCommand;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Vector;

import command.COMMAND_TYPE;
import command.FLAG_TYPE;
import command.commands.*;

public class CommandExecutionService {
	private volatile static CommandParser com_parse;
	private volatile static DB_Manager dbm;
	private volatile static Player player;
	private volatile static SystemScanner sys_scan;
	private volatile static BufferedReader in;
	private volatile static CommandExecutionService instance;
	private volatile static String[] commands = {"add",
			"create",
			"delete",
			"exit",
			"help",
			"info",
			"next",
			"np",
			"open",
			"pause",
			"play",
			"previous",
			"scan",
			"seek",
			"stop",
			"sv"
	};
	private volatile static String[] com_help = {"add: add a song to a playlist\nFlags: -p <playlist_name>\nUsage: add -p <playlist_name> <song_name>",
			"create: create a playlist\nFlags: -p <playlist_name>\nUsage: create -p <playlist_name>",
			"delete: deletes a song, playlist, or removes a song from a playlist\nFlags: -p <playlist_name>\nUsage: delete <song_name> - deletes song from DB"
			+ "\ndelete -p <playlist_name> - deletes playlist from DB\ndelete -p <playlist_name> <song_name> - removes song from playlist",
			"exit: exits SMTPlayer",
			"help: displays all commands or specific info for a command\nUsage: help - displays all commands\nhelp <command_name> - display info for <command_name>",
			"info: displays all relevant info for the currently playing song",
			"next: skip the current song and play the next one in the playlist",
			"np: display the name of the currently playing song",
			"open: open a playlist, group (Music, Artists, Genres, Playlists), or a specific artist or genre\nFlags: -p <playlist_name>\n-a <artist_name>\n-g <genre_name>"
			+ "\nUsage: open -p <playlist_name> - opens a playlist\nopen -a <artist_name> - returns a playlists containing all songs by a given artist"
			+ "\nopen -g <genre_name> - returns a playlist containing all songs with the specified genre\nopen <group_name> - opens a DB group and returns either a playlist"
			+ "or a list of playlists, genres, or artists that exist in the DB",
			"pause: pauses the currently playing song",
			"play: starts playing a song, playlist, genre, artist, or the playlist currently loaded in the player\nFlags: -a <artist_name>\n-g <genre_name>\n-p <playlist_name>"
			+ "\n-s <song_name>\nUsage: play -a <artist_name> - start playing all songs by a specified artist\nplay -g <genre_name> - play all songs of a specific genre"
			+ "\nplay -p <playlist_name> - start playing all songs in specified playlist\nplay -s <song_name> - play the specified song\nplay - play the playlist currently loaded in the player"
			+ " if the current song in the playlist is paused, the song will be resumed, if the song is stopped, the song will restart",
			"previous: play the previous song in the playlist",
			"scan: perform a scan of the current or to be specified directory",
			"seek: seek a specfied number of milliseconds into the currently playing song\nUsage: seek <milliseconds>",
			"stop: stops playing the current song. Current song will restart at the beginning if the play command is called after this command",
			"sv: set the general volume of the player from 0.0 (mute) to 1.0 (max)\nUsage: sv <volume_lvl> NOTE: the specified volume must be in range 0.0 to 1.0"
	};
	
	private CommandExecutionService(){}
	
	public static CommandExecutionService getInstance()
	{
		if(instance == null)
		{
			synchronized(CommandExecutionService.class)
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
	
	public void setDBM(DB_Manager d)
	{
		if(d != null && dbm == null)
			this.dbm = d;
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
				//done
				add(command);
			}
			else if(command.getType() == COMMAND_TYPE.CREATE)
			{
				//done
				create(command);
			}
			else if(command.getType() == COMMAND_TYPE.DELETE)
			{
				//done
				delete(command);
			}
			else if(command.getType() == COMMAND_TYPE.EXIT)
			{
				//done
				System.out.println("Exiting media player...");
				return;
			}
			else if(command.getType() == COMMAND_TYPE.HELP)
			{
				//done
				help(command);
			}
			else if(command.getType() == COMMAND_TYPE.NEXT)
			{
				//done
				next(command);
			}
			else if(command.getType() == COMMAND_TYPE.OPEN)
			{
				//done
				open(command);
			}
			else if(command.getType() == COMMAND_TYPE.PAUSE)
			{
				//done
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
				//done
				previous(command);
			}
			else if(command.getType() == COMMAND_TYPE.SCAN)
			{
				//done
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
				//done
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
			else if(command.getType() == COMMAND_TYPE.SETVOL)
			{
				//done
				setvol(command);
			}
			else if(command.getType() == COMMAND_TYPE.INFO)
			{
				info(command);
			}
			else if(command.getType() == COMMAND_TYPE.NOWPLAYING)
			{
				np(command);
			}
		}else
			System.err.println("No command to parse.");
	}
	
	private void np(AbstractCommand command) {
		// TODO Auto-generated method stub
		
	}

	private void info(AbstractCommand command) {
		// TODO Auto-generated method stub
		
	}

	private void setvol(AbstractCommand c) {
		if(c.getFlags().size() == 1 && c.getFlags().get(0)._Flag == FLAG_TYPE.NOFLAG && player.getMediaPlayer() != null)
		{
			double vol = 0.3;
			try
			{
				vol = Double.parseDouble(c.getFlags().get(0)._Parameter);
			}
			catch(Exception e)
			{
				System.err.println("Not a valid volume value");
				return;
			}
			if(vol < 0.000000000001)
				vol = 0.0;
			else if(1.0 - vol < 0.000000000001)
				vol = 1.0;
			player.getMediaPlayer().setVolume(vol);
		}
		else
			System.err.println("Coudld not set volume");
	}

	//these methods stubbed out pending consultation with team members
	public void add(AbstractCommand c)
	{
		if(c.getFlags().size() == 1 && c.getFlags().get(0)._Flag == FLAG_TYPE.PLAYLIST)
		{
			String[] params = c.getFlags().get(0)._Parameter.split(" ");
			if(params.length == 2)
			{
				Vector<MediaFile> temp = dbm.findAllFilesByName(params[1]);
				if(temp.size() == 1)
				{
					boolean suc = dbm.addToPlaylist(params[0], temp.get(0).musicId);
					if(suc)
					{
						System.out.println("Song added to playlist successfully");
						return;
					}
				}
				else
				{
					System.out.println("--- Multiple entries found ---");
					int count = 0;
					for(MediaFile m : temp)
					{
						System.out.println(count + ". " + m.filename + " (" + m.directory + ")");
						count++;
					}
					System.out.print("Which would you like to add? (0-" + (temp.size() - 1) + "): ");
					try {
						String choice = in.readLine();
						int idx = Integer.parseInt(choice);
						MediaFile m = temp.get(idx);
						boolean succ = dbm.addToPlaylist(params[0], m.musicId);
						if(succ)
						{
							System.out.println("Song \"" + m.filename + "\" successfully added to playlist");
							return;
						}
					} catch (IOException e) {
						System.err.println("FATAL ERROR: Cannot read from STD_IN");
						System.exit(-1);
					}
					catch(Exception e)
					{
						System.err.println("Unable to add song to playlist");
						return;
					}
				}
			}
			else
			{
				System.err.println("Not enough params");
				return;
			}
		}
		else
		{
			System.err.println("Invalid command");
			return;
		}
	}
	
	public void create(AbstractCommand c)
	{
		if(c.getFlags().size() == 1 && c.getFlags().get(0)._Flag == FLAG_TYPE.PLAYLIST)
		{
			String playlist = c.getFlags().get(0)._Parameter;
			boolean success = dbm.createPlaylist(playlist);
			if(success)
				System.out.println("Playlist \"" + playlist + "\" added to database");
			else
				System.err.println("Could not add playlist \"" + playlist + "\" to database");
		}
		else
			System.err.println("Cannot create playlist");
	}
	
	public void delete(AbstractCommand c)
	{
		if(c.getFlags().size() == 1 && c.getFlags().get(0)._Flag == FLAG_TYPE.NOFLAG)
		{
			Vector<MediaFile> temp = dbm.findAllFilesByName(c.getFlags().get(0)._Parameter);
			if(temp.size() == 1)
			{
				boolean success = dbm.deleteMediaFile(temp.get(0).musicId);
				if(success)
				{
					System.out.println("Song \"" + temp.get(0).filename + "\" successfully removed from library");
					return;
				}
			}
			else
			{
				System.out.println("--- Multiple entries found ---");
				int count = 0;
				for(MediaFile m : temp)
				{
					System.out.println(count + ". " + m.filename + " (" + m.directory + ")");
					count++;
				}
				System.out.print("Which would you like to delete? (0-" + (temp.size() - 1) + "): ");
				try {
					String choice = in.readLine();
					int idx = Integer.parseInt(choice);
					MediaFile m = temp.get(idx);
					boolean succ = dbm.deleteMediaFile(m.musicId);
					if(succ)
					{
						System.out.println("Song \"" + m.filename + "\" successfully removed from library");
						return;
					}
				} catch (IOException e) {
					System.err.println("FATAL ERROR: Cannot read from STD_IN");
					System.exit(-1);
				}
				catch(Exception e)
				{
					System.err.println("Unable to remove song from library");
					return;
				}
			}
		}
		else if(c.getFlags().size() == 1 && c.getFlags().get(0)._Flag == FLAG_TYPE.PLAYLIST)
		{
			String[] param = c.getFlags().get(0)._Parameter.split(" ");
			if(param.length == 1)
			{
				boolean succ = dbm.deletePlaylist(param[0]);
				if(succ)
				{
					System.out.println("Playlist \"" + param[0] + "\" successfully removed from library");
					return;
				}
			}
			else if(param.length == 2)
			{
				Vector<MediaFile> temp = dbm.findInPlaylist(param[0], param[1]);
				if(temp.size() == 1)
				{
					boolean suc = dbm.removeFromPlaylist(temp.get(0).musicId, param[0]);
					if(suc)
					{
						System.out.println("Song \"" + param[1] + "\" successfully removed from playlist \"" + param[0] + "\"");
						return;
					}
				}
				else
				{
					System.out.println("--- Multiple entries found ---");
					int count = 0;
					for(MediaFile m : temp)
					{
						System.out.println(count + ". " + m.filename + " (" + m.directory + ")");
						count++;
					}
					System.out.print("Which would you like to delete? (0-" + (temp.size() - 1) + "): ");
					try {
						String choice = in.readLine();
						int idx = Integer.parseInt(choice);
						MediaFile m = temp.get(idx);
						boolean succ = dbm.removeFromPlaylist(m.musicId, param[0]);
						if(succ)
						{
							System.out.println("Song \"" + m.filename + "\" successfully removed from playlist \"" + param[0] + "\"");
							return;
						}
					} catch (IOException e) {
						System.err.println("FATAL ERROR: Cannot read from STD_IN");
						System.exit(-1);
					}
					catch(Exception e)
					{
						System.err.println("Unable to remove song from playlist");
						return;
					}
				}
			}
			else
			{
				System.err.println("Too many command parameters");
				return;
			}
		}
		else
		{
			System.err.println("Invalid command");
			return;
		}
	}
	
	public void help(AbstractCommand c)
	{
		if(c.getFlags().size() == 0)
			for(String s : commands)
				System.out.println(s);
		else if(c.getFlags().size() == 1 && c.getFlags().get(0)._Flag == FLAG_TYPE.NOFLAG)
		{
			String com = c.getFlags().get(0)._Parameter;
			for(String s : com_help)
			{
				if(s.startsWith(com))
				{
					System.out.println(s);
					break;
				}
			}
		}
		else
			System.err.println("Not a valid command");
	}
	
	public void next(AbstractCommand c)
	{
		if(!player.noPlaylist())
		{
			player.skip();
		}
		else
			System.err.println("Cannot skip, no playlist loaded");
	}
	
	public void open(AbstractCommand c)
	{
		if(c.getFlags().size() == 1 && c.getFlags().get(0)._Flag == FLAG_TYPE.NOFLAG)
		{
			String param = c.getFlags().get(0)._Parameter;
			if(param.equalsIgnoreCase("artists") || param.equalsIgnoreCase("genres") || param.equalsIgnoreCase("playlists"))
			{
				Vector<TempMediaFile> temp = param.equalsIgnoreCase("artists") ? dbm.getArtists() : param.equalsIgnoreCase("genres") ? dbm.getGenres() : dbm.getPlaylists();
				System.out.println("--- Results ---");
				for(TempMediaFile t : temp)
					System.out.print(t.filename);
			}
			else if(param.equalsIgnoreCase("music"))
			{
				Vector<TempMediaFile> temp = dbm.getAllMusic();
				player.createPlaylist(temp);
				System.out.println("--- Results ---");
				for(TempMediaFile t : temp)
					System.out.println(t.filename);
			}
			else
			{
				System.err.println("Not a valid parameter");
				return;
			}
		}
		else if(c.getFlags().size() == 1 && c.getFlags().get(0)._Flag == FLAG_TYPE.PLAYLIST)
		{
			Vector<TempMediaFile> temp = dbm.getPlaylist(c.getFlags().get(0)._Parameter);
			player.createPlaylist(temp);
			System.out.println("--- Results ---");
			for(TempMediaFile t : temp)
				System.out.println(t.filename);
		}
		else if(c.getFlags().size() == 1 && c.getFlags().get(0)._Flag == FLAG_TYPE.ARTIST)
		{
			Vector<TempMediaFile> temp = dbm.getPlaylist(c.getFlags().get(0)._Parameter);
			player.createPlaylist(temp);
			System.out.println("--- Results ---");
			for(TempMediaFile t : temp)
				System.out.println(t.filename);
		}
		else if(c.getFlags().size() == 1 && c.getFlags().get(0)._Flag == FLAG_TYPE.GENRE)
		{
			Vector<TempMediaFile> temp = dbm.getPlaylist(c.getFlags().get(0)._Parameter);
			player.createPlaylist(temp);
			System.out.println("--- Results ---");
			for(TempMediaFile t : temp)
				System.out.println(t.filename);
		}
		else
		{
			System.err.println("Not a valid command");;
			return;
		}
	}
	
	public void play(AbstractCommand c)
	{
		
	}
	
	public void previous(AbstractCommand c)
	{
		if(!player.noPlaylist())
		{
			player.previous();
		}
		else
			System.err.println("Cannot go back, no playlist loaded");
	}
	
	public void seek(AbstractCommand c)
	{
		if(player.getMediaPlayer() != null)
		{
			if(c.getFlags().size() == 1 && c.getFlags().get(0)._Flag == FLAG_TYPE.NOFLAG)
				player.getMediaPlayer().seek(Duration.valueOf(c.getFlags().get(0)._Parameter));
			else
			{
				System.err.println("Not a valid command");
				return;
			}
		}
		else
			System.err.println("No media found");
	}
}
