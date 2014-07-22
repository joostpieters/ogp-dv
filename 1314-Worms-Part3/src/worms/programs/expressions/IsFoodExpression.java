package worms.programs.expressions;

import worms.exceptions.IllegalTypeException;
import worms.model.Program;
import worms.programs.Expression;
import worms.programs.Type;
import worms.programs.types.BoolType;
import worms.programs.types.EntityType;
import worms.programs.types.FoodEntityType;

public class IsFoodExpression extends Expression<BoolType> {

	private Expression<? extends Type> entity;

	public IsFoodExpression(Program program, int line, int column, Expression<? extends Type> entity) {
		super(program, line, column, BoolType.class);
		
		this.entity = entity;
	}

	private boolean isValidType(Expression<? extends Type> expression) {
		return (expression.getType() == EntityType.class);
	}
	
	@Override
	public BoolType evaluate() {
		if ( !isValidType(entity) )
			throw new IllegalTypeException();
		EntityType entity = (EntityType) this.entity.evaluate();
		
		return new BoolType( entity instanceof FoodEntityType );
		
	}

}
