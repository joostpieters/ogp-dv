package worms.programs.expressions;

import worms.model.Program;
import worms.programs.Expression;
import worms.programs.types.*;

public class BooleanLiteral extends Expression<BoolType> {

	private BoolType boolType;

	public BooleanLiteral(Program program, int line, int column, boolean value) {
		super(program, line, column, BoolType.class);
		this.boolType = new BoolType(value);
	}

	@Override
	public BoolType evaluate() {
		return this.boolType;
	}

}
