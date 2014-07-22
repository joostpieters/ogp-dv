package worms.programs.statements;

import worms.exceptions.IllegalTypeException;
import worms.model.Program;
import worms.programs.Expression;
import worms.programs.Statement;
import worms.programs.Type;
import worms.programs.types.DoubleType;

public class RepeatStatement extends Statement {

	private Statement s;
	private Expression<? extends Type> e;

	public RepeatStatement(Program program, int line, int column, Statement s, Expression<? extends Type> e) {
		super(program, line, column);
		this.e = e;
		this.s = s;
	}

	private boolean isValidType(Expression<? extends Type> expression) {
		return (expression.getType() == DoubleType.class);
	}
	
	@Override
	public void execute(int line, int column) {
		if ( !isValidType(e) ) 
			throw new IllegalTypeException();
		DoubleType double1 = (DoubleType) e.evaluate();
		
		double cntr =  double1.roundToNearestInteger();
		while ( cntr != 0 ) {
			s.execute(line, column);
			cntr--;
		}	
	}

}
