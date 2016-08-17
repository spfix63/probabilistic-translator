package exec;

public class Num extends Variable 
{

	private float value;
	
	public Num()
	{
		value = 0;
		type = INT;
	}

	public Num(int val) 
	{
		value = val;
		type = INT;
	}
	
	public Num(float val) 
	{
		value = val;
		type = REAL;
	}

	public float getValue() 
	{
		return value;
	}

	public void setValue(float f)
	{
		value = f;
		type = REAL;
	}

	public void setValue(int f)
	{
		value = f;
		type = INT;
	}

	public void setValue(String v)
	{
		try
		{
			value = Integer.valueOf(v);
			type = INT;
		}
		catch (NumberFormatException e)
		{
			value = Integer.valueOf(v);
			type = REAL;
		}
	}
	
	public String toString() 
	{
		return type == INT ? String.valueOf((int)value) : String.valueOf(value);
	}
}
