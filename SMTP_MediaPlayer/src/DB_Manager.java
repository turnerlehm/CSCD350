
import javax.swing.*;
import java.sql.*;
import java.util.Vector;

/**
	DB_Manager.java
	Revision: 5
	Revised: 11/3/2015
	Last revised: 11/22/2015
	Revising author(s): Sam Arutyunyan
	Author: Sam Arutyunyan
	Description: DB_Manager handles connecting to, retrieving and storing items to a local SQLite database
*/
public class DB_Manager
{
    Connection c;
    //eager instance because we always use this singleton
    private static DB_Manager instance = new DB_Manager();    
    //private constructor, this is a singleton 
    private DB_Manager(){}
    public static DB_Manager getInstance()
    {
        return instance;
    }
    
    /**
     * Initializes the database connection
     *
     * Creates a Connection object that connects to a local sqlite database file.
     * This connection object can be used in the rest of the database methods to communicate with the database
     */
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
            JOptionPane.showMessageDialog(null, "error in DBM init()");
            System.exit(0);
        }
    }
    
    /**
     * Shuts down the database connection
     *
     * Closes the connection object and does any other cleanup necessary for closing.
     */
    public void shutdown()
    {
        if(c != null)
        {
            try
            {
                c.close();
            } 
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            c = null;
        }
    }

    /**
     * Searches database for all files or groups of the specified parameters
     * 
     * Intended to be wrapped by specific group function calls by the user
     * Because wrapper methods are parameterless, there is no risk of SQL injection
     * 
     * @param key the main field to select from table
     * @param table the table from which music is selected
     * @param group a grouping system to prevent duplicates (pass music_id to ignore grouping)
     * @return Returns a list of TempMediaFile from the database.
     */
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
            rs.close();
            st.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return list;
    }
    
    /**
     * Searches database for all music files
	 *
     * @return Returns a list of TempMediaFile from the database. The file objects store music name in .filename string
     */
    Vector<TempMediaFile> getAllMusic()
    {
        return getList("filename", "music", "music_id");
    }
    
    /**
     * Searches database for all genre names
	 *
     * @return Returns a list of TempMediaFile from the database. The file objects store genre name in .filename string
     */
    Vector<TempMediaFile> getGenres()
    {       
    	Vector<TempMediaFile> tmf = getList("genre", "music", "genre");
    	for(TempMediaFile t : tmf)
    	{
    		t.musicId = -1;
    	}
    	return tmf;    	
    }
    
    /**
     * Searches database for all artist names
	 *
     * @return Returns a list of TempMediaFile from the database. The file objects store artist name in .filename string
     */
    Vector<TempMediaFile> getArtists() 
    {        
    	Vector<TempMediaFile> tmf =  getList("artist", "music", "artist");
    	for(TempMediaFile t : tmf)
    	{
    		t.musicId = -1;
    	}
    	return tmf;
    }
    
    /**
     * Searches database for all playlist names
	 *
     * @return Returns a list of TempMediaFile from the database. The file objects store playlist name in .filename string
     */
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
            rs.close();
            st.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return list;
    }  

    /**
     * Searches database for music files of the specified parameters
     * 
     * Intended to be wrapped by specific group function calls by the user
     *      
     * @param group the specific group to retrieve music files from (eg: genre, artist)
     * @param name specific subgroup name from group (eg: group=genre, name=techno)
     * 
     * @return Returns a list of TempMediaFile from the database, storing filename and musicId.
     */
    private Vector<TempMediaFile> getSpecificList(String group, String name) 
    {
    	Vector<TempMediaFile> artistMusic = new Vector<TempMediaFile>();
        try 
        {           
            //PreparedStatement st = c.prepareStatement("SELECT filename, music_id FROM music WHERE lower(?) = ?;");
            PreparedStatement st = c.prepareStatement("SELECT filename, music_id FROM music WHERE " + group + " = ?;");            
            st.setString(1, name.toLowerCase());
            ResultSet rs = st.executeQuery();
            
            // Fetch each row from the result set
            while (rs.next()) 
            {
                String str = rs.getString("filename");
                if(str != null) artistMusic.add(new TempMediaFile(str, rs.getInt("music_id")));
            }
            rs.close();
            st.close();
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        return artistMusic;
    }
    
    /**
     * Searches database for all music files belonging to a specific genre
	 * @param genreName the specific genre to filter music by
	 *
     * @return Returns a list of TempMediaFile from the database, which store filename and music_id
     */
    Vector<TempMediaFile> getGenre(String genreName) 
    {
        return getSpecificList("genre", genreName);
    }
    
    /**
     * Searches database for all music files belonging to a specific artist
	 * @param artistName the specific artist to filter music by
	 *
     * @return Returns a list of TempMediaFile from the database, which store filename and music_id
     */
    Vector<TempMediaFile> getArtist(String artistName) 
    {  
        return getSpecificList("artist", artistName);
    }
        
    /**
     * Searches database for all music files belonging to a specific playlist
     * 
	 * @param playlistName the specific playlistName to filter music by
	 *
     * @return Returns a list of TempMediaFile from the database, which store filename and music_id
     */
    Vector<TempMediaFile> getPlaylist(String playlistName)
    {
    	Vector<TempMediaFile> playlistMusic = new Vector<TempMediaFile>();        
		try
		{		    
		    int id = getPlaylistId(playlistName);
		    if(id > -1)
		    {
		    	Statement st = c.createStatement();
			    ResultSet rs = st.executeQuery("SELECT music_id FROM playlist_connections WHERE playlist_id = " + id + ";");
			    if(rs.isBeforeFirst())
			    {
			    	while (rs.next())
				    {
				        int curID = rs.getInt("music_id");
				        Statement st2 = c.createStatement();
				        ResultSet rs2 = st2.executeQuery("SELECT filename FROM music WHERE music_id = " + curID + ";");
				        rs2.next();
				        String str = rs2.getString("filename");
				        if(str != null) 
			        	{
			        		playlistMusic.add(new TempMediaFile(str, curID));
			        	}
				        rs2.close();
				        st2.close();
				    }
			    	rs.close();
				    st.close();
			    }			    
		    }		    
		} 
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return playlistMusic;
    }
    
    /**
     * retrieves the directory path of a music file by it's id
     * 
     * the id of the file should be retrieved via TempMediaFile.musicId from findAllFilesByName(string filename);
     * 
	 * @param id the unique musicId to look up from the database
	 *
     * @return Returns a String holding the absolute directory path
     */
    String getPath(int id)
    {
    	 String path = "";
    	          
         try
         {
        	 Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT directory_path FROM music WHERE music_id = " + id + ";");

             rs.next();             
             path = rs.getString("directory_path");
             if(path == "" || path == null)
        	 {
        	 	path = null;
        	 }
             rs.close();
             st.close();             
         } catch (SQLException e)
         {
             e.printStackTrace();
         }

         return path;
    }
    
    /**
     * retrieves the unique playlist_id by it's name
     *  
	 * @param playlistName the name of het playlist being searched
	 *
     * @return Returns a int playlist id
     */
    private int getPlaylistId(String playlistName)
	{
    	int id = -1;
        try 
        {           
            PreparedStatement st = c.prepareStatement("SELECT playlist_id FROM playlists WHERE LOWER(playlist_name) = ?;");
            st.setString(1, playlistName.toLowerCase());
            ResultSet rs = st.executeQuery();
            
            if(rs.isBeforeFirst())
            {
            	rs.next();
                id = rs.getInt("playlist_id");
            }
            rs.close();
            st.close();            
        }
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        return id;
	}
	
    /**
     * Removes a playlist from the database
     *  
     *  Also automatically removes any files connected to that playlist from playlist_connections table
     *  
	 * @param playlist_id unique playlist id to lookup the playlist to be deleted
	 *
     * @return Returns a true if successfully deleted
     */
	boolean deletePlaylist(String name)
	{		
		int playlist_id = getPlaylistId(name);
		try
        {
            PreparedStatement st = c.prepareStatement("DELETE FROM playlists WHERE playlist_id = " + playlist_id + ";");
            st.executeUpdate();
            st.close();            
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    	breakConnections("playlist_id", playlist_id);
    	return true;
	}
	
	/**
     * Adds a new playlist to the database
     *  
	 * @param playlistName the name of hte playlist to be created
	 *
     * @return Returns a true if successfully added
     */
	boolean createPlaylist(String playlistName)
    {
        try
        {
            PreparedStatement st = c.prepareStatement("INSERT INTO playlists (playlist_name) VALUES(?);");
            st.setString(1, playlistName);
            st.executeUpdate();
            st.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
	/**
     * Adds a music file to a playlist
     *
     * assumes both playlist and music file have already been added to database and just makes
     * a connection between them
     * 
	 * @param playlistId the unique of the playlist to be added to
	 * @param musicId the unique of the music file to be added
	 * 
     * @return Returns a true if successfully added
     */
    boolean addToPlaylist(String playlist_name, int musicID)
    {             
    	int playlistId = getPlaylistId(playlist_name);
        int result = 0;                
        try
        {
            PreparedStatement st = c.prepareStatement("INSERT INTO playlist_connections (music_id, playlist_id) VALUES(?,?);");
            st.setInt(1, musicID);
            st.setInt(2, playlistId);
            result = st.executeUpdate();
            st.close();            
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        if(result == 0) return false; else return true;
    }
    
    /**
     * Adds a music file to the database
     * 
	 * @param media a MediaFile object which holds all the fields a music file can save
	 * 
     * @return Returns the unique music_id that is automatically generated
     */
    int addMedia(MediaFile media)
    {
        try
        {
            PreparedStatement st = c.prepareStatement(
                    "INSERT INTO music (filename, extension, directory_path, artist, genre) VALUES(?,?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
            st.setString(1, media.getFilename());
            st.setString(2, media.getExt());
            st.setString(3, media.getDirectory());
            st.setString(4, media.getArtist());            
            st.setString(5, media.getGenre());
            st.executeUpdate();
            ResultSet rs = st.getGeneratedKeys();
            rs.next();
            int id =  rs.getInt(1);
            rs.close();
            st.close();            
            return id;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return -1;
        }
    }
    
    /**
     * Removes a music file from the database
     * 
     * All playlist entries for This music file will also be removed
     * 
	 * @param musicId the unique id of the music file to be removed
	 * 
     * @return Returns true if successfully removed
     */
    boolean deleteMediaFile(int musicId)
    {
    	try
        {
            PreparedStatement st = c.prepareStatement("DELETE FROM music WHERE music_id = " + musicId + ";");
            st.executeUpdate();
            st.close();            
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    	breakConnections("music_id", musicId);
    	return true;
    }    
    /**
     * Removes all entries in playlist_connections for a specific id
     * 
     * Can be called after deleting a media file or when deleting a playlist
     * Will remove all either by playlistId or musicId
     * 
	 * @param id the unique id of the music file or playlist to be removed
	 * @param key specifies which field we remove by, either playlist_id or music_id
	 * 
     * @return Returns true if successfully removed
     */
    private boolean breakConnections(String key, int musicId)
    {
    	try
        {
            PreparedStatement st = c.prepareStatement("DELETE FROM playlist_connections WHERE " + key + " = " + musicId + ";");
            st.executeUpdate();
            st.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    	return true;
    }
    
    /**
     * Removes a specific file from a specific playlist
     * 
	 * @param musicId the unique id of the music file to be removed from a playlist
	 * @param playlistId the unique id of the playlist from which the music is being removed
	 * 
     * @return Returns true if successfully removed
     */
    boolean removeFromPlaylist(int musicId, int playlistId)
    {    	
    	try
        {
            PreparedStatement st = c.prepareStatement("DELETE FROM playlist_connections WHERE music_id = " + musicId + " AND playlist_id = " + playlistId + ";");
            st.executeUpdate();
            st.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    	return true;
    }

    /**
     * Finds all music files found for specific filename.
     * 
     * Returns full MediaFile object because path must be returned to check for duplicates
     * 
	 * @param filename the name of the music file to be looked up in the database
	 * 
     * @return Returns Vector<MediaFile> containing all entries found for the filename
     */
    Vector<MediaFile> findAllFilesByName(String filename)
    {    	
    	Vector<MediaFile> list = new Vector<MediaFile>();
        try
        {
            PreparedStatement st = c.prepareStatement("SELECT * FROM music WHERE lower(filename) = ?;");
            st.setString(1, filename.toLowerCase());               
            ResultSet rs = st.executeQuery();
            while (rs.next())
            {
                String str = rs.getString("filename");
                if(str != null)
            	{
            		list.add(new MediaFile(str, rs.getString("extension"), rs.getString("directory_path"), 
            				rs.getString("artist"), rs.getString("genre"), rs.getInt("music_id")));
            	}
            }
            st.close();
            rs.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Finds all music files with specific filename which are attached to a specific playlist
     * 
     * Returns full MediaFile object because path must be returned to check for duplicates
     * 
	 * @param musicName the name of the music file to be looked up in the playlist
	 * @param playlistName the name of the playlist to be searched
	 * 
     * @return Returns Vector<MediaFile> containing all entries found for the filename
     */
    Vector<MediaFile> findInPlaylist(String playlistName, String musicName)
    {
    	Vector<MediaFile> list = new Vector<MediaFile>();
        try
        {
            int pId = getPlaylistId(playlistName);
            Vector<MediaFile> musics = findAllFilesByName(musicName);
            

            for(MediaFile mf : musics)
            {            	
            	Statement st = c.createStatement();
                ResultSet rs = st.executeQuery("SELECT music_id FROM playlist_connections WHERE playlist_id = " + pId + " AND music_id = " + mf.musicId + ";");
                if(rs.isBeforeFirst())
                {
            		list.add(new MediaFile(mf.filename, mf.ext, mf.directory, 
            				mf.artist, mf.genre, mf.musicId));
            	}
                rs.close();
                st.close();
            }
            
        } 
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return list;
    }
    MediaFile getInfo(int musicId)
    {
    	MediaFile mf = null;
        try
        {	
        	Statement st = c.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM music WHERE music_id = " + musicId + ";");
            if(rs.isBeforeFirst())
            {
        		mf = new MediaFile(rs.getString("filename"), rs.getString("extension"), rs.getString("directory_path"), rs.getString("artist"),
        				rs.getString("genre"), musicId);
        	}
            rs.close();
            st.close();           
            
        } 
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return mf;
    }
}
/** Summary
    static DB_Manager 		getInstance();    
    void 					init();//call at start of main
    void 					shutdown();//call at end of main
    Vector<TempMediaFile> 	getAllMusic();//returns all music files in entire database
    Vector<TempMediaFile> 	getGenres();//returns all genre names
    Vector<TempMediaFile> 	getArtists();//returns all artist names
    Vector<TempMediaFile> 	getPlaylists();//returns all playlist names
    Vector<TempMediaFile> 	getGenre(String genreName);//returns all music files belonging to a specific genre
    Vector<TempMediaFile> 	getArtist(String artistName);//returns all music files belonging to a specific artist
    Vector<TempMediaFile> 	getPlaylist(String playlistName);//returns all music files belonging to a specific playlist
    String 					getPath(int id);//retrieves the directory path of a music file by it's id    
	boolean 				deletePlaylist(String name);//Removes a playlist from the database
	boolean 				createPlaylist(String playlistName);//Adds a new playlist to the database 
    boolean 				addToPlaylist(int playlistId, int musicID);// Adds a music file to a playlist    
    int 					addMedia(MediaFile media);//Adds a music file to the database
    boolean 				deleteMediaFile(int musicId)//Removes a music file from the database
    boolean 				removeFromPlaylist(int musicId, int playlistId);//Removes a specific file from a specific playlist
    Vector<MediaFile> 		findAllFilesByName(String filename);//Finds all music files found for specific filename.
    Vector<MediaFile> 		findInPlaylist(String playlistName, String musicName);//Finds all music files with specific filename which are attached to a specific playlist
*/