package exec;

import instr.Assign;
import instr.Empty;
import instr.Expr;
import instr.Goto;
import instr.If;
import instr.IfFalse;
import instr.Instruction;
import instr.Rand;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

public class Exe 
{
	private HashMap<String, Integer> labelMap;
	private ArrayList<String> lines; 
	private ArrayList<Instruction> instructions; 

	private Scanner input;
	
	public Exe()
	{
		labelMap = new HashMap<String, Integer>();
		lines = new ArrayList<String>();
		instructions = new ArrayList<Instruction>();
		input = new Scanner(System.in);
	}
	
	public void program()
	{
		long start = System.nanoTime();
		
		parseLabels();
		execute();
		
		long finish = System.nanoTime();
		System.out.println("Total runtime: " + (finish - start) / 1000000 + " ms");
		
		Env.print();
	}

	private void parseLabels() 
	{
		int linecounter = 0;
		String line;
		while (input.hasNextLine())
		{
			line = input.nextLine();
			String[] s = line.split(":", 2);
			while (s.length > 1)
			{
				labelMap.put(s[0].trim(), linecounter);
				line = s[1];
				s = line.split(":", 2);
			}
			
			lines.add(line.trim());
			instructions.add(null);
			
			linecounter++;
		}
	}

	private void execute() 
	{
		Integer nextline = labelMap.get("L1");
		if (nextline == null) {
			Env.error("Starting label L1 not found");
		}
		
		Instruction ins;
		while (nextline < lines.size())
		{
			ins = instructions.get(nextline);
			if (ins == null)
			{
				ins = parseLine(nextline);
				instructions.set(nextline, ins);
			}
			//System.out.println(lines.get(nextline));
			nextline = instructions.get(nextline).execute(nextline);
			System.out.println(Env.varMap.get("num") );
		}
	}

	private Instruction parseLine(int nextline) 
	{		
		String line = lines.get(nextline);
		String[] tokens = line.split("[ \t]+", 2);
		
		//System.out.println(line);
		
		String[] iftokens;
		String label, expr;
		switch (tokens[0]) 
		{
		case "goto":
			//goto label
			return new Goto(labelMap.get(tokens[1]));
			
		case "if":
			//if expr goto label
			iftokens = tokens[1].split("goto");
			label = iftokens[1].trim();
			expr = iftokens[0].trim();
			
			return new If(new Expr(expr), labelMap.get(label));

		case "iffalse":
			//iffalse expr goto label
			iftokens = tokens[1].split("goto");
			label = iftokens[1].trim();
			expr = iftokens[0].trim();

			return new IfFalse(new Expr(expr), labelMap.get(label));
			
		case "rand":
			//rand id bound
			iftokens = tokens[1].split("[ \t]+");
			return new Rand(iftokens[0], new Expr(iftokens[1]));
			
		default:
			iftokens = line.split("=", 2);
			if (iftokens.length == 2)
			{
				//id = expr
				label = iftokens[0].trim();
				expr = iftokens[1].trim();
				return new Assign(label, new Expr(expr));
			}
			return new Empty();
		}
	}
}
