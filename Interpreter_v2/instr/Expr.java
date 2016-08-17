package instr;

import java.util.ArrayList;
import java.util.Arrays;

import exec.Bool;
import exec.Env;
import exec.Num;
import exec.VarArray;
import exec.Variable;

public class Expr 
{
	public static final int BOOL = 1, INT = 2, REAL = 3, VAR = 4;
	private static final int Add = 1, Sub = 2, Mul = 3, Div = 4,
				And = 5, Or = 6, Equal = 7, NotEqual = 8,
				LE = 9, GE = 10, More = 11, Less = 12;
	
	private int returnType;
	private int leftType;
	private int rightType;
	
	private int op;

	private boolean single = false;
	private float leftnum;
	private float rightnum;
	
	private boolean leftbool;
	private boolean rightbool;

	private Variable leftvar;
	private Variable rightvar;
	
	private Variable leftindex;
	private Variable rightindex;
	
	public Expr(String expr) 
	{
		String[] tokens = expr.split(String.format("((?<=%1$s)|(?=%1$s))", 
				"\\+|-|\\*|/|<|>|&&|\\|\\||==|!=|minus"));
		String[] tokens2 = expr.split(String.format("((?<=%1$s)|(?=%1$s))", 
				"<=|>="));
		if (tokens2.length == 3)
			tokens = tokens2;
		
		//System.out.println(Arrays.toString(tokens));
		if (tokens.length == 1)
		{
			single = true;
			parseLeft(expr.trim());
		}
		else if (tokens.length == 2)
		{
			parseLeft("0");
			parseRight(tokens[1].trim());
			parseOp("-");
		}
		else
		{
			parseLeft(tokens[0].trim());
			parseOp(tokens[1].trim());
			parseRight(tokens[2].trim());
		}
		determineReturnType();
	}
	

	public int getReturnType() 
	{
		return returnType;
	}

	private void parseRight(String expr) 
	{
		try {
			rightnum = Integer.valueOf(expr);
			rightType = INT;
		} catch (NumberFormatException e) {
			try {
				rightnum = Float.valueOf(expr);
				rightType = REAL;
			} catch (NumberFormatException e1) {
				if (expr.equals("true")) {
					rightbool = true; 
					rightType = BOOL;
				} else if (expr.equals("false")) {
					rightbool = false;
					rightType = BOOL;
				}
				else {
					
					String[] tok = expr.split("[\\[\\]]");
					if (tok.length > 1) 
						rightindex = Env.getVariable(tok[1].trim());
					rightvar = Env.getVariable(tok[0].trim());
					rightType = VAR;
				}
			}	
		}
	}

	private void parseLeft(String expr) 
	{
		try {
			leftnum = Integer.valueOf(expr);
			leftType = INT;
		} catch (NumberFormatException e) {
			try {
				leftnum = Float.valueOf(expr);
				leftType = REAL;
			} catch (NumberFormatException e1) {
				if (expr.equals("true")) {
					leftbool = true; 
					leftType = BOOL;
				} else if (expr.equals("false")) {
					leftbool = false;
					leftType = BOOL;
				}
				else {
					String[] tok = expr.split("[\\[\\]]");
					if (tok.length > 1) 
						leftindex = Env.getVariable(tok[1].trim());
					leftvar = Env.getVariable(tok[0].trim());
					leftType = VAR;
				}
			}	
		}
	}

	private void determineReturnType()
	{
		int left = leftType;
		if (left == VAR)
		{
			int type = leftvar.getType();
			if (type == Variable.ARRAY)
				type = ((VarArray)leftvar).getElementType();
			switch (type) 
			{
			case Variable.BOOL: 
				left = BOOL;
				break;
			case Variable.INT: 
				left = INT;
				break;
			case Variable.REAL: 
				left = REAL;
				break;
			}
		}
		if (single) 
		{
			returnType = left;
			return;
		}
				
		int right = rightType;
		if (right == VAR)
		{
			int type = rightvar.getType();
			if (type == Variable.ARRAY)
				type = ((VarArray)rightvar).getElementType();
			switch (type)  
			{
			case Variable.BOOL: 
				right = BOOL;
				break;
			case Variable.INT: 
				right = INT;
				break;
			case Variable.REAL: 
				right = REAL;
				break;
			}
		}
		
		if (op <= 4) //arithmetic
			returnType = Math.max(left, right);
		else
			returnType = BOOL;
	}

	public boolean evalBool()
	{
		Variable left = leftvar;
		Variable right = rightvar;
		if (leftType == VAR && left.getType() == Variable.ARRAY)
			left = ((VarArray)left).getElement((Num)leftindex);
		if (rightType == VAR && right.getType() == Variable.ARRAY)
			right = ((VarArray)right).getElement((Num)rightindex);
		
		boolean lb = leftbool, rb = rightbool, numbers = true;
		if (leftType == VAR && left.getType() == Variable.BOOL)
		{
			lb = ((Bool)left).getValue();
		}
		if (single)
			return lb;
		
		if (rightType == VAR && right.getType() == Variable.BOOL)
		{
			numbers = false;
			rb = ((Bool)right).getValue();
		}

		float lf = leftnum, rf = rightnum;
		if (leftType == VAR && left.isNum())
			lf = ((Num)left).getValue();
		if (rightType == VAR && right.isNum())
			rf = ((Num)right).getValue();
		
		switch (op)
		{
		case And:
			return lb && rb;
		case Or:
			return lb || rb;
		case Equal:
			if (numbers)
				return lf == rf;
			else
				return lb == rb;
		case NotEqual:
			if (numbers)
				return lf != rf;
			else
				return lb != rb;
		case GE:
			return lf >= rf;
		case LE:
			return lf <= rf;
		case More:
			return lf > rf;
		case Less:
			return lf < rf;
		}
		Env.error("Bad boolean expression");
		return false;
	}

	public float evalFloat()
	{
		Variable left = leftvar;
		Variable right = rightvar;
		if (leftType == VAR && left.getType() == Variable.ARRAY)
			left = ((VarArray)left).getElement((Num)leftindex);
		if (rightType == VAR && right.getType() == Variable.ARRAY)
			right = ((VarArray)right).getElement((Num)rightindex);
		
		float lf = leftnum, rf = rightnum;
		if (leftType == VAR && left.isNum())
			lf = ((Num)left).getValue();
		if (single)
			return lf;
		
		if (rightType == VAR && right.isNum())
			rf = ((Num)right).getValue();
		
		switch (op)
		{
		case Add:
			return lf + rf;
		case Sub:
			return lf - rf;
		case Mul:
			return lf * rf;
		case Div:
			return lf / rf;
		}
		Env.error("Bad arithmetic expression: float");
		return 0;
	}
	
	public int evalInt()
	{		
		Variable left = leftvar;
		Variable right = rightvar;
		if (leftType == VAR && left.getType() == Variable.ARRAY)
			left = ((VarArray)left).getElement((Num)leftindex);
		if (rightType == VAR && right.getType() == Variable.ARRAY)
			right = ((VarArray)right).getElement((Num)rightindex);
		
		int lf = (int) leftnum, rf = (int) rightnum;
		if (leftType == VAR && left.isNum())
			lf = (int) ((Num)left).getValue();
		if (single)
			return lf;
		
		if (rightType == VAR && right.isNum())
			rf = (int) ((Num)right).getValue();
		switch (op)
		{
		case Add:
			return lf + rf;
		case Sub:
			return lf - rf;
		case Mul:
			return lf * rf;
		case Div:
			return lf / rf;
		}
		Env.error("Bad arithmetic expression: int");
		return 0;
	}
	

	private void parseOp(String val) {
		switch (val)
		{
		case "+":
			op = Add;
			break;
		case "-":
			op = Sub;
			break;
		case "*":
			op = Mul;
			break;
		case "/":
			op = Div;
			break;
		case "&&":
			op = And;
			break;
		case "||":
			op = Or;
			break;
		case "==":
			op = Equal;
			break;
		case "!=":
			op = NotEqual;
			break;
		case ">=":
			op = GE;
			break;
		case "<=":
			op = LE;
			break;
		case ">":
			op = More;
			break;
		case "<":
			op = Less;
			break;
		}
		
	}
	
}
