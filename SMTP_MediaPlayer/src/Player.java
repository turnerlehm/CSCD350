import java.io.File;
import java.util.Vector;

import javafx.scene.media.*;

public class Player 
{
	private static volatile Player instance;
	private volatile MediaPlayer mp;
	private Vector<MediaPlayer> playlist;
	
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
	
	public void createPlaylist(Vector<String> plist)
	{
		this.playlist = new Vector<MediaPlayer>();
		for(String s : plist)
			this.playlist.add(createPlayer(s));
	}
	
	public void startPlaying()
	{
		mp = playlist.get(0);
		getMediaPlayer().play();
		nowPlaying(mp);
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
						nowPlaying(next);
				}
			});
		}
	}
	
	public void nowPlaying(MediaPlayer m)
	{
		String file = m.getMedia().getSource();
		file = file.substring(0, file.lastIndexOf('.'));
		file = file.substring(file.lastIndexOf('/') + 1).replaceAll("%20", " ");
		file = file.replace("file:", "");
		System.out.println("--- Now Playing: " +file+ " ---");
	}
	
	private MediaPlayer createPlayer(String path)
	{
		new javafx.embed.swing.JFXPanel();
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
}
