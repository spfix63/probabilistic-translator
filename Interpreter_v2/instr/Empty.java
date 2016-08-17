package instr;


public class Empty implements Instruction
{

	public int execute(int line) 
	{
		return line + 1;
	}

}
