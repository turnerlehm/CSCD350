import java.io.File;

import javafx.scene.media.*;

public class AudioPlayer 
{
	private static volatile AudioPlayer instance;
	private volatile MediaPlayer mp;
	
	private AudioPlayer(){}
	
	public static AudioPlayer getInstance()
	{
		if(instance == null)
		{
			synchronized(AudioPlayer.class)
			{
				if(instance == null)
					instance = new AudioPlayer();
			}
		}
		return instance;
	}
	
	public void playAudio(String file)
	{
		new javafx.embed.swing.JFXPanel();
		String URI = new File(file).toURI().toString();
		mp = new MediaPlayer(new Media(URI));
		mp.setVolume(0.3);
		mp.setAutoPlay(true);
		mp.play();
		if(mp.getCurrentTime().greaterThanOrEqualTo(mp.getTotalDuration()))
			return;
	}
}
