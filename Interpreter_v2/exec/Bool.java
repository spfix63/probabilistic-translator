package exec;

public class Bool extends Variable 
{
	private boolean value;
	public Bool()
	{
		value = false;
		type = BOOL;
	}
	
	public Bool(boolean val) 
	{
		value = val;
		type = BOOL;
	}

	public boolean getValue() 
	{
		return value;
	}

	public void setValue(boolean v)
	{
		value = v;
	}
	
	public void setValue(String v)
	{
		value = v.equals("true") ? true : false;
	}
	
	public String toString() 
	{
		return String.valueOf(value);
	}
}
