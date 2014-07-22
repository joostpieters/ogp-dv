/**
 * 
 */
package worms.programs;

import worms.model.Program;


/**
 * @author Delphine
 *
 */
public abstract class Expression<T extends Type> {
	
	private Program program;
	private Class<T> type;
	private int line;
	private int column;

	public Expression(Program program, int line, int column, Class<T> type) {
		this.program = program;
		this.type = type;
		this.line = line;
		this.column = column;
	}

	/**
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * @return the line
	 */
	public int getLine() {
		return line;
	}

	/**
	 * @return the type
	 */
	public Class<T> getType() {
		return type;
	}
	
	/**
	 * @return the program
	 */
	public Program getProgram() {
		return program;
	}
	
	public abstract T evaluate();


}
