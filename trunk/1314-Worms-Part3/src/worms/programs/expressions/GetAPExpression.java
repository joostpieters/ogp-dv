package worms.programs.expressions;

import worms.exceptions.IllegalTypeException;
import worms.model.Program;
import worms.programs.Expression;
import worms.programs.Type;
import worms.programs.types.DoubleType;
import worms.programs.types.WormEntityType;

public class GetAPExpression extends Expression<DoubleType> {

	private Expression<? extends Type> entity;

	public GetAPExpression(Program program, int line, int column, Expression<? extends Type> e) {
		super(program, line, column, DoubleType.class);
		
		this.entity = e;
	}
	
	private boolean isValidType(Expression<? extends Type> expression) {
		return (expression.getType() == WormEntityType.class);
	}

	@Override
	public DoubleType evaluate() {
		if ( !isValidType(entity) ) 
			throw new IllegalTypeException();
		WormEntityType worm = (WormEntityType) this.entity.evaluate();
		
		return new DoubleType( worm.getValue().getActionPoints());
	}

}
