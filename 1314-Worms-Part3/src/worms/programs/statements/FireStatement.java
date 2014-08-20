package worms.programs.statements;

import worms.exceptions.IllegalTypeException;
import worms.exceptions.InterruptException;
import worms.model.Program;
import worms.model.Worm;
import worms.programs.ActionStatement;
import worms.programs.Expression;
import worms.programs.Type;
import worms.programs.types.DoubleType;

public class FireStatement extends ActionStatement {

	private Expression<? extends Type> yield;

	public FireStatement(Program program, int line, int column, Expression<? extends Type> yield) {
		super(program, line, column);
		
		this.yield = yield;
	}
	
	private boolean isValidType(Expression<? extends Type> yield) {
		return (yield.evaluate().getType() == DoubleType.class);
	}
	
	@Override
	public void execute(int line, int column) {
		if ( ! isValidType(yield) ) 
			throw new IllegalTypeException();
		DoubleType yield = (DoubleType) this.yield.evaluate();
		
		if ( ! getProgram().getHandler().fire( (Worm) getProgram().getAgent(), yield.intValue() ) )
			throw new InterruptException(getLine() + 1, getColumn() + 1);
	}

}
