package worms.programs.statements;

import worms.exceptions.IllegalTypeException;
import worms.model.Program;
import worms.programs.Expression;
import worms.programs.Statement;
import worms.programs.Type;
import worms.programs.types.BoolType;

public class IfStatement extends Statement {

	private Expression<? extends Type> condition;
	private Statement then;
	private Statement otherwise;

	public IfStatement(Program program, int line, int column, Expression<? extends Type> condition, 
			Statement then, Statement otherwise) {
		super(program, line, column);
		
		if (then == null || otherwise == null)
			throw new IllegalArgumentException(" then and otherwise statements cannot be null");
		
		this.condition = condition;
		this.then = then;
		this.otherwise = otherwise;
	}
		
	private boolean isValidType(Expression<? extends Type> condition) {
		return (condition.getType() == BoolType.class);
	}

	@Override
	public void execute(int line, int column) {
		if ( ! isValidType(condition) ) 
			throw new IllegalTypeException();
		BoolType condition = (BoolType) this.condition.evaluate();
		
		if ( condition.getValue() ) 
			then.execute(line, column);
		else
			otherwise.execute(line, column);
	}

}
