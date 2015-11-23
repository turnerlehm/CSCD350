//import javaFX.fxml.FXML;
import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Turtle on 11/3/2015.
 */
public class DB_Manager
{
    Connection c;
    private static DB_Manager instance = new DB_Manager();//eager instance (we always use this singleton)    
    private DB_Manager(){}
    public static DB_Manager getInstance()
    {
        return instance;
    }
    public void init()
    {
        c = null;
        try
        {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:media_database.db");
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "error in DBM init");
            System.exit(0);
        }
    }
    public void shutdown()
    {
        if(c != null)
        {
            try
            {
                c.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
            c = null;
        }
    }
    List<String> getAllMusic()
    {
        List<String> allMusic = new ArrayList<String>();
        try
        {
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery("SELECT filename FROM music;");
            while (rs.next())
            {
                String str = rs.getString("filename");
                allMusic.add(str);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return allMusic;
    }
    List<String> getGenres()
    {
        List<String> genres = new ArrayList<String>();
        PreparedStatement st = null;
        try {
            st = c.prepareStatement("SELECT genre FROM music GROUP BY genre;");
            ResultSet rs = st.executeQuery();

            // Fetch each row from the result set
            while (rs.next())
            {
                String str = rs.getString("genre");

                genres.add(str);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return genres;
    }

    List<String> getGenre(String genreName) {
        List<String> genreMusic = new ArrayList<String>();
        PreparedStatement stmt = null;
        try {

            stmt = c.prepareStatement("SELECT * FROM music WHERE lower(genre) = '" + genreName.toLowerCase() + "';");
            ResultSet rs = stmt.executeQuery();

            // Fetch each row from the result set
            while (rs.next()) {
                String str = rs.getString("filename");

                genreMusic.add(str);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genreMusic;
    }

    List<String> getArtists() {
        List<String> artists = null;
        try {
            artists = new ArrayList<String>();
            String artistName = "mozart";
            PreparedStatement stmt = c.prepareStatement("SELECT artist FROM music GROUP BY artist;");
            ResultSet rs = stmt.executeQuery();

            // Fetch each row from the result set
            while (rs.next()) {
                String str = rs.getString("artist");

                artists.add(str);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artists;
    }

    List<String> getArtist(String artistName) {
        List<String> artistMusic = null;
        try {
            artistMusic = new ArrayList<String>();
            PreparedStatement stmt = c.prepareStatement("SELECT * FROM music WHERE lower(artist) = '" + artistName.toLowerCase() + "';");
            ResultSet rs = stmt.executeQuery();

            // Fetch each row from the result set
            while (rs.next()) {
                String str = rs.getString("filename");

                artistMusic.add(str);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artistMusic;
    }

    List<String> getPlaylists() {
        List<String> playlistMusic = null;
        try {
            playlistMusic = new ArrayList<String>();
            PreparedStatement stmt = c.prepareStatement("SELECT playlist_name FROM playlists;");
            ResultSet rs = stmt.executeQuery();

            // Fetch each row from the result set
            while (rs.next()) {
                String str = rs.getString("playlist_name");

                playlistMusic.add(str);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playlistMusic;
    }

    List<String> getPlaylist(String playlistName)
    {

        List<String> playlistMusic = new ArrayList<String>();
        Statement st = null;
        try
        {
            st = c.createStatement();
            ResultSet rs = st.executeQuery("SELECT playlist_id FROM playlists WHERE lower(playlist_name) = '" + playlistName.toLowerCase() + "';");

            rs.next();
            int id = rs.getInt("playlist_id");//return single playlist id

            rs = st.executeQuery("SELECT music_id FROM playlist_connections WHERE playlist_id = " + id + ";");

            while (rs.next())
            {
                int curID = rs.getInt("music_id");
                Statement st2 = c.createStatement();
                ResultSet rs2 = st2.executeQuery("SELECT filename FROM music WHERE music_id = " + curID + ";");
                rs2.next();
                String str = rs2.getString("filename");
                playlistMusic.add(str);
            }
            st.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return playlistMusic;
    }
    
    String getPath(String fileName)
    {
    	 String path = "";
    	 
         Statement st = null;
         try
         {
             st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT directory_path FROM music WHERE lower(filename) = '" + fileName.toLowerCase() + "';");

             rs.next();
             
             path = rs.getString("directory_path");//return single playlist id
             if(path == "" || path == null) path = null;
             st.close();
         } catch (SQLException e)
         {
             e.printStackTrace();
         }

         return path;
    }
    int getPlaylistID(String playlistName)//TODO implement
	{
		return -1;
	}
	
	boolean deletePlaylist(String playlistName)//TODO implement
	{
		
		return true;		
	}
	boolean createPlaylist(String playlistName)//TODO implement
    {
        try
        {
            PreparedStatement statement = c.prepareStatement("INSERT INTO playlists (playlist_name) VALUES(?);");
            statement.setString(1, playlistName);
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    //assumes both playlist and music file have already been added to database and just makes
    //a connection between them
    boolean addToPlaylist(String newPlaylist, int musicID)
    {
        //get playlist id
        int pId = -1;
        int result = 0;
        try
        {
            PreparedStatement st = c.prepareStatement("SELECT playlist_id FROM playlists WHERE playlist_name = ?;");
            st.setString(1, newPlaylist);
            ResultSet rs = st.executeQuery();

            rs.next();
            pId = rs.getInt("playlist_id");


        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        System.out.println("playlist: " + newPlaylist + ", pId: " + pId + " musicID: " + musicID);

        //add playlist_id/music_id combos to playlist_coneections
        try
        {
            PreparedStatement st = c.prepareStatement("INSERT INTO playlist_connections (music_id, playlist_id) VALUES(?,?);");
            st.setInt(1, musicID);
            st.setInt(2, pId);
            result = st.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        if(result == 0) return false; else return true;
    }
    //returns music_id that is automatically generated
    int addMedia(MediaFile media)
    {
        try
        {
            PreparedStatement st = c.prepareStatement(
                    "INSERT INTO music (filename, extension, artist, directory_path, genre) VALUES(?,?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
            st.setString(1, media.getFilename());
            st.setString(2, media.getExt());
            st.setString(3, media.getArtist());
            st.setString(4, media.getDirectory());
            st.setString(5, media.getGenre());
            st.executeUpdate();
            ResultSet rs = st.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return -1;
        }
    }
}