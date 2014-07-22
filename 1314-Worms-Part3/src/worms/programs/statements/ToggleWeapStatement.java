package worms.programs.statements;

import worms.exceptions.InterruptException;
import worms.model.Program;
import worms.model.Worm;
import worms.programs.ActionStatement;

public class ToggleWeapStatement extends ActionStatement {

	public ToggleWeapStatement(Program program, int line, int column) {
		super(program, line, column);
	}

	@Override
	public void execute(int line, int column) {
		if ( ! getProgram().getHandler().toggleWeapon( (Worm) getProgram().getAgent() ))
			throw new InterruptException(getLine() + 1, getColumn() + 1);

	}

}
