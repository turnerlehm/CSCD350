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
	
	public void playPlaylist(Vector<String> playlist)
	{
		this.playlist = new Vector<MediaPlayer>();
		for(String s : playlist)
			this.playlist.addElement(createPlayer(s));
		this.mp = this.playlist.get(0);
		mp.play();
		for(int i = 0; i < this.playlist.size(); i++)
		{
			
		}
	}
	
	private MediaPlayer createPlayer(String path)
	{
		Media media = new Media(new File(path).toURI().toString());
		MediaPlayer tmp = new MediaPlayer(media);
		tmp.setVolume(0.3);
		tmp.setAutoPlay(true);
		return tmp;
	}
	
	public void playSong(String file)
	{
		String[] parsed = file.split("\\");
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
