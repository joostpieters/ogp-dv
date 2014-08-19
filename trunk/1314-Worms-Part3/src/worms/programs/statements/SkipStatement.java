package worms.programs.statements;

import worms.model.Program;
import worms.programs.ActionStatement;

public class SkipStatement extends ActionStatement {

	public SkipStatement(Program program, int line, int column) {
		super(program, line, column);
	}

	@Override
	public void execute(int line, int column) {
	}

}
