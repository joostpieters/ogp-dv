package worms.programs.expressions;

import worms.model.Program;
import worms.programs.Expression;
import worms.programs.types.EntityType;

public class NullExpression extends Expression<EntityType> {

	public NullExpression(Program program, int line, int column) {
		super(program, line, column, EntityType.class);
		
	}

	@Override
	public EntityType evaluate() {
		return new EntityType(null);		
	}

}
