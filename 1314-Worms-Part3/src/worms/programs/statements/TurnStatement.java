package worms.programs.statements;

import worms.exceptions.IllegalTypeException;
import worms.exceptions.InterruptException;
import worms.model.Program;
import worms.model.Worm;
import worms.programs.ActionStatement;
import worms.programs.Expression;
import worms.programs.Type;
import worms.programs.types.DoubleType;

public class TurnStatement extends ActionStatement {

	private Expression<? extends Type> angle;

	public TurnStatement(Program program, int line, int column, Expression<? extends Type> angle) {
		super(program, line, column);
		
		this.angle = angle;
	}

	private boolean isValidType(Expression<? extends Type> angle) {
		return (angle.getType() == DoubleType.class);
	}
	
	@Override
	public void execute(int line, int column) {
		if ( ! isValidType(angle) )
			throw new IllegalTypeException();
		DoubleType angle = (DoubleType) this.angle.evaluate();
		
		if (! getProgram().getHandler().turn( (Worm) getProgram().getAgent(), angle.getValue() ) )
			throw new InterruptException(getLine() + 1, getColumn() + 1);

	}

}
