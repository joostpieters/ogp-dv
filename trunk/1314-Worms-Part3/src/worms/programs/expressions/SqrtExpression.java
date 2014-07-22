package worms.programs.expressions;

import worms.exceptions.IllegalTypeException;
import worms.model.Program;
import worms.programs.Expression;
import worms.programs.Type;
import worms.programs.types.DoubleType;

public class SqrtExpression extends Expression<DoubleType> {

	private Expression<? extends Type> e;

	public SqrtExpression(Program program, int line, int column, Expression<? extends Type> e) {
		super(program, line, column, DoubleType.class);
		
		this.e = e;
	}

	private boolean isValidType(Expression<? extends Type> expression) {
		return (expression.getType() == DoubleType.class);
	}

	@Override
	public DoubleType evaluate() {
		if ( !isValidType(e) ) 
			throw new IllegalTypeException();
		DoubleType double1 = (DoubleType) e.evaluate();
				
		return double1.sqrt();
	}

}
