package instr;

public class IfFalse extends If 
{
	public IfFalse(Expr e, int jumpLineNumber) 
	{
		super(e, jumpLineNumber);
	}
	
	public int execute(int currentLine)
	{
		if (super.execute(currentLine) == currentLine + 1)
			return getLine();
		else
			return currentLine + 1;
	}
}
