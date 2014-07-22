package worms.programs.statements;

import worms.exceptions.InterruptException;
import worms.model.Program;
import worms.programs.ActionStatement;

public class SkipStatement extends ActionStatement {

	public SkipStatement(Program program, int line, int column) {
		super(program, line, column);
	}

	@Override
	public void execute(int line, int column) {
		if ((line <= getLine()) && (column <= getColumn()))
			throw new InterruptException(getLine() + 1, getColumn() + 1);
	}

}
