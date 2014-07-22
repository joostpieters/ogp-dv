package worms.programs.statements;

import worms.model.Program;
import worms.programs.Expression;
import worms.programs.Statement;
import worms.programs.Type;

public class PrintStatement extends Statement {

	private Expression<? extends Type> expression;

	public PrintStatement(Program program, int line, int column, Expression<? extends Type> e) {
		super(program, line, column);
		this.expression = e;
	}

	@Override
	public void execute(int line, int column) {
		String message = "null";
		if (expression.evaluate().getValue() != null)
			message = expression.evaluate().getValue().toString();
		getProgram().getHandler().print(message);
	}

}
