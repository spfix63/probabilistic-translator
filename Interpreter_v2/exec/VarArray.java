package exec;

import java.util.ArrayList;

public class VarArray extends Variable 
{
	private int elementType;
	ArrayList<Variable> varList;
	
	public VarArray(int elType) 
	{
		type = ARRAY;
		elementType = elType;
		varList = new ArrayList<Variable>();
	}

	public int getElementType() 
	{
		return elementType;
	}

	public Variable getElement(int index)
	{
		return varList.get(convertIndex(index));
	}
	
	public Variable getElement(Num index)
	{
		return getElement((int)index.getValue());
	}
	
	private int convertIndex(int index) 
	{
		if (elementType == Variable.BOOL)
			return index;
		else if (elementType == Variable.INT)
			return index / 4;
		else //if (elementType == Variable.REAL)
			return index / 8;
	}

	public Variable createElement(int index)
	{
		index = convertIndex(index);
		if (index >= varList.size())
		{
			varList.ensureCapacity(index + 1);
			while (varList.size() < index + 1) {
				varList.add(null);
		    }
		}
		Variable v = varList.get(index);
		if (v == null)
		{
			v = Env.makeEl(elementType);
			varList.set(index, v);
		}
		return v;
	}
	
	public Variable createElement(Num index)
	{
		return createElement((int)index.getValue());
	}
	
	public String toString() 
	{
		return varList.toString();
			
	}
}
