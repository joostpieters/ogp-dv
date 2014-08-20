package worms.programs.statements;

import worms.exceptions.IllegalTypeException;
import worms.model.Program;
import worms.programs.Expression;
import worms.programs.Statement;
import worms.programs.Type;

public class AssignmentStatement extends Statement {

	private String name;
	private Expression<? extends Type> rhs;

	public AssignmentStatement(Program program, int line, int column, String variableName, Expression<? extends Type> rhs) {
		super(program, line, column);
		this.name = variableName;
		this.rhs = rhs;
	}

	@Override
	public void execute(int line, int column) {
		Type global = getProgram().getGlobal(name);
		if ( global.getType() != rhs.getType() )
			throw new IllegalTypeException();
		global.setValue(rhs.evaluate().getValue());
		
	}

}
