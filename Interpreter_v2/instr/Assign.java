package instr;

import exec.Bool;
import exec.Env;
import exec.Num;
import exec.VarArray;
import exec.Variable;

public class Assign implements Instruction
{
	private Variable dest;
	private Variable index;
	private Expr expr;
	
	public Assign(String id, Expr ex) 
	{
		int type = 0;
		switch (ex.getReturnType())
		{
		case Expr.BOOL:
			type = Variable.BOOL;
			break;
		case Expr.INT:
			type = Variable.INT;
			break;
		case Expr.REAL:
			type = Variable.REAL;
			break;
		default:
			Env.error("type error");
		}
		
		String[] tok = id.split("[\\[\\]]");
		if (tok.length > 1) 
		{
			index = Env.getVariable(tok[1].trim());
			dest = Env.createArray(tok[0].trim(), type);
		}
		else
			dest = Env.createVariable(tok[0].trim(), type);
		expr = ex;
	}
	
	public int execute(int currentLine)
	{
		Variable var = dest;
		if (var.getType() == Variable.ARRAY)
			var = ((VarArray)var).createElement((Num)index);
			
		if (var.getType() == Variable.BOOL)
			((Bool) var).setValue(expr.evalBool());
		else if (var.getType() == Variable.INT)
			((Num) var).setValue(expr.evalInt());
		else if (var.getType() == Variable.REAL)
			((Num) var).setValue(expr.evalFloat());
		return currentLine + 1;
	}
	
}
