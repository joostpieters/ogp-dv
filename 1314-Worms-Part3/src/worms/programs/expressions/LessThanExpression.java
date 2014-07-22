package worms.programs.expressions;

import worms.exceptions.IllegalTypeException;
import worms.model.Program;
import worms.programs.Expression;
import worms.programs.Type;
import worms.programs.types.BoolType;
import worms.programs.types.DoubleType;

public class LessThanExpression extends Expression<BoolType> {

	private Expression<? extends Type> e1;
	private Expression<? extends Type> e2;

	public LessThanExpression(Program program, int line, int column, Expression<? extends Type> e1, Expression<? extends Type> e2) {
		super(program, line, column, BoolType.class);
		
		this.e1 = e1;
		this.e2 = e2;
	}

	private boolean isValidType(Expression<? extends Type> expression) {
		return (expression.getType() == DoubleType.class);
	}
	
	@Override
	public BoolType evaluate() {
		if ( !isValidType(e1) || !isValidType(e2)) 
			throw new IllegalTypeException();
		DoubleType double1 = (DoubleType) e1.evaluate();
		DoubleType double2 = (DoubleType) e2.evaluate();
		
		return new BoolType( double1.getValue() < double2.getValue() );
		
	}

}
