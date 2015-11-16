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
    	 if((tokens[x].equals("play") || tokens[x].equals("PLAY") || tokens[x].equals("Play")) && x == 0)
    	 {
        	 cmd = new Command(COMMAND_TYPE.PLAY);     
         }
    	 else if((tokens[x].equals("pause") || tokens[x].equals("Pause") || tokens[x].equals("PAUSE")) && x == 0)
    	 {
        	 return new Command(COMMAND_TYPE.PAUSE);     
         }
    	 else if((tokens[x].equals("stop") || tokens[x].equals("Stop") || tokens[x].equals("STOP")) && x == 0)
    	 {
        	 return new Command(COMMAND_TYPE.STOP);     
         }
    	 else if((tokens[x].equals("open") || tokens[x].equals("OPEN") || tokens[x].equals("Open")) && x == 0)
    	 {
        	 cmd = new Command(COMMAND_TYPE.OPEN);     
         }
    	 else if((tokens[x].equals("exit") || tokens[x].equals("EXIT") || tokens[x].equals("Exit") ) && x == 0)
    	 {
        	 return cmd = new Command(COMMAND_TYPE.EXIT);     
         }
    	 else if((tokens[x].equals("help") || tokens[x].equals("Help") || tokens[x].equals("HELP") ) && x == 0)
    	 {
        	 return cmd = new Command(COMMAND_TYPE.HELP);     
         }
    	 else if(x != 0 && cmd != null)
    	 {
	         if(tokens[x].equals("-A") || tokens[x].equals("-a"))
	         {
	        	 String p = parseParameter(x,tokens);
	        	 cmd.addFlag(new Flag(FLAG_TYPE.ARTIST, p));
	        	 x += p.split(" ").length;
	         }
	         else if(tokens[x].equals("-S") || tokens[x].equals("-s"))
	         {
	        	 String p = parseParameter(x,tokens);
	        	 cmd.addFlag(new Flag(FLAG_TYPE.SONG, p));
	        	 x += p.split(" ").length;
	         }
	         else if(tokens[x].equals("-G") || tokens[x].equals("-g"))
	         {
	        	 String p = parseParameter(x,tokens);
	        	 cmd.addFlag(new Flag(FLAG_TYPE.GENRE, p));
	        	 x += p.split(" ").length;
	         }
	         else if(tokens[x].equals("-P") || tokens[x].equals("-p"))
	         {
	        	 String p = parseParameter(x,tokens);
	        	 cmd.addFlag(new Flag(FLAG_TYPE.PLAYLIST, p));
	        	 x += p.split(" ").length;
	         }
	         else if(tokens[x].equals("-E") || tokens[x].equals("-e"))
	         {
	        	 String p = parseParameter(x,tokens);
	        	 cmd.addFlag(new Flag(FLAG_TYPE.FILETYPE, p));
	        	 x += p.split(" ").length;
	         }
	         else
	         {
	        	 parameter += tokens[x] + " ";
	         }
    	 }
    	 else
         {
    		 throw new InvalidCommandException("Entered Command is Not Valid");  
         }
      }
      if(!(parameter.equals("")))
      {
    	  parameter = parameter.trim();
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
	   return parameter.trim();
   }
}