package command;

public class Command
{
	public final COMMAND_TYPE _Command_Type;
	private FLAG_TYPE _Flag;
	private String _Parameter;
	
	public Command(COMMAND_TYPE type)
	{
		_Command_Type = type;
		_Parameter = "";
		_Flag = FLAG_TYPE.NOFLAG;
	}
	
	public void addFlag(FLAG_TYPE flagToAdd)
	{
		_Flag = flagToAdd;
	}
	
	public void setParameter(String parameterIn)
	{
		_Parameter = parameterIn;
	}
	
	public FLAG_TYPE getFlag()
	{
		return this._Flag;
	}
	
	public String getParameter()
	{
		return _Parameter;
	}
}