/**
 * Created by Sam Arutyunyan on 11/2/2015.
 */
public class MediaFile
{
    String filename;
    String ext;    
    String artist;
    String directory;
    String genre;
    int id;//used in rare case of checking duplicates and needing object with directory
    public MediaFile(String file_name, String extension, String path, String artist, String genre)
    {
    	this(file_name, extension, path, artist, genre, -1);
    }
    public MediaFile(String file_name, String extension, String path, String artist, String genre, int music_id)
    {
        filename = file_name;
        ext = extension;
        directory = path;
        this.artist = artist;
        this.genre = genre;
        id = music_id;
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

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
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

