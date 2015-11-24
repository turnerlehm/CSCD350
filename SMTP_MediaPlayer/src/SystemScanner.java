
import java.util.*;

import javafx.collections.MapChangeListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.*;

/*
SystemScanner.java

Revision: 2
Revised: 11/16/2015
Last revised: N/A
Revising author(s): Turner Lehmbecker
Author(s): Turner Lehmbecker

A simple file system scanner that scans the file system of a computer (or specified directory) for .mp3 and .wav files. 
This scanner is thread safe.

Maintains a current file list and a library file list for data integrity.
*/

public class SystemScanner
{
   private volatile static SystemScanner instance;
   //private volatile boolean run;
   
   //thread safe list
   private Vector<String> files;
   private Vector<String> paths;
   private Vector<String> lib_files = new Vector<String>();
   private Vector<String> lib_paths = new Vector<String>();
   private volatile DB_Manager dbm;
   private volatile String artist;
   private volatile String genre;
   
   private SystemScanner(){}
   
   public static SystemScanner getInstance()
   {
      if(instance == null)
      {
         synchronized(SystemScanner.class)
         {
            if(instance == null)
               instance = new SystemScanner();
         }
      }
      return instance;
   }
   
   public void setDBM(DB_Manager d)
   {
	   if(d != null)
		   this.dbm = d;
   }
   
   private synchronized void search(String directory, Vector<String> file_list, Vector<String> file_paths)
   {
      if(directory.equalsIgnoreCase("exit"))
      {
         return;
      }
      else
      {
         if(directory.compareTo("") == 0)
         {
            directory = System.getProperty("user.dir");   
         }
         File dir;
         try
         {
            dir = new File(directory);
         }
         catch(Exception e)
         {
            e.printStackTrace();
            return;
         }
         if(dir != null)
         {
            if(!dir.isDirectory())
            {
               if(dir.getName().substring(dir.getName().lastIndexOf('.') + 1).equalsIgnoreCase("mp3") ||
                  dir.getName().substring(dir.getName().lastIndexOf('.') + 1).equalsIgnoreCase("wav"))
               {
                  file_list.add(dir.getName());
                  file_paths.add(dir.toString());
                  lib_files.add(dir.getName());
                  lib_paths.add(dir.toString());
               }
            }
            else
            {
               File[] files = dir.listFiles();
               if(files != null)
               {
                  for(File f : files)
                     search(f.toString(), file_list, file_paths);
               }      
            }
         }
      }
   }
   
   public synchronized void printFileNames()
   {
      System.out.println("Found " + files.size() + " files");
      for(String s : files)
         System.out.println(s);
      notify();
   }
   
   public synchronized void printFilePaths()
   {
      System.out.println("--- Absolute Paths ---");
      for(String s : paths)
         System.out.println(s);
      notify();
   }
   
   public Vector<String> getPaths(){return this.paths;}
   public Vector<String> getFileNames(){return this.files;}
   public Vector<String> getLibrary(){return this.lib_files;}
   public Vector<String> getLibraryPaths(){return this.lib_paths;}
   
   public synchronized void scan(BufferedReader in) throws IOException
   {
      String dir;
      files = new Vector<String>();
      paths = new Vector<String>();
      System.out.println("Enter directory to search (leave blank to search current directory only)");
      System.out.println("Windows: C:" + File.separator + "path" + File.separator + "to" + File.separator + "dir");
      System.out.println("Linux/UNIX: /path/to/dir");
      System.out.println("Type 'exit' to exit");
      System.out.print("$>: ");
      dir = in.readLine();
      System.out.println("Beginning scan...this may take a few minutes depending on directory size");
      search(dir, files, paths);
      System.out.println("Scan complete");
      notify();
   }
   
   private synchronized void addToDB()
   {
	   if(dbm != null)
	   {
		   new javafx.embed.swing.JFXPanel();
		   for(int i = 0; i < files.size(); i++)
		   {
			   artist = "";
			   genre = "";
			   MediaPlayer temp = new MediaPlayer(new Media(paths.get(i)));
			   temp.getMedia().getMetadata().addListener(new MapChangeListener<String, Object>(){
				   public void onChanged(Change<? extends String, ? extends Object> ch)
				   {
					   if(ch.getKey().equals("artist"))
						   artist = ch.getValueAdded().toString();
					   if(ch.getKey().equals("genre"))
						   genre = ch.getValueAdded().toString();
				   }
			   });
			   String fname = files.get(i).substring(0, files.get(i).lastIndexOf('.'));
			   String ext = files.get(i).substring(files.get(i).lastIndexOf('.') + 1);
			   String dir = paths.get(i);
			   dbm.addMedia(new MediaFile(fname, ext, dir, artist, genre));
		   }
	   }
   }
   
   //sanity check
   /*public static void main(String[] args)
   {
      SystemScanner sc = SystemScanner.getInstance();
      sc.scan();
   }*/
}