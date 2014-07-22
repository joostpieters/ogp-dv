package worms.programs.expressions;

import worms.model.Program;
import worms.programs.Expression;
import worms.programs.Type;

public class VariableAccessExpression extends Expression<Type> {

	private String name;

	public VariableAccessExpression(Program program, int line, int column, String name) {
		super(program, line, column, Type.class);
		this.name = name;
	}

	@Override
	public Type evaluate() {
		return getProgram().getGlobal(name);
	}

}
