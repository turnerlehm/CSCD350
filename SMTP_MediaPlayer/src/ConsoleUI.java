import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*; 

public class ConsoleUI extends Thread
{
	private volatile static ConsoleUI instance;
	
	private volatile static BufferedReader std_in;
	private volatile static SystemScanner sys_scan;
	private volatile static CommandExecutionService executor;
	private volatile static boolean scanned;
	
	private ConsoleUI(){}
	
	public static ConsoleUI getInstance()
	{
		if(instance == null)
		{
			synchronized(ConsoleUI.class)
			{
				if(instance == null)
					instance = new ConsoleUI();
			}
		}
		return instance;
	}
	
	public void initMenu()
	{
		System.out.println("--- Welcome to SMTPlayer v1.something ---");
		System.out.println("Enter group to open (by name)");
		System.out.println("1. Music");
		System.out.println("2. Artist");
		System.out.println("3. Genre");
		System.out.println("4. Playlists");
		System.out.println("Type 'help' for more commands");
	}
	
	public void setScanner(SystemScanner s)
	{
		if(s == null)
			throw new IllegalArgumentException("System scanner cannot be null!");
		if(sys_scan == null)
			this.sys_scan = s;
		else
			return;
	}
	
	public void setExecutor(CommandExecutionService ces)
	{
		if(ces == null)
			throw new IllegalArgumentException("Command execution service cannot be null!");
		if(executor == null)
			this.executor = ces;
		else
			return;
	}
	
	//Can only be called once
	public void initScan()
	{
		if(std_in == null || sys_scan == null)
			return;
		if(scanned)
			return;
		else
		{
			try {
				sys_scan.scan(std_in);
				scanned = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
		}
	}
	
	public void initInput()
	{
		if(std_in == null)
			this.std_in = new BufferedReader(new InputStreamReader(System.in));
		else
			return;
	}
	
	public BufferedReader getInput(){return this.std_in;}
	
	public void run()
	{
		String cmd = "";
		do
		{
			System.out.print("$> ");
			try
			{
				cmd = this.std_in.readLine();
				if(cmd != null)
					executor.execute(cmd);
			}
			catch(IOException ioe)
			{
				System.err.println("--- Could not read from STDIN ---");
				ioe.printStackTrace();
				return;
			}
			catch(Exception e)
			{
				System.err.println("--- An unknown error occurred ---");
				e.printStackTrace();
				return;
			}
		}while(!cmd.equalsIgnoreCase("exit"));
		return;
	}
}
