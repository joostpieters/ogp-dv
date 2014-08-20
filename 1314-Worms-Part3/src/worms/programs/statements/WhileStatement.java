package worms.programs.statements;

import worms.exceptions.IllegalTypeException;
import worms.model.Program;
import worms.programs.Expression;
import worms.programs.Statement;
import worms.programs.Type;
import worms.programs.types.BoolType;

public class WhileStatement extends Statement {

	private Expression<? extends Type> condition;
	private Statement body;

	public WhileStatement(Program program, int line, int column, Expression<? extends Type> condition, Statement body) {
		super(program, line, column);
		
		if (body == null)
			throw new IllegalArgumentException("Body Statement cannot be zero");
		
		this.condition = condition;
		this.body = body;
	}

	private boolean isValidType(Expression<? extends Type> condition) {
		return (condition.evaluate().getType() == BoolType.class);
	}
	
	@Override
	public void execute(int line, int column) {
		if ( ! isValidType(condition) ) 
			throw new IllegalTypeException();
		while ( ((BoolType) this.condition.evaluate()).getValue() ) {
			body.execute(line, column);
		}
	}

}
