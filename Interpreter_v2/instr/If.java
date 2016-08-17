package instr;

public class If implements Instruction
{
	private int jumpLine;
	Expr expr;
	public If(Expr e, int jumpLineNumber) 
	{
		jumpLine = jumpLineNumber;
		expr = e;
	}
	
	public int getLine()
	{
		return jumpLine;
	}
	
	public int execute(int currentLine)
	{
		if (expr.evalBool())
			return jumpLine;
		return currentLine + 1;
	}
}
