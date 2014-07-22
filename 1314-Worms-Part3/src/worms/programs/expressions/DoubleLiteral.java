package worms.programs.expressions;

import worms.model.Program;
import worms.programs.Expression;
import worms.programs.types.DoubleType;

public class DoubleLiteral extends Expression<DoubleType> {

	private DoubleType doubleType;

	public DoubleLiteral(Program program, int line, int column, double value) {
		super(program, line, column, DoubleType.class);
		this.doubleType = new DoubleType(value);
	}

	@Override
	public DoubleType evaluate() {
		return this.doubleType;
		
	}

}
