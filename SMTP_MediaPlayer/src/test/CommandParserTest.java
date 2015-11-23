package test;

import command.*;
import command.commands.*;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CommandParserTest
{
	CommandParser parser;
	
	@Before
	public void setUp() throws Exception
	{
		parser = CommandParser.getInstance();
	}

	@After
	public void tearDown() throws Exception
	{
		
	}
	
	@Test
	public void testParseCommand()
	{
		String cs;
		Command cmd;
		try
		{
			//expected result: Command with type pause with no flags or parameters set
			cs = "pause and some random bull";
			cmd = parser.parseCommand(cs);
			assertEquals(COMMAND_TYPE.PAUSE, cmd.getType());
			assertEquals(0, cmd.getFlags().size());
		}
		catch(Exception e)
		{
			fail();
		}
		
		try
		{
			//expected result: Command with type open with a default NOFLAG and parameter play stop
			cs = "Open stop play";
			cmd = parser.parseCommand(cs);
			assertEquals(COMMAND_TYPE.OPEN, cmd.getType());
			assertEquals(1, cmd.getFlags().size());
			assertTrue(cmd.getFlags().get(0)._Parameter.equals("stop play"));
		}
		catch(Exception e)
		{
			fail();
		}
		
		try
		{
			//expected result: Exception to be thrown
			cs = "-a Open stop play";
			cmd = parser.parseCommand(cs);
		}
		catch(Exception e)
		{
			assert(true);
		}
		
		try
		{
			//expected result: Exception to be thrown, because there's is no argument for -a flag
			cs = "play -a -s stop play";
			cmd = parser.parseCommand(cs);
		}
		catch(Exception e)
		{
			assert(true);
		}
		
		try
		{
			//expected result: play command with 2 flags, artist and song, artist Luke Bryan, song play it again
			cs = "play -a Luke Bryan -s play it again";
			cmd = parser.parseCommand(cs);
			assertEquals(COMMAND_TYPE.PLAY, cmd.getType());
			assertEquals(2, cmd.getFlags().size());
			assertEquals(FLAG_TYPE.ARTIST, cmd.getFlags().get(0)._Flag);
			assertTrue(cmd.getFlags().get(0)._Parameter.equals("Luke Bryan"));
			assertEquals(FLAG_TYPE.SONG, cmd.getFlags().get(1)._Flag);
			assertTrue(cmd.getFlags().get(1)._Parameter.equals("play it again"));
		}
		catch(Exception e)
		{
			fail();
		}
	}
}
