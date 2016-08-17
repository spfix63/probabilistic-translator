package instr;

public class Goto implements Instruction
{
	private int jumpLine;
	public Goto(int line) 
	{
		jumpLine = line;
	}
	
	public int execute(int line) 
	{
		return jumpLine;
	}
}
