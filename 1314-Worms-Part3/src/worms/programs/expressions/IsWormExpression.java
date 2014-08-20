package worms.programs.expressions;

import worms.exceptions.IllegalTypeException;
import worms.model.Program;
import worms.programs.Expression;
import worms.programs.Type;
import worms.programs.types.BoolType;
import worms.programs.types.EntityType;
import worms.programs.types.WormEntityType;

public class IsWormExpression extends Expression<BoolType> {

	private Expression<EntityType> entity;

	@SuppressWarnings("unchecked")
	public IsWormExpression(Program program, int line, int column, Expression<? extends Type> entity) {
		super(program, line, column, BoolType.class);
		
		if ( !isValidType(entity) ) 
			throw new IllegalTypeException();
		
		this.entity = (Expression<EntityType>) entity;
	}

	private boolean isValidType(Expression<? extends Type> expression) {
		return (expression.evaluate().getType() == EntityType.class);
	}
	
	@Override
	public BoolType evaluate() {
		return new BoolType( entity.evaluate() instanceof WormEntityType );
		
	}

}
