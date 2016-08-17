package exec;

public class Variable 
{
	public final static int BOOL = 101, INT = 102, REAL = 103, ARRAY = 103;
	
	protected int type;
	public int getType() 
	{
		return type;
	}
	
	public boolean isNum()
	{
		return (type == INT) || (type == REAL);
	}

}
