
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.lang.Object;
/**
 * Created by Sam Arutyunyan on 11/2/2015.
 */
public class Utils //TODO how to prevent object of this type being made (static class?)
{
    public static ArrayList<MediaFile> getMediaFiles(String directoryName, boolean recursive, ArrayList<String> extensions)
    {
        File directory = new File(directoryName);
        ArrayList<MediaFile> names = new ArrayList<>();
        FileFilter textFilter = new FileFilter()
        {
            @Override
            public boolean accept(File file)
            {
                //System.out.println("onaccept: dir=" + .getName());
                if ((file.isDirectory() && recursive) || validateExtension(file.getName(), extensions))
                {
                    return true;
                } else {
                    return false;
                }
            }
        };
        // get all the files from a directory
        File[] fList = directory.listFiles(textFilter);
        for (File file : fList)
        {
            if (file.isFile())
            {


                names.add(new MediaFile(file.getName().substring(0, file.getName().length() - 4),
                            file.getName().substring(file.getName().length() - 3), file.getAbsolutePath()));

            } else if (file.isDirectory())
            {
                names.addAll(getMediaFiles(file.getAbsolutePath(), recursive, extensions));
            }
        }
        return names;
    }
    static boolean validateExtension(String fileName, ArrayList<String> extensions)
    {
        //check if filename contains any of the strings from passed list
        if(extensions == null)//all filetypes
        {
            return true;
        }
        for(String ext : extensions)
        {
            if(fileName.endsWith(ext))
            {
                return true;
            }
        }
        return false;
    }
    public static void printFileNames(String path)
    {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++)
        {
            if (listOfFiles[i].isFile())
            {
                System.out.println("File " + listOfFiles[i].getName());
            } else if (listOfFiles[i].isDirectory())
            {
                System.out.println("Directory " + listOfFiles[i].getName());
            }
        }
    }

}
