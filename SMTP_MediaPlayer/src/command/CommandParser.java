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
        	 cmd.addFlag(FLAG_TYPE.ARTIST);
         }
         else if(tokens[x].equals("-S") || tokens[x].equals("-s"))
         {
        	 if(cmd == null)
        		 throw new InvalidCommandException("Declare a Command Before Setting a Flag");
        	 cmd.addFlag(FLAG_TYPE.SONG);
         }
         else if(tokens[x].equals("-G") || tokens[x].equals("-g"))
         {
        	 if(cmd == null)
        		 throw new InvalidCommandException("Declare a Command Before Setting a Flag");
        	 cmd.addFlag(FLAG_TYPE.GENRE);
         }
         else if(tokens[x].equals("-P") || tokens[x].equals("-p"))
         {
        	 if(cmd == null)
        		 throw new InvalidCommandException("Declare a Command Before Setting a Flag");
        	 cmd.addFlag(FLAG_TYPE.PLAYLIST);
         }
         else
         {
        	 parameter += tokens[x] + " ";
         }
      }
      if(cmd == null)
    	  throw new InvalidCommandException("Entered Command is Invalid");
      cmd.setParameter(parameter);
      return cmd;
   }
}
