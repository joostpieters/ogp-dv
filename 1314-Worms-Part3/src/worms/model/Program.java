package worms.model;
import java.util.HashMap;
import java.util.Map;

import be.kuleuven.cs.som.annotate.*;
import worms.exceptions.InterruptException;
import worms.gui.game.IActionHandler;
import worms.programs.Expression;
import worms.programs.ProgramFactoryImpl;
import worms.programs.Statement;
import worms.programs.Type;
import worms.model.programs.ParseOutcome;
import worms.model.programs.ProgramParser;

/**
 * A class of programs with a program text and action handler, and an agent which executes the program.
 * 
 * @invar  The agent attached to each program must be a proper agent for that program.
 * 		   | hasProperAgent()
 * @author Delphine
 *
 */
public class Program {
	
	public Program(String programText, IActionHandler handler) {
		if (!isValidHandler(handler))
			throw new IllegalArgumentException();
		
		this.handler = handler;
		this.programText = programText;
	}

	private final String programText;
	
	private boolean isValidHandler(IActionHandler handler) {
		return (handler != null);
	}
	
	@Basic
	public IActionHandler getHandler() {
		return handler;
	}
		
	private final IActionHandler handler;
	
	@Basic @Raw
	public Character getAgent() {
		return this.agent;
	}
	
	@Raw
	public boolean canHaveAsAgent(Character agent) {
		return (agent != null) && (agent.isAlive());
	}
	
	@Raw
	public boolean hasProperAgent() {
		return canHaveAsAgent(getAgent()) && ( (getAgent() == null) || (getAgent().getProgram() == this) );
	}

	@Raw
	public void setAgent(@Raw Character character) throws IllegalArgumentException {
		if (! canHaveAsAgent(character))
			throw new IllegalArgumentException("Not a valid agent for this program");
		this.agent = character;
		
	}
	
	private Character agent;

	/**
	 * @return the globals
	 */
	public Map<String, Type> getGlobals() {
		return globals;
	}

	/**
	 * @param globals the globals to set
	 */
	public void setGlobals(Map<String, Type> globals) {
		for ( Map.Entry<String, Type> entry : globals.entrySet() ) {
			this.globals.put(entry.getKey(), entry.getValue());
		}
	}
	
	public Type getGlobal(String key) {
		if ( ! globals.containsKey(key) ) 
			throw new IllegalArgumentException("Map does not contain this global!");
		return globals.get(key);
	}

	private Map<String, Type> globals = new HashMap<String,Type>();
	
	private Statement programStatement;

	public ParseOutcome<?> parse() {
		ProgramParser<Expression<? extends Type>, Statement, Type> parser = 
				new ProgramParser<Expression<? extends Type>, Statement, Type>(new ProgramFactoryImpl(this));
		parser.parse(this.programText);
		if ( parser.getErrors().isEmpty() ) {
			this.programStatement = parser.getStatement();
			setGlobals( parser.getGlobals() );
			return ParseOutcome.success(this);
		}
		else 
			return ParseOutcome.failure(parser.getErrors());
	}
	
	public void execute() {
		if ( ! hasProperAgent() ) 
			throw new IllegalStateException("This program has no valid owner!");
		try {
			this.programStatement.execute( getLine(), getColumn() );
		} 
		catch ( InterruptException exc ) {
			setLine( exc.getLine() );
			setColumn( exc.getColumn() );
		}
	}
	
	public int getLine() {
		return this.line;
	}
	
	public int getColumn() {
		return this.column;
	}
	
	public void setLine(int line) {
		this.line = line;
	}
	
	public void setColumn(int column) {
		this.column = column;
	}
	
	private int line = 0;
	
	private int column = 0;
	
}
