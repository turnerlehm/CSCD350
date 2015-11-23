package command;

import command.commands.*;

public class CommandParser
{
   private static CommandParser instance = null;

   private CommandParser()
   {
      //not to be called;
   }
   
   public static CommandParser getInstance()
   {
      if(instance == null)
      {
         instance = new CommandParser();
      }
      return instance;
   }
   
   public Command parseCommand(String commandToParse) throws InvalidCommandException
   {
      Command cmd = null;
      String parameter = "";
      String p;
      String[] tokens = commandToParse.split(" ");
      COMMAND_TYPE command;
      FLAG_TYPE flag;
      for (int x=0; x < tokens.length; x++)
      {
    	 if(x == 0)
    	 {
    		 command = validCommand(tokens[x]);
    		 switch(command)
    		 {
    	 	 	case EXIT:
    	 	 		return cmd = new CommandExit();
    	 	 	case HELP:
    	 	 		cmd = new CommandHelp();
    	        	 if(tokens.length == 1)
    	        	 {
    	        		 return cmd;
    	        	 }
    	 	 		break;
    	 	 	case NEXT:
    	 	 		return new CommandNext();
    	 	 	case ADD:
    	 	 		cmd = new CommandAdd();
    	 	 		break;
    	 	 	case CREATE:
    	 	 		cmd = new CommandCreate();
    	 	 		break;
    	 	 	case DELETE:
    	 	 		cmd = new CommandDelete();
    	 	 		break;
    	 	 	case OPEN:
    	 	 		cmd = new CommandOpen();
    	 	 		break;
    	 	 	case PAUSE:
    	 	 		return new CommandPause();
    	 	 	case PLAY:
    	 	 		cmd = new CommandPlay();
    	 	 		break;
    	 	 	case PREVIOUS:
    	 	 		return new CommandPrevious();
    	 	 	case SCAN:
    	 	 		cmd = new CommandScan();
    	 	 		cmd.addFlag(new Flag(FLAG_TYPE.DIRECTORY, parseParameter(x,tokens)));
    	 	 		return cmd;
    	 	 	case SEEK:
    	 	 		cmd = new CommandSeek();
    	 	 		break;
    	 	 	case STOP:
    	 	 		return new CommandStop();
    		 }
    	 }
    	 else if(x != 0 && cmd != null)
    	 {
    		 flag = validFlagType(tokens[x]);
    		 switch(flag)
    		 {
    		 	case ARTIST:
    		 		p = parseParameter(x,tokens);
    		 		cmd.addFlag(new Flag(FLAG_TYPE.ARTIST, p));
   	        	 	x += p.split(" ").length;
    		 		break;
    		 	case SONG:
    		 		p = parseParameter(x,tokens);
    		 		cmd.addFlag(new Flag(FLAG_TYPE.SONG, p));
   	        	 	x += p.split(" ").length;
    		 		break;
    		 	case GENRE:
    		 		p = parseParameter(x,tokens);
    		 		cmd.addFlag(new Flag(FLAG_TYPE.GENRE, p));
   	        	 	x += p.split(" ").length;
    		 		break;
    		 	case PLAYLIST:
    		 		p = parseParameter(x,tokens);
    		 		cmd.addFlag(new Flag(FLAG_TYPE.PLAYLIST, p));
   	        	 	x += p.split(" ").length;
    		 		break;
    		 	case MILISECONDS:
    		 		p = parseParameter(x,tokens);
    		 		try
    		 		{
    		 			Integer.parseInt(p);
    		 		}
    		 		catch(Exception e)
    		 		{
    		 			throw new InvalidCommandException("Enter Milisecond Time was not an integer value");
    		 		}
    		 		cmd.addFlag(new Flag(FLAG_TYPE.MILISECONDS, p));
    		 		x += p.split(" ").length;
    		 		break;
    		 	case FILETYPE:
    		 		p = parseParameter(x,tokens);
    		 		cmd.addFlag(new Flag(FLAG_TYPE.FILETYPE, p));
   	        	 	x += p.split(" ").length;
    		 		break;
    		 	case NOTVALID:
    		 		parameter += tokens[x] + " ";
    		 		break;
    		 }
    	 }
    	 else
         {
    		 throw new InvalidCommandException("Entered Command is Not Valid");  
         }
      }
      if(!(parameter.equals("")))
      {
    	  cmd.addFlag(new Flag(FLAG_TYPE.NOFLAG, parameter.trim()));
      }
      if(cmd.getFlags().size() == 0)
    	  throw new InvalidCommandException("Entered command must have parameters");
      return cmd;
   }
   
   private String parseParameter(int currentPosition, String[] tokens) throws InvalidCommandException
   {
	   String parameter = "";
	   int u = currentPosition + 1;
	   for(int x = u; x < tokens.length; x++)
	   {
		   if(!(validFlagType(tokens[x]).equals(FLAG_TYPE.NOTVALID)))
	       {
	    	   if(parameter.equals(""))
			   {
				   throw new InvalidCommandException("Invalid Parameter");
			   }
	    	   else if(!parameter.equals(""))
			   {
				   break;
			   }
	       }
		   else
	       {
	    	   parameter += tokens[x] + " ";
	       }
	   }
	   if(parameter.equals(""))
		   throw new InvalidCommandException("No Parameter for set flag");
	   return parameter.trim();
   }
   
   private COMMAND_TYPE validCommand(String cIn)
   {
	   String command = cIn.toLowerCase();
	   if(command.equals("play"))
  	   {
		   return COMMAND_TYPE.PLAY;
       }
	   else if(command.equals("add"))
	   {
		   return COMMAND_TYPE.ADD;
	   }
	   else if(command.equals("create"))
	   {
		   return COMMAND_TYPE.CREATE;
	   }
	   else if(command.equals("delete"))
	   {
		   return COMMAND_TYPE.DELETE;
	   }
  	   else if(command.equals("pause"))
  	   {
  		   return COMMAND_TYPE.PAUSE;
       }
  	   else if(command.equals("stop"))
  	   {
  		   return COMMAND_TYPE.STOP;
       }
  	   else if(command.equals("next"))
  	   {
  		   return COMMAND_TYPE.NEXT;
       }
  	   else if(command.equals("previous"))
  	   {
  		   return COMMAND_TYPE.PREVIOUS;
       }
  	   else if(command.equals("seek"))
  	   {
  		   return COMMAND_TYPE.SEEK;  
       }
  	   else if(command.equals("open"))
  	   {
  		   return COMMAND_TYPE.OPEN;
       }
  	   else if(command.equals("exit"))
  	   {
  		   return COMMAND_TYPE.EXIT;
       }
  	   else if(command.equals("help"))
  	   {
  		   return COMMAND_TYPE.HELP;
       }
  	   else if(command.equals("scan"))
  	   {
  		   return COMMAND_TYPE.SCAN;
  	   }
	   return COMMAND_TYPE.NOTVALID;
   }
   
   private FLAG_TYPE validFlagType(String fIn)
   {
	   if(fIn.equals("-a"))
	   {
		   return FLAG_TYPE.ARTIST;
	   }
	   else if(fIn.equals("-g"))
	   {
		   return FLAG_TYPE.GENRE;
	   }
	   else if(fIn.equals("-p"))
	   {
		   return FLAG_TYPE.PLAYLIST;
	   }
	   else if(fIn.equals("-s"))
	   {
		   return FLAG_TYPE.SONG;
	   }
	   else if(fIn.equals("-e"))
	   {
		   return FLAG_TYPE.FILETYPE;
	   }
	   else if(fIn.equals("-ms"))
	   {
		   return FLAG_TYPE.MILISECONDS;
	   }
	   return FLAG_TYPE.NOTVALID;
   }
}