import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Vector;

import javafx.scene.media.*;

public class Player 
{
	private static volatile Player instance;
	private volatile MediaPlayer mp;
	private Vector<MediaPlayer> playlist;
	private volatile DB_Manager dbm;
	private volatile int cur_idx;
	private Vector<TempMediaFile> files;
	
	private Player(){}
	
	public static Player getInstance()
	{
		if(instance == null)
		{
			synchronized(Player.class)
			{
				if(instance == null)
					instance = new Player();
			}
		}
		return instance;
	}
	
	public MediaPlayer getMediaPlayer(){return this.mp;}
	
	public void setDBM(DB_Manager d){this.dbm = d;}
	
	
	public void skip()
	{
		MediaPlayer cur = mp;
		cur.stop();
		MediaPlayer next = playlist.get((playlist.indexOf(cur) + 1) % playlist.size());
		mp = next;
		cur_idx = (cur_idx + 1) % playlist.size();
		next.play();
	}
	
	public void previous()
	{
		MediaPlayer cur = mp;
		cur.stop();
		MediaPlayer next;
		if(playlist.indexOf(cur) == 0)
		{
			next = playlist.get(playlist.size() - 1);
			cur_idx = playlist.size() - 1;
		}
		else
		{
			next = playlist.get(playlist.indexOf(cur) - 1);
			cur_idx--;
		}
		next.play();
	}
	
	public void createPlaylist(Vector<TempMediaFile> plist)
	{
		if(dbm != null)
		{
			this.playlist = new Vector<MediaPlayer>();
			for(TempMediaFile t : plist)
			{
				String path = dbm.getPath(t.musicId);
				MediaPlayer temp = createPlayer(path);
				if(temp != null)
					this.playlist.add(temp);
				else
				{
					dbm.deleteMediaFile(t.musicId);
					plist.remove(t);
				}
			}
			this.files = plist;
		}
		else
		{
			System.err.println("Database manager not initialized");
			return;
		}
	}
	
	public void startPlaying()
	{
		mp = playlist.get(0);
		getMediaPlayer().play();
		//nowPlaying(mp);
	}
	
	public void playPlaylist()
	{
		for(int i = 0; i < this.playlist.size(); i++)
		{
			MediaPlayer player = this.playlist.get(i);
			MediaPlayer next = this.playlist.get((i + 1) % this.playlist.size());
			player.setOnEndOfMedia(new Runnable() 
			{ public void run(){
						player.stop();
						mp = next;
						next.play();
						//nowPlaying(next);
				}
			});
		}
	}
	
	/*public void nowPlaying(MediaPlayer m)
	{
		String file = m.getMedia().getSource();
		file = file.substring(0, file.lastIndexOf('.'));
		file = file.substring(file.lastIndexOf('/') + 1).replaceAll("%20", " ");
		file = file.replace("file:", "");
		System.out.println("--- Now Playing: " +file+ " ---");
	}*/
	
	private MediaPlayer createPlayer(String path)
	{
		new javafx.embed.swing.JFXPanel();
		try
		{
			FileReader temp = new FileReader(path);
		}
		catch(Exception e)
		{
			return null;
		}
		Media media = new Media(new File(path).toURI().toString());
		MediaPlayer tmp = new MediaPlayer(media);
		tmp.setVolume(0.3);
		//tmp.setAutoPlay(true);
		return tmp;
	}
	
	public void playSong(String file)
	{
		String[] parsed = file.split(File.pathSeparator);
		if(parsed.length != 0)
			System.out.println("--- Now Playing: " + parsed[parsed.length - 1] + " ---");
		else
			System.out.println("--- Now Playing: " + file + " ---");
		new javafx.embed.swing.JFXPanel();
		String URI = new File(file).toURI().toString();
		mp = new MediaPlayer(new Media(URI));
		mp.setVolume(0.3);
		mp.setAutoPlay(true);
		mp.play();
	}

	public boolean noPlaylist() {
		return playlist.size() == 0;
	}
}
