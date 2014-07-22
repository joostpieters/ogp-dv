package worms.programs;

import worms.model.Program;

public abstract class Statement {
	
	
	private int line;
	private int column;
	private Program program;

	public Statement(Program program, int line, int column) {
		this.program = program;
		this.line = line;
		this.column = column;
	}

	public abstract void execute(int line, int column);

	/**
	 * @return the line
	 */
	public int getLine() {
		return line;
	}

	/**
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * @return the program
	 */
	public Program getProgram() {
		return program;
	}

}
