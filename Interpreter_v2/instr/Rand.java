package instr;

import exec.Bool;
import exec.Env;
import exec.Num;
import exec.Variable;

public class Rand implements Instruction
{
	private Num dest;
	private Expr expr;
	
	public Rand(String id, Expr ex) 
	{
		dest = (Num)Env.createVariable(id, Variable.INT);
		expr = ex;
	}
	
	public int execute(int currentLine)
	{
		dest.setValue(Env.random.nextInt(expr.evalInt()));
		return currentLine + 1;
	}
}
