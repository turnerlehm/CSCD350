package command;

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
      String[] tokens = commandToParse.split(" ");
      for (int x=0; x < tokens.length; x++)
      {
    	 if(tokens[x].equals("play") || tokens[x].equals("PLAY") || tokens[x].equals("Play"))
    	 {
    		 if(cmd != null)
    			 throw new InvalidCommandException();
        	 cmd = new Command(COMMAND_TYPE.PLAY);     
         }
    	 else if(tokens[x].equals("pause") || tokens[x].equals("Pause") || tokens[x].equals("PAUSE"))
    	 {
    		 if(cmd != null)
    			 throw new InvalidCommandException();
        	 cmd = new Command(COMMAND_TYPE.PAUSE);     
         }
    	 else if(tokens[x].equals("stop") || tokens[x].equals("Stop") || tokens[x].equals("STOP"))
    	 {
    		 if(cmd != null)
    			 throw new InvalidCommandException();
        	 cmd = new Command(COMMAND_TYPE.STOP);     
         }
    	 else if(tokens[x].equals("open") || tokens[x].equals("OPEN") || tokens[x].equals("Open"))
    	 {
    		 if(cmd != null)
    			 throw new InvalidCommandException();
        	 cmd = new Command(COMMAND_TYPE.OPEN);     
         }
    	 else if(tokens[x].equals("exit") || tokens[x].equals("EXIT") || tokens[x].equals("Exit"))
    	 {
    		 if(cmd != null)
    			 throw new InvalidCommandException();
        	 return cmd = new Command(COMMAND_TYPE.EXIT);     
         }
    	 else if(tokens[x].equals("help") || tokens[x].equals("Help") || tokens[x].equals("HELP"))
    	 {
    		 if(cmd != null)
    			 throw new InvalidCommandException();
        	 return cmd = new Command(COMMAND_TYPE.HELP);     
         }
         else if(tokens[x].equals("-A") || tokens[x].equals("-a"))
         {
        	 if(cmd == null)
        		 throw new InvalidCommandException("Declare a Command Before Setting a Flag");
        	 String p = parseParameter(x,tokens);
        	 cmd.addFlag(new Flag(FLAG_TYPE.ARTIST, p));
        	 x += p.split(" ").length;
         }
         else if(tokens[x].equals("-S") || tokens[x].equals("-s"))
         {
        	 if(cmd == null)
        		 throw new InvalidCommandException("Declare a Command Before Setting a Flag");
        	 String p = parseParameter(x,tokens);
        	 cmd.addFlag(new Flag(FLAG_TYPE.SONG, p));
        	 x += p.split(" ").length;
         }
         else if(tokens[x].equals("-G") || tokens[x].equals("-g"))
         {
        	 if(cmd == null)
        		 throw new InvalidCommandException("Declare a Command Before Setting a Flag");
        	 String p = parseParameter(x,tokens);
        	 cmd.addFlag(new Flag(FLAG_TYPE.GENRE, p));
        	 x += p.split(" ").length;
         }
         else if(tokens[x].equals("-P") || tokens[x].equals("-p"))
         {
        	 if(cmd == null)
        		 throw new InvalidCommandException("Declare a Command Before Setting a Flag");
        	 String p = parseParameter(x,tokens);
        	 cmd.addFlag(new Flag(FLAG_TYPE.PLAYLIST, p));
        	 x += p.split(" ").length;
         }
         else if(tokens[x].equals("-E") || tokens[x].equals("-e"))
         {
        	 if(cmd == null)
        		 throw new InvalidCommandException("Declare a Command Before Setting a Flag");
        	 String p = parseParameter(x,tokens);
        	 cmd.addFlag(new Flag(FLAG_TYPE.FILETYPE, p));
        	 x += p.split(" ").length;
         }
         else
         {
        	 parameter += tokens[x] + " ";
         }
      }
      if(!(parameter.equals("")))
      {
    	  parameter.trim();
    	  cmd.addFlag(new Flag(FLAG_TYPE.NOFLAG, parameter));
      }
      return cmd;
   }
   
   private String parseParameter(int currentPosition, String[] tokens) throws InvalidCommandException
   {
	   String parameter = "";
	   int u = currentPosition + 1;
	   for(int x = u; x < tokens.length; x++)
	   {
	       if(tokens[x].equals("-A") || tokens[x].equals("-a"))
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
	       else if(tokens[x].equals("-S") || tokens[x].equals("-s"))
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
	       else if(tokens[x].equals("-G") || tokens[x].equals("-g"))
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
	       else if(tokens[x].equals("-P") || tokens[x].equals("-p"))
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
	       else if(tokens[x].equals("-E") || tokens[x].equals("-e"))
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
	   parameter.trim();
	   return parameter;
   }
}