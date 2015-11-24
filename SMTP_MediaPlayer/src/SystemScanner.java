
import java.util.*;
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
   private Vector<String> cur_plist;
   private Vector<String> cur_plist_paths;
   
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
   
   public synchronized void search(String directory, Vector<String> file_list, Vector<String> file_paths)
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
   
   //sanity check
   /*public static void main(String[] args)
   {
      SystemScanner sc = SystemScanner.getInstance();
      sc.scan();
   }*/
}