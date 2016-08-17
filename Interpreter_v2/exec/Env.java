package exec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Map.Entry;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class Env 
{
	public static Random random = new Random();
	public static HashMap<String, Variable> varMap = new HashMap<String, Variable>();

	public static Variable createVariable(String var, int type)
	{
		/*String[] tok = var.split("[\\[\\]]");
		int index = 0;
		if (tok.length > 1)
			index = getIndex(tok[1].trim());
		VarArray va = varMap.get(tok[0].trim());
		if (va == null)
		{
			va = new VarArray(type);
			varMap.put(tok[0].trim(), va);
		}
		return va.createElement(index);*/
		
		Variable v = varMap.get(var);
		if (v == null)
		{
			v = makeEl(type);
			varMap.put(var, v);
		}
		return v;
	}
	
	public static Variable createArray(String var, int type)
	{
		/*String[] tok = var.split("[\\[\\]]");
		int index = 0;
		if (tok.length > 1)
			index = getIndex(tok[1].trim());
		VarArray va = varMap.get(tok[0].trim());
		if (va == null)
		{
			va = new VarArray(type);
			varMap.put(tok[0].trim(), va);
		}
		return va.createElement(index);*/
		
		Variable va = varMap.get(var);
		if (va == null)
		{
			va = new VarArray(type);
			varMap.put(var, va);
		}
		return va;
	}
	
	public static Variable getVariable(String var) 
	{
		/*String[] tok = var.split("[\\[\\]]");
		int index = 0;
		if (tok.length > 1)
			index = getIndex(tok[1].trim());
		VarArray va = varMap.get(tok[0].trim());
		if (va == null)
			return null;
		return va.getElement(index);*/
		return varMap.get(var);
	}
	
	public static Variable makeEl(int type) 
	{
		if (type == Variable.BOOL)
			return new Bool();
		else if (type == Variable.INT)
			return new Num(0);
		else //if (elementType == Variable.REAL)
			return new Num(0.0f);
	}
	
	private static int getIndex(String var)
	{
		return (int) ((Num)getVariable(var)).getValue();
	}
	
	public static void error(String err)
	{
		throw new Error(err);
	}

	public static void print() 
	{
		System.out.println("Result:");
		for (Entry<String, Variable> entry : varMap.entrySet())
		{
			if (!entry.getKey().matches("t\\d+"))
			{
				System.out.println(entry.getKey() + " = " + entry.getValue());
			}
		}
	}
}
