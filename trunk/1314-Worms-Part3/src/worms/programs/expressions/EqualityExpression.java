package worms.programs.expressions;

import worms.model.Program;
import worms.programs.Expression;
import worms.programs.Type;
import worms.programs.types.BoolType;

public class EqualityExpression extends Expression<BoolType> {

	private Expression<? extends Type> e1;
	private Expression<? extends Type> e2;

	public EqualityExpression(Program program, int line, int column, Expression<? extends Type> e1, Expression<? extends Type> e2) {
		super(program, line, column, BoolType.class);
		this.e1 = e1;
		this.e2 = e2;
	}

	@Override
	public BoolType evaluate() {
		return new BoolType(e1.evaluate().equals(e2.evaluate()));
	}

}
