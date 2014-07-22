package worms.programs.expressions;

import worms.model.Program;
import worms.programs.Expression;
import worms.programs.types.EntityType;

public class SelfExpression extends Expression<EntityType> {

	public SelfExpression(Program program, int line, int column) {
		super(program, line, column, EntityType.class);
	}

	@Override
	public EntityType evaluate() {
		return new EntityType( getProgram().getAgent() );
		
	}

}
