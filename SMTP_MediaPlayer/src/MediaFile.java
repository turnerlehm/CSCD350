/**
 * Created by Sam Arutyunyan on 11/2/2015.
 */
public class MediaFile
{
    String filename;
<<<<<<< HEAD
    String ext;
    int id;
    String artist;
    String directory;
    String genre;
    public MediaFile(String file_name, String extension, String path)
    {
        filename = file_name;
        ext = extension;
        directory = path;
=======
    String ext;    
    String artist;
    String directory;
    String genre;
    int musicId;//used in rare case of checking duplicates and needing object with directory
    public MediaFile(String file_name, String extension, String path, String artist, String genre)
    {
    	this(file_name, extension, path, artist, genre, -1);
    }
    public MediaFile(String file_name, String extension, String directoryPath, String artist, String genre, int musicId)
    {
        filename = file_name;
        ext = extension;
        directory = directoryPath;
        this.artist = artist;
        this.genre = genre;
        this.musicId = musicId;
>>>>>>> refs/remotes/origin/master
    }

    public String getFilename()
    {
        return filename;
    }

    public void setFilename(String filename)
    {
        this.filename = filename;
    }

    public String getExt()
    {
        return ext;
    }

    public void setExt(String ext)
    {
        this.ext = ext;
    }

<<<<<<< HEAD
    public int getId()
    {
        return id;
=======
    public int getMusicId()
    {
        return musicId;
>>>>>>> refs/remotes/origin/master
    }

    public void setId(int id)
    {
<<<<<<< HEAD
        this.id = id;
=======
        this.musicId = id;
>>>>>>> refs/remotes/origin/master
    }

    public String getArtist()
    {
        return artist;
    }

    public void setArtist(String artist)
    {
        this.artist = artist;
    }

    public String getDirectory()
    {
        return directory;
    }

    public void setDirectory(String directory)
    {
        this.directory = directory;
    }

    public String getGenre()
    {
        return genre;
    }

    public void setGenre(String genre)
    {
        this.genre = genre;
    }
}
<<<<<<< HEAD
=======

>>>>>>> refs/remotes/origin/master
