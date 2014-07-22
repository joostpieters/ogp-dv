package worms.programs.expressions;

import worms.exceptions.IllegalTypeException;
import worms.model.Program;
import worms.programs.Expression;
import worms.programs.Type;
import worms.programs.types.DoubleType;
import worms.programs.types.EntityType;

public class GetXExpression extends Expression<DoubleType> {

	private Expression<? extends Type> entity;

	public GetXExpression(Program program, int line, int column, Expression<? extends Type> e) {
		super(program, line, column, DoubleType.class);
		
		this.entity = e;
	}
	
	private boolean isValidType(Expression<? extends Type> expression) {
		System.out.println(expression.getType());
		return (expression.getType() == EntityType.class);
	}

	@Override
	public DoubleType evaluate() {
		if ( !isValidType(entity) )
			throw new IllegalTypeException();
		EntityType entity = (EntityType) this.entity.evaluate();
		
		return new DoubleType( entity.getValue().getPosition().getX() );
	}

}
