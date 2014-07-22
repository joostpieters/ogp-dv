package worms.programs.statements;

import java.util.List;

import worms.model.Program;
import worms.programs.Statement;

public class SequenceStatement extends Statement {

	private List<Statement> statements;

	public SequenceStatement(Program program, int line, int column, List<Statement> statements) {
		super(program, line, column);
		if (statements == null)
			throw new IllegalArgumentException("The list of statements cannot be zero");
		
		this.statements = statements;
	}

	@Override
	public void execute(int line, int column) {
		for (Statement statement: statements)
			statement.execute(line, column);
	}

}
