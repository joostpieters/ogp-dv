package worms.exceptions;

@SuppressWarnings("serial")
public class InterruptException extends RuntimeException {

	public InterruptException(int line, int column) {
		//super("Program interrupted at " + line + ":" + column);

		this.line = line;
		this.column = column;
	}
	
	public int getColumn() {
		// TODO Auto-generated method stub
		return column;
	}

	public int getLine() {
		// TODO Auto-generated method stub
		return line;
	}

	private int line;
	private int column;
}
