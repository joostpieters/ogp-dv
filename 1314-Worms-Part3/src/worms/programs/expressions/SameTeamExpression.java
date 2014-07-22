package worms.programs.expressions;

import worms.model.Program;
import worms.programs.Expression;
import worms.programs.Type;
import worms.programs.types.BoolType;

public class SameTeamExpression extends Expression<BoolType> {

	public SameTeamExpression(Program program, int line, int column, Expression<? extends Type> entity) {
		super(program, line, column, BoolType.class);
	}

	@Override
	public BoolType evaluate() {
		return new BoolType( false );	
	}

}
