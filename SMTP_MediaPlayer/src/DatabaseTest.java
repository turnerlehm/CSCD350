import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class DatabaseTest 
{
	DB_Manager dbm;
	@Before
	public void setUp() throws Exception
	{
		dbm = DB_Manager.getInstance();
		dbm.init();		
	}

	@After
	public void tearDown() throws Exception
	{
		dbm.shutdown();		
	}

	@Test
	public void test_getAllMusic() 
	{
		Vector<TempMediaFile> ls = dbm.getAllMusic();
		assertNotEquals(ls.size(), 0);		
	}
	@Test
	public void test_getGenres() 
	{
		Vector<TempMediaFile> ls = dbm.getGenres();
		assertNotEquals(ls.size(), 0);				
	}
	@Test
	public void test_getArtists() 
	{
		Vector<TempMediaFile> ls = dbm.getArtists();
		assertNotEquals(ls.size(), 0);		
	}
	@Test
	public void test_getPlaylists() 
	{
		Vector<TempMediaFile> ls = dbm.getPlaylists();
		assertNotEquals(ls.size(), 0);		
	}
	@Test
	public void test_getGenre() 
	{
		Vector<TempMediaFile> ls = dbm.getGenre("classical");
		assertNotEquals(ls.size(), 0);		
	}
	@Test
	public void test_getArtist() 
	{
		Vector<TempMediaFile> ls = dbm.getArtist("daft");
		assertNotEquals(ls.size(), 0);		
	}	 
	@Test
	public void test_getPlaylist() 
	{
		Vector<TempMediaFile> ls = dbm.getPlaylist("Relaxing");
		assertNotEquals(ls.size(), 0);		
	}
	@Test
	public void test_getPath() 
	{				
		Vector<MediaFile> ls = dbm.findAllFilesByName("moonlight sonata");
		System.out.println("size: " + ls.size());
		assertEquals(dbm.getPath(ls.get(0).musicId), "c:/music");	
	}
	@Test
	public void test_getPlaylistID() 
	{
		assertEquals(dbm.getPlaylistID("relaxing"), 1);
	}
	@Test
	public void test_deleteAndCreatePlaylist() 
	{
		dbm.createPlaylist("abc_testPlaylist");
		int id = dbm.getPlaylistID("abc_testPlaylist");
		assertNotEquals(id, -1);
		System.out.println("playlist id to delete: " + id);	
	
		dbm.deletePlaylist(id);
		assertEquals(dbm.getPlaylistID("abc_testPlaylist"), -1);
	}
	@Test
	public void test_addToAndRemoveFromPlaylist() 
	{
		dbm.addToPlaylist(1, 3);
		assertNotEquals(dbm.findInPlaylist("Relaxing", "Strobe").size(), 0);
		dbm.removeFromPlaylist(3, 1);
		assertEquals(dbm.findInPlaylist("Relaxing", "Strobe").size(), 0);
	}
	@Test
	public void test_addAndDeleteMedia() 
	{
		int id = dbm.addMedia(new MediaFile("test_filename", "test_ext", "test_path", "test_artist", "test_genre"));
		assertNotEquals(id, -1);
		Vector<MediaFile> ls = dbm.findAllFilesByName("test_filename");
		assertNotEquals(ls.size(), 0);
		if(ls.size() > 0)
		{
			MediaFile testMedia = ls.get(0);
			assertEquals(testMedia.filename, "test_filename");
			assertEquals(testMedia.ext, "test_ext");
			assertEquals(testMedia.directory, "test_path");
			assertEquals(testMedia.artist, "test_artist");
			assertEquals(testMedia.genre, "test_genre");	
		}			
		
		dbm.deleteMediaFile(id);
		assertEquals(dbm.findAllFilesByName("test_media").size(), 0);
	}
	@Test
	public void test_findAllFilesByName() 
	{
		assertEquals(dbm.findAllFilesByName("strobe").size(), 1);
		
	}
	@Test
	public void test_findInPlaylist() 
	{
		assertEquals(dbm.findInPlaylist("Relaxing", "moonlight sonata").size(), 1);
	}

}
