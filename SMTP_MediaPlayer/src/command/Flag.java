package command;

public class Flag
{
	public final FLAG_TYPE _Flag;
	public final String _Parameter;
	
	public Flag(FLAG_TYPE type, String flag_Paremeter)
	{
		_Flag = type;
		_Parameter = flag_Paremeter;
	}
	
	public boolean equals(Object in)
	{
		if(in instanceof Flag)
		{
			if(_Flag.equals(((Flag) in)._Flag))
				return true;
		}
		return false;
	}
}
