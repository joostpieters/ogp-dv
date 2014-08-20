package worms.programs.expressions;

import worms.exceptions.IllegalTypeException;
import worms.model.Program;
import worms.programs.Expression;
import worms.programs.Type;
import worms.programs.types.BoolType;

public class NotExpression extends Expression<BoolType> {

	private Expression<? extends Type> e;

	public NotExpression(Program program, int line, int column, Expression<? extends Type> e) {
		super(program, line, column, BoolType.class);
			
		this.e = e;
	}

	private boolean isValidType(Expression<? extends Type> expression) {
		 return (expression.evaluate().getType() == BoolType.class);
	}

	
	@Override
	public BoolType evaluate() {
		if ( !isValidType(e) ) 
			throw new IllegalTypeException();
		BoolType bool = (BoolType) e.evaluate();
		
		return new BoolType( !bool.getValue() );
	}

}
