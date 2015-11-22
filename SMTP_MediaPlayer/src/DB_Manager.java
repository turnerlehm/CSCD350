//import javaFX.fxml.FXML;
import javax.swing.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

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
  //expecting to call from parameterless functions, no danger of SQL injection
    private Vector<TempMediaFile> getList(String key, String table, String group)
    {
    	Vector<TempMediaFile> list = new Vector<TempMediaFile>();
        try
        {
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery("SELECT " + key + ", music_id FROM " + table + " GROUP BY " + group + ";");
            while (rs.next())
            {
                String str = rs.getString(key);
                if(str != null) list.add(new TempMediaFile(str, rs.getInt("music_id")));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return list;
    }
    Vector<TempMediaFile> getAllMusic()
    {
        return getList("filename", "music", "music_id");
    }
    Vector<TempMediaFile> getGenres()
    {       
    	Vector<TempMediaFile> tmf = getList("genre", "music", "genre");
    	for(TempMediaFile t : tmf)
    	{
    		t.db_id = -1;
    	}
    	return tmf;    	
    }
    Vector<TempMediaFile> getArtists() 
    {        
    	Vector<TempMediaFile> tmf =  getList("artist", "music", "artist");
    	for(TempMediaFile t : tmf)
    	{
    		t.db_id = -1;
    	}
    	return tmf;
    }
    Vector<TempMediaFile> getPlaylists() 
    {      	
    	Vector<TempMediaFile> list = new Vector<TempMediaFile>();
        try
        {
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery("SELECT playlist_name FROM playlists GROUP BY playlist_id;");
            while (rs.next())
            {
                String str = rs.getString("playlist_name");
                if(str != null) list.add(new TempMediaFile(str, -1));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return list;
    }  

    private Vector<TempMediaFile> getSpecificList(String group, String name) //TODO code in here should be sql injection safe
    {
    	Vector<TempMediaFile> artistMusic = new Vector<TempMediaFile>();
        try 
        {           
            PreparedStatement stmt = c.prepareStatement("SELECT filename, music_id FROM music WHERE lower(" + group + ") = '" + name.toLowerCase() + "';");
            ResultSet rs = stmt.executeQuery();

            // Fetch each row from the result set
            while (rs.next()) {
                String str = rs.getString("filename");
                if(str != null) artistMusic.add(new TempMediaFile(str, rs.getInt("music_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artistMusic;
    }
    Vector<TempMediaFile> getGenre(String genreName) 
    {
        return getSpecificList("genre", genreName);
    }
    
    Vector<TempMediaFile> getArtist(String artistName) 
    {  
        return getSpecificList("artist", artistName);
    }
    
    Vector<TempMediaFile> getPlaylist(String playlistName)
    {
    	Vector<TempMediaFile> playlistMusic = new Vector<TempMediaFile>();
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
                if(str != null) playlistMusic.add(new TempMediaFile(str, curID));
            }
            st.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return playlistMusic;
    }
    
    //the id of the file should be retrieved via TempMediaFile.db_id from findAllFilesByName(string filename);
    String getPath(int id)//TODO this should return a list of all files found with same name
    {
    	 String path = "";
    	 
         Statement st = null;
         try
         {
             st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT directory_path FROM music WHERE music_id = " + id + ";");

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
    int getPlaylistID(String playlistName)
	{
    	int id = -1;
        try 
        {           
            PreparedStatement stmt = c.prepareStatement("SELECT playlist_id FROM playlists WHERE playlist_name = '" + playlistName + "';");
            ResultSet rs = stmt.executeQuery();

            // Fetch each row from the result set
            rs.next();
            id = rs.getInt("playlist_id");          
            
        }
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        return id;
	}
	
	boolean deletePlaylist(int playlist_id)
	{		
		try
        {
            PreparedStatement st = c.prepareStatement("DELETE FROM music WHERE music_id = " + playlist_id + ";");
            st.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    	breakConnections("playlist_id", playlist_id);
    	return true;
	}
	boolean createPlaylist(String playlistName)
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
    boolean deleteMediaFile(int music_id)
    {
    	try
        {
            PreparedStatement st = c.prepareStatement("DELETE FROM music WHERE music_id = " + music_id + ";");
            st.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    	breakConnections("music_id", music_id);
    	return true;
    }
    //removes entry for all playlist_connections to this music id.
    //should be called right after deleteMediaFile()
    private boolean breakConnections(String key, int id)
    {
    	try
        {
            PreparedStatement st = c.prepareStatement("DELETE FROM playlist_connections WHERE " + key + " = " + id + ";");
            st.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    	return true;
    }
    boolean removeFromPlaylist(int music_id, int playlist_id)
    {    	
    	try
        {
            PreparedStatement st = c.prepareStatement("DELETE FROM playlist_connections WHERE music_id = " + music_id + " AND playlist_id = " + playlist_id + ";");
            st.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    	return true;
    }
    //returns full MediaFile object because path must be returned to check for duplicates
    Vector<MediaFile> findAllFilesByName(String filename)
    {    	
    	Vector<MediaFile> list = new Vector<MediaFile>();
        try
        {
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM music WHERE filename = '" + filename + "';");
            while (rs.next())
            {
                String str = rs.getString("filename");
                if(str != null)
                	{
                		list.add(new MediaFile(str, rs.getString("extension"), rs.getString("directory_path"), 
                				rs.getString("artist"), rs.getString("genre"), rs.getInt("music_id")));
                	}
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return list;
    }
    //meant to be used with delete command to check for duplicates 
    //delete music1 -p workout; if music1 has duplicate entries further prompts
    Vector<MediaFile> findInPlaylist(String playlistName, String musicName)
    {
    	Vector<MediaFile> list = new Vector<MediaFile>();
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
                ResultSet rs2 = st2.executeQuery("SELECT filename FROM music WHERE music_id = " + curID + " AND filename = '" + musicName + "';");
                rs2.next();
                String str = rs2.getString("filename");
                if(str != null)
            	{
            		list.add(new MediaFile(str, rs.getString("extension"), rs.getString("directory_path"), 
            				rs.getString("artist"), rs.getString("genre"), rs.getInt("music_id")));
            	}
            }
            st.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return list;
    }
}